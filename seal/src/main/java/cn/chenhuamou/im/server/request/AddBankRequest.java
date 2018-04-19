package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class AddBankRequest {


    public AddBankRequest(String userName, String bankCode, String bankNum, String bankUserName, String bankUserPwd, String bankNumNew) {
        UserName = userName;
        BankCode = bankCode;
        BankNum = bankNum;
        BankUserName = bankUserName;
        BankUserPwd = bankUserPwd;
        BankNumNew = bankNumNew;
    }

    /**
     * UserName : string
     * BankCode : string
     * BankNum : string
     * BankUserName : string
     * BankUserPwd : string
     * BankNumNew : string
     */

    private String UserName;
    private String BankCode;
    private String BankNum;
    private String BankUserName;
    private String BankUserPwd;
    private String BankNumNew;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getBankCode() {
        return BankCode;
    }

    public void setBankCode(String BankCode) {
        this.BankCode = BankCode;
    }

    public String getBankNum() {
        return BankNum;
    }

    public void setBankNum(String BankNum) {
        this.BankNum = BankNum;
    }

    public String getBankUserName() {
        return BankUserName;
    }

    public void setBankUserName(String BankUserName) {
        this.BankUserName = BankUserName;
    }

    public String getBankUserPwd() {
        return BankUserPwd;
    }

    public void setBankUserPwd(String BankUserPwd) {
        this.BankUserPwd = BankUserPwd;
    }

    public String getBankNumNew() {
        return BankNumNew;
    }

    public void setBankNumNew(String BankNumNew) {
        this.BankNumNew = BankNumNew;
    }
}

