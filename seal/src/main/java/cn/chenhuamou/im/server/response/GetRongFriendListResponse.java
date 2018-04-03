package cn.chenhuamou.im.server.response;

import java.util.List;

import cn.chenhuamou.im.db.Groups;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongFriendListResponse {


    private List<ValueEntity> Value;

    private ResultEntity Code;

    public List<ValueEntity> getValue() {
        return Value;
    }

    public void setValue(List<ValueEntity> value) {
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
         * UserName : string
         * HeadImg : string
         * IsOnLine : true
         */

        private String UserName;
        private String HeadImg;
        private boolean IsOnLine;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getHeadImg() {
            return HeadImg;
        }

        public void setHeadImg(String HeadImg) {
            this.HeadImg = HeadImg;
        }

        public boolean isIsOnLine() {
            return IsOnLine;
        }

        public void setIsOnLine(boolean IsOnLine) {
            this.IsOnLine = IsOnLine;
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
