package cn.chenhuamou.im.server.request;


/**
 * Created by AMing on 15/12/23.
 * Company RongCloud
 */
public class RegisterRequest {


    public RegisterRequest(String userName, float bonus, String userType, String userPassword) {
        UserName = userName;
        Bonus = bonus;
        UserType = userType;
        UserPassword = userPassword;
    }

    /**
     * UserName : string
     * Bonus : 0
     * UserType : string
     * UserPassword : string
     */

    private String UserName;
    private float Bonus;
    private String UserType;
    private String UserPassword;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public float getBonus() {
        return Bonus;
    }

    public void setBonus(float Bonus) {
        this.Bonus = Bonus;
    }

    public String getUserType() {
        return UserType;
    }

    public void setUserType(String UserType) {
        this.UserType = UserType;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String UserPassword) {
        this.UserPassword = UserPassword;
    }
}
