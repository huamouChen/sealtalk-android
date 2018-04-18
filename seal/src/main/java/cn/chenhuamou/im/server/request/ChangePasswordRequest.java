package cn.chenhuamou.im.server.request;


/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class ChangePasswordRequest {


    public ChangePasswordRequest(String oldPwd, String newPwd) {
        OldPwd = oldPwd;
        NewPwd = newPwd;
    }

    /**
     * OldPwd : string
     * NewPwd : string
     */

    private String OldPwd;
    private String NewPwd;

    public String getOldPwd() {
        return OldPwd;
    }

    public void setOldPwd(String OldPwd) {
        this.OldPwd = OldPwd;
    }

    public String getNewPwd() {
        return NewPwd;
    }

    public void setNewPwd(String NewPwd) {
        this.NewPwd = NewPwd;
    }
}
