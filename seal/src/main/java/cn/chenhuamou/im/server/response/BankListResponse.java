package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class BankListResponse {


    /**
     * BankName : string
     * BankCodeName : string
     * QQ : string
     * Email : string
     * ID : 0
     * UserName : string
     * BankCode : string
     * BankNum : string
     * BankUserName : string
     * Province : string
     * IsDefault : true
     * CreateTime : 2018-04-19T02:03:38.458Z
     * IsDelete : true
     */

    private String BankName;
    private String BankCodeName;
    private String QQ;
    private String Email;
    private int ID;
    private String UserName;
    private String BankCode;
    private String BankNum;
    private String BankUserName;
    private String Province;
    private boolean IsDefault;
    private String CreateTime;
    private boolean IsDelete;

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public String getBankCodeName() {
        return BankCodeName;
    }

    public void setBankCodeName(String BankCodeName) {
        this.BankCodeName = BankCodeName;
    }

    public String getQQ() {
        return QQ;
    }

    public void setQQ(String QQ) {
        this.QQ = QQ;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

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

    public String getProvince() {
        return Province;
    }

    public void setProvince(String Province) {
        this.Province = Province;
    }

    public boolean isIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean IsDefault) {
        this.IsDefault = IsDefault;
    }

    public String getCreateTime() {
        return CreateTime;
    }

    public void setCreateTime(String CreateTime) {
        this.CreateTime = CreateTime;
    }

    public boolean isIsDelete() {
        return IsDelete;
    }

    public void setIsDelete(boolean IsDelete) {
        this.IsDelete = IsDelete;
    }
}
