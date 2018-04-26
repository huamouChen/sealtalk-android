package cn.chenhuamou.im.server.response;

        import java.util.List;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class GetUserChargeListResponse {

    /**
     * PageCount : 0
     * List : [{"StateName":"string","UserTypeName":"string","ID":0,"UserName":"string","RecordCode":"string","ChargeMoney":0,"BeforeMoney":0,"AddTime":"2018-04-25T03:38:26.081Z","Commt":"string","State":"string","AfterMoney":0,"IsCheck":true,"Ps":"string","BankId":0,"CreateUserName":"string","HandleTime":"2018-04-25T03:38:26.081Z"}]
     * ListExt : [{"StateName":"string","UserTypeName":"string","ID":0,"UserName":"string","RecordCode":"string","ChargeMoney":0,"BeforeMoney":0,"AddTime":"2018-04-25T03:38:26.081Z","Commt":"string","State":"string","AfterMoney":0,"IsCheck":true,"Ps":"string","BankId":0,"CreateUserName":"string","HandleTime":"2018-04-25T03:38:26.081Z"}]
     * Obj : {}
     * Code : {"CodeId":"string","Description":"string"}
     */

    private int PageCount;
    private ObjBean Obj;
    private CodeBean Code;
    private List<ChargeBean> List;
    private List<ChargeExtBean> ListExt;

    public int getPageCount() {
        return PageCount;
    }

    public void setPageCount(int PageCount) {
        this.PageCount = PageCount;
    }

    public ObjBean getObj() {
        return Obj;
    }

    public void setObj(ObjBean Obj) {
        this.Obj = Obj;
    }

    public CodeBean getCode() {
        return Code;
    }

    public void setCode(CodeBean Code) {
        this.Code = Code;
    }

    public List<ChargeBean> getList() {
        return List;
    }

    public void setList(List<ChargeBean> List) {
        this.List = List;
    }

    public List<ChargeExtBean> getListExt() {
        return ListExt;
    }

    public void setListExt(List<ChargeExtBean> ListExt) {
        this.ListExt = ListExt;
    }

    public static class ObjBean {
    }

    public static class CodeBean {
        /**
         * CodeId : string
         * Description : string
         */

        private String CodeId;
        private String Description;

        public String getCodeId() {
            return CodeId;
        }

        public void setCodeId(String CodeId) {
            this.CodeId = CodeId;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String Description) {
            this.Description = Description;
        }
    }

    public static class ChargeBean {
        /**
         * StateName : string
         * UserTypeName : string
         * ID : 0
         * UserName : string
         * RecordCode : string
         * ChargeMoney : 0
         * BeforeMoney : 0
         * AddTime : 2018-04-25T03:38:26.081Z
         * Commt : string
         * State : string
         * AfterMoney : 0
         * IsCheck : true
         * Ps : string
         * BankId : 0
         * CreateUserName : string
         * HandleTime : 2018-04-25T03:38:26.081Z
         */

        private String StateName;
        private String UserTypeName;
        private int ID;
        private String UserName;
        private String RecordCode;
        private String ChargeMoney;
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

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }

        public String getUserTypeName() {
            return UserTypeName;
        }

        public void setUserTypeName(String UserTypeName) {
            this.UserTypeName = UserTypeName;
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

        public String getRecordCode() {
            return RecordCode;
        }

        public void setRecordCode(String RecordCode) {
            this.RecordCode = RecordCode;
        }

        public String getChargeMoney() {
            return ChargeMoney;
        }

        public void setChargeMoney(String ChargeMoney) {
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

    public static class ChargeExtBean {
        /**
         * StateName : string
         * UserTypeName : string
         * ID : 0
         * UserName : string
         * RecordCode : string
         * ChargeMoney : 0
         * BeforeMoney : 0
         * AddTime : 2018-04-25T03:38:26.081Z
         * Commt : string
         * State : string
         * AfterMoney : 0
         * IsCheck : true
         * Ps : string
         * BankId : 0
         * CreateUserName : string
         * HandleTime : 2018-04-25T03:38:26.081Z
         */

        private String StateName;
        private String UserTypeName;
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

        public String getStateName() {
            return StateName;
        }

        public void setStateName(String StateName) {
            this.StateName = StateName;
        }

        public String getUserTypeName() {
            return UserTypeName;
        }

        public void setUserTypeName(String UserTypeName) {
            this.UserTypeName = UserTypeName;
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
}
