package cn.chenhuamou.im.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.alibaba.fastjson.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.pinyin.CharacterParser;
import cn.chenhuamou.im.server.response.AgreeFriendApplyResponse;
import cn.chenhuamou.im.server.response.ExtraResponse;
import cn.chenhuamou.im.server.response.UserRelationshipResponse;
import cn.chenhuamou.im.server.utils.CommonUtils;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.json.JsonMananger;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.adapter.NewFriendListAdapter;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.message.ContactNotificationMessage;


public class NewFriendListActivity extends BaseActivity implements NewFriendListAdapter.OnItemButtonClick, View.OnClickListener {

    private static final int GET_ALL = 11;
    private static final int AGREE_FRIENDS = 12;
    public static final int FRIEND_LIST_REQUEST_CODE = 1001;
    private ListView shipListView;
    private NewFriendListAdapter adapter;
    private String friendId;
    private TextView isData;
    private UserRelationshipResponse userRelationshipResponse = new UserRelationshipResponse();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friendlist);
        initView();
        if (!CommonUtils.isNetworkConnected(mContext)) {
            NToast.shortToast(mContext, R.string.check_network);
            return;
        }
        LoadDialog.show(mContext);
        request(GET_ALL);
        adapter = new NewFriendListAdapter(mContext);
        shipListView.setAdapter(adapter);

        initMyData();
    }


    private void initMyData() {

        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<UserRelationshipResponse.ResultEntity> list = new ArrayList<>();
                for (Conversation item : conversations) {
                    if (item.getObjectName().equals("RC:ContactNtf")) {
                        UserRelationshipResponse.ResultEntity resultEntity = new UserRelationshipResponse.ResultEntity();
                        resultEntity.setDisplayName(item.getTargetId());
                        resultEntity.setMessage(item.getTargetId() + " 想加你为好友");
                        resultEntity.setStatus(11);
                        resultEntity.setUpdatedAt("");
                        String applyId = "";
                        if (item.getLatestMessage() instanceof ContactNotificationMessage) {
                            ContactNotificationMessage message = (ContactNotificationMessage) item.getLatestMessage();
                            String extraJson = message.getExtra();
                            if (!TextUtils.isEmpty(extraJson)) {
                                JSONObject jsonObject= JSONObject.parseObject(extraJson);
                                applyId = jsonObject.getIntValue("ApplyId") + "";
                            }
                        }

                        resultEntity.setUser(new UserRelationshipResponse.ResultEntity.UserEntity(item.getTargetId(), item.getTargetId(), applyId));
                        list.add(resultEntity);
                    }
                }

                adapter.removeAll();
                adapter.addData(list);
                adapter.notifyDataSetChanged();
                LoadDialog.dismiss(mContext);
                userRelationshipResponse.setResult(list);

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
            }
        });

        adapter.setOnItemButtonClick(this);
    }

    protected void initView() {
        setTitle(R.string.new_friends);
        shipListView = (ListView) findViewById(R.id.shiplistview);
        isData = (TextView) findViewById(R.id.isData);
        Button rightButton = getHeadRightButton();
        rightButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.de_address_new_friend));
        rightButton.setOnClickListener(this);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_ALL:
                return action.getFriendList();
            case AGREE_FRIENDS:
                // applyid 放在消息的 extra 里面， 然后保存在 用户的头像地址里面
                UserRelationshipResponse.ResultEntity bean = userRelationshipResponse.getResult().get(index);
                return action.agreeFriend(bean.getUser().getPortraitUri());
        }
        return super.doInBackground(requestCode, id);
    }


    @Override
    @SuppressWarnings("unchecked")
    public void onSuccess(int requestCode, final Object result) {
        if (result != null) {
            switch (requestCode) {
                case GET_ALL:
                    LoadDialog.dismiss(mContext);
                    break;
                case AGREE_FRIENDS:
                    AgreeFriendApplyResponse agreeFriendApplyResponse = (AgreeFriendApplyResponse) result;
                    if (agreeFriendApplyResponse.getCode() != null) {
                        UserRelationshipResponse.ResultEntity bean = userRelationshipResponse.getResult().get(index);
                        SealUserInfoManager.getInstance().addFriend(new Friend(bean.getUser().getId(),
                                bean.getUser().getNickname(),
                                Uri.parse(bean.getUser().getPortraitUri()),
                                bean.getDisplayName(),
                                String.valueOf(bean.getStatus()),
                                null,
                                null,
                                null,
                                CharacterParser.getInstance().getSpelling(bean.getUser().getNickname()),
                                CharacterParser.getInstance().getSpelling(bean.getDisplayName())));
                        // 通知好友列表刷新数据
                        NToast.shortToast(mContext, R.string.agreed_friend);
                        LoadDialog.dismiss(mContext);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealAppContext.UPDATE_FRIEND);
                        request(GET_ALL); //刷新 UI 按钮
                    } else {
                        // 通知好友列表刷新数据
                        NToast.shortToast(mContext, "接受好友失败");
                        LoadDialog.dismiss(mContext);
                    }
            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case GET_ALL:
                break;

        }
    }


    @Override
    protected void onDestroy() {
        if (adapter != null) {
            adapter = null;
        }
        super.onDestroy();
    }

    private int index;

    @Override
    public boolean onButtonClick(int position, View view, int status) {
        index = position;
        switch (status) {
            case 11: //收到了好友邀请
                if (!CommonUtils.isNetworkConnected(mContext)) {
                    NToast.shortToast(mContext, R.string.check_network);
                    break;
                }
                LoadDialog.show(mContext);
                friendId = userRelationshipResponse.getResult().get(position).getUser().getId();
                request(AGREE_FRIENDS);
                break;
            case 10: // 发出了好友邀请
                break;
            case 21: // 忽略好友邀请
                break;
            case 20: // 已是好友
                break;
            case 30: // 删除了好友关系
                break;
        }
        return false;
    }

    private Date stringToDate(UserRelationshipResponse.ResultEntity resultEntity) {
        String updatedAt = resultEntity.getUpdatedAt();
        String updatedAtDateStr = updatedAt.substring(0, 10) + " " + updatedAt.substring(11, 16);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date updateAtDate = null;
        try {
            updateAtDate = simpleDateFormat.parse(updatedAtDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return updateAtDate;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(NewFriendListActivity.this, SearchFriendActivity.class);
        startActivityForResult(intent, FRIEND_LIST_REQUEST_CODE);
    }
}
