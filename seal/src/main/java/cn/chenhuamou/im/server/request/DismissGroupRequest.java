package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/29.
 * Company RongCloud
 */
public class DismissGroupRequest {


    public DismissGroupRequest(String groupId) {
        this.groupId = groupId;
    }

    /**
     * groupId : 0
     */

    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
