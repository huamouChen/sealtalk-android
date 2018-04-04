package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class KickMyGroupRequest {

    public KickMyGroupRequest(int groupId, List<String> kickUsers) {
        GroupId = groupId;
        KickUsers = kickUsers;
    }

    /**
     * GroupId : 0
     * KickUsers : ["string"]
     */

    private int GroupId;
    private List<String> KickUsers;

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }

    public List<String> getKickUsers() {
        return KickUsers;
    }

    public void setKickUsers(List<String> KickUsers) {
        this.KickUsers = KickUsers;
    }
}

