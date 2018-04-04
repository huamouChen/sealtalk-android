package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class InviteMyGroupRequest {


    public InviteMyGroupRequest(int groupId, String groupName, List<String> members) {
        GroupId = groupId;
        GroupName = groupName;
        Members = members;
    }

    /**
     * GroupId : 0
     * GroupName : string
     * Members : ["string"]
     */

    private int GroupId;
    private String GroupName;
    private List<String> Members;

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String GroupName) {
        this.GroupName = GroupName;
    }

    public List<String> getMembers() {
        return Members;
    }

    public void setMembers(List<String> Members) {
        this.Members = Members;
    }
}
