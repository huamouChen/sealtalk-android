package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/29.
 * Company RongCloud
 */
public class QuitGroupResponse {

    private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


//    private CodeEntity Code;
//
//    public CodeEntity getCode() {
//        return Code;
//    }
//
//    public void setCode(CodeEntity code) {
//        Code = code;
//    }

    private class CodeEntity {

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
