package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateMyGroupRequest {

    public CreateMyGroupRequest(String owner, String groupName, byte[] groupImgStream, List<String> members) {
        Owner = owner;
        GroupName = groupName;
        GroupImgStream = groupImgStream;
        Members = members;
    }

    /**
     * Owner : string
     * Members : ["string"]
     * GroupName : string
     * GroupImgStream : string
     */

    private String Owner;
    private String GroupName;
    private byte[] GroupImgStream;
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

    public byte[] getGroupImgStream() {
        return GroupImgStream;
    }

    public void setGroupImgStream(byte[] GroupImgStream) {
        this.GroupImgStream = GroupImgStream;
    }

    public List<String> getMembers() {
        return Members;
    }

    public void setMembers(List<String> Members) {
        this.Members = Members;
    }
}
