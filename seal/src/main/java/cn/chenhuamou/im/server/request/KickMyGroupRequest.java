package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class KickMyGroupRequest {


    private String groupId;

    private String kickUser;


    public KickMyGroupRequest(String groupId, String kickUser) {
        this.groupId = groupId;
        this.kickUser = kickUser;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getKickUser() {
        return kickUser;
    }

    public void setKickUser(String kickUser) {
        this.kickUser = kickUser;
    }
}
