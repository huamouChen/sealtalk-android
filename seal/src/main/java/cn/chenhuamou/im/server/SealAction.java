package cn.chenhuamou.im.server;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.util.List;

import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.network.http.RequestParams;
import cn.chenhuamou.im.server.request.AddGroupMemberRequest;
import cn.chenhuamou.im.server.request.AddToBlackListRequest;
import cn.chenhuamou.im.server.request.AgreeFriendsRequest;
import cn.chenhuamou.im.server.request.ChangePasswordRequest;
import cn.chenhuamou.im.server.request.CheckPhoneRequest;
import cn.chenhuamou.im.server.request.CreateGroupRequest;
import cn.chenhuamou.im.server.request.CreateMyGroupRequest;
import cn.chenhuamou.im.server.request.DeleteFriendRequest;
import cn.chenhuamou.im.server.request.DeleteGroupMemberRequest;
import cn.chenhuamou.im.server.request.DismissGroupRequest;
import cn.chenhuamou.im.server.request.FriendInvitationRequest;
import cn.chenhuamou.im.server.request.JoinGroupRequest;
import cn.chenhuamou.im.server.request.JoinMyGroupRequest;
import cn.chenhuamou.im.server.request.KickMyGroupRequest;
import cn.chenhuamou.im.server.request.LoginRequest;
import cn.chenhuamou.im.server.request.QuitGroupRequest;
import cn.chenhuamou.im.server.request.RegisterRequest;
import cn.chenhuamou.im.server.request.RemoveFromBlacklistRequest;
import cn.chenhuamou.im.server.request.RestPasswordRequest;
import cn.chenhuamou.im.server.request.SendCodeRequest;
import cn.chenhuamou.im.server.request.SetFriendDisplayNameRequest;
import cn.chenhuamou.im.server.request.SetGroupDisplayNameRequest;
import cn.chenhuamou.im.server.request.SetGroupNameRequest;
import cn.chenhuamou.im.server.request.SetGroupPortraitRequest;
import cn.chenhuamou.im.server.request.SetNameRequest;
import cn.chenhuamou.im.server.request.SetPortraitRequest;
import cn.chenhuamou.im.server.request.VerifyCodeRequest;
import cn.chenhuamou.im.server.response.AddGroupMemberResponse;
import cn.chenhuamou.im.server.response.AddToBlackListResponse;
import cn.chenhuamou.im.server.response.AgreeFriendApplyResponse;
import cn.chenhuamou.im.server.response.AgreeFriendsResponse;
import cn.chenhuamou.im.server.response.ApplyFriendResponse;
import cn.chenhuamou.im.server.response.ChangePasswordResponse;
import cn.chenhuamou.im.server.response.CheckPhoneResponse;
import cn.chenhuamou.im.server.response.CreateGroupResponse;
import cn.chenhuamou.im.server.response.CreateMyGroupResponse;
import cn.chenhuamou.im.server.response.DefaultConversationResponse;
import cn.chenhuamou.im.server.response.DeleteFriendResponse;
import cn.chenhuamou.im.server.response.DeleteGroupMemberResponse;
import cn.chenhuamou.im.server.response.DismissGroupResponse;
import cn.chenhuamou.im.server.response.FriendInvitationResponse;
import cn.chenhuamou.im.server.response.GetBlackListResponse;
import cn.chenhuamou.im.server.response.GetFriendInfoByIDResponse;
import cn.chenhuamou.im.server.response.GetGroupInfoResponse;
import cn.chenhuamou.im.server.response.GetGroupMemberResponse;
import cn.chenhuamou.im.server.response.GetGroupResponse;
import cn.chenhuamou.im.server.response.GetRongGroupMembersResponse;
import cn.chenhuamou.im.server.response.GetRongGroupResponse;
import cn.chenhuamou.im.server.response.GetRongTokenResponse;
import cn.chenhuamou.im.server.response.GetUserInfoByIdResponse;
import cn.chenhuamou.im.server.response.GetUserInfoByPhoneResponse;
import cn.chenhuamou.im.server.response.GetUserInfoResponse;
import cn.chenhuamou.im.server.response.GetUserInfosResponse;
import cn.chenhuamou.im.server.response.IsAliveResponse;
import cn.chenhuamou.im.server.response.JoinGroupResponse;
import cn.chenhuamou.im.server.response.JoinMyGroupResponse;
import cn.chenhuamou.im.server.response.KickMyGroupResponse;
import cn.chenhuamou.im.server.response.KqwfPcddResponse;
import cn.chenhuamou.im.server.response.LoginResponse;
import cn.chenhuamou.im.server.response.QiNiuTokenResponse;
import cn.chenhuamou.im.server.response.QuitGroupResponse;
import cn.chenhuamou.im.server.response.QuitMyGroupResponse;
import cn.chenhuamou.im.server.response.RefuseFriendApplyResponse;
import cn.chenhuamou.im.server.response.RegisterResponse;
import cn.chenhuamou.im.server.response.RemoveFromBlackListResponse;
import cn.chenhuamou.im.server.response.RestPasswordResponse;
import cn.chenhuamou.im.server.response.SendCodeResponse;
import cn.chenhuamou.im.server.response.SetFriendDisplayNameResponse;
import cn.chenhuamou.im.server.response.SetGroupDisplayNameResponse;
import cn.chenhuamou.im.server.response.SetGroupNameResponse;
import cn.chenhuamou.im.server.response.SetGroupPortraitResponse;
import cn.chenhuamou.im.server.response.SetNameResponse;
import cn.chenhuamou.im.server.response.SetPortraitResponse;
import cn.chenhuamou.im.server.response.SyncTotalDataResponse;
import cn.chenhuamou.im.server.response.UserRelationshipResponse;
import cn.chenhuamou.im.server.response.VerifyCodeResponse;
import cn.chenhuamou.im.server.response.VersionResponse;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.json.JsonMananger;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class SealAction extends BaseAction {
//    private final String CONTENT_TYPE = "application/x-www-form-urlencoded";
    private final String CONTENT_TYPE = "application/json";
    private final String ENCODING = "utf-8";

    /**
     * 构造方法
     *
     * @param context 上下文
     */
    public SealAction(Context context) {
        super(context);
    }


    /**
     * 检查手机是否被注册
     *
     * @param region 国家码
     * @param phone  手机号
     * @throws HttpException
     */
    public CheckPhoneResponse checkPhoneAvailable(String region, String phone) throws HttpException {
        String url = getURL("user/check_phone_available");
        String json = JsonMananger.beanToJson(new CheckPhoneRequest(phone, region));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        CheckPhoneResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, CheckPhoneResponse.class);
        }
        return response;
    }


    /**
     * 发送验证码
     *
     * @param region 国家码
     * @param phone  手机号
     * @throws HttpException
     */
    public SendCodeResponse sendCode(String region, String phone) throws HttpException {
        String url = getURL("user/send_code");
        String json = JsonMananger.beanToJson(new SendCodeRequest(region, phone));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SendCodeResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = JsonMananger.jsonToBean(result, SendCodeResponse.class);
        }
        return response;
    }

    /*
    * 200: 验证成功
    1000: 验证码错误
    2000: 验证码过期
    异常返回，返回的 HTTP Status Code 如下：

    400: 错误的请求
    500: 应用服务器内部错误
    * */

    /**
     * 验证验证码是否正确(必选先用手机号码调sendcode)
     *
     * @param region 国家码
     * @param phone  手机号
     * @throws HttpException
     */
    public VerifyCodeResponse verifyCode(String region, String phone, String code) throws HttpException {
        String url = getURL("user/verify_code");
        String json = JsonMananger.beanToJson(new VerifyCodeRequest(region, phone, code));
        VerifyCodeResponse response = null;
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            Log.e("VerifyCodeResponse", result);
            response = jsonToBean(result, VerifyCodeResponse.class);
        }
        return response;
    }

    /**
     * 注册
     *
     * @param nickname           昵称
     * @param password           密码
     * @param verification_token 验证码
     * @throws HttpException
     */
    public RegisterResponse register(String nickname, String password, String verification_token) throws HttpException {
        String url = getURL("user/register");
        StringEntity entity = null;
        try {
            entity = new StringEntity(JsonMananger.beanToJson(new RegisterRequest(nickname, password, verification_token)), ENCODING);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        RegisterResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            NLog.e("RegisterResponse", result);
            response = jsonToBean(result, RegisterResponse.class);
        }
        return response;
    }

    /**
     * 登录: 登录成功后，会设置 Cookie，后续接口调用需要登录的权限都依赖于 Cookie。
     *
     * @param name          用户名
     * @param password      密码
     * @param time
     * @param rememberme
     * @param validatecode
     * @throws HttpException
     */
    public LoginResponse login(String name, String password, String time, boolean rememberme, String validatecode) throws HttpException {
        String uri = getURL("api/Log/Login");
        String json = JsonMananger.beanToJson(new LoginRequest(name, password, time, rememberme, validatecode));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, uri, entity, CONTENT_TYPE);
        LoginResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("LoginResponse", result);
            response = JsonMananger.jsonToBean(result, LoginResponse.class);
        }
        return response;
    }


    /**
     * 获取 token 前置条件需要登录   502 坏的网关 测试环境用户已达上限
     *
     * @throws HttpException
     */
    public GetRongTokenResponse getToken() throws HttpException {
        String url = getURL("/api/Rong/Token");
        String result = httpManager.get(url);
        GetRongTokenResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetTokenResponse", result);
            response = jsonToBean(result, GetRongTokenResponse.class);
        }
        return response;
    }


    /**
     * 设置自己的昵称
     *
     * @param nickname 昵称
     * @throws HttpException
     */
    public SetNameResponse setName(String nickname) throws HttpException {
        String url = getURL("user/set_nickname");
        String json = JsonMananger.beanToJson(new SetNameRequest(nickname));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SetNameResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetNameResponse.class);
        }
        return response;
    }

    /**
     * 设置用户头像
     *
     * @param portraitUri 头像 path
     * @throws HttpException
     */
    public SetPortraitResponse setPortrait(String portraitUri) throws HttpException {
        String url = getURL("user/set_portrait_uri");
        String json = JsonMananger.beanToJson(new SetPortraitRequest(portraitUri));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SetPortraitResponse response = null;
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetPortraitResponse.class);
        }
        return response;
    }


    /**
     * 当前登录用户通过旧密码设置新密码  前置条件需要登录才能访问
     *
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @throws HttpException
     */
    public ChangePasswordResponse changePassword(String oldPassword, String newPassword) throws HttpException {
        String url = getURL("user/change_password");
        String json = JsonMananger.beanToJson(new ChangePasswordRequest(oldPassword, newPassword));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        ChangePasswordResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("ChangePasswordResponse", result);
            response = jsonToBean(result, ChangePasswordResponse.class);
        }
        return response;
    }


    /**
     * 通过手机验证码重置密码
     *
     * @param password           密码，6 到 20 个字节，不能包含空格
     * @param verification_token 调用 /user/verify_code 成功后返回的 activation_token
     * @throws HttpException
     */
    public RestPasswordResponse restPassword(String password, String verification_token) throws HttpException {
        String uri = getURL("user/reset_password");
        String json = JsonMananger.beanToJson(new RestPasswordRequest(password, verification_token));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, uri, entity, CONTENT_TYPE);
        RestPasswordResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("RestPasswordResponse", result);
            response = jsonToBean(result, RestPasswordResponse.class);
        }
        return response;
    }

    /**
     * 根据 id 去服务端查询用户信息
     *
     * @param userid 用户ID
     * @throws HttpException
     */
    public GetUserInfoByIdResponse getUserInfoById(String userid) throws HttpException {
        String url = getURL("user/" + userid);
        String result = httpManager.get(url);
        GetUserInfoByIdResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfoByIdResponse.class);
        }
        return response;
    }


    /**
     * 通过国家码和手机号查询用户信息
     *
     * @param region 国家码
     * @param phone  手机号
     * @throws HttpException
     */
    public GetUserInfoByPhoneResponse getUserInfoFromPhone(String region, String phone) throws HttpException {
        String url = getURL("user/find/" + region + "/" + phone);
        String result = httpManager.get(url);
        GetUserInfoByPhoneResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfoByPhoneResponse.class);
        }
        return response;
    }


    /**
     * 发送好友邀请
     *
     * @param userid           好友id
     * @param addFriendMessage 添加好友的信息
     * @throws HttpException
     */
    public FriendInvitationResponse sendFriendInvitation(String userid, String addFriendMessage) throws HttpException {
        String url = getURL("api/Im/ApplyFriend");
        String json = JsonMananger.beanToJson(new FriendInvitationRequest(userid, addFriendMessage));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        FriendInvitationResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, FriendInvitationResponse.class);
        }
        return response;
    }


    /**
     * 获取发生过用户关系的列表  可以理解为好友列表
     *
     * @throws HttpException
     */
    public UserRelationshipResponse getAllUserRelationship() throws HttpException {
        String url = getURL("friendship/all");
        String result = httpManager.get(url);
        UserRelationshipResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, UserRelationshipResponse.class);
        }
        return response;
    }

    /**
     * 根据userId去服务器查询好友信息
     *
     * @throws HttpException
     */
    public GetFriendInfoByIDResponse getFriendInfoByID(String userid) throws HttpException {
        String url = getURL("friendship/" + userid + "/profile");
        String result = httpManager.get(url);
        GetFriendInfoByIDResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetFriendInfoByIDResponse.class);
        }
        return response;
    }

    /**
     * 同意对方好友邀请
     *
     * @param friendId 好友ID
     * @throws HttpException
     */
    public AgreeFriendsResponse agreeFriends(String friendId) throws HttpException {
        String url = getURL("api/Im/AgreeFirendApply");
        String json = JsonMananger.beanToJson(new AgreeFriendsRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AgreeFriendsResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AgreeFriendsResponse.class);
        }
        return response;
    }


    /**
     * 拒绝对方好友邀请
     *
     * @param friendId 好友ID
     * @throws HttpException
     */
    public AgreeFriendsResponse refuseFriends(String friendId) throws HttpException {
        String url = getURL("api/Im/RefuseFirendApply");
        String json = JsonMananger.beanToJson(new AgreeFriendsRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AgreeFriendsResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AgreeFriendsResponse.class);
        }
        return response;
    }

    /**
     * 创建群组
     *
     * @param name      群组名
     * @param memberIds 群组成员id
     * @throws HttpException
     */
    public CreateGroupResponse createGroup(String name, List<String> memberIds) throws HttpException {
        String url = getURL("group/create");
        String json = JsonMananger.beanToJson(new CreateGroupRequest(name, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        CreateGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, CreateGroupResponse.class);
        }
        return response;
    }

    /**
     * 创建者设置群组头像
     *
     * @param groupId     群组Id
     * @param portraitUri 群组头像
     * @throws HttpException
     */
    public SetGroupPortraitResponse setGroupPortrait(String groupId, String portraitUri) throws HttpException {
        String url = getURL("group/set_portrait_uri");
        String json = JsonMananger.beanToJson(new SetGroupPortraitRequest(groupId, portraitUri));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupPortraitResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupPortraitResponse.class);
        }
        return response;
    }

    /**
     * 获取当前用户所属群组列表
     *
     * @throws HttpException
     */
    public GetGroupResponse getGroups() throws HttpException {
        String url = getURL("user/groups");
        String result = httpManager.get(mContext, url);
        GetGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupResponse.class);
        }
        return response;
    }

    /**
     * 根据 群组id 查询该群组信息   403 群组成员才能看
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public GetGroupInfoResponse getGroupInfo(String groupId) throws HttpException {
        String url = getURL("group/" + groupId);
        String result = httpManager.get(mContext, url);
        GetGroupInfoResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupInfoResponse.class);
        }
        return response;
    }

    /**
     * 根据群id获取群组成员
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public GetGroupMemberResponse getGroupMember(String groupId) throws HttpException {
        String url = getURL("group/" + groupId + "/members");
        String result = httpManager.get(mContext, url);
        GetGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 当前用户添加群组成员
     *
     * @param groupId   群组Id
     * @param memberIds 成员集合
     * @throws HttpException
     */
    public AddGroupMemberResponse addGroupMember(String groupId, List<String> memberIds) throws HttpException {
        String url = getURL("group/add");
        String json = JsonMananger.beanToJson(new AddGroupMemberRequest(groupId, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AddGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AddGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 创建者将群组成员提出群组
     *
     * @param groupId   群组Id
     * @param memberIds 成员集合
     * @throws HttpException
     */
    public DeleteGroupMemberResponse deleGroupMember(String groupId, List<String> memberIds) throws HttpException {
        String url = getURL("group/kick");
        String json = JsonMananger.beanToJson(new DeleteGroupMemberRequest(groupId, memberIds));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DeleteGroupMemberResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DeleteGroupMemberResponse.class);
        }
        return response;
    }

    /**
     * 创建者更改群组昵称
     *
     * @param groupId 群组Id
     * @param name    群昵称
     * @throws HttpException
     */
    public SetGroupNameResponse setGroupName(String groupId, String name) throws HttpException {
        String url = getURL("group/rename");
        String json = JsonMananger.beanToJson(new SetGroupNameRequest(groupId, name));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupNameResponse.class);
        }
        return response;
    }

    /**
     * 用户自行退出群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public QuitGroupResponse quitGroup(String groupId) throws HttpException {
        String url = getURL("api/Im/QuitImGroup");
        String json = JsonMananger.beanToJson(new QuitGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        QuitGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, QuitGroupResponse.class);
        }
        return response;
    }

    /**
     * 用户加入群组  貌似没有这个行为，别人不邀请是加不进去的
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public QuitGroupResponse joinGroup(String groupId) throws HttpException {
        String url = getURL("api/Im/JoinImGroup");
        String json = JsonMananger.beanToJson(new QuitGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        QuitGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, QuitGroupResponse.class);
        }
        return response;
    }

    /**
     * 创建者解散群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public DismissGroupResponse dissmissGroup(String groupId) throws HttpException {
        String url = getURL("group/dismiss");
        String json = JsonMananger.beanToJson(new DismissGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DismissGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DismissGroupResponse.class);
        }
        return response;
    }


    /**
     * 修改自己的当前的群昵称
     *
     * @param groupId     群组Id
     * @param displayName 群名片
     * @throws HttpException
     */
    public SetGroupDisplayNameResponse setGroupDisplayName(String groupId, String displayName) throws HttpException {
        String url = getURL("group/set_display_name");
        String json = JsonMananger.beanToJson(new SetGroupDisplayNameRequest(groupId, displayName));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetGroupDisplayNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetGroupDisplayNameResponse.class);
        }
        return response;
    }

    /**
     * 删除好友
     *
     * @param friendId 好友Id
     * @throws HttpException
     */
    public DeleteFriendResponse deleteFriend(String friendId) throws HttpException {
        String url = getURL("friendship/delete");
        String json = JsonMananger.beanToJson(new DeleteFriendRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        DeleteFriendResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DeleteFriendResponse.class);
        }
        return response;
    }

    /**
     * 设置好友的备注名称
     *
     * @param friendId    好友Id
     * @param displayName 备注名
     * @throws HttpException
     */
    public SetFriendDisplayNameResponse setFriendDisplayName(String friendId, String displayName) throws HttpException {
        String url = getURL("friendship/set_display_name");
        String json = JsonMananger.beanToJson(new SetFriendDisplayNameRequest(friendId, displayName));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        SetFriendDisplayNameResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SetFriendDisplayNameResponse.class);
        }
        return response;
    }

    /**
     * 获取黑名单
     *
     * @throws HttpException
     */
    public GetBlackListResponse getBlackList() throws HttpException {
        String url = getURL("user/blacklist");
        String result = httpManager.get(mContext, url);
        GetBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetBlackListResponse.class);
        }
        return response;
    }

    /**
     * 加入黑名单
     *
     * @param friendId 群组Id
     * @throws HttpException
     */
    public AddToBlackListResponse addToBlackList(String friendId) throws HttpException {
        String url = getURL("user/add_to_blacklist");
        String json = JsonMananger.beanToJson(new AddToBlackListRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        AddToBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, AddToBlackListResponse.class);
        }
        return response;
    }

    /**
     * 移除黑名单
     *
     * @param friendId 好友Id
     * @throws HttpException
     */
    public RemoveFromBlackListResponse removeFromBlackList(String friendId) throws HttpException {
        String url = getURL("user/remove_from_blacklist");
        String json = JsonMananger.beanToJson(new RemoveFromBlacklistRequest(friendId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        RemoveFromBlackListResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, RemoveFromBlackListResponse.class);
        }
        return response;
    }

    public QiNiuTokenResponse getQiNiuToken() throws HttpException {
        String url = getURL("user/get_image_token");
        String result = httpManager.get(mContext, url);
        QiNiuTokenResponse q = null;
        if (!TextUtils.isEmpty(result)) {
            q = jsonToBean(result, QiNiuTokenResponse.class);
        }
        return q;
    }


    /**
     * 当前用户加入某群组
     *
     * @param groupId 群组Id
     * @throws HttpException
     */
    public JoinGroupResponse JoinGroup(String groupId) throws HttpException {
        String url = getURL("group/join");
        String json = JsonMananger.beanToJson(new JoinGroupRequest(groupId));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        JoinGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, JoinGroupResponse.class);
        }
        return response;
    }


    /**
     * 获取默认群组 和 聊天室
     *
     * @throws HttpException
     */
    public DefaultConversationResponse getDefaultConversation() throws HttpException {
        String url = getURL("misc/demo_square");
        String result = httpManager.get(mContext, url);
        DefaultConversationResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, DefaultConversationResponse.class);
        }
        return response;
    }

    /**
     * 根据一组ids 获取 一组用户信息
     *
     * @param ids 用户 id 集合
     * @throws HttpException
     */
    public GetUserInfosResponse getUserInfos(List<String> ids) throws HttpException {
        String url = getURL("user/batch?");
        StringBuilder sb = new StringBuilder();
        for (String s : ids) {
            sb.append("id=");
            sb.append(s);
            sb.append("&");
        }
        String stringRequest = sb.substring(0, sb.length() - 1);
        String newUrl = url + stringRequest;
        String result = httpManager.get(mContext, newUrl);
        GetUserInfosResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, GetUserInfosResponse.class);
        }
        return response;
    }

    /**
     * 获取版本信息
     *
     * @throws HttpException
     */
    public VersionResponse getSealTalkVersion() throws HttpException {
        String url = getURL("misc/client_version");
        String result = httpManager.get(mContext, url.trim());
        VersionResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, VersionResponse.class);
        }
        return response;
    }

    public SyncTotalDataResponse syncTotalData(String version) throws HttpException {
        String url = getURL("user/sync/" + version);
        String result = httpManager.get(mContext, url);
        SyncTotalDataResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, SyncTotalDataResponse.class);
        }
        return response;
    }

//    /**
//     * 根据userId去服务器查询好友信息
//     *
//     * @throws HttpException
//     */
//    public GetFriendInfoByIDResponse getFriendInfoByID(String userid) throws HttpException {
//        String url = getURL("friendship/" + userid + "/profile");
//        String result = httpManager.get(url);
//        GetFriendInfoByIDResponse response = null;
//        if (!TextUtils.isEmpty(result)) {
//            response = jsonToBean(result, GetFriendInfoByIDResponse.class);
//        }
//        return response;
//    }
    /**
     //     * 根据userId去服务器查询好友信息
     //     *
     //     * @throws HttpException
     //     */




/*--------------------------------自己的接口----------------------------------------------------------------*/


    /**
     * 判断是否存活
     *
     * @throws HttpException
     */
    public IsAliveResponse isAlive() throws HttpException {
        String url = getURL("api/Im/Alive");
        String result = httpManager.get(mContext, url);
        IsAliveResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetRongTokenResponse", result);
            response = jsonToBean(result, IsAliveResponse.class);
        }
        return response;
    }


    /**
     * 获取 融云 token 前置条件需要登录
     *
     * @throws HttpException
     */
    public GetRongTokenResponse getRongToken() throws HttpException {
        String url = getURL("api/Im/Token");
        String result = httpManager.get(mContext, url);
        GetRongTokenResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetRongTokenResponse", result);
            response = jsonToBean(result, GetRongTokenResponse.class);
        }
        return response;
    }

    /**
     * 获取 好友成员
     *
     * @throws HttpException
     */
    public GetRongGroupMembersResponse getFriendList() throws HttpException {
        String url = getURL("api/Im/ListFriends");
        String result = httpManager.get(mContext, url);
        GetRongGroupMembersResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetRongGroupMembersResponse", result);
            response = jsonToBean(result, GetRongGroupMembersResponse.class);
        }
        return response;
    }

    /**
     * 获取 群组
     *
     * @throws HttpException
     */
    public GetRongGroupResponse getRongGroups(String userName) throws HttpException {
        String url = getURL("api/Im/Groups");
        String result = httpManager.get(mContext, url);
        GetRongGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetRongGroupResponse", result);
            response = jsonToBean(result, GetRongGroupResponse.class);
        }
        return response;
    }

    /**
     * 获取 群组成员
     *
     * @throws HttpException
     */
    public GetRongGroupMembersResponse getRongGroupMembers(String groupId) throws HttpException {
        String url = getURL("api/Im/GroupUsers");
        String result = httpManager.get(mContext, url, new RequestParams("groupId", groupId));
        GetRongGroupMembersResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetRongGroupMembersResponse", result);
            response = jsonToBean(result, GetRongGroupMembersResponse.class);
        }
        return response;
    }


    /**
     * 查询用户信息
     *
     * @throws HttpException
     */
    public GetUserInfoResponse getUserInfo(String phoneStr) throws HttpException {
        String url = getURL("api/Im/FindUser");
        String result = httpManager.get(mContext, url, new RequestParams("toUser", phoneStr));
        GetUserInfoResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("GetUserInfoResponse", result);
            response = jsonToBean(result, GetUserInfoResponse.class);
        }
        return response;
    }


    /**
     * 添加好友
     *
     * @throws HttpException
     */
    public ApplyFriendResponse applyFriend(String userId) throws HttpException {
        String url = getURL("api/Im/ApplyFriend");
        String result = httpManager.post(mContext, url,  new RequestParams("toUser", userId));
        ApplyFriendResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("ApplyFriendResponse", result);
            response = jsonToBean(result, ApplyFriendResponse.class);
        }
        return response;
    }

    /**
     * 同意好友申请
     *
     * @throws HttpException
     */
    public AgreeFriendApplyResponse agreeFriend(String applyId) throws HttpException {
        String url = getURL("api/Im/ApplyFriend");
        String result = httpManager.post(mContext, url,  new RequestParams("applyId", applyId));
        AgreeFriendApplyResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("AgreeFriendApplyResponse", result);
            response = jsonToBean(result, AgreeFriendApplyResponse.class);
        }
        return response;
    }

    /**
     * 拒绝好友申请
     *
     * @throws HttpException
     */
    public RefuseFriendApplyResponse refuseFriend(String applyId) throws HttpException {
        String url = getURL("api/Im/ApplyFriend");
        String result = httpManager.post(mContext, url,  new RequestParams("applyId", applyId));
        RefuseFriendApplyResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("RefuseFriendApplyResponse", result);
            response = jsonToBean(result, RefuseFriendApplyResponse.class);
        }
        return response;
    }


    /**
     * 创建群组
     *
     * @throws HttpException
     */
    public CreateMyGroupResponse createGroup(String owner, String groupName, List<String> members) throws HttpException {
        String url = getURL("api/Im/CreateGroup");
        String json = JsonMananger.beanToJson(new CreateMyGroupRequest(owner, groupName, members));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        CreateMyGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, CreateMyGroupResponse.class);
        }
        return response;
    }


    /**
     * 加入群组
     *
     * @throws HttpException
     */
    public JoinMyGroupResponse joinMyGroup(String groupId, String groupName) throws HttpException {
        String url = getURL("api/Im/JoinImGroup");
        String json = JsonMananger.beanToJson(new JoinMyGroupRequest(groupId, groupName));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        JoinMyGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, JoinMyGroupResponse.class);
        }
        return response;
    }



    /**
     * 离开群组
     *
     * @throws HttpException
     */
    public QuitMyGroupResponse quitMyGroup(String groupId) throws HttpException {
        String url = getURL("api/Im/QuitImGroup");
        String result = httpManager.post(mContext, url,  new RequestParams("groupId", groupId));
        QuitMyGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("QuitMyGroupResponse", result);
            response = jsonToBean(result, QuitMyGroupResponse.class);
        }
        return response;
    }



    /**
     * 群组踢人
     *
     * @throws HttpException
     */
    public KickMyGroupResponse kickMyGroup(String groupId, String kickUser) throws HttpException {
        String url = getURL("api/Im/KickGroup");
        String json = JsonMananger.beanToJson(new KickMyGroupRequest(groupId, kickUser));
        StringEntity entity = null;
        try {
            entity = new StringEntity(json, ENCODING);
            entity.setContentType(CONTENT_TYPE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String result = httpManager.post(mContext, url, entity, CONTENT_TYPE);
        KickMyGroupResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            response = jsonToBean(result, KickMyGroupResponse.class);
        }
        return response;
    }



    /**
     * KQWF PCDD
     *
     * @throws HttpException
     */
    public KqwfPcddResponse postKQWFPCDD(String bettingNumber) throws HttpException {
        String url = getURL("api/Lottery/KQWFImBettingPcdd");
        String result = httpManager.post(mContext, url,  new RequestParams("BettingNumber", bettingNumber));
        KqwfPcddResponse response = null;
        if (!TextUtils.isEmpty(result)) {
            NLog.e("KqwfPcddResponse", result);
            response = jsonToBean(result, KqwfPcddResponse.class);
        }
        return response;
    }
}