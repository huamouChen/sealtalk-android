package cn.chenhuamou.im.server.request;


/**
 * Created by AMing on 15/12/23.
 * Company RongCloud
 */
public class ValidateCodeRequest {

    private String id;



    public ValidateCodeRequest(String id) {
        this.id = id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
