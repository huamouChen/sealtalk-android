package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongTokenResponse {



    private ValueEntity Value;

    private  ResultEntity Code;

    public ValueEntity getValue() {
        return Value;
    }

    public void setValue(ValueEntity value) {
        Value = value;
    }

    public ResultEntity getCode() {
        return Code;
    }

    public void setCode(ResultEntity code) {
        Code = code;
    }

    public static class ValueEntity {

        /**
         * Id : 0
         * UserName : string
         * RongToken : string
         * AddTime : 2018-03-22T10:12:05.735Z
         */

        private int Id;
        private String UserName;
        private String RongToken;
        private String AddTime;

        public int getId() {
            return Id;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getRongToken() {
            return RongToken;
        }

        public void setRongToken(String RongToken) {
            this.RongToken = RongToken;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }
    }

    public static class ResultEntity {

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
}
