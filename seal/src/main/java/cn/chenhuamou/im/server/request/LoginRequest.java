package cn.chenhuamou.im.server.request;


import cn.chenhuamou.im.server.response.LoginResponse;

/**
 * Created by AMing on 15/12/24.
 * Company RongCloud
 */
public class LoginRequest {

    private String Name;
    private String Pwd;
    private String Time;
    private boolean RememberMe;
    private String ValidateCode;


    private String region;
    private String phone;
    private String password;

    public LoginRequest(String region, String phone, String password) {
        this.region = region;
        this.phone = phone;
        this.password = password;
    }

    public LoginRequest(String name, String password, String time, boolean rememberme, String validateCode) {
        this.Name = name;
        this.Pwd = password;
        this.Time = time;
        this.RememberMe = rememberme;
        this.ValidateCode = validateCode;
    }



    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPwd() {
        return Pwd;
    }

    public void setPwd(String pwd) {
        Pwd = pwd;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public boolean isRememberMe() {
        return RememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        RememberMe = rememberMe;
    }

    public String getValidateCode() {
        return ValidateCode;
    }

    public void setValidateCode(String validateCode) {
        ValidateCode = validateCode;
    }
}
