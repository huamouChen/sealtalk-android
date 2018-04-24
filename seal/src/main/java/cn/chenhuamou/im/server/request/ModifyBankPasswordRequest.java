package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class ModifyBankPasswordRequest {


    public ModifyBankPasswordRequest(String userName, String oldPwd, String newPwd) {
        UserName = userName;
        OldPwd = oldPwd;
        NewPwd = newPwd;
    }

    /**
     * UserName : string
     * OldPwd : string
     * NewPwd : string
     */

    private String UserName;
    private String OldPwd;
    private String NewPwd;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

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

