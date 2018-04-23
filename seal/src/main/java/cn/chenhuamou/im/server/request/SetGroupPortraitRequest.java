package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class SetGroupPortraitRequest {

    public SetGroupPortraitRequest(String groupId, byte[] groupImgStream) {
        GroupId = groupId;
        GroupImgStream = groupImgStream;
    }

    /**
     * GroupId : 0
     * GroupImgStream : string
     */

    private String GroupId;
    private byte[] GroupImgStream;

    public String getGroupId() {
        return GroupId;
    }

    public void setGroupId(String GroupId) {
        this.GroupId = GroupId;
    }

    public byte[] getGroupImgStream() {
        return GroupImgStream;
    }

    public void setGroupImgStream(byte[] GroupImgStream) {
        this.GroupImgStream = GroupImgStream;
    }
}
