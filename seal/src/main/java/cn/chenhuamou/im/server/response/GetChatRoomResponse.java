package cn.chenhuamou.im.server.response;

import java.util.List;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class GetChatRoomResponse {


    /**
     * Value : [{"GroupId":"string","GroupName":"string","GroupOwner":"string","IsOfficial":true,"GroupImage":"string","AddTime":"2018-04-19T09:13:59.420Z"}]
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
         * GroupId : string
         * GroupName : string
         * GroupOwner : string
         * IsOfficial : true
         * GroupImage : string
         * AddTime : 2018-04-19T09:13:59.420Z
         */

        private String GroupId;
        private String GroupName;
        private String GroupOwner;
        private boolean IsOfficial;
        private String GroupImage;
        private String AddTime;

        public String getGroupId() {
            return GroupId;
        }

        public void setGroupId(String GroupId) {
            this.GroupId = GroupId;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getGroupOwner() {
            return GroupOwner;
        }

        public void setGroupOwner(String GroupOwner) {
            this.GroupOwner = GroupOwner;
        }

        public boolean isIsOfficial() {
            return IsOfficial;
        }

        public void setIsOfficial(boolean IsOfficial) {
            this.IsOfficial = IsOfficial;
        }

        public String getGroupImage() {
            return GroupImage;
        }

        public void setGroupImage(String GroupImage) {
            this.GroupImage = GroupImage;
        }

        public String getAddTime() {
            return AddTime;
        }

        public void setAddTime(String AddTime) {
            this.AddTime = AddTime;
        }
    }
}
