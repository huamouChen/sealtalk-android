package cn.chenhuamou.im.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.WriterException;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.DBManager;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.db.GroupMember;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.db.GroupsDao;
import cn.chenhuamou.im.model.SealSearchConversationResult;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.pinyin.CharacterParser;
import cn.chenhuamou.im.server.response.DismissGroupResponse;
import cn.chenhuamou.im.server.response.GetGroupInfoResponse;
import cn.chenhuamou.im.server.response.GetRongGroupInfoResponse;
import cn.chenhuamou.im.server.response.GetRongGroupMembersResponse;
import cn.chenhuamou.im.server.response.GetRongGroupResponse;
import cn.chenhuamou.im.server.response.QiNiuTokenResponse;
import cn.chenhuamou.im.server.response.QuitGroupResponse;
import cn.chenhuamou.im.server.response.QuitMyGroupResponse;
import cn.chenhuamou.im.server.response.SetGroupDisplayNameResponse;
import cn.chenhuamou.im.server.response.SetGroupNameResponse;
import cn.chenhuamou.im.server.response.SetGroupPortraitResponse;
import cn.chenhuamou.im.server.utils.CommonUtils;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.OperationRong;
import cn.chenhuamou.im.server.utils.RongGenerate;
import cn.chenhuamou.im.server.utils.json.JsonMananger;
import cn.chenhuamou.im.server.utils.photo.PhotoUtils;
import cn.chenhuamou.im.server.widget.BottomMenuDialog;
import cn.chenhuamou.im.server.widget.DialogWithYesOrNoUtils;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.server.widget.SelectableRoundedImageView;
import cn.chenhuamou.im.ui.widget.DemoGridView;
import cn.chenhuamou.im.ui.widget.switchbutton.SwitchButton;
import cn.chenhuamou.im.utils.QRCodeUtils;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.utilities.PromptPopupDialog;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/27.
 * Company RongCloud
 */
public class GroupDetailActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;

    public static final String UPDATE_GROUP_MEMBER = "update_group_member";

    private static final int DISMISS_GROUP = 26;
    private static final int QUIT_GROUP = 27;
    private static final int SET_GROUP_NAME = 29;
    private static final int GET_GROUP_INFO = 30;
    private static final int UPDATE_GROUP_NAME = 32;
    private static final int GET_QI_NIU_TOKEN = 133;
    private static final int UPDATE_GROUP_HEADER = 25;
    private static final int SEARCH_TYPE_FLAG = 1;
    private static final int CHECKGROUPURL = 39;


    private static final int PERMISSIONS_FOR_TAKE_PHOTO = 10;
    private static final int PERMISSIONS_FOR_PICK_IMAGE = 11;
    //图片文件路径
    private String picPath;
    //图片对应Uri
    private Uri photoUri;
    //拍照对应RequestCode
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    //裁剪图片
    private static final int CROP_PICTURE = 3;


    private boolean isCreated = false;
    private DemoGridView mGridView;
    private List<GroupMember> mGroupMember = new ArrayList<>();
    private TextView mTextViewMemberSize, mGroupDisplayNameText;
    private String groupPortrait = "";
    private SelectableRoundedImageView mGroupHeader, mGroupQRCodeImg;
    private SwitchButton messageTop, messageNotification;
    private Groups mGroup;
    private String fromConversationId;
    private Conversation.ConversationType mConversationType;
    private boolean isFromConversation;
    private LinearLayout mGroupAnnouncementDividerLinearLayout;
    private TextView mGroupName;

    private BottomMenuDialog dialog;

    private String imageUrl;
    private Uri selectUri;
    private byte[] portraitBytes;  // 群头像流
    private String newGroupName;
    private LinearLayout mGroupNotice;
    private LinearLayout mSearchMessagesLinearLayout;
    private Button mDismissBtn;
    private Button mQuitBtn;
    private SealSearchConversationResult mResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_group);

        //群组会话界面点进群组详情
        fromConversationId = getIntent().getStringExtra("TargetId");
        mConversationType = (Conversation.ConversationType) getIntent().getSerializableExtra("conversationType");

        initViews();
        setTitle(R.string.group_info);


        if (!TextUtils.isEmpty(fromConversationId)) {
            isFromConversation = true;
        }

        if (isFromConversation) {//群组会话页进入
            getGroups();
            getGroupMembers();
        }


        SealAppContext.getInstance().pushActivity(this);

        setGroupsInfoChangeListener();

    }

    private void getGroups() {
        SealUserInfoManager.getInstance().getGroupsByID(fromConversationId, new SealUserInfoManager.ResultCallback<Groups>() {
            @Override
            public void onSuccess(Groups groups) {
                if (groups != null) {
                    mGroup = groups;
                    initGroupData();
                }
            }

            @Override
            public void onError(String errString) {

            }
        });
    }

    private void getGroupMembers() {

        SealUserInfoManager.getInstance().getGroupMembers(fromConversationId, new SealUserInfoManager.ResultCallback<List<GroupMember>>() {
            @Override
            public void onSuccess(List<GroupMember> groupMembers) {
                LoadDialog.dismiss(mContext);
                if (groupMembers != null && groupMembers.size() > 0) {
                    mGroupMember = groupMembers;
                    initGroupMemberData();
                } else {
                    //群组添加人员，主要是因为创建群组的时候，后台发送的消息不全切类型不对，懒得和他说，免得接口又有问题
                    SealUserInfoManager.getInstance().getGroups(fromConversationId);
                    SealUserInfoManager.getInstance().getGroupMember(fromConversationId);
                    BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, fromConversationId);
                    getGroupMembers();
                }
            }

            @Override
            public void onError(String errString) {
                LoadDialog.dismiss(mContext);
            }
        });
    }

    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_NAME);
        BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_GROUP_MEMBER);
        BroadcastManager.getInstance(this).destroy(SealAppContext.GROUP_DISMISS);
        super.onDestroy();
    }

    private void initGroupData() {
        // 群组头像
        String portraitUri = "";
        if (mGroup.getHeaderImage() == null || mGroup.getHeaderImage().isEmpty()) {
            portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mGroup);
        } else {
            portraitUri = BaseAction.DOMAIN + mGroup.getHeaderImage();
        }
        groupPortrait = portraitUri;
        ImageLoader.getInstance().displayImage(portraitUri, mGroupHeader, App.getOptions());

        mGroupName.setText(mGroup.getName());

        if (RongIM.getInstance() != null) {
            String groupId = mGroup.getUserId();
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, groupId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {
                    if (conversation == null) {
                        return;
                    }
                    if (conversation.isTop()) {
                        messageTop.setChecked(true);
                    } else {
                        messageTop.setChecked(false);
                    }

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });

            // 消息免打扰的状态
            RongIM.getInstance().getConversationNotificationStatus(Conversation.ConversationType.GROUP, mGroup.getGroupsId(), new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                @Override
                public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {

                    if (conversationNotificationStatus == Conversation.ConversationNotificationStatus.DO_NOT_DISTURB) {
                        messageNotification.setChecked(true);
                    } else {
                        messageNotification.setChecked(false);
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }

        // 当前用户登录账号
        String loginId = getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
        if (mGroup.getRole() != null && mGroup.getRole().equals(loginId))
            isCreated = true;
        if (!isCreated) {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mGroupNotice.setVisibility(View.VISIBLE);
        } else {
            mGroupAnnouncementDividerLinearLayout.setVisibility(View.VISIBLE);
            mDismissBtn.setVisibility(View.VISIBLE);
            mQuitBtn.setVisibility(View.GONE);
            mGroupNotice.setVisibility(View.VISIBLE);
        }
        if (CommonUtils.isNetworkConnected(mContext)) {
            request(CHECKGROUPURL);
        }
    }

    private void initGroupMemberData() {
        if (mGroupMember != null && mGroupMember.size() > 0) {
            setTitle(getString(R.string.group_info) + "(" + mGroupMember.size() + ")");
            mTextViewMemberSize.setText(getString(R.string.group_member_size) + "(" + mGroupMember.size() + ")");
            mGridView.setAdapter(new GridAdapter(mContext, mGroupMember));
        } else {
            return;
        }

        for (GroupMember member : mGroupMember) {
            if (member.getUserId().equals(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""))) {
                if (!TextUtils.isEmpty(member.getDisplayName())) {
                    mGroupDisplayNameText.setText(member.getDisplayName());
                } else {
                    mGroupDisplayNameText.setText("无");
                }
            }
        }
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case QUIT_GROUP:
                return action.quitMyGroup(fromConversationId);
            case DISMISS_GROUP:
                return action.dissmissGroup(fromConversationId);
            case SET_GROUP_NAME:
                return action.setGroupDisplayName(fromConversationId, newGroupName);
            case GET_GROUP_INFO:
                return action.getGroupInfo(fromConversationId);
            case UPDATE_GROUP_HEADER:
                return action.setGroupPortrait(fromConversationId, portraitBytes);
            case GET_QI_NIU_TOKEN:
                return action.getQiNiuToken();
            case UPDATE_GROUP_NAME:
                return action.setGroupName(fromConversationId, newGroupName);
            case CHECKGROUPURL:
                return action.getGroupInfo(fromConversationId);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case QUIT_GROUP:  // 离开群组
                    QuitMyGroupResponse response = (QuitMyGroupResponse) result;
                    if (response.getCode().getCodeId().equals("100")) {
                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        NToast.shortToast(mContext, getString(R.string.quit_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;

                case DISMISS_GROUP:
                    DismissGroupResponse response1 = (DismissGroupResponse) result;
                    if (response1.getCode().getCodeId().equals("100")) {
                        RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Conversation>() {
                            @Override
                            public void onSuccess(Conversation conversation) {
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, fromConversationId, null);
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode e) {

                                    }
                                });
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {

                            }
                        });
                        SealUserInfoManager.getInstance().deleteGroups(new Groups(fromConversationId));
                        SealUserInfoManager.getInstance().deleteGroupMembers(fromConversationId);
                        BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.GROUP_LIST_UPDATE);
                        setResult(501, new Intent());
                        NToast.shortToast(mContext, getString(R.string.dismiss_success));
                        LoadDialog.dismiss(mContext);
                        finish();
                    }
                    break;

                case SET_GROUP_NAME:
                    SetGroupDisplayNameResponse response2 = (SetGroupDisplayNameResponse) result;
                    if (response2.getCode() == 200) {
                        request(GET_GROUP_INFO);
                    }
                    break;
                case GET_GROUP_INFO:
                    GetGroupInfoResponse response3 = (GetGroupInfoResponse) result;
                    if (response3.getCode() == 200) {
                        int i;
                        if (isCreated) {
                            i = 0;
                        } else {
                            i = 1;
                        }
                        GetGroupInfoResponse.ResultEntity bean = response3.getResult();
                        SealUserInfoManager.getInstance().addGroup(
                                new Groups(bean.getId(), bean.getName(), bean.getPortraitUri(), newGroupName, String.valueOf(i), null)
                        );
                        mGroupName.setText(newGroupName);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, Uri.parse(bean.getPortraitUri())));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;
                case UPDATE_GROUP_HEADER:
                    SetGroupPortraitResponse response5 = (SetGroupPortraitResponse) result;
                    if (response5.getCode().getCodeId().equals("100")) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                        String portriatString = (response5.getValue().getGroupImage() != null && !response5.getValue().getGroupImage().isEmpty()) ? (BaseAction.DOMAIN + response5.getValue().getGroupImage()) : "";
                        ImageLoader.getInstance().displayImage(portriatString, mGroupHeader, App.getOptions());
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, mGroup.getName(), Uri.parse(portriatString)));
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "更改失败");
                    }

                    break;

                case UPDATE_GROUP_NAME:
                    SetGroupNameResponse response7 = (SetGroupNameResponse) result;
                    if (response7.getCode() == 200) {
                        SealUserInfoManager.getInstance().addGroup(
                                new Groups(mGroup.getGroupsId(), newGroupName, mGroup.getPortraitUri(), mGroup.getRole())
                        );
                        mGroupName.setText(newGroupName);
                        RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, TextUtils.isEmpty(mGroup.getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(newGroupName, mGroup.getGroupsId())) : Uri.parse(mGroup.getPortraitUri())));
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.update_success));
                    }
                    break;
                case CHECKGROUPURL:
                    GetGroupInfoResponse groupInfoResponse = (GetGroupInfoResponse) result;
                    if (groupInfoResponse.getCode() == 200) {
                        if (groupInfoResponse.getResult() != null) {
                            mGroupName.setText(groupInfoResponse.getResult().getName());
                            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(groupInfoResponse);
                            ImageLoader.getInstance().displayImage(portraitUri, mGroupHeader, App.getOptions());
                            RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, groupInfoResponse.getResult().getName(), TextUtils.isEmpty(groupInfoResponse.getResult().getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(groupInfoResponse.getResult().getName(), groupInfoResponse.getResult().getId())) : Uri.parse(groupInfoResponse.getResult().getPortraitUri())));
                        }
                    }
                    break;
            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case QUIT_GROUP:
                NToast.shortToast(mContext, "退出群组请求失败");
                LoadDialog.dismiss(mContext);
                break;
            case DISMISS_GROUP:
                NToast.shortToast(mContext, "解散群组请求失败");
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            // 群二维码
            case R.id.ll_group_qr_code:
                Intent qrIntent = new Intent(this, MyQRCodeActivity.class);
                qrIntent.putExtra(SealConst.IsFromGroup, true);
                qrIntent.putExtra(SealConst.TargetId, fromConversationId);
                qrIntent.putExtra(SealConst.Nick_Name, mGroup.getName());
                qrIntent.putExtra(SealConst.Portrait, groupPortrait);
                startActivity(qrIntent);
                break;
            case R.id.group_quit:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, getString(R.string.confirm_quit_group), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        LoadDialog.show(mContext);
                        request(QUIT_GROUP);
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.group_dismiss:
                DialogWithYesOrNoUtils.getInstance().showDialog(mContext, getString(R.string.confirm_dismiss_group), new DialogWithYesOrNoUtils.DialogCallBack() {
                    @Override
                    public void executeEvent() {
                        LoadDialog.show(mContext);
                        request(DISMISS_GROUP);
                    }

                    @Override
                    public void executeEditEvent(String editText) {

                    }

                    @Override
                    public void updatePassword(String oldPassword, String newPassword) {

                    }
                });
                break;
            case R.id.ac_ll_search_chatting_records:
                Intent searchIntent = new Intent(GroupDetailActivity.this, SealSearchChattingDetailActivity.class);
                searchIntent.putExtra("filterString", "");
                ArrayList<Message> arrayList = new ArrayList<>();
                searchIntent.putParcelableArrayListExtra("filterMessages", arrayList);
                mResult = new SealSearchConversationResult();
                Conversation conversation = new Conversation();
                conversation.setTargetId(fromConversationId);
                conversation.setConversationType(mConversationType);
                mResult.setConversation(conversation);
                Groups groupInfo = DBManager.getInstance().getDaoSession().getGroupsDao().queryBuilder().where(GroupsDao.Properties.GroupsId.eq(fromConversationId)).unique();
                if (groupInfo != null) {
                    String portraitUri = groupInfo.getPortraitUri();
                    mResult.setId(groupInfo.getGroupsId());

                    if (!TextUtils.isEmpty(portraitUri)) {
                        mResult.setPortraitUri(portraitUri);
                    }
                    if (!TextUtils.isEmpty(groupInfo.getName())) {
                        mResult.setTitle(groupInfo.getName());
                    } else {
                        mResult.setTitle(groupInfo.getGroupsId());
                    }

                    searchIntent.putExtra("searchConversationResult", mResult);
                    searchIntent.putExtra("flag", SEARCH_TYPE_FLAG);
                    startActivity(searchIntent);
                }
                break;
            case R.id.group_clean:
                PromptPopupDialog.newInstance(mContext,
                        getString(R.string.clean_group_chat_history)).setLayoutRes(io.rong.imkit.R.layout.rc_dialog_popup_prompt_warning)
                        .setPromptButtonClickedListener(new PromptPopupDialog.OnPromptButtonClickedListener() {
                            @Override
                            public void onPositiveButtonClicked() {
                                if (RongIM.getInstance() != null) {
//                                    if (mGroup != null) {
                                    RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, fromConversationId, new RongIMClient.ResultCallback<Boolean>() {
                                        @Override
                                        public void onSuccess(Boolean aBoolean) {
                                            NToast.shortToast(mContext, getString(R.string.clear_success));
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode errorCode) {
                                            NToast.shortToast(mContext, getString(R.string.clear_failure));
                                        }
                                    });
                                    RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, fromConversationId, System.currentTimeMillis(), null);
//                                    }
                                }
                            }
                        }).show();

                break;
            case R.id.group_member_size_item:
                Intent intent = new Intent(mContext, TotalGroupMemberActivity.class);
                intent.putExtra("targetId", fromConversationId);
                startActivity(intent);
                break;
            case R.id.group_member_online_status:
                intent = new Intent(mContext, MembersOnlineStatusActivity.class);
                intent.putExtra("targetId", fromConversationId);
                startActivity(intent);
                break;
            case R.id.ll_group_port:
                if (isCreated) {
                    showPhotoDialog();
                }
                break;
            case R.id.ll_group_name:
                if (isCreated) {
                    DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.new_group_name), getString(R.string.confirm), new DialogWithYesOrNoUtils.DialogCallBack() {
                        @Override
                        public void executeEvent() {

                        }

                        @Override
                        public void executeEditEvent(String editText) {
                            if (TextUtils.isEmpty(editText)) {
                                return;
                            }
                            if (editText.length() < 2 && editText.length() > 10) {
                                NToast.shortToast(mContext, "群名称应为 2-10 字");
                                return;
                            }

                            if (AndroidEmoji.isEmoji(editText) && editText.length() < 4) {
                                NToast.shortToast(mContext, "群名称表情过短");
                                return;
                            }
                            newGroupName = editText;
                            LoadDialog.show(mContext);
                            request(UPDATE_GROUP_NAME);
                        }

                        @Override
                        public void updatePassword(String oldPassword, String newPassword) {

                        }
                    });
                }
                break;
            case R.id.group_announcement:
                Intent tempIntent = new Intent(mContext, GroupNoticeActivity.class);
                tempIntent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
                tempIntent.putExtra("targetId", fromConversationId);
                startActivity(tempIntent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.sw_group_top:
                if (isChecked) {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), true);
                    }
                } else {
                    if (mGroup != null) {
                        OperationRong.setConversationTop(mContext, Conversation.ConversationType.GROUP, mGroup.getGroupsId(), false);
                    }
                }
                break;
            case R.id.sw_group_notfaction:
                if (isChecked) {
                    if (fromConversationId != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, fromConversationId, true);
                    }
                } else {
                    if (fromConversationId != null) {
                        OperationRong.setConverstionNotif(mContext, Conversation.ConversationType.GROUP, fromConversationId, false);
                    }
                }

                break;
        }
    }


    private class GridAdapter extends BaseAdapter {

        private List<GroupMember> list;
        Context context;


        public GridAdapter(Context context, List<GroupMember> list) {
            if (list.size() >= 31) {
                this.list = list.subList(0, 30);
            } else {
                this.list = list;
            }

            this.context = context;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.social_chatsetting_gridview_item, parent, false);
            }
            SelectableRoundedImageView iv_avatar = (SelectableRoundedImageView) convertView.findViewById(R.id.iv_avatar);
            TextView tv_username = (TextView) convertView.findViewById(R.id.tv_username);
            ImageView badge_delete = (ImageView) convertView.findViewById(R.id.badge_delete);

            // 最后一个item，减人按钮   // 减号按钮 踢人按钮
            if (position == getCount() - 1 && isCreated) {
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.icon_btn_deleteperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isDeleteGroupMember", true);
                        intent.putExtra("GroupId", mGroup.getGroupsId());
                        intent.putExtra("GroupName", mGroup.getName());
                        startActivityForResult(intent, 101);
                    }

                });
            } else if ((isCreated && position == getCount() - 2) || (!isCreated && position == getCount() - 1)) { // 加号按钮 邀请好友
                tv_username.setText("");
                badge_delete.setVisibility(View.GONE);
                iv_avatar.setImageResource(R.drawable.jy_drltsz_btn_addperson);

                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(GroupDetailActivity.this, SelectFriendsActivity.class);
                        intent.putExtra("isAddGroupMember", true);
                        intent.putExtra("GroupId", mGroup.getGroupsId());
                        startActivityForResult(intent, 100);

                    }
                });
            } else { // 普通成员
                final GroupMember bean = list.get(position);
                Friend friend = SealUserInfoManager.getInstance().getFriendByID(bean.getUserId());
                if (friend != null && !TextUtils.isEmpty(friend.getDisplayName())) {
                    tv_username.setText(friend.getDisplayName());
                } else {
                    tv_username.setText(bean.getName());
                }

                String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(bean);
                ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, App.getOptions());
                iv_avatar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserInfo userInfo = new UserInfo(bean.getUserId(), bean.getName(), TextUtils.isEmpty(bean.getPortraitUri().toString()) ? Uri.parse(RongGenerate.generateDefaultAvatar(bean.getName(), bean.getUserId())) : bean.getPortraitUri());
                        Intent intent = new Intent(context, UserDetailActivity.class);
                        Friend friend = CharacterParser.getInstance().generateFriendFromUserInfo(userInfo);
                        intent.putExtra("friend", friend);
                        intent.putExtra("conversationType", Conversation.ConversationType.GROUP.getValue());
                        //Groups not Serializable,just need group name
                        intent.putExtra("groupName", mGroup.getName());
                        intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                        // 群组不允许互相添加好友，传个参数过去判断是否是由群组点开的
                        intent.putExtra(SealConst.IsFromGroup, true);
                        context.startActivity(intent);
                    }

                });

            }

            return convertView;
        }

        @Override
        public int getCount() {
            if (isCreated) {
                return list.size() + 2;
            } else {
                return list.size() + 1;
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<GroupMember> list) {
            this.list = list;
            notifyDataSetChanged();
        }

    }


    // 拿到新增的成员刷新adapter
    @Override
    @SuppressWarnings("unchecked")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 拍照
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                }
                if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
                    photoUri = Uri.fromFile(new File(picPath));
                    if (Build.VERSION.SDK_INT > 23) {
                        photoUri = FileProvider.getUriForFile(this, "cn.chenhuamou.im.fileprovider", new File(picPath));
                        cropForN(picPath, CROP_PICTURE);
                    } else {
                        startPhotoZoom(photoUri, CROP_PICTURE);
                    }
                } else {
                    //错误提示
                    NToast.shortToast(mContext, "头像设置失败");
                }
            }

            // 裁剪后的图片
            if (requestCode == CROP_PICTURE) {
                if (photoUri != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                    if (bitmap != null) {
                        mGroupHeader.setImageBitmap(bitmap);
                        portraitBytes = bitMap2StringBase64(bitmap);
                        LoadDialog.show(mContext);
                        request(UPDATE_GROUP_HEADER);
                    }
                } else {
                    NToast.shortToast(mContext, "头像设置失败");
                }
            }
        }

        // 从相册获取
        if (requestCode == PERMISSIONS_FOR_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    mGroupHeader.setImageBitmap(bit);
                    portraitBytes = bitMap2StringBase64(bit);
                    LoadDialog.show(mContext);
                    request(UPDATE_GROUP_HEADER);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    NToast.shortToast(mContext, "头像设置失败");
                }
            } else {
                NToast.shortToast(mContext, "头像设置失败");
            }
        }

        if (requestCode == 100 || requestCode == 101) {
            if (data != null) {
                List<Friend> newMemberData = (List<Friend>) data.getSerializableExtra("newAddMember");
                List<Friend> deleMember = (List<Friend>) data.getSerializableExtra("deleteMember");
                if (newMemberData != null && newMemberData.size() > 0) {
                    for (Friend friend : newMemberData) {
                        GroupMember member = new GroupMember(fromConversationId,
                                friend.getUserId(),
                                friend.getName(),
                                friend.getPortraitUri(),
                                null);
                        mGroupMember.add(1, member);
                    }


                    initGroupMemberData();
                } else if (deleMember != null && deleMember.size() > 0) {
                    for (Friend friend : deleMember) {
                        for (GroupMember member : mGroupMember) {
                            if (member.getUserId().equals(friend.getUserId())) {
                                mGroupMember.remove(member);
                                break;
                            }
                        }
                    }
                    initGroupMemberData();
                }


                // 刷新本地数据，更新群组成员的本地数据库
                SealUserInfoManager.getInstance().getGroups(fromConversationId);
                SealUserInfoManager.getInstance().getGroupMember(fromConversationId);
                BroadcastManager.getInstance(mContext).sendBroadcast(UPDATE_GROUP_MEMBER, fromConversationId);


            }
        }


    }

    private void setGroupsInfoChangeListener() {
        //有些权限只有群主有,比如修改群名称等,已经更新UI不需要再更新
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_NAME, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String result = intent.getStringExtra("result");
                    if (result != null) {
                        try {
                            List<String> nameList = JsonMananger.jsonToBean(result, List.class);
                            if (nameList.size() != 3)
                                return;
                            String groupID = nameList.get(0);
                            if (groupID != null && !groupID.equals(fromConversationId))
                                return;
                            if (mGroup != null && mGroup.getRole().equals("0"))
                                return;
                            String groupName = nameList.get(1);
                            String operationName = nameList.get(2);
                            mGroupName.setText(groupName);
                            newGroupName = groupName;
                            NToast.shortToast(mContext, operationName + context.getString(R.string.rc_item_change_group_name)
                                    + "\"" + groupName + "\"");
                            RongIM.getInstance().refreshGroupInfoCache(new Group(fromConversationId, newGroupName, TextUtils.isEmpty(mGroup.getPortraitUri()) ? Uri.parse(RongGenerate.generateDefaultAvatar(newGroupName, mGroup.getGroupsId())) : Uri.parse(mGroup.getPortraitUri())));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_GROUP_MEMBER, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String groupID = intent.getStringExtra("String");
                    if (groupID != null && groupID.equals(fromConversationId))
                        getGroupMembers();
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealAppContext.GROUP_DISMISS, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null) {
                    String groupID = intent.getStringExtra("String");
                    if (groupID != null && groupID.equals(fromConversationId)) {
                        if (mGroup.getRole().equals("1"))
                            backAsGroupDismiss();
                    }
                }
            }
        });
    }

    private void backAsGroupDismiss() {
        this.setResult(501, new Intent());
        finish();
    }


    /*
     * 前台处理图片
     * */
    private byte[] bitMap2StringBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        return bos.toByteArray();
    }


    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);

        // 拍照
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                //小于6.0版本直接操作
                if (Build.VERSION.SDK_INT < 23) {
                    takePictures();
                } else {
                    //6.0以后权限处理
                    permissionForM();
                }
            }
        });

        // 从本地导入
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                choosePhoto();
            }
        });
        dialog.show();
    }

    /*
     * 从相册获取图片
     * */
    private void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//相片类型
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PERMISSIONS_FOR_PICK_IMAGE);
    }


    /**
     * 拍照获取图片
     */
    private void takePictures() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "手机未插入内存卡", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 图片裁剪，参数根据自己需要设置
     *
     * @param uri
     * @param REQUE_CODE_CROP
     */
    private void startPhotoZoom(Uri uri,
                                int REQUE_CODE_CROP) {
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);//输出是X方向的比例
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 600);//输出X方向的像素
        intent.putExtra("outputY", 450);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * 7.0以上版本图片裁剪操作
     *
     * @param imagePath
     * @param REQUE_CODE_CROP
     */
    private void cropForN(String imagePath, int REQUE_CODE_CROP) {
        Uri cropUri = getImageContentUri(new File(imagePath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");
        intent.putExtra("crop", "true");
        //输出是X方向的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 450);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUE_CODE_CROP);
    }


    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 安卓6.0以上版本权限处理
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_FOR_TAKE_PHOTO);
        } else {
            takePictures();
        }
    }

    /*
     * android 6.0 动态申请权限
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_FOR_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictures();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void initViews() {
        messageTop = findViewById(R.id.sw_group_top);
        messageNotification = findViewById(R.id.sw_group_notfaction);
        messageTop.setOnCheckedChangeListener(this);
        messageNotification.setOnCheckedChangeListener(this);
        LinearLayout groupClean = findViewById(R.id.group_clean);
        mGridView = findViewById(R.id.gridview);
        mTextViewMemberSize = findViewById(R.id.group_member_size);
        mGroupHeader = findViewById(R.id.group_header);
        LinearLayout mGroupDisplayName = findViewById(R.id.group_displayname);
        mGroupDisplayNameText = findViewById(R.id.group_displayname_text);
        mGroupName = findViewById(R.id.group_name);
        mQuitBtn = findViewById(R.id.group_quit);
        mDismissBtn = findViewById(R.id.group_dismiss);
        RelativeLayout totalGroupMember = findViewById(R.id.group_member_size_item);
        RelativeLayout memberOnlineStatus = findViewById(R.id.group_member_online_status);
        LinearLayout mGroupPortL = findViewById(R.id.ll_group_port);
        LinearLayout mGroupNameL = findViewById(R.id.ll_group_name);
        mGroupAnnouncementDividerLinearLayout = findViewById(R.id.ac_ll_group_announcement_divider);
        mGroupNotice = findViewById(R.id.group_announcement);
        mSearchMessagesLinearLayout = findViewById(R.id.ac_ll_search_chatting_records);
        mGroupPortL.setOnClickListener(this);
        mGroupNameL.setOnClickListener(this);
        totalGroupMember.setOnClickListener(this);
        mGroupDisplayName.setOnClickListener(this);
        memberOnlineStatus.setOnClickListener(this);
        if (getSharedPreferences("config", Context.MODE_PRIVATE).getBoolean("isDebug", false)) {
            memberOnlineStatus.setVisibility(View.VISIBLE);
        }

        // 群二维码
        mGroupQRCodeImg = findViewById(R.id.group_qr_code);
        // 二维码
        try {
            Bitmap bitmapQRCode = QRCodeUtils.createQRCode(fromConversationId, 30);
            mGroupQRCodeImg.setBackground(new BitmapDrawable(bitmapQRCode));
        } catch (WriterException e) {
            e.printStackTrace();
        }
        // 点击群二维码
        findViewById(R.id.ll_group_qr_code).setOnClickListener(this);


        mQuitBtn.setOnClickListener(this);
        mDismissBtn.setOnClickListener(this);
        groupClean.setOnClickListener(this);
        mGroupNotice.setOnClickListener(this);
        mSearchMessagesLinearLayout.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        SealAppContext.getInstance().popActivity(this);
        super.onBackPressed();
    }
}
