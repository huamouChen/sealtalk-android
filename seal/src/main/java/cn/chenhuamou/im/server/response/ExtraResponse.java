package cn.chenhuamou.im.server.response;


/**
 * Created by AMing on 16/1/8.
 * Company RongCloud
 */
public class ExtraResponse {
    private int ApplyId;

    public ExtraResponse(int applyId) {
        ApplyId = applyId;
    }

    public int getApplyId() {
        return ApplyId;
    }

    public void setApplyId(int applyId) {
        ApplyId = applyId;
    }
}
