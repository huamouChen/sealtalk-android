package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class GetBankstResponse {


    /**
     * ID : 0
     * BankUserName : string
     * BankCode : string
     * BankNum : string
     * Commt : string
     * MinChargeMoney : 0
     * MaxChargeMoney : 0
     * BankCodeName : string
     * BankUrl : string
     * ImageGUID : string
     * ShowImageGUID : string
     * BanksID : 0
     * IsChargeUrl : true
     * ChargeUrl : string
     * SysBankName : string
     * Limitcondition : {"Regdays":0,"Charges":0,"Xiaofei":0,"UserType":"string"}
     * Limit : {}
     * Fee : 0
     * BankImg : string
     * BankImgHead : string
     */

    private int ID;
    private String BankUserName;
    private String BankCode;
    private String BankNum;
    private String Commt;
    private int MinChargeMoney;
    private int MaxChargeMoney;
    private String BankCodeName;
    private String BankUrl;
    private String ImageGUID;
    private String ShowImageGUID;
    private int BanksID;
    private boolean IsChargeUrl;
    private String ChargeUrl;
    private String SysBankName;
    private LimitconditionBean Limitcondition;
    private LimitBean Limit;
    private int Fee;
    private String BankImg;
    private String BankImgHead;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getBankUserName() {
        return BankUserName;
    }

    public void setBankUserName(String BankUserName) {
        this.BankUserName = BankUserName;
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

    public String getCommt() {
        return Commt;
    }

    public void setCommt(String Commt) {
        this.Commt = Commt;
    }

    public int getMinChargeMoney() {
        return MinChargeMoney;
    }

    public void setMinChargeMoney(int MinChargeMoney) {
        this.MinChargeMoney = MinChargeMoney;
    }

    public int getMaxChargeMoney() {
        return MaxChargeMoney;
    }

    public void setMaxChargeMoney(int MaxChargeMoney) {
        this.MaxChargeMoney = MaxChargeMoney;
    }

    public String getBankCodeName() {
        return BankCodeName;
    }

    public void setBankCodeName(String BankCodeName) {
        this.BankCodeName = BankCodeName;
    }

    public String getBankUrl() {
        return BankUrl;
    }

    public void setBankUrl(String BankUrl) {
        this.BankUrl = BankUrl;
    }

    public String getImageGUID() {
        return ImageGUID;
    }

    public void setImageGUID(String ImageGUID) {
        this.ImageGUID = ImageGUID;
    }

    public String getShowImageGUID() {
        return ShowImageGUID;
    }

    public void setShowImageGUID(String ShowImageGUID) {
        this.ShowImageGUID = ShowImageGUID;
    }

    public int getBanksID() {
        return BanksID;
    }

    public void setBanksID(int BanksID) {
        this.BanksID = BanksID;
    }

    public boolean isIsChargeUrl() {
        return IsChargeUrl;
    }

    public void setIsChargeUrl(boolean IsChargeUrl) {
        this.IsChargeUrl = IsChargeUrl;
    }

    public String getChargeUrl() {
        return ChargeUrl;
    }

    public void setChargeUrl(String ChargeUrl) {
        this.ChargeUrl = ChargeUrl;
    }

    public String getSysBankName() {
        return SysBankName;
    }

    public void setSysBankName(String SysBankName) {
        this.SysBankName = SysBankName;
    }

    public LimitconditionBean getLimitcondition() {
        return Limitcondition;
    }

    public void setLimitcondition(LimitconditionBean Limitcondition) {
        this.Limitcondition = Limitcondition;
    }

    public LimitBean getLimit() {
        return Limit;
    }

    public void setLimit(LimitBean Limit) {
        this.Limit = Limit;
    }

    public int getFee() {
        return Fee;
    }

    public void setFee(int Fee) {
        this.Fee = Fee;
    }

    public String getBankImg() {
        return BankImg;
    }

    public void setBankImg(String BankImg) {
        this.BankImg = BankImg;
    }

    public String getBankImgHead() {
        return BankImgHead;
    }

    public void setBankImgHead(String BankImgHead) {
        this.BankImgHead = BankImgHead;
    }

    public static class LimitconditionBean {
    }

    public static class LimitBean {
    }
}
