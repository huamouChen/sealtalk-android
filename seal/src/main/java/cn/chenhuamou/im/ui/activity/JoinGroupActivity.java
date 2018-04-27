package cn.chenhuamou.im.ui.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetRongGroupInfoResponse;
import cn.chenhuamou.im.server.response.InviteMyGroupResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Rex on 2018/4/27.
 * Email chenhm4444@gmail.com
 */
public class JoinGroupActivity extends BaseActivity implements View.OnClickListener {

    public static final String REFRESH_GROUP_UI = "REFRESH_GROUP_UI";

    private String targetId;
    private List<String> members; // 要加入群组的成员列表
    private String groupName = "", groupPortrait = "";

    private ImageView mGroupHeaderImg;
    private TextView mGroupNameTv, mGroupMemberNumTv;
    private Button btn_comfirm;

    private static final int GET_GROUP_INFO = 1000;
    private static final int JOIN_GROUP = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_group);

        initData();

        initView();

        request(GET_GROUP_INFO);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_GROUP_INFO:
                return action.getGroupInfo(targetId);
            case JOIN_GROUP:
                return action.inviteMyGroup(targetId, groupName, members);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case GET_GROUP_INFO:
                GetRongGroupInfoResponse getRongGroupInfoResponse = (GetRongGroupInfoResponse)result;
                if (getRongGroupInfoResponse.getCode().getCodeId().equals("100")) {
                    groupName = getRongGroupInfoResponse.getValue().getGroupName();
                    mGroupNameTv.setText(groupName);
                    // 设置头像
                    if (!getRongGroupInfoResponse.getValue().getGroupImage().isEmpty()) {
                        groupPortrait = BaseAction.DOMAIN + getRongGroupInfoResponse.getValue().getGroupImage();
                    }
                    String portraitUri = SealUserInfoManager.getInstance().getPortraitUri
                            (new UserInfo(targetId, groupName, Uri.parse(groupPortrait)));
                    ImageLoader.getInstance().displayImage(portraitUri, mGroupHeaderImg, App.getOptions());

                } else {
                    NToast.shortToast(mContext, "获取群组消息失败，请重试");
                }
                break;
            case JOIN_GROUP:
                InviteMyGroupResponse inviteMyGroupResponse = (InviteMyGroupResponse)result;
                if (inviteMyGroupResponse.getCode().getCodeId().equals("100")) { // 成功加入群组
                    // 成功加入群组，开启会话，刷新本地群组数据库
                    Groups groups = new Groups(targetId, groupName, groupPortrait);
                    SealUserInfoManager.getInstance().addGroup(groups);
                    BroadcastManager.getInstance(this).sendBroadcast(REFRESH_GROUP_UI, groups);
                    RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, targetId, groupName);
                    finish();
                } else {
                    NToast.shortToast(mContext, "加入群组失败，请重试");
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case GET_GROUP_INFO:
                NToast.shortToast(mContext, "获取群组消息失败，请重试");
                break;
            case JOIN_GROUP:
                NToast.shortToast(mContext, "加入群组失败，请重试");
                break;
        }
    }

    /*
     * 初始化数据
     * */
    private void initData() {
        // 获取传过来的值
        targetId = getIntent().getStringExtra(SealConst.TargetId);
        // 初始化加入群组的成员数组，扫描进来的，元素也就是当前用户
        members = new ArrayList<>();
        String userId = getSharedPreferences(SealConst.SharedPreferencesName, MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
        members.add(userId);
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        setTitle("加入群组");
        mGroupHeaderImg = findViewById(R.id.img_group_header);
        mGroupNameTv = findViewById(R.id.tv_group_name);
        mGroupMemberNumTv = findViewById(R.id.tv_group_member_num);
        btn_comfirm = findViewById(R.id.btn_comfirm);

        btn_comfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        LoadDialog.show(mContext);
        request(JOIN_GROUP);
    }
}
