package cn.rongcloud.im.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.rongcloud.im.App;
import cn.rongcloud.im.R;
import cn.rongcloud.im.SealConst;
import cn.rongcloud.im.SealUserInfoManager;
import cn.rongcloud.im.db.Groups;
import cn.rongcloud.im.server.broadcast.BroadcastManager;
import cn.rongcloud.im.server.network.http.HttpException;
import cn.rongcloud.im.server.pinyin.Group;
import cn.rongcloud.im.server.response.GetRongGroupResponse;
import cn.rongcloud.im.server.utils.CommonUtils;
import cn.rongcloud.im.server.utils.NToast;
import cn.rongcloud.im.server.utils.RongGenerate;
import cn.rongcloud.im.server.widget.LoadDialog;
import cn.rongcloud.im.server.widget.SelectableRoundedImageView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/3/8.
 * Company RongCloud
 */
public class GroupListActivity extends BaseActivity {

    private static final int GET_RONG_GROUPS = 700;

    private ListView mGroupListView;
    private GroupAdapter adapter;
    private TextView mNoGroups;
    private EditText mSearch;
    private List<Groups> mList;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fr_group_list);
        setTitle(R.string.my_groups);
        mGroupListView = (ListView) findViewById(R.id.group_listview);
        mNoGroups = (TextView) findViewById(R.id.show_no_group);
        mSearch = (EditText) findViewById(R.id.group_search);
        mTextView = (TextView)findViewById(R.id.foot_group_size);
        initData();

        // 注册广播来更新 群聊的信息
        BroadcastManager.getInstance(mContext).addAction(SealConst.GROUP_LIST_UPDATE, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        });
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case GET_RONG_GROUPS:
                SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
                String userName = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");
                return action.getRongGroups(userName);
        }

        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case GET_RONG_GROUPS:
                    GetRongGroupResponse getRongGroupResponse = (GetRongGroupResponse) result;
                    mList = getRongGroupResponse.getValue();
                    if (mList != null && mList.size() > 0) {
                        adapter = new GroupAdapter(mContext, mList);
                        mGroupListView.setAdapter(adapter);
                        mNoGroups.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);
                        mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
                        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Groups bean = (Groups) adapter.getItem(position);
                                RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
                            }
                        });

                        mSearch.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                            }

                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                                filterData(s.toString());
                            }

                            @Override
                            public void afterTextChanged(Editable s) {
                            }
                        });
                    } else {
                        mNoGroups.setVisibility(View.VISIBLE);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case GET_RONG_GROUPS:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.get_groups_api_fail);
                break;
        }
    }

    private void initData() {
        request(GET_RONG_GROUPS);



//        // 暂时写死群组的信息
//        List<Groups> list = new ArrayList<>();
//        Groups groups1 = new Groups("g123", "地球最强战队", "http://huamouchen.info/bmw.jpg");
//        Groups groups2 = new Groups("g456", "吹牛逼打酱油", "http://huamouchen.info/bmw.jpg");
//        list.add(groups1); list.add(groups2);
//
//        mList = list;
//        if (mList != null && mList.size() > 0) {
//            adapter = new GroupAdapter(mContext, mList);
//            mGroupListView.setAdapter(adapter);
//            mNoGroups.setVisibility(View.GONE);
//            mTextView.setVisibility(View.VISIBLE);
//            mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
//            mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Groups bean = (Groups) adapter.getItem(position);
//                    RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
//                }
//            });
//
//            mSearch.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//                    filterData(s.toString());
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//                }
//            });
//        } else {
//            mNoGroups.setVisibility(View.VISIBLE);
//        }






//        // 从网络获取 群聊列表
//        SealUserInfoManager.getInstance().getGroups(new SealUserInfoManager.ResultCallback<List<Groups>>() {
//            @Override
//            public void onSuccess(List<Groups> groupsList) {
//
//                // 暂时写死群组的信息
//                List<Groups> list = new ArrayList<>();
//                Groups groups1 = new Groups("g123", "地球最强战队", "http://huamouchen.info/bmw.jpg");
//                Groups groups2 = new Groups("g456", "吹牛逼打酱油", "http://huamouchen.info/bmw.jpg");
//                list.add(groups1); list.add(groups2);
//                groupsList = list;
//
//                mList = groupsList;
//                if (mList != null && mList.size() > 0) {
//                    adapter = new GroupAdapter(mContext, mList);
//                    mGroupListView.setAdapter(adapter);
//                    mNoGroups.setVisibility(View.GONE);
//                    mTextView.setVisibility(View.VISIBLE);
//                    mTextView.setText(getString(R.string.ac_group_list_group_number, mList.size()));
//                    mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                        @Override
//                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                            Groups bean = (Groups) adapter.getItem(position);
//                            RongIM.getInstance().startGroupChat(GroupListActivity.this, bean.getGroupsId(), bean.getName());
//                        }
//                    });
//
//                    mSearch.addTextChangedListener(new TextWatcher() {
//                        @Override
//                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                        }
//
//                        @Override
//                        public void onTextChanged(CharSequence s, int start, int before, int count) {
//                            filterData(s.toString());
//                        }
//
//                        @Override
//                        public void afterTextChanged(Editable s) {
//                        }
//                    });
//                } else {
//                    mNoGroups.setVisibility(View.VISIBLE);
//                }
            }

//            @Override
//            public void onError(String errString) {
//
//            }
//        });
//    }

    private void filterData(String s) {
        List<Groups> filterDataList = new ArrayList<>();
        if (TextUtils.isEmpty(s)) {
            filterDataList = mList;
        } else {
            for (Groups groups : mList) {
                if (groups.getName().contains(s)) {
                    filterDataList.add(groups);
                }
            }
        }
        adapter.updateListView(filterDataList);
        mTextView.setText(getString(R.string.ac_group_list_group_number, filterDataList.size()));
    }


    class GroupAdapter extends BaseAdapter {

        private Context context;

        private List<Groups> list;

        public GroupAdapter(Context context, List<Groups> list) {
            this.context = context;
            this.list = list;
        }

        /**
         * 传入新的数据 刷新UI的方法
         */
        public void updateListView(List<Groups> list) {
            this.list = list;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            if (list != null) return list.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (list == null)
                return null;

            if (position >= list.size())
                return null;

            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            final Groups mContent = list.get(position);
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.group_item_new, parent, false);
                viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.groupname);
                viewHolder.mImageView = (SelectableRoundedImageView) convertView.findViewById(R.id.groupuri);
                viewHolder.groupId = (TextView) convertView.findViewById(R.id.group_id);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.tvTitle.setText(mContent.getName());
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(mContent);
            ImageLoader.getInstance().displayImage(portraitUri, viewHolder.mImageView, App.getOptions());
            if (context.getSharedPreferences("config", MODE_PRIVATE).getBoolean("isDebug", false)) {
                viewHolder.groupId.setVisibility(View.VISIBLE);
                viewHolder.groupId.setText(mContent.getGroupsId());
            }
            return convertView;
        }


        class ViewHolder {
            /**
             * 昵称
             */
            TextView tvTitle;
            /**
             * 头像
             */
            SelectableRoundedImageView mImageView;
            /**
             * userId
             */
            TextView groupId;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BroadcastManager.getInstance(mContext).destroy(SealConst.GROUP_LIST_UPDATE);
    }


}
