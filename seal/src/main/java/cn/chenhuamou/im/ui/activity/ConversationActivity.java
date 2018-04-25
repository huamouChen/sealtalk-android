package cn.chenhuamou.im.ui.activity;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrmf360.rylib.modules.JrmfExtensionModule;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import cn.chenhuamou.contactcard.ContactCardExtensionModule;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.GroupMember;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.KqwfPcddResponse;
import cn.chenhuamou.im.server.response.LotteryInfoResponse;
import cn.chenhuamou.im.server.response.LotteryOpenNumResponse;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.ui.fragment.ConversationFragmentEx;
import cn.chenhuamou.im.ui.widget.LoadingDialog;
import cn.chenhuamou.im.ui.widget.MyBetExtensionModule;
import cn.chenhuamou.im.ui.widget.MyExtensionModule;
import io.rong.callkit.RongCallKit;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.RongKitIntent;
import io.rong.imkit.fragment.UriFragment;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Discussion;
import io.rong.imlib.model.PublicServiceProfile;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;
import io.rong.recognizer.RecognizeExtensionModule;

//CallKit start 1
//CallKit end 1

/**
 * 会话页面
 * 1，设置 ActionBar title
 * 2，加载会话页面
 * 3，push 和 通知 判断
 */
public class ConversationActivity extends BaseActivity implements View.OnClickListener {

    private String TAG = ConversationActivity.class.getSimpleName();
    /**
     * 对方id
     */
    private String mTargetId;
    /**
     * 会话类型
     */
    private Conversation.ConversationType mConversationType;
    /**
     * title
     */
    private String title;
    /**
     * 是否在讨论组内，如果不在讨论组内，则进入不到讨论组设置页面
     */
    private boolean isFromPush = false;

    private LoadingDialog mDialog;

    private SharedPreferences sp;

    private final String TextTypingTitle = "对方正在输入...";
    private final String VoiceTypingTitle = "对方正在讲话...";

    private Handler mHandler;

    private String betMsg = "";


    // 投注
    private static final int Bet = 1000;
    // 当前期号 上期期号 当前期号剩余时间
    private static final int LotteryInfo = 2000;
    // 获取开奖历史
    private static final int LotteryNum = 3000;

    private RelativeLayout relativeLayout_lottery_info;
    private TextView tv_current_num, tv_pre_num;
    private ProgressBar progressBar_lottery_time;
    private String pre_num, current_num; // 上期开奖号码  当前开奖期号
    public static final int Update_ProgressBar = 0x111, Update_Lottery_Info = 0x222, Start_Timer = 0x333;


    // 定时器
    private long progress = 0;  // 进度 时间的进度
    private int total_time = 5 * 60 * 1000; //开奖时间  PC蛋蛋是5分钟
    public static final int schedule = 1;  // 双星间隔
    private Timer mTimer;

    private RongIM.OnSendMessageListener msgListener;

    public static final int SET_TEXT_TYPING_TITLE = 1;
    public static final int SET_VOICE_TYPING_TITLE = 2;
    public static final int SET_TARGET_ID_TITLE = 0;

    private Button mRightButton;
    private RelativeLayout layout_announce;
    private TextView tv_announce;
    private ImageView iv_arrow;

    @Override
    @TargetApi(23)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        mDialog = new LoadingDialog(this);
        layout_announce = (RelativeLayout) findViewById(R.id.ll_annouce);
        iv_arrow = (ImageView) findViewById(R.id.iv_announce_arrow);
        layout_announce.setVisibility(View.GONE);
        tv_announce = (TextView) findViewById(R.id.tv_announce_msg);

        mRightButton = getHeadRightButton();

        Intent intent = getIntent();

        if (intent == null || intent.getData() == null)
            return;

        // 消息接受方 ID
        mTargetId = intent.getData().getQueryParameter("targetId");

        //10000 为 Demo Server 加好友的 id，若 targetId 为 10000，则为加好友消息，默认跳转到 NewFriendListActivity
        // Demo 逻辑
        if (mTargetId != null && mTargetId.equals("10000")) {
            startActivity(new Intent(ConversationActivity.this, NewFriendListActivity.class));
            return;
        }
        mConversationType = Conversation.ConversationType.valueOf(intent.getData()
                .getLastPathSegment().toUpperCase(Locale.US));

        title = intent.getData().getQueryParameter("title");

        setActionBarTitle(mConversationType, mTargetId);


        if (mConversationType.equals(Conversation.ConversationType.GROUP)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon2_menu));
        } else if (mConversationType.equals(Conversation.ConversationType.PRIVATE) | mConversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE) | mConversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            mRightButton.setBackground(getResources().getDrawable(R.drawable.icon1_menu));
        } else {
            mRightButton.setVisibility(View.GONE);
            mRightButton.setClickable(false);
        }
        mRightButton.setOnClickListener(this);

        isPushMessage(intent);
        if (mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setAnnounceListener();
        }

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SET_TEXT_TYPING_TITLE:
                        setTitle(TextTypingTitle);
                        break;
                    case SET_VOICE_TYPING_TITLE:
                        setTitle(VoiceTypingTitle);
                        break;
                    case SET_TARGET_ID_TITLE:
                        setActionBarTitle(mConversationType, mTargetId);
                        break;
                    case Update_ProgressBar:
                        progressBar_lottery_time.setProgress((int) progress);
                        break;
                    case Update_Lottery_Info:
                        request(LotteryInfo);  // 时间到了之后，重新获取期号信息
                        break;
                    case Start_Timer:
                        startTimer();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });

        RongIMClient.setTypingStatusListener(new RongIMClient.TypingStatusListener() {
            @Override
            public void onTypingStatusChanged(Conversation.ConversationType type, String targetId, Collection<TypingStatus> typingStatusSet) {
                //当输入状态的会话类型和targetID与当前会话一致时，才需要显示
                if (type.equals(mConversationType) && targetId.equals(mTargetId)) {
                    int count = typingStatusSet.size();
                    //count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                    if (count > 0) {
                        Iterator iterator = typingStatusSet.iterator();
                        TypingStatus status = (TypingStatus) iterator.next();
                        String objectName = status.getTypingContentType();

                        MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                        MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                        //匹配对方正在输入的是文本消息还是语音消息
                        if (objectName.equals(textTag.value())) {
                            mHandler.sendEmptyMessage(SET_TEXT_TYPING_TITLE);
                        } else if (objectName.equals(voiceTag.value())) {
                            mHandler.sendEmptyMessage(SET_VOICE_TYPING_TITLE);
                        }
                    } else {//当前会话没有用户正在输入，标题栏仍显示原来标题
                        mHandler.sendEmptyMessage(SET_TARGET_ID_TITLE);
                    }
                }
            }
        });

        SealAppContext.getInstance().pushActivity(this);

        //CallKit start 2
        RongCallKit.setGroupMemberProvider(new RongCallKit.GroupMembersProvider() {
            @Override
            public ArrayList<String> getMemberList(String groupId, final RongCallKit.OnGroupMembersResult result) {
                getGroupMembersForCall();
                mCallMemberResult = result;
                return null;
            }
        });

        // 发送消息的监听
        sendMessageListener();

        // 自定义插件
        setMyExtensionModule();


        // BetActivity 下注接口回调
        BroadcastManager.getInstance(mContext).addAction("Bet", new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String string = intent.getStringExtra("String");
//                NToast.shortToast(mContext, string);
            }
        });


        // BetActivity 下注发送消息
        BroadcastManager.getInstance(mContext).addAction("SendBetMessage", new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String betString = intent.getStringExtra("String");
                betString += "\n期号：" + current_num;
                TextMessage mTextMessage = TextMessage.obtain(betString);
                io.rong.imlib.model.Message myMessage = io.rong.imlib.model.Message.obtain(mTargetId, Conversation.ConversationType.GROUP, mTextMessage);
                RongIM.getInstance().sendMessage(myMessage, null, null, new IRongCallback.ISendMediaMessageCallback() {
                    @Override
                    public void onProgress(io.rong.imlib.model.Message message, int i) {
                    }

                    @Override
                    public void onCanceled(io.rong.imlib.model.Message message) {
                    }

                    @Override
                    public void onAttached(io.rong.imlib.model.Message message) {
                    }

                    @Override
                    public void onSuccess(io.rong.imlib.model.Message message) {
                    }

                    @Override
                    public void onError(io.rong.imlib.model.Message message, RongIMClient.ErrorCode errorCode) {
                    }
                });
            }
        });

        initView();

        // 获取当前期号 上期期号 剩余时间
        initData();
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        startTimer();
    }

    // 自定义插件区域的插件
    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        JrmfExtensionModule jrmfExtensionModule = null;
        RecognizeExtensionModule recognizeExtensionModule = null;
        ContactCardExtensionModule contactCardExtensionModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    continue;
                }
                if (module instanceof JrmfExtensionModule) {
                    jrmfExtensionModule = (JrmfExtensionModule) module;
                }
            }


            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().unregisterExtensionModule(jrmfExtensionModule);

                // 群聊才显示下注的插件，之后也可以改为指定群，或者从后台获取是否显示
                if (mConversationType == Conversation.ConversationType.GROUP) {
                    RongExtensionManager.getInstance().registerExtensionModule(new MyBetExtensionModule());
                } else {
                    RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
                }
            }
        }
    }


    /**
     * 设置通告栏的监听
     */
    private void setAnnounceListener() {
        if (fragment != null) {
            fragment.setOnShowAnnounceBarListener(new ConversationFragmentEx.OnShowAnnounceListener() {
                @Override
                public void onShowAnnounceView(String announceMsg, final String announceUrl) {
                    layout_announce.setVisibility(View.VISIBLE);
                    tv_announce.setText(announceMsg);
                    layout_announce.setClickable(false);
                    if (!TextUtils.isEmpty(announceUrl)) {
                        iv_arrow.setVisibility(View.VISIBLE);
                        layout_announce.setClickable(true);
                        layout_announce.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String str = announceUrl.toLowerCase();
                                if (!TextUtils.isEmpty(str)) {
                                    if (!str.startsWith("http") && !str.startsWith("https")) {
                                        str = "http://" + str;
                                    }
                                    Intent intent = new Intent(RongKitIntent.RONG_INTENT_ACTION_WEBVIEW);
                                    intent.setPackage(v.getContext().getPackageName());
                                    intent.putExtra("url", str);
                                    v.getContext().startActivity(intent);
                                }
                            }
                        });
                    } else {
                        iv_arrow.setVisibility(View.GONE);
                    }
                }
            });
        }
    }

    /**
     * 判断是否是 Push 消息，判断是否需要做 connect 操作
     */
    private void isPushMessage(Intent intent) {

        if (intent == null || intent.getData() == null)
            return;
        //push
        if (intent.getData().getScheme().equals("rong") && intent.getData().getQueryParameter("isFromPush") != null) {

            //通过intent.getData().getQueryParameter("push") 为true，判断是否是push消息
            if (intent.getData().getQueryParameter("isFromPush").equals("true")) {
                //只有收到系统消息和不落地 push 消息的时候，pushId 不为 null。而且这两种消息只能通过 server 来发送，客户端发送不了。
                //RongIM.getInstance().getRongIMClient().recordNotificationEvent(id);
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                isFromPush = true;
                enterActivity();
            } else if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(mContext, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterActivity();
            } else {
                if (intent.getData().getPath().contains("conversation/system")) {
                    Intent intent1 = new Intent(mContext, MainActivity.class);
                    intent1.putExtra("systemconversation", true);
                    startActivity(intent1);
                    SealAppContext.getInstance().popAllActivity();
                    return;
                }
                enterFragment(mConversationType, mTargetId);
            }

        } else {
            if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.DISCONNECTED)) {
                if (mDialog != null && !mDialog.isShowing()) {
                    mDialog.show();
                }
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        enterActivity();
                    }
                }, 300);
            } else {
                enterFragment(mConversationType, mTargetId);
            }
        }
    }


    /**
     * 收到 push 消息后，选择进入哪个 Activity
     * 如果程序缓存未被清理，进入 MainActivity
     * 程序缓存被清理，进入 LoginActivity，重新获取token
     * <p>
     * 作用：由于在 manifest 中 intent-filter 是配置在 ConversationActivity 下面，所以收到消息后点击notifacition 会跳转到 DemoActivity。
     * 以跳到 MainActivity 为例：
     * 在 ConversationActivity 收到消息后，选择进入 MainActivity，这样就把 MainActivity 激活了，当你读完收到的消息点击 返回键 时，程序会退到
     * MainActivity 页面，而不是直接退回到 桌面。
     */
    private void enterActivity() {

        String token = sp.getString(SealConst.Rong_Token, "");

        if (token.equals("default")) {
            NLog.e("ConversationActivity push", "push2");
            startActivity(new Intent(ConversationActivity.this, LoginActivity.class));
            SealAppContext.getInstance().popAllActivity();
        } else {
            NLog.e("ConversationActivity push", "push3");
            reconnect(token);
        }
    }

    private void reconnect(String token) {
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {

                Log.e(TAG, "---onTokenIncorrect--");
            }

            @Override
            public void onSuccess(String s) {
                Log.i(TAG, "---onSuccess--" + s);
                NLog.e("ConversationActivity push", "push4");

                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);

            }

            @Override
            public void onError(RongIMClient.ErrorCode e) {
                Log.e(TAG, "---onError--" + e);
                if (mDialog != null)
                    mDialog.dismiss();

                enterFragment(mConversationType, mTargetId);
            }
        });

    }

    private ConversationFragmentEx fragment;

    /**
     * 加载会话页面 ConversationFragmentEx 继承自 ConversationFragment
     *
     * @param mConversationType 会话类型
     * @param mTargetId         会话 Id
     */
    private void enterFragment(Conversation.ConversationType mConversationType, String mTargetId) {

        fragment = new ConversationFragmentEx();

        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(mConversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", mTargetId).build();

        fragment.setUri(uri);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //xxx 为你要加载的 id
        transaction.add(R.id.rong_content, fragment);
        transaction.commitAllowingStateLoss();
    }

    /**
     * 设置会话页面 Title
     *
     * @param conversationType 会话类型
     * @param targetId         目标 Id
     */
    private void setActionBarTitle(Conversation.ConversationType conversationType, String targetId) {

        if (conversationType == null)
            return;

        if (conversationType.equals(Conversation.ConversationType.PRIVATE)) {
            setPrivateActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.GROUP)) {
            setGroupActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.DISCUSSION)) {
            setDiscussionActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CHATROOM)) {
            setTitle(title);
        } else if (conversationType.equals(Conversation.ConversationType.SYSTEM)) {
            setTitle(R.string.de_actionbar_system);
        } else if (conversationType.equals(Conversation.ConversationType.APP_PUBLIC_SERVICE)) {
            setAppPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.PUBLIC_SERVICE)) {
            setPublicServiceActionBar(targetId);
        } else if (conversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
            setTitle(R.string.main_customer);
        } else {
            setTitle(R.string.de_actionbar_sub_defult);
        }

    }

    /**
     * 设置群聊界面 ActionBar
     *
     * @param targetId 会话 Id
     */
    private void setGroupActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            setTitle(title);
        } else {
            setTitle(targetId);
        }
    }

    /**
     * 设置应用公众服务界面 ActionBar
     */
    private void setAppPublicServiceActionBar(String targetId) {
        if (targetId == null)
            return;

        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.APP_PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置公共服务号 ActionBar
     */
    private void setPublicServiceActionBar(String targetId) {

        if (targetId == null)
            return;


        RongIM.getInstance().getPublicServiceProfile(Conversation.PublicServiceType.PUBLIC_SERVICE
                , targetId, new RongIMClient.ResultCallback<PublicServiceProfile>() {
                    @Override
                    public void onSuccess(PublicServiceProfile publicServiceProfile) {
                        setTitle(publicServiceProfile.getName());
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
    }

    /**
     * 设置讨论组界面 ActionBar
     */
    private void setDiscussionActionBar(String targetId) {

        if (targetId != null) {

            RongIM.getInstance().getDiscussion(targetId
                    , new RongIMClient.ResultCallback<Discussion>() {
                        @Override
                        public void onSuccess(Discussion discussion) {
                            setTitle(discussion.getName());
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            if (e.equals(RongIMClient.ErrorCode.NOT_IN_DISCUSSION)) {
                                setTitle("不在讨论组中");
                                supportInvalidateOptionsMenu();
                            }
                        }
                    });
        } else {
            setTitle("讨论组");
        }
    }


    /**
     * 设置私聊界面 ActionBar
     */
    private void setPrivateActionBar(String targetId) {
        if (!TextUtils.isEmpty(title)) {
            if (title.equals("null")) {
                if (!TextUtils.isEmpty(targetId)) {
                    UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
                    if (userInfo != null) {
                        setTitle(userInfo.getName());
                    }
                }
            } else {
                setTitle(title);
            }

        } else {
            setTitle(targetId);
        }
    }

    /**
     * 根据 targetid 和 ConversationType 进入到设置页面
     */
    private void enterSettingActivity() {

        if (mConversationType == Conversation.ConversationType.PUBLIC_SERVICE
                || mConversationType == Conversation.ConversationType.APP_PUBLIC_SERVICE) {

            RongIM.getInstance().startPublicServiceProfile(this, mConversationType, mTargetId);
        } else {
            UriFragment fragment = (UriFragment) getSupportFragmentManager().getFragments().get(0);
            //得到讨论组的 targetId
            mTargetId = fragment.getUri().getQueryParameter("targetId");

            if (TextUtils.isEmpty(mTargetId)) {
                NToast.shortToast(mContext, "讨论组尚未创建成功");
            }


            Intent intent = null;
            if (mConversationType == Conversation.ConversationType.GROUP) {
                intent = new Intent(this, GroupDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.GROUP);
            } else if (mConversationType == Conversation.ConversationType.PRIVATE) {
                intent = new Intent(this, PrivateChatDetailActivity.class);
                intent.putExtra("conversationType", Conversation.ConversationType.PRIVATE);
            } else if (mConversationType == Conversation.ConversationType.DISCUSSION) {
                intent = new Intent(this, DiscussionDetailActivity.class);
                intent.putExtra("TargetId", mTargetId);
                startActivityForResult(intent, 166);
                return;
            }
            intent.putExtra("TargetId", mTargetId);
            if (intent != null) {
                startActivityForResult(intent, 500);
            }

        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 501) {
            SealAppContext.getInstance().popAllActivity();
        }
    }

    @Override
    protected void onDestroy() {
        //CallKit start 3
        RongCallKit.setGroupMemberProvider(null);
        //CallKit end 3

        // 取消定时器
        if (mTimer != null) {
            mTimer.cancel();
        }


        BroadcastManager.getInstance(mContext).destroy("Bet");
        BroadcastManager.getInstance(mContext).destroy("SendBetMessage");
        RongIMClient.setTypingStatusListener(null);
        SealAppContext.getInstance().popActivity(this);
        super.onDestroy();


        msgListener = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            if (fragment != null && !fragment.onBackPressed()) {
                if (isFromPush) {
                    isFromPush = false;
                    startActivity(new Intent(this, MainActivity.class));
                    SealAppContext.getInstance().popAllActivity();
                } else {
                    if (fragment.isLocationSharing()) {
                        fragment.showQuitLocationSharingDialog(this);
                        return true;
                    }
                    if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                            || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                        SealAppContext.getInstance().popActivity(this);
                    } else {
                        SealAppContext.getInstance().popActivity(this);
                    }
                }
            }
        }
        return false;
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //CallKit start 4
    private RongCallKit.OnGroupMembersResult mCallMemberResult;

    private void getGroupMembersForCall() {
        SealUserInfoManager.getInstance().getGroupMembers(mTargetId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                ArrayList<String> userIds = new ArrayList<>();
                if (groupMembers != null) {
                    for (GroupMember groupMember : groupMembers) {
                        if (groupMember != null) {
                            userIds.add(groupMember.getUserId());
                        }
                    }
                }
                mCallMemberResult.onGotMemberList(userIds);
            }

            @Override
            public void onError(String errString) {
                mCallMemberResult.onGotMemberList(null);
            }
        });
    }
    //CallKit end 4

    @Override
    public void onClick(View v) {
        enterSettingActivity();
    }

    @Override
    public void onHeadLeftButtonClick(View v) {
        if (fragment != null && !fragment.onBackPressed()) {
            if (fragment.isLocationSharing()) {
                fragment.showQuitLocationSharingDialog(this);
                return;
            }
            hintKbTwo();
            if (isFromPush) {
                isFromPush = false;
                startActivity(new Intent(this, MainActivity.class));
            }
            if (mConversationType.equals(Conversation.ConversationType.CHATROOM)
                    || mConversationType.equals(Conversation.ConversationType.CUSTOMER_SERVICE)) {
                SealAppContext.getInstance().popActivity(this);
            } else {
                SealAppContext.getInstance().popAllActivity();
            }
        }
    }

    // 发送消息的监听
    private void sendMessageListener() {
        // 群组或者聊天室的时候，把文本消息发送到服务器
        if (mConversationType == Conversation.ConversationType.GROUP || mConversationType == Conversation.ConversationType.CHATROOM) {
            msgListener = new RongIM.OnSendMessageListener() {
                @Override
                public io.rong.imlib.model.Message onSend(io.rong.imlib.model.Message message) {
                    if (message.getContent() instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) message.getContent();
                        betMsg = textMessage.getContent();
                        request(Bet);
//                        if (msgContent.startsWith("#") && msgContent.endsWith("#")) {
//                            // 截取 betMsg
//                            betMsg = msgContent.substring(1, msgContent.length() - 1);
//                            betMsg = msgContent;
//                            request(Bet);
//                        }
                    }
                    return message;
                }

                @Override
                public boolean onSent(io.rong.imlib.model.Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                    return false;
                }
            };

            RongIM.getInstance().setSendMessageListener(msgListener);
        }
    }


    /*
     * 绑定控件
     * */
    private void initView() {
        relativeLayout_lottery_info = (RelativeLayout) findViewById(R.id.rl_lottery_info);

        tv_current_num = (TextView) findViewById(R.id.tv_current_num);
        tv_pre_num = (TextView) findViewById(R.id.tv_pre_num);
        progressBar_lottery_time = (ProgressBar) findViewById(R.id.pb_horizontial);
        progressBar_lottery_time.setMax(total_time);


    }


    // 获取彩种信息
    private void initData() {
        if (mConversationType == Conversation.ConversationType.GROUP) {
            request(LotteryInfo);
        }
    }

    /*
     * 开始计时
     * */
    private void startTimer() {
        // 计时器
        mTimer = new Timer();
        // 计时器任务
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (progress < total_time) {  // 时间没走完，持续更新进度，当前是每5秒更新一次
                    progress += (schedule * 1000);
                    mHandler.sendEmptyMessage(Update_ProgressBar);
                } else {
                    // 延迟两秒再重新获取开奖号码，获取开奖号码可能不及时，因为后台生成数据需要时间，app去拉去也需要时间，
                    mHandler.sendEmptyMessage(LotteryInfo);
                    mTimer.cancel();

                }
            }
        };
        // 开始任务
        mTimer.schedule(timerTask, 0, schedule * 1000);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case Bet:
                return action.postKQWFPCDD(betMsg, mTargetId);
            case LotteryInfo:
                return action.getLotteryInfo("90002");
            case LotteryNum:
                return action.getLotteryOpenNumber("90002");
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case Bet:
                KqwfPcddResponse kqwfPcddResponse = (KqwfPcddResponse) result;
                if (kqwfPcddResponse.isResult()) {
                    NToast.shortToast(mContext, "投注成功");
                } else {
                    NToast.shortToast(mContext, kqwfPcddResponse.getError());
                }
                break;
            case LotteryInfo:
                LotteryInfoResponse lotteryInfoResponse = (LotteryInfoResponse) result;
                tv_current_num.setText("当前期号：" + lotteryInfoResponse.getCurrentIssueNo());
                current_num = lotteryInfoResponse.getCurrentIssueNo();
                pre_num = lotteryInfoResponse.getPreviewIssueNo();

                // 剩余时间
                System.out.println("-------------------" + lotteryInfoResponse.getRemainTime());
                progress = total_time - lotteryInfoResponse.getRemainTime();  // 这是剩余的时间
                System.out.println("-------------------progress" + progress);
                progressBar_lottery_time.setProgress(total_time - (int) progress); // 用总的时间减去剩余时间才是当前的进度

                // 开始倒计时
                mHandler.sendEmptyMessage(Start_Timer);
                // 获取开奖号码
                request(LotteryNum);
                break;
            case LotteryNum:
                List<LotteryOpenNumResponse> lotteryList = (List<LotteryOpenNumResponse>) result;
                tv_pre_num.setText("第" + pre_num + "期开奖号码：" + lotteryList.get(0).getLotteryOpenNo());
                relativeLayout_lottery_info.setVisibility(View.VISIBLE); // 显示开
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case Bet:
                NToast.shortToast(mContext, "投注失败");
                break;
            case LotteryInfo:
                NToast.shortToast(mContext, "获取彩种信息失败，请重新进入群聊");
                break;
            case LotteryNum:
                NToast.shortToast(mContext, "获取上期开奖号码失败，请重新进入群聊");
                break;
        }
    }
}
