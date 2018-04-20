package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class CreateMyGroupResponse {


    /**
     * Value : {"GroupId":2858,"GroupName":"chmGro","GroupImage":"/Images/Group_2858_20180420180418.jpg"}
     * Code : {"CodeId":"100","Description":"成功"}
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
         * GroupId : 2858
         * GroupName : chmGro
         * GroupImage : /Images/Group_2858_20180420180418.jpg
         */

        private int GroupId;
        private String GroupName;
        private String GroupImage;

        public int getGroupId() {
            return GroupId;
        }

        public void setGroupId(int GroupId) {
            this.GroupId = GroupId;
        }

        public String getGroupName() {
            return GroupName;
        }

        public void setGroupName(String GroupName) {
            this.GroupName = GroupName;
        }

        public String getGroupImage() {
            return GroupImage;
        }

        public void setGroupImage(String GroupImage) {
            this.GroupImage = GroupImage;
        }
    }

    public static class CodeBean {
        /**
         * CodeId : 100
         * Description : 成功
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
