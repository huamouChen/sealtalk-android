package cn.chenhuamou.im.server.response;


/**
 * Created by AMing on 16/1/7.
 * Company RongCloud
 */
public class FriendInvitationResponse {


//    private CodeEntity Code;
//
//    public CodeEntity getCode() {
//        return Code;
//    }
//
//    public void setCode(CodeEntity code) {
//        Code = code;
//    }

    //    /**
//     * code : 200
//     * result : {"action":"Sent"}
//     * message : Request sent.
//     */

    private int code;
    /**
     * action : Sent
     */

    private ResultEntity result;
    private String message;

    public void setCode(int code) {
        this.code = code;
    }

    public void setResult(ResultEntity result) {
        this.result = result;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public ResultEntity getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public static class ResultEntity {
        private String action;

        public void setAction(String action) {
            this.action = action;
        }

        public String getAction() {
            return action;
        }
    }

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
