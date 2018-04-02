package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class CreateMyGroupResponse {


    private CodeEntity Code;

    public CodeEntity getCode() {
        return Code;
    }

    public void setCode(CodeEntity code) {
        Code = code;
    }

    public class CodeEntity {

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
