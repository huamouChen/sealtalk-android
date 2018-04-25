package cn.chenhuamou.im.server.response;

import java.util.List;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class GetUserCashResponse {


    /**
     * PageCount : 0
     * List : [{"StateName":"string","UserTypeName":"string","BankName":"string","BankNum":"string","BankUserName":"string","Province":"string","ID":0,"UserName":"string","RecordCode":"string","WithDrawMoney":0,"BeforeMoney":0,"AddTime":"2018-04-25T03:38:26.071Z","Commt":"string","State":"string","AfterMoney":0,"BankId":0,"RiskAdminUserId":0,"FinanceAdminUserId":0,"PayTime":"2018-04-25T03:38:26.071Z"}]
     * ListExt : [{"StateName":"string","UserTypeName":"string","BankName":"string","BankNum":"string","BankUserName":"string","Province":"string","ID":0,"UserName":"string","RecordCode":"string","WithDrawMoney":0,"BeforeMoney":0,"AddTime":"2018-04-25T03:38:26.071Z","Commt":"string","State":"string","AfterMoney":0,"BankId":0,"RiskAdminUserId":0,"FinanceAdminUserId":0,"PayTime":"2018-04-25T03:38:26.071Z"}]
     * Obj : {}
     * Code : {"CodeId":"string","Description":"string"}
     */

    private int PageCount;
    private ObjBean Obj;
    private CodeBean Code;
    private List<TakeCashBean> List;
    private List<TakeCashExtBean> ListExt;

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

    public List<TakeCashBean> getList() {
        return List;
    }

    public void setList(List<TakeCashBean> List) {
        this.List = List;
    }

    public List<TakeCashExtBean> getListExt() {
        return ListExt;
    }

    public void setListExt(List<TakeCashExtBean> ListExt) {
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

    public static class TakeCashBean {
        /**
         * StateName : string
         * UserTypeName : string
         * BankName : string
         * BankNum : string
         * BankUserName : string
         * Province : string
         * ID : 0
         * UserName : string
         * RecordCode : string
         * WithDrawMoney : 0
         * BeforeMoney : 0
         * AddTime : 2018-04-25T03:38:26.071Z
         * Commt : string
         * State : string
         * AfterMoney : 0
         * BankId : 0
         * RiskAdminUserId : 0
         * FinanceAdminUserId : 0
         * PayTime : 2018-04-25T03:38:26.071Z
         */

        private String StateName;
        private String UserTypeName;
        private String BankName;
        private String BankNum;
        private String BankUserName;
        private String Province;
        private int ID;
        private String UserName;
        private String RecordCode;
        private int WithDrawMoney;
        private int BeforeMoney;
        private String AddTime;
        private String Commt;
        private String State;
        private int AfterMoney;
        private int BankId;
        private int RiskAdminUserId;
        private int FinanceAdminUserId;
        private String PayTime;

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

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String BankName) {
            this.BankName = BankName;
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

        public int getWithDrawMoney() {
            return WithDrawMoney;
        }

        public void setWithDrawMoney(int WithDrawMoney) {
            this.WithDrawMoney = WithDrawMoney;
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

        public int getBankId() {
            return BankId;
        }

        public void setBankId(int BankId) {
            this.BankId = BankId;
        }

        public int getRiskAdminUserId() {
            return RiskAdminUserId;
        }

        public void setRiskAdminUserId(int RiskAdminUserId) {
            this.RiskAdminUserId = RiskAdminUserId;
        }

        public int getFinanceAdminUserId() {
            return FinanceAdminUserId;
        }

        public void setFinanceAdminUserId(int FinanceAdminUserId) {
            this.FinanceAdminUserId = FinanceAdminUserId;
        }

        public String getPayTime() {
            return PayTime;
        }

        public void setPayTime(String PayTime) {
            this.PayTime = PayTime;
        }
    }

    public static class TakeCashExtBean {
        /**
         * StateName : string
         * UserTypeName : string
         * BankName : string
         * BankNum : string
         * BankUserName : string
         * Province : string
         * ID : 0
         * UserName : string
         * RecordCode : string
         * WithDrawMoney : 0
         * BeforeMoney : 0
         * AddTime : 2018-04-25T03:38:26.071Z
         * Commt : string
         * State : string
         * AfterMoney : 0
         * BankId : 0
         * RiskAdminUserId : 0
         * FinanceAdminUserId : 0
         * PayTime : 2018-04-25T03:38:26.071Z
         */

        private String StateName;
        private String UserTypeName;
        private String BankName;
        private String BankNum;
        private String BankUserName;
        private String Province;
        private int ID;
        private String UserName;
        private String RecordCode;
        private int WithDrawMoney;
        private int BeforeMoney;
        private String AddTime;
        private String Commt;
        private String State;
        private int AfterMoney;
        private int BankId;
        private int RiskAdminUserId;
        private int FinanceAdminUserId;
        private String PayTime;

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

        public String getBankName() {
            return BankName;
        }

        public void setBankName(String BankName) {
            this.BankName = BankName;
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

        public int getWithDrawMoney() {
            return WithDrawMoney;
        }

        public void setWithDrawMoney(int WithDrawMoney) {
            this.WithDrawMoney = WithDrawMoney;
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

        public int getBankId() {
            return BankId;
        }

        public void setBankId(int BankId) {
            this.BankId = BankId;
        }

        public int getRiskAdminUserId() {
            return RiskAdminUserId;
        }

        public void setRiskAdminUserId(int RiskAdminUserId) {
            this.RiskAdminUserId = RiskAdminUserId;
        }

        public int getFinanceAdminUserId() {
            return FinanceAdminUserId;
        }

        public void setFinanceAdminUserId(int FinanceAdminUserId) {
            this.FinanceAdminUserId = FinanceAdminUserId;
        }

        public String getPayTime() {
            return PayTime;
        }

        public void setPayTime(String PayTime) {
            this.PayTime = PayTime;
        }
    }
}
