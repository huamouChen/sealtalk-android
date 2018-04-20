package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class RechargeResponse {


    /**
     * ID : 0
     * UserName : string
     * RecordCode : string
     * ChargeMoney : 0
     * BeforeMoney : 0
     * AddTime : 2018-04-20T06:01:21.138Z
     * Commt : string
     * State : string
     * AfterMoney : 0
     * IsCheck : true
     * Ps : string
     * BankId : 0
     * CreateUserName : string
     * HandleTime : 2018-04-20T06:01:21.138Z
     */

    private int ID;
    private String UserName;
    private String RecordCode;
    private int ChargeMoney;
    private int BeforeMoney;
    private String AddTime;
    private String Commt;
    private String State;
    private int AfterMoney;
    private boolean IsCheck;
    private String Ps;
    private int BankId;
    private String CreateUserName;
    private String HandleTime;

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

    public String getRecordCode() {
        return RecordCode;
    }

    public void setRecordCode(String RecordCode) {
        this.RecordCode = RecordCode;
    }

    public int getChargeMoney() {
        return ChargeMoney;
    }

    public void setChargeMoney(int ChargeMoney) {
        this.ChargeMoney = ChargeMoney;
    }

    public int getBeforeMoney() {
        return BeforeMoney;
    }

    public void setBeforeMoney(int BeforeMoney) {
        this.BeforeMoney = BeforeMoney;
    }

    public String getAddTime() {
        return AddTime;
    }

    public void setAddTime(String AddTime) {
        this.AddTime = AddTime;
    }

    public String getCommt() {
        return Commt;
    }

    public void setCommt(String Commt) {
        this.Commt = Commt;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public int getAfterMoney() {
        return AfterMoney;
    }

    public void setAfterMoney(int AfterMoney) {
        this.AfterMoney = AfterMoney;
    }

    public boolean isIsCheck() {
        return IsCheck;
    }

    public void setIsCheck(boolean IsCheck) {
        this.IsCheck = IsCheck;
    }

    public String getPs() {
        return Ps;
    }

    public void setPs(String Ps) {
        this.Ps = Ps;
    }

    public int getBankId() {
        return BankId;
    }

    public void setBankId(int BankId) {
        this.BankId = BankId;
    }

    public String getCreateUserName() {
        return CreateUserName;
    }

    public void setCreateUserName(String CreateUserName) {
        this.CreateUserName = CreateUserName;
    }

    public String getHandleTime() {
        return HandleTime;
    }

    public void setHandleTime(String HandleTime) {
        this.HandleTime = HandleTime;
    }
}
