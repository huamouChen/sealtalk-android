package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/18.
 * Company RongCloud
 */
public class SetNameRequest {

    private String NickName;


    public SetNameRequest(String nickName) {
        NickName = nickName;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String nickName) {
        NickName = nickName;
    }
}
