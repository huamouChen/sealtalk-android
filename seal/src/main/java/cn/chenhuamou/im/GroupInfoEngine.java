package cn.chenhuamou.im;

import android.content.Context;
import android.net.Uri;

import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetGroupInfoResponse;
import cn.chenhuamou.im.server.response.GetRongGroupInfoResponse;
import io.rong.imlib.model.Group;

/**
 * 群组信息提供者的异步请求类
 * Created by AMing on 16/1/26.
 * Company RongCloud
 */
public class GroupInfoEngine implements OnDataListener {

    private static final int REQUEST_GROUP_INFO = 19;
    private static GroupInfoEngine instance;
    private GroupInfoListeners mListener;

    private String groupId;
    private Group group;

    private Context context;


    private GroupInfoEngine(Context context) {
        this.context = context;
    }


    public static GroupInfoEngine getInstance(Context context) {
        if (instance == null) {
            instance = new GroupInfoEngine(context);
        }
        return instance;
    }


    public Group startEngine(String groupId) {
        setGroupId(groupId);
        AsyncTaskManager.getInstance(context).request(groupId, REQUEST_GROUP_INFO, this);
        return getGroup();
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return new SealAction(context).getRongGroupInfo(id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            GetRongGroupInfoResponse getRongGroupInfoResponse = (GetRongGroupInfoResponse) result;
            if (getRongGroupInfoResponse.getCode().getCodeId().equals("100")) {
                group = new Group(getRongGroupInfoResponse.getValue().getGroupId(), getRongGroupInfoResponse.getValue().getGroupName(), Uri.parse(""));
                if (mListener != null) {
                    mListener.onResult(group);
                }
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case REQUEST_GROUP_INFO:
                break;
        }
    }

    public String getGroupId() {
        return groupId;
    }

    public void setmListener(GroupInfoListeners mListener) {
        this.mListener = mListener;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public interface GroupInfoListeners {
        void onResult(Group info);
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}
