package cn.chenhuamou.im.server.response;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class SetNameResponse {

    /**
     * Error : string
     * Parameter : string
     * Result : true
     */

    private String Error;
    private String Parameter;
    private boolean Result;

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
}
