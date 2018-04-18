package cn.chenhuamou.im.server.response;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class ChangePasswordResponse {


    /**
     * Error : string
     * Parameter : string
     * Result : true
     * Code : {"CodeId":"string","Description":"string"}
     */

    private String Error;
    private String Parameter;
    private boolean Result;
    private CodeBean Code;

    public String getError() {
        return Error;
    }

    public void setError(String Error) {
        this.Error = Error;
    }

    public String getParameter() {
        return Parameter;
    }

    public void setParameter(String Parameter) {
        this.Parameter = Parameter;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

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
