package cn.chenhuamou.im.server.request;

import java.util.List;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class AgreeMyFriendRequest {

    private String ApplyId;

    public AgreeMyFriendRequest(String applyId) {
        this.ApplyId = applyId;
    }

    public String getApplyId() {
        return ApplyId;
    }

    public void setApplyId(String applyId) {
        ApplyId = applyId;
    }
}
