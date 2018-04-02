package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateMyGroupRequest {

    public CreateMyGroupRequest(String owner, String groupName, List<String> members) {
        this.owner = owner;
        this.groupName = groupName;
        this.members = members;
    }

    private String owner;

    private String groupName;

    private List<String> members;

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<String> getMembers() {
        return members;
    }

    public void setMembers(List<String> members) {
        this.members = members;
    }
}
