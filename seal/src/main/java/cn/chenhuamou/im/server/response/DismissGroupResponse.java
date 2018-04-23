package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/29.
 * Company RongCloud
 */
public class DismissGroupResponse {

    /**
     * Code : {"CodeId":"string","Description":"string"}
     */

    private CodeBean Code;

    public CodeBean getCode() {
        return Code;
    }

    public void setCode(CodeBean Code) {
        this.Code = Code;
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
