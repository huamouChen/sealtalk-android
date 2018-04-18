package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class ApplyFriendRequest {

    public ApplyFriendRequest(String toUser, String message) {
        ToUser = toUser;
        Message = message;
    }

    /**
     * ToUser : string
     * Message : string
     */

    private String ToUser;
    private String Message;

    public String getToUser() {
        return ToUser;
    }

    public void setToUser(String ToUser) {
        this.ToUser = ToUser;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }
}
