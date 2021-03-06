package cn.chenhuamou.im;

/**
 * Created by AMing on 16/5/26.
 * Company RongCloud
 */
public class SealConst {

    public static final String SharedPreferencesName = "config";

    public static final int DISCUSSION_REMOVE_MEMBER_REQUEST_CODE = 1;
    public static final int DISCUSSION_ADD_MEMBER_REQUEST_CODE = 2;

    public static final boolean ISOPENDISCUSSION = false;
    public static final int PERSONALPROFILEFROMCONVERSATION = 3;
    public static final int PERSONALPROFILEFROMGROUP = 4;

    public static final String GROUP_LIST_UPDATE = "GROUP_LIST_UPDATE";
    public static final String EXIT = "EXIT";

    public static final String TOKEN = "TOKEN";  // app 登录token
    public static final String Rong_Token = "RongToken";   // 融云token

    public static final String CHANGEINFO = "CHANGEINFO";
    public static final String SEALTALK_LOGIN_ID = "loginid";
    public static final String SEALTALK_LOGIN_NAME = "loginnickname";
    public static final String SEALTALK_LOGING_PORTRAIT = "loginPortrait";
    public static final String SEALTALK_LOGING_PHONE = "loginphone";
    public static final String SEALTALK_LOGING_PASSWORD = "loginpassword";

    // 是否是由群组点开查看用户信息
    public static final String IsFromGroup = "isFromGroup";
    // 个人
    public static final String IsPrivate = "IsPrivate";
    public static final String TargetId = "TargetId";
    public static final String Portrait = "Portrait";

    // 资金密码
    public static final String IsHavePayPassword = "IsHavePayPassword";

    // 绑定手机
    public static final String Bind_Phone = "bindPhone";
    // 绑定手机
    public static final String Nick_Name = "NickName";

    // 是设置资金密码 还是修改资金密码
    public static final String IsCreatwPayPwd = "isCreatwPayPwd";


    // 起始日期
    public static final String StartDate = "StartDate";
    // 截止日期
    public static final String EndDate = "EndDate";


    //上拉加载更多
    public static final int PULL_LOAD_MORE = 0;
    //正在加载更多
    public static final int LOADING_MORE = 1;
    //没有更多
    public static final int NO_MORE = 2;


    // 扫一扫
    public static final String ScanQRCode = "ScanQRCode";

    // 刷新我的钱包界面
    public static final String RefreshMyWallet = "RefreshMyWallet";





}
