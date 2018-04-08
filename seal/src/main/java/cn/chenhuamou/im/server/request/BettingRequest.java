package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class BettingRequest {


    public BettingRequest(String bettingMsg, int groupId) {
        BettingMsg = bettingMsg;
        GroupId = groupId;
    }

    /**
     * BettingMsg : string
     * GroupId : 0
     */

    private String BettingMsg;
    private int GroupId;

    public String getBettingMsg() {
        return BettingMsg;
    }

    public void setBettingMsg(String BettingMsg) {
        this.BettingMsg = BettingMsg;
    }

    public int getGroupId() {
        return GroupId;
    }

    public void setGroupId(int GroupId) {
        this.GroupId = GroupId;
    }
}

