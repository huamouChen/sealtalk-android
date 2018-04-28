package cn.chenhuamou.im.ui.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetChatRoomResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;


public class DiscoverFragment extends Fragment implements OnDataListener {

    private static final int GETDEFCONVERSATION = 333;

    private ListView chatroomListView;

    private AsyncTaskManager atm = AsyncTaskManager.getInstance(getActivity());
    private ArrayList<GetChatRoomResponse.ValueBean> chatroomList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chatroom_list, container, false);
        initViews(view);
        atm.request(GETDEFCONVERSATION, this);
        return view;
    }

    private void initViews(View view) {
        chatroomListView = view.findViewById(R.id.listView_chatroom);
        // 点击 listView
        chatroomListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RongIM.getInstance().startConversation(getActivity(), Conversation.ConversationType.CHATROOM, chatroomList.get(0).getGroupId(), chatroomList.get(0).getGroupName());
            }
        });

        //回调时的线程并不是UI线程，不能在回调中直接操作UI 聊天室状态监听 加入中  已经加入 离开 错误
        RongIMClient.getInstance().setChatRoomActionListener(new RongIMClient.ChatRoomActionListener() {
            @Override
            public void onJoining(String chatRoomId) {

            }

            @Override
            public void onJoined(String chatRoomId) {

            }

            @Override
            public void onQuited(String chatRoomId) {

            }

            @Override
            public void onError(String chatRoomId, final RongIMClient.ErrorCode code) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == RongIMClient.ErrorCode.RC_NET_UNAVAILABLE || code == RongIMClient.ErrorCode.RC_NET_CHANNEL_INVALID) {
                            NToast.shortToast(getActivity(), getString(R.string.network_not_available));
                        } else {
                            NToast.shortToast(getActivity(), getString(R.string.fr_chat_room_join_failure));
                        }
                    }
                });
            }
        });
    }


    /*
     * list view adpter
     * */
    private class ChatroomAdapter extends BaseAdapter {

        private ChatroomHolder holder = null;

        private Context aContext;
        private List<GetChatRoomResponse.ValueBean> mDatas;

        public ChatroomAdapter(Context context, List<GetChatRoomResponse.ValueBean> data) {
            this.aContext = context;
            this.mDatas = data;
        }

        @Override
        public int getCount() {
            return mDatas != null ? mDatas.size() : 0;
        }

        @Override
        public GetChatRoomResponse.ValueBean getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                holder = new ChatroomHolder();
                convertView = LayoutInflater.from(aContext).inflate(R.layout.cell_chatroom, parent, false);
                holder.tv_title = convertView.findViewById(R.id.tv_title);
                holder.iv_heder = convertView.findViewById(R.id.iv_header);
                convertView.setTag(holder);
            } else {
                holder = (ChatroomHolder) convertView.getTag();
            }
            // 赋值
            GetChatRoomResponse.ValueBean itemBean = getItem(position);
            holder.tv_title.setText(itemBean.getGroupName());
            // 设置头像
            String groupPortrait = "";
            if (itemBean.getGroupImage() != null && !itemBean.getGroupImage().isEmpty()) {
                groupPortrait = BaseAction.DOMAIN + itemBean.getGroupImage();
            }
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri
                    (new UserInfo(itemBean.getGroupId(), itemBean.getGroupName(), Uri.parse(groupPortrait)));
            ImageLoader.getInstance().displayImage(portraitUri, holder.iv_heder, App.getOptions());
            return convertView;
        }

        private class ChatroomHolder {
            private ImageView iv_heder;
            private TextView tv_title;

            public ChatroomHolder() {

            }
        }
    }


    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        return new SealAction(getActivity()).getChatRoom();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onSuccess(int requestCode, Object result) {
        GetChatRoomResponse response = (GetChatRoomResponse) result;
        if (response.getCode().getCodeId().equals("100")) {
            ArrayList<GetChatRoomResponse.ValueBean> resultEntityArrayList = new ArrayList();
            chatroomList = new ArrayList();
            if (response.getValue().size() > 0) {
                resultEntityArrayList.clear();
                chatroomList.clear();
                chatroomList.addAll(response.getValue());
                chatroomListView.setAdapter(new ChatroomAdapter(getContext(), chatroomList));
            }
        } else {
            NToast.shortToast(getContext(), "获取聊天室列表失败");
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(getContext());
        NToast.shortToast(getContext(), "获取聊天室列表失败");
    }
}
