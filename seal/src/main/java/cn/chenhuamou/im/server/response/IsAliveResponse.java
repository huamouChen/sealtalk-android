package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class IsAliveResponse {

    private ResultEntity Code;

    public ResultEntity getCode() {
        return Code;
    }

    public void setCode(ResultEntity code) {
        Code = code;
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
