package cn.chenhuamou.im.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.xys.libzxing.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.HomeWatcherReceiver;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.GetPicThread;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.pinyin.CharacterParser;
import cn.chenhuamou.im.server.response.IsAliveResponse;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.RongGenerate;
import cn.chenhuamou.im.server.utils.json.JsonMananger;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.adapter.ConversationListAdapterEx;
import cn.chenhuamou.im.ui.fragment.ContactsFragment;
import cn.chenhuamou.im.ui.fragment.DiscoverFragment;
import cn.chenhuamou.im.ui.fragment.MineFragment;
import cn.chenhuamou.im.ui.widget.DragPointView;
import cn.chenhuamou.im.ui.widget.MorePopWindow;
import io.rong.common.RLog;
import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;
//import io.rong.toolkit.TestActivity;

@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity implements
        ViewPager.OnPageChangeListener,
        View.OnClickListener,
        DragPointView.OnDragListencer,
        IUnReadMessageObserver, OnDataListener {

    private static final int RONG_ALIVE = 1000;
    private static final int REQ_PERM_CAMERA = 100;

    public static final String UPDATE_GROUP_MEMBER = "update_group_member";


    public static ViewPager mViewPager;
    private List<Fragment> mFragment = new ArrayList<>();
    private ImageView moreImage, mImageChats, mImageContact, mImageFind, mImageMe, mMineRed;
    private TextView mTextChats, mTextContact, mTextFind, mTextMe;
    private DragPointView mUnreadNumView;
    private ImageView mSearchImageView;

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;

    // 网络请求
    public AsyncTaskManager mAsyncTaskManager;
    protected SealAction action;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mContext = this;
        isDebug = getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false);
        initViews();
        changeTextViewColor();
        changeSelectedTabState(0);
        initMainViewPager();
//        registerHomeKeyReceiver(this);

        mAsyncTaskManager = AsyncTaskManager.getInstance(getApplicationContext());
        // Activity管理
        action = new SealAction(mContext);
        mAsyncTaskManager.request(RONG_ALIVE, this);
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);

        // 刷新用户信息
        RongIM.getInstance().refreshUserInfoCache(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), sp.getString(SealConst.Nick_Name, ""), Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, ""))));

        BroadcastManager.getInstance(mContext).addAction("REFRESH_GROUP_UI", new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String json = intent.getStringExtra("result");
                try {
                    Groups groups = JsonMananger.jsonToBean(json, Groups.class);
                    RongIM.getInstance().refreshGroupInfoCache(new Group(groups.getUserId(), groups.getGroupName(), Uri.parse(groups.getPortraitUri())));
                } catch (HttpException e) {
                    e.printStackTrace();
                }
            }
        });

        // 扫一扫
        BroadcastManager.getInstance(mContext).addAction(SealConst.ScanQRCode, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                scanQRCode();
            }
        });

    }

    private void initViews() {
        // 底部 4个 tabBar item
        RelativeLayout chatRLayout = findViewById(R.id.seal_chat);
        RelativeLayout contactRLayout = findViewById(R.id.seal_contact_list);
        RelativeLayout foundRLayout = findViewById(R.id.seal_find);
        RelativeLayout mineRLayout = findViewById(R.id.seal_me);
        // 图片和文字主要是为了点击的时候切换变色
        mImageChats = findViewById(R.id.tab_img_chats);
        mImageContact = findViewById(R.id.tab_img_contact);
        mImageFind = findViewById(R.id.tab_img_find);
        mImageMe = findViewById(R.id.tab_img_me);
        mTextChats = findViewById(R.id.tab_text_chats);
        mTextContact = findViewById(R.id.tab_text_contact);
        mTextFind = findViewById(R.id.tab_text_find);
        mTextMe = findViewById(R.id.tab_text_me);
        mMineRed = findViewById(R.id.mine_red);
        moreImage = findViewById(R.id.seal_more);
        mSearchImageView = findViewById(R.id.ac_iv_search);

        // 设置点击监听
        chatRLayout.setOnClickListener(this);
        contactRLayout.setOnClickListener(this);
        foundRLayout.setOnClickListener(this);
        mineRLayout.setOnClickListener(this);
        moreImage.setOnClickListener(this);
        mSearchImageView.setOnClickListener(this);

        // 添加广播 提醒红点
        BroadcastManager.getInstance(mContext).addAction(MineFragment.SHOW_RED, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mMineRed.setVisibility(View.VISIBLE);
            }
        });
    }


    private void initMainViewPager() {
        // 最近会话列表
        Fragment conversationList = initConversationList();

        mViewPager = (ViewPager) findViewById(R.id.main_viewpager);

        mUnreadNumView = (DragPointView) findViewById(R.id.seal_num);
        mUnreadNumView.setOnClickListener(this);
        mUnreadNumView.setDragListencer(this);

        // 添加底部的 tab item
        mFragment.add(conversationList);
        mFragment.add(new ContactsFragment());
        mFragment.add(new DiscoverFragment());
        mFragment.add(new MineFragment());
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }
        };
        mViewPager.setAdapter(fragmentPagerAdapter);
        mViewPager.setOffscreenPageLimit(mFragment.size());
        mViewPager.setOnPageChangeListener(this);
        initData();
    }


    private Fragment initConversationList() {
        if (mConversationListFragment == null) {
            ConversationListFragment listFragment = new ConversationListFragment();
            listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
            Uri uri;
            if (isDebug) {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM,
                        Conversation.ConversationType.DISCUSSION
                };

            } else {
                uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                        .appendPath("conversationlist")
                        .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                        .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                        .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                        .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                        .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                        .build();
                mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                        Conversation.ConversationType.GROUP,
                        Conversation.ConversationType.PUBLIC_SERVICE,
                        Conversation.ConversationType.APP_PUBLIC_SERVICE,
                        Conversation.ConversationType.SYSTEM
                };
            }
            listFragment.setUri(uri);
            mConversationListFragment = listFragment;
            return listFragment;
        } else {
            return mConversationListFragment;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        changeTextViewColor();
        changeSelectedTabState(position);
    }

    private void changeTextViewColor() {
        mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat));
        mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts));
        mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found));
        mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me));
        mTextChats.setTextColor(Color.parseColor("#abadbb"));
        mTextContact.setTextColor(Color.parseColor("#abadbb"));
        mTextFind.setTextColor(Color.parseColor("#abadbb"));
        mTextMe.setTextColor(Color.parseColor("#abadbb"));
    }

    private void changeSelectedTabState(int position) {
        switch (position) {
            case 0:
                mTextChats.setTextColor(Color.parseColor("#0099ff"));
                mImageChats.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_chat_hover));
                break;
            case 1:
                mTextContact.setTextColor(Color.parseColor("#0099ff"));
                mImageContact.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_contacts_hover));
                break;
            case 2:
                mTextFind.setTextColor(Color.parseColor("#0099ff"));
                mImageFind.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_found_hover));
                break;
            case 3:
                mTextMe.setTextColor(Color.parseColor("#0099ff"));
                mImageMe.setBackgroundDrawable(getResources().getDrawable(R.drawable.tab_me_hover));
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }


    long firstClick = 0;
    long secondClick = 0;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seal_chat:
                if (mViewPager.getCurrentItem() == 0) {
                    if (firstClick == 0) {
                        firstClick = System.currentTimeMillis();
                    } else {
                        secondClick = System.currentTimeMillis();
                    }
                    RLog.i("MainActivity", "time = " + (secondClick - firstClick));
                    if (secondClick - firstClick > 0 && secondClick - firstClick <= 800) {
                        mConversationListFragment.focusUnreadItem();
                        firstClick = 0;
                        secondClick = 0;
                    } else if (firstClick != 0 && secondClick != 0) {
                        firstClick = 0;
                        secondClick = 0;
                    }
                }
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.seal_contact_list:
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.seal_find:
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.seal_me:
                mViewPager.setCurrentItem(3, false);
                mMineRed.setVisibility(View.GONE);
                break;
            case R.id.seal_more:
                MorePopWindow morePopWindow = new MorePopWindow(MainActivity.this);
                morePopWindow.showPopupWindow(moreImage);
                break;
            case R.id.ac_iv_search:
                startActivity(new Intent(MainActivity.this, SealSearchActivity.class));
                break;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(0, false);
        }
    }

    protected void initData() {

        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };

        // 未读消息
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }

    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString(SealConst.Rong_Token, "");
                if (TextUtils.isEmpty(cacheToken)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(mContext);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(mContext);
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onCountChanged(int count) {
        if (count == 0) {
            mUnreadNumView.setVisibility(View.GONE);
        } else if (count > 0 && count < 100) {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(String.valueOf(count));
        } else {
            mUnreadNumView.setVisibility(View.VISIBLE);
            mUnreadNumView.setText(R.string.no_read_message);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null != this.getCurrentFocus() && event.getAction() == MotionEvent.ACTION_UP) {
            InputMethodManager mInputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            return mInputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        RongIM.getInstance().removeUnReadMessageCountChangedObserver(this);
        if (mHomeKeyReceiver != null)
            this.unregisterReceiver(mHomeKeyReceiver);
        super.onDestroy();
    }

    @Override
    public void onDragOut() {
        mUnreadNumView.setVisibility(View.GONE);
        NToast.shortToast(mContext, getString(R.string.clear_success));
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0) {
                    for (Conversation c : conversations) {
                        RongIM.getInstance().clearMessagesUnreadStatus(c.getConversationType(), c.getTargetId(), null);
                    }
                }
            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {

            }
        }, mConversationsTypes);

    }

    private HomeWatcherReceiver mHomeKeyReceiver = null;

    //如果遇见 Android 7.0 系统切换到后台回来无效的情况 把下面注册广播相关代码注释或者删除即可解决。下面广播重写 home 键是为了解决三星 note3 按 home 键花屏的一个问题
    private void registerHomeKeyReceiver(Context context) {
        if (mHomeKeyReceiver == null) {
            mHomeKeyReceiver = new HomeWatcherReceiver();
            final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            try {
                context.registerReceiver(mHomeKeyReceiver, homeFilter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        return action.isAlive();
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        IsAliveResponse isAliveResponse = (IsAliveResponse) result;
        if (isAliveResponse.getCode() != null) {
            NLog.d("------------------------isAlive响应成功", "------------------------isAlive响应成功");
        } else {
            NLog.d("------------------------isAlive响应失败", "------------------------isAlive响应失败");
        }

    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        NLog.d("------------------------isAlive响应失败", "------------------------isAlive响应失败");
    }


    /*
     * 开始扫描二维码
     * */
    private void scanQRCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_PERM_CAMERA);
            return;
        }

        // 二维码扫描
        Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
        startActivityForResult(intent, REQ_PERM_CAMERA);
    }


    /*
     * 结果回调
     * */

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == REQ_PERM_CAMERA && resultCode == RESULT_OK) {
            String scanResult = data.getStringExtra("result");
            String[] resultStrings = scanResult.split("\\|");

            if (resultStrings.length == 2 && (resultStrings[1].equals(SealConst.IsFromGroup) || resultStrings[1].equals(SealConst.IsPrivate))) {
                // 个人二维码
                if (resultStrings[1].equals(SealConst.IsPrivate)) {
                    qrCodeDealwithPrivate(resultStrings[0]);
                } else { // 群组二维码
                    qrCodeDealwithGroup(resultStrings[0]);
                }
            } else {
                NToast.shortToast(mContext, "暂时不支持该二维码");
            }
        }
    }

    /*
     * 处理个人二维码
     * */
    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;
    private void qrCodeDealwithPrivate(String targetId) {
        Intent intent = new Intent(this, UserDetailActivity.class);
        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        Friend friend = new Friend(targetId, targetId, Uri.parse(""));
        intent.putExtra("friend", friend);
        startActivity(intent);
    }

    /*
     * 处理群组二维码
     * */
    private void qrCodeDealwithGroup(String targetId) {

    }

    /*
     * 权限申请
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_PERM_CAMERA:
                // 摄像头权限申请
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获得授权
                    scanQRCode();
                } else {
                    // 被禁止授权
                    Toast.makeText(MainActivity.this, "请至权限中心打开本应用的相机访问权限", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
