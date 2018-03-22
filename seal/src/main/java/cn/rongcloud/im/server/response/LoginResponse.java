package cn.rongcloud.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class LoginResponse {


    /**
     * Result : 0
     * Error : null
     * ID : 185182
     * UserName : Rex
     * UserLeve : 01
     * UserType : 3
     * UserMoney : 9.9999999E7
     * Bonus : 7
     * UserLeveName : 普通
     * UserTypeName : 招商
     * LoginIP : 192.168.1.40
     * UserLockMoney : 0
     * UserIntegral : 0
     * ParentUserName : aka
     * State : 01
     * LoginGuid : 1e9090ab-4f7a-4d2c-8036-20f55464ad85
     * OpenAccountLimit : 0
     * TryLogCnt : 0
     * RegistTime : 2018-03-21T14:18:15.91
     * HasDailyWages : false
     * CanDailyWages : false
     * GASecret : null
     * Token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJ1bmlxdWVfbmFtZSI6IlJleCIsInJvbGUiOiJ1c2VyIiwiaXNzIjoibGVnZW5kIiwiYXVkIjoiQW55IiwiZXhwIjoxNTIxNzExNzE1LCJuYmYiOjE1MjE2MjUzMTV9.eXTfH5NXz2CZyImcY0JwzWu_Ph5rhTJH3K8-Lzcjb_Y
     * RongToken : kaQ1in8ebR8lTm/6YvQQt0VU1MUu3Il7vjBCh/OcCKH8iCHB5FAMI5kw2oNtESqaLWGYVI6pDp8=
     */

    private int Result;
    private String Error;
    private int ID;
    private String UserName;
    private String UserLeve;
    private String UserType;
    private double UserMoney;
    private int Bonus;
    private String UserLeveName;
    private String UserTypeName;
    private String LoginIP;
    private int UserLockMoney;
    private int UserIntegral;
    private String ParentUserName;
    private String State;
    private String LoginGuid;
    private int OpenAccountLimit;
    private int TryLogCnt;
    private String RegistTime;
    private boolean HasDailyWages;
    private boolean CanDailyWages;
    private Object GASecret;
    private String Token;
    private String RongToken;

    public int getResult() {
        return Result;
    }

    public void setResult(int Result) {
        this.Result = Result;
    }

    public String getError() {
        return Error;
    }

    public void setError(String Error) {
        this.Error = Error;
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

    public String getUserLeve() {
        return UserLeve;
    }

    public void setUserLeve(String UserLeve) {
        this.UserLeve = UserLeve;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public double getUserMoney() {
        return UserMoney;
    }

    public void setUserMoney(double UserMoney) {
        this.UserMoney = UserMoney;
    }

    public int getBonus() {
        return Bonus;
    }

    public void setBonus(int Bonus) {
        this.Bonus = Bonus;
    }

    public String getUserLeveName() {
        return UserLeveName;
    }

    public void setUserLeveName(String UserLeveName) {
        this.UserLeveName = UserLeveName;
    }

    public String getUserTypeName() {
        return UserTypeName;
    }

    public void setUserTypeName(String UserTypeName) {
        this.UserTypeName = UserTypeName;
    }

    public String getLoginIP() {
        return LoginIP;
    }

    public void setLoginIP(String LoginIP) {
        this.LoginIP = LoginIP;
    }

    public int getUserLockMoney() {
        return UserLockMoney;
    }

    public void setUserLockMoney(int UserLockMoney) {
        this.UserLockMoney = UserLockMoney;
    }

    public int getUserIntegral() {
        return UserIntegral;
    }

    public void setUserIntegral(int UserIntegral) {
        this.UserIntegral = UserIntegral;
    }

    public String getParentUserName() {
        return ParentUserName;
    }

    public void setParentUserName(String ParentUserName) {
        this.ParentUserName = ParentUserName;
    }

    public String getState() {
        return State;
    }

    public void setState(String State) {
        this.State = State;
    }

    public String getLoginGuid() {
        return LoginGuid;
    }

    public void setLoginGuid(String LoginGuid) {
        this.LoginGuid = LoginGuid;
    }

    public int getOpenAccountLimit() {
        return OpenAccountLimit;
    }

    public void setOpenAccountLimit(int OpenAccountLimit) {
        this.OpenAccountLimit = OpenAccountLimit;
    }

    public int getTryLogCnt() {
        return TryLogCnt;
    }

    public void setTryLogCnt(int TryLogCnt) {
        this.TryLogCnt = TryLogCnt;
    }

    public String getRegistTime() {
        return RegistTime;
    }

    public void setRegistTime(String RegistTime) {
        this.RegistTime = RegistTime;
    }

    public boolean isHasDailyWages() {
        return HasDailyWages;
    }

    public void setHasDailyWages(boolean HasDailyWages) {
        this.HasDailyWages = HasDailyWages;
    }

    public boolean isCanDailyWages() {
        return CanDailyWages;
    }

    public void setCanDailyWages(boolean CanDailyWages) {
        this.CanDailyWages = CanDailyWages;
    }

    public Object getGASecret() {
        return GASecret;
    }

    public void setGASecret(Object GASecret) {
        this.GASecret = GASecret;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }

    public String getRongToken() {
        return RongToken;
    }

    public void setRongToken(String RongToken) {
        this.RongToken = RongToken;
    }
}
