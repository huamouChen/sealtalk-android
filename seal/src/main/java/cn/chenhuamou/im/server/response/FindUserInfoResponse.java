package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class FindUserInfoResponse {


    /**
     * Value : {"UserName":"string","Headimg":"string","Relation":0,"Notes":"string","Exist":true}
     * Code : {"CodeId":"string","Description":"string"}
     */

    private ValueBean Value;
    private CodeBean Code;

    public ValueBean getValue() {
        return Value;
    }

    public void setValue(ValueBean Value) {
        this.Value = Value;
    }

    public CodeBean getCode() {
        return Code;
    }

    public void setCode(CodeBean Code) {
        this.Code = Code;
    }

    public static class ValueBean {
        /**
         * UserName : string
         * Headimg : string
         * Relation : 0
         * Notes : string
         * Exist : true
         */

        private String UserName;
        private String Headimg;
        private int Relation;
        private String Notes;
        private boolean Exist;
        private String NickName;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getHeadimg() {
            return Headimg;
        }

        public void setHeadimg(String Headimg) {
            this.Headimg = Headimg;
        }

        public int getRelation() {
            return Relation;
        }

        public void setRelation(int Relation) {
            this.Relation = Relation;
        }

        public String getNotes() {
            return Notes;
        }

        public void setNotes(String Notes) {
            this.Notes = Notes;
        }

        public boolean isExist() {
            return Exist;
        }

        public void setExist(boolean Exist) {
            this.Exist = Exist;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }
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
}
