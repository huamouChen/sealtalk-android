package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class SetGroupPortraitResponse {


    /**
     * Value : {"GroupImage":"string"}
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
         * GroupImage : string
         */

        private String GroupImage;

        public String getGroupImage() {
            return GroupImage;
        }

        public void setGroupImage(String GroupImage) {
            this.GroupImage = GroupImage;
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
