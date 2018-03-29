package cn.chenhuamou.im.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.pinyin.CharacterParser;
import cn.chenhuamou.im.server.pinyin.PinyinComparator;
import cn.chenhuamou.im.server.pinyin.SideBar;
import cn.chenhuamou.im.server.response.GetRongGroupMembersResponse;
import cn.chenhuamou.im.server.response.GetRongGroupResponse;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.SelectableRoundedImageView;
import cn.chenhuamou.im.ui.activity.GroupListActivity;
import cn.chenhuamou.im.ui.activity.NewFriendListActivity;
import cn.chenhuamou.im.ui.activity.PublicServiceActivity;
import cn.chenhuamou.im.ui.activity.UserDetailActivity;
import cn.chenhuamou.im.ui.adapter.FriendListAdapter;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * tab 2 通讯录的 Fragment
 * Created by Bob on 2015/1/25.
 */
public class ContactsFragment extends Fragment implements View.OnClickListener, OnDataListener {

    private static final int GET_RONG_GROUPS = 700;
    private static final int GET_RONG_GROUP_MEMBERS = 800;

    private SelectableRoundedImageView mSelectableRoundedImageView;
    private TextView mNameTextView;
    private TextView mNoFriends;
    private TextView mUnreadTextView;
    private View mHeadView;
    private EditText mSearchEditText;
    private ListView mListView;
    private PinyinComparator mPinyinComparator;
    private SideBar mSidBar;

    private SealAction action;
    private AsyncTaskManager mAsyncTaskManager;
    private List<Friend> friendList = new ArrayList<>();
    private List<Friend> friendListFilter = new ArrayList<>(); // 用来判断是否添加过了
    private String groupId = "";
    private String userName = "";

    /**
     * 中部展示的字母提示
     */
    private TextView mDialogTextView;

    private List<Friend> mFriendList;
    private List<Friend> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;

    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_address, container, false);
        initView(view);
        initData();
        updateUI();
        refreshUIListener();
        return view;
    }

    //  好友详情界面
    private void startFriendDetailsPage(Friend friend) {
        Intent intent = new Intent(getActivity(), UserDetailActivity.class);
        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
        intent.putExtra("friend", friend);
        startActivity(intent);
    }

    private void initView(View view) {
        mSearchEditText = (EditText) view.findViewById(R.id.search);
        mListView = (ListView) view.findViewById(R.id.listview);
        mNoFriends = (TextView) view.findViewById(R.id.show_no_friend);
        mSidBar = (SideBar) view.findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) view.findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(getActivity());

        // 头部 新的朋友 群组 公众号 自己 4个选项
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header,
                null);
        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
        RelativeLayout publicServiceLayout = (RelativeLayout) mHeadView.findViewById(R.id.publicservice);
        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
        updatePersonalUI();
        mListView.addHeaderView(mHeadView);
        mNoFriends.setVisibility(View.VISIBLE);

        selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        publicServiceLayout.setOnClickListener(this);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }

    private void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        userName = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");

        mAsyncTaskManager = AsyncTaskManager.getInstance(getContext());
        action = new SealAction(getContext());
        mFriendList = new ArrayList<>();

        FriendListAdapter adapter = new FriendListAdapter(getActivity(), mFriendList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (mDialogTextView != null) {
            mDialogTextView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr 需要过滤的 String
     */
    private void filterData(String filterStr) {
        List<Friend> filterDateList = new ArrayList<>();

        try {
            if (TextUtils.isEmpty(filterStr)) {
                filterDateList = mFriendList;
            } else {
                filterDateList.clear();
                for (Friend friendModel : mFriendList) {
                    String name = friendModel.getName();
                    String displayName = friendModel.getDisplayName();
                    if (!TextUtils.isEmpty(displayName)) {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr) || displayName.contains(filterStr) || mCharacterParser.getSpelling(displayName).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    } else {
                        if (name.contains(filterStr) || mCharacterParser.getSpelling(name).startsWith(filterStr)) {
                            filterDateList.add(friendModel);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据a-z进行排序
        Collections.sort(filterDateList, mPinyinComparator);
        mFilteredFriendList = filterDateList;
        mFriendListAdapter.updateListView(filterDateList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.re_newfriends:  // 新的朋友
                mUnreadTextView.setVisibility(View.GONE);
                Intent intent = new Intent(getActivity(), NewFriendListActivity.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.re_chatroom:   // 群聊
                startActivity(new Intent(getActivity(), GroupListActivity.class));
                break;
            case R.id.publicservice:  // 公众号
                Intent intentPublic = new Intent(getActivity(), PublicServiceActivity.class);
                startActivity(intentPublic);
                break;
            case R.id.contact_me_item:  // 当前用户，也就是自己
                RongIM.getInstance().startPrivateChat(getActivity(), mId, mCacheName);
                break;
        }
    }

    private void refreshUIListener() {
        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    updateUI();
                }
            }
        });

        BroadcastManager.getInstance(getActivity()).addAction(SealAppContext.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    mUnreadTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePersonalUI();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_FRIEND);
            BroadcastManager.getInstance(getActivity()).destroy(SealAppContext.UPDATE_RED_DOT);
            BroadcastManager.getInstance(getActivity()).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        // 从网络获取好友列表
//        SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
//            @Override
//            public void onSuccess(List<Friend> friendsList) {
//                updateFriendsList(friendsList);
//            }
//
//            @Override
//            public void onError(String errString) {
//                updateFriendsList(null);
//            }
//        });
//        updateFriendsList(list);

        mAsyncTaskManager.request(GET_RONG_GROUPS, this);


    }

    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        switch (requestCode) {
            case GET_RONG_GROUPS:
                return action.getRongGroups(userName);
            case GET_RONG_GROUP_MEMBERS:
                return action.getRongGroupMembers(groupId);
        }
        return null;

    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case GET_RONG_GROUPS:
                GetRongGroupResponse getRongGroupResponse = (GetRongGroupResponse) result;
                // token 失效，返回登录界面
                if (getRongGroupResponse.getCode().getCodeId().equals("401")) {
                    NToast.shortToast(getContext(), getString(R.string.token_not_available));
                    BroadcastManager.getInstance(getContext()).sendBroadcast(SealConst.EXIT);
                    return;
                }
                List<Groups> list = getRongGroupResponse.getValue();
                for (int i = 0; i < list.size(); i++) {
                    Groups groups = list.get(i);
                    groupId = groups.getGroupId();
                    mAsyncTaskManager.request(GET_RONG_GROUP_MEMBERS, this);
                    // 循环完才会执行，所以拿到的ID 都是最后的,所以让线程睡一下，再去获取数据，但是这样并不能完美的解决这个问题，后续需要再修改
                    try {
                        Thread.sleep(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case GET_RONG_GROUP_MEMBERS:
                GetRongGroupMembersResponse getRongGroupMembersResponse = (GetRongGroupMembersResponse) result;
                // token 失效，返回登录界面
                if (getRongGroupMembersResponse.getCode().getCodeId().equals("401")) {
                    NToast.shortToast(getContext(), getString(R.string.token_not_available));
                    BroadcastManager.getInstance(getContext()).sendBroadcast(SealConst.EXIT);
                    return;
                }
                List<Groups> membersList = getRongGroupMembersResponse.getValue();
                for (int i = 0; i < membersList.size(); i++) {
                    Groups groups = membersList.get(i);
                    String userID = groups.getUserName();
                    // 是当前用户，就不添加了
                    if (userID.equals(userName)) continue;

                    // 已经添加过了，就不重复添加了，因为不同的群组可能会有相同的人
                    boolean isHave = false;
                    for (Friend item : friendListFilter) {
                        if (item.getName().equals(userID)) {isHave = true; break;}
                    }
                    if (isHave) continue;
                    Friend friend = new Friend(groups.getUserName(), groups.getUserName(), Uri.parse(""));
                    // 设置这两个属性主要是为了通讯录界面进行排序
                    friend.setDisplayName(groups.getUserName());
                    friend.setDisplayNameSpelling(groups.getUserName());
                    friendList.add(friend);
                }
                // 先清空之前的，再添加所有的
                friendListFilter.clear();
                friendListFilter.addAll(friendList);
                updateFriendsList(friendList);
                // 把好友写入到数据库
                addFrientToDataBase(friendList);
                break;
        }
    }

    // 把好友写进
    private void addFrientToDataBase(List<Friend> list) {
        for (Friend item : list) {
            SealUserInfoManager.getInstance().addFriend(item);
        }

    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {

    }

    private void updateFriendsList(List<Friend> friendsList) {
        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        boolean isReloadList = false;
        if (mFriendList != null && mFriendList.size() > 0) {
//            mFriendList.clear();
            isReloadList = true;
        }

        if (friendsList != null) {
            mFriendList = friendsList;
        }
        if (mFriendList != null && mFriendList.size() > 0) {
            handleFriendDataForSort();
            mNoFriends.setVisibility(View.GONE);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);
        if (isReloadList) {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter.updateListView(mFriendList);
        } else {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter = new FriendListAdapter(getActivity(), mFriendList);

            mListView.setAdapter(mFriendListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        startFriendDetailsPage(mFriendList.get(position - 1));
                    } else {
                        startFriendDetailsPage(mFilteredFriendList.get(position));
                    }
                }
            });


            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Friend bean = mFriendList.get(position - 1);
                    startFriendDetailsPage(bean);
                    return true;
                }
            });
            mSearchEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                    filterData(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() != 0) {
                        if (mListView.getHeaderViewsCount() > 0) {
                            mListView.removeHeaderView(mHeadView);
                        }
                    } else {
                        if (mListView.getHeaderViewsCount() == 0) {
                            mListView.addHeaderView(mHeadView);
                        }
                    }
                }
            });
        }
    }

    private void updatePersonalUI() {
        SharedPreferences sp = SealAppContext.getInstance().getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        mId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");
        mCacheName = sp.getString(SealConst.SEALTALK_LOGIN_NAME, "");
        final String header = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        mNameTextView.setText(mCacheName);
        if (!TextUtils.isEmpty(mId)) {
            UserInfo userInfo = new UserInfo(mId, mCacheName, Uri.parse(header));
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(userInfo);
            ImageLoader.getInstance().displayImage(portraitUri, mSelectableRoundedImageView, App.getOptions());
        }
    }

    private void handleFriendDataForSort() {
        for (Friend friend : mFriendList) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            return spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
        } else {
            return "#";
        }
    }
}
