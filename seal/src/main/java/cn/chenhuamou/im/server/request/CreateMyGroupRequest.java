package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateMyGroupRequest {


    public CreateMyGroupRequest(String owner, String groupName, List<String> members) {
        Owner = owner;
        GroupName = groupName;
        Members = members;
    }

    /**
     * Owner : string
     * Members : ["string"]
     * GroupName : string
     */

    private String Owner;
    private String GroupName;
    private List<String> Members;

    public String getOwner() {
        return Owner;
    }

    public void setOwner(String Owner) {
        this.Owner = Owner;
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
