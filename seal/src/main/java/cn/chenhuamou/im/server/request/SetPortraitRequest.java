package cn.chenhuamou.im.server.request;

/**
 * Created by AMing on 16/1/13.
 * Company RongCloud
 */
public class SetPortraitRequest {

    private byte[] ImgStream;


    public SetPortraitRequest(byte[] portraitUri) {
        this.ImgStream = portraitUri;
    }

    public byte[] getImgStream() {
        return ImgStream;
    }

    public void setImgStream(byte[] imgStream) {
        this.ImgStream = imgStream;
    }
}
