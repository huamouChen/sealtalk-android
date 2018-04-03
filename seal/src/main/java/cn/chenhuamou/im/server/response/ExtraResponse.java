package cn.chenhuamou.im.server.response;


/**
 * Created by AMing on 16/1/8.
 * Company RongCloud
 */
public class ExtraResponse {
    private String ApplyId;

    public ExtraResponse(String applyId) {
        ApplyId = applyId;
    }

    public String getApplyId() {
        return ApplyId;
    }

    public void setApplyId(String applyId) {
        ApplyId = applyId;
    }
}
