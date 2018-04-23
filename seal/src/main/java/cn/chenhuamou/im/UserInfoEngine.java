package cn.chenhuamou.im;

import android.content.Context;
import android.net.Uri;


import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.FindUserInfoResponse;
import cn.chenhuamou.im.server.response.GetUserInfoByIdResponse;
import io.rong.imlib.model.UserInfo;

/**
 * 用户信息提供者的异步请求类
 * Created by AMing on 15/12/10.
 * Company RongCloud
 */
public class UserInfoEngine implements OnDataListener {


    private static UserInfoEngine instance;
    private UserInfoListener mListener;
    private Context context;

    public static UserInfoEngine getInstance(Context context) {
        if (instance == null) {
            instance = new UserInfoEngine(context);
        }
        return instance;
    }

    private UserInfoEngine(Context context) {
        this.context = context;
    }


    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private static final int REQUSERINFO = 4234;

    public void startEngine(String userid) {
        setUserid(userid);
        AsyncTaskManager.getInstance(context).request(userid, REQUSERINFO, this);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return new SealAction(context).getUserInfoById(id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            FindUserInfoResponse res = (FindUserInfoResponse) result;
            if (res.getCode().getCodeId().equals("100")) {
                String nickName = (res.getValue().getNickName() != null && !res.getValue().getNickName().isEmpty()) ? res.getValue().getNickName() : res.getValue().getUserName();
                String portrait = (res.getValue().getHeadimg() != null && !res.getValue().getHeadimg().isEmpty()) ? (BaseAction.DOMAIN + res.getValue().getHeadimg()) : "";
                UserInfo userInfo = new UserInfo(res.getValue().getUserName(), nickName, Uri.parse(portrait));
                if (mListener != null) {
                    mListener.onResult(userInfo);
                }
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (mListener != null) {
            mListener.onResult(null);
        }
    }

    public void setListener(UserInfoListener listener) {
        this.mListener = listener;
    }

    public interface UserInfoListener {
        void onResult(UserInfo info);
    }
}
