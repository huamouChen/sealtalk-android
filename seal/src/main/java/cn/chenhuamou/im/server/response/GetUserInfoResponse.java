package cn.chenhuamou.im.server.response;

import java.util.List;

import cn.chenhuamou.im.db.Groups;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetUserInfoResponse {


    private ValueEntity Value;

    private ResultEntity Code;

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

    public class ValueEntity {


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
