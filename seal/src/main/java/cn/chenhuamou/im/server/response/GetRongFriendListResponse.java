package cn.chenhuamou.im.server.response;

import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetRongFriendListResponse {

    /**
     * Value : [{"UserName":"string","NickName":"string","HeaderImage":"string","IsOnLine":"string"}]
     * Code : {"CodeId":"string","Description":"string"}
     */

    private CodeBean Code;
    private List<ValueBean> Value;

    public CodeBean getCode() {
        return Code;
    }

    public void setCode(CodeBean Code) {
        this.Code = Code;
    }

    public List<ValueBean> getValue() {
        return Value;
    }

    public void setValue(List<ValueBean> Value) {
        this.Value = Value;
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

    public static class ValueBean {
        /**
         * UserName : string
         * NickName : string
         * HeaderImage : string
         * IsOnLine : string
         */

        private String UserName;
        private String NickName;
        private String HeaderImage;
        private String IsOnLine;

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String UserName) {
            this.UserName = UserName;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String NickName) {
            this.NickName = NickName;
        }

        public String getHeaderImage() {
            return HeaderImage;
        }

        public void setHeaderImage(String HeadImg) {
            this.HeaderImage = HeadImg;
        }

        public String getIsOnLine() {
            return IsOnLine;
        }

        public void setIsOnLine(String IsOnLine) {
            this.IsOnLine = IsOnLine;
        }
    }
}
