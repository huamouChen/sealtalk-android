package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/7/20.
 * Company RongCloud
 */
public class VersionResponse {

    /**
     * Value : {"VersionCode":"string"}
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
         * VersionCode : string
         */

        private String VersionCode;
        private String FileUrl;

        public String getVersionCode() {
            return VersionCode;
        }

        public void setVersionCode(String VersionCode) {
            this.VersionCode = VersionCode;
        }

        public String getFileUrl() {
            return FileUrl;
        }

        public void setFileUrl(String fileUrl) {
            FileUrl = fileUrl;
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
