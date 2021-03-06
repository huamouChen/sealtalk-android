package cn.chenhuamou.im.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.KqwfPcddResponse;
import cn.chenhuamou.im.server.utils.NToast;

/**
 * Created by Rex on 2018/4/8.
 * Email chenhm4444@gmail.com
 */
public class BetActivity extends Activity implements View.OnClickListener, OnDataListener {


    protected Context mContext;
    public AsyncTaskManager mAsyncTaskManager;
    protected SealAction action;

    private String targetId;
    private String conversationType;

    private static final int Bet = 1000;


    private LinearLayout linear_mask;

    private Button btn_confirm;
    private RecyclerView rv_history;     // 历史记录  RecyclerView
    private RecyclerView rv_play;       // 玩法选择 RecyclerView
    private RecyclerView rv_hz;         // 和值 RecyclerView
    private RecyclerView rv_money;     // 下注金额 RecyclerView

    private HZAdapter play_adapter;
    private HZAdapter hz_adapter;
    private HZAdapter money_adapter;

    private List<String> play_list;
    private List<String> hz_list;
    private List<String> money_list;

    private EditText mEditText;   // 金额输入框

    private String playString;   // 玩法
    private String moneyString;  // 下注金额


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        String betString = String.format("#%s|%s#", playString, moneyString);
        return action.postKQWFPCDD(betString, targetId);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        switch (requestCode) {
            case Bet:
                KqwfPcddResponse kqwfPcddResponse = (KqwfPcddResponse) result;

                if (kqwfPcddResponse.isResult()) {
                    BroadcastManager.getInstance(mContext).sendBroadcast("Bet", "投注成功");
                } else {
                    BroadcastManager.getInstance(mContext).sendBroadcast("Bet", kqwfPcddResponse.getError());
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case Bet:
                NToast.shortToast(mContext, "KQWF-PCDD失败");
                break;
        }
    }


    // 点击事件
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.linear_mask:
                finish();
                break;

            case R.id.btn_confirm: {
                // 只有是群聊的时候才发送消息或者指定的群才发送消息
                if (conversationType.equals("group")) {
                    if (TextUtils.isEmpty(playString)) {
                        NToast.shortToast(mContext, "玩法不能为空");
                        return;
                    }

                    if (TextUtils.isEmpty(moneyString)) {
                        NToast.shortToast(mContext, "金额不能为空");
                        return;
                    }

                    // 调用PC蛋蛋接口
                    mAsyncTaskManager.request(Bet, this);
                    // 发送消息
                    String betString = playString + moneyString;
                    BroadcastManager.getInstance(mContext).sendBroadcast("SendBetMessage", betString);
                }

                finish();
            }
            break;
        }


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bet);
        mContext = this;

        mAsyncTaskManager = AsyncTaskManager.getInstance(getApplicationContext());
        // Activity管理
        action = new SealAction(mContext);

        initView();
        initData();
    }

    // 初始化数据
    private void initData() {
        Intent intent = getIntent();
        targetId = intent.getStringExtra("targetId");
        conversationType = intent.getStringExtra("conversationType");
    }

    // 初始化 view
    private void initView() {
        linear_mask = (LinearLayout) findViewById(R.id.linear_mask);
        linear_mask.setOnClickListener(this);

        mEditText = (EditText) findViewById(R.id.et_money);
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 && Integer.valueOf(s.toString()) > 0) {
                    money_adapter.refreshData(-1);
                    moneyString = s.toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);


        // 历史记录
        rv_history = (RecyclerView) findViewById(R.id.rv_history);
        List<String> list1 = new ArrayList<>();
        list1.add("豹子");
        list1.add("大双");
        list1.add("小双");
        list1.add("蓝波");
        rv_history.setAdapter(new HZAdapter(list1));
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        rv_history.setLayoutManager(gridLayoutManager);

        // 玩法选择
        rv_play = (RecyclerView) findViewById(R.id.rv_play);
        play_list = new ArrayList<>();
        play_list.add("极大");
        play_list.add("极小");
        play_list.add("大双");
        play_list.add("小双");
        play_list.add("大单");
        play_list.add("小单");
        play_list.add("大");
        play_list.add("小");
        play_list.add("双");
        play_list.add("单");
        play_list.add("豹子");
        play_list.add("蓝波");
        play_list.add("绿波");
        play_list.add("红波");
        play_adapter = new HZAdapter(play_list);
        rv_play.setAdapter(play_adapter);
        play_adapter.setmBetItemClickListner(new HZAdapter.BetItemClickInterface() {
            @Override
            public void onBetItemClick(View itemView, int position) {
                if (itemView.isSelected()) {
                    position = -1;
                }
                playString = position != -1 ? play_list.get(position) : "";
                play_adapter.refreshData(position);
            }
        });
        GridLayoutManager gridLayoutManager2 = new GridLayoutManager(this, 4);
        rv_play.setLayoutManager(gridLayoutManager2);


        // 和值
        rv_hz = (RecyclerView) findViewById(R.id.rv_hz);
        List<String> list = new ArrayList<>();
        list.add("17");
        list.add("20");
        list.add("23");
        list.add("26");
        hz_adapter = new HZAdapter(list);
        hz_adapter.setmBetItemClickListner(new HZAdapter.BetItemClickInterface() {
            @Override
            public void onBetItemClick(View itemView, int position) {
                if (itemView.isSelected()) {
                    position = -1;
                }
                hz_adapter.refreshData(position);
            }
        });
        rv_hz.setAdapter(hz_adapter);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(this, 4);
        rv_hz.setLayoutManager(gridLayoutManager3);


        // 下注金额
        rv_money = (RecyclerView) findViewById(R.id.rv_money);
        money_list = new ArrayList<>();
        money_list.add("10元");
        money_list.add("100元");
        money_list.add("500元");
        money_list.add("1000元");

        money_adapter = new HZAdapter(money_list);
        money_adapter.setmBetItemClickListner(new HZAdapter.BetItemClickInterface() {
            @Override
            public void onBetItemClick(View itemView, int position) {
                if (itemView.isSelected()) {
                    position = -1;
                }
                moneyString = position != -1 ? money_list.get(position) : "";
                moneyString = moneyString.length() > 0 ? moneyString.substring(0, moneyString.length() - 1) : moneyString;
                money_adapter.refreshData(position);
                // 清空输入框
                mEditText.setText("");
                mEditText.clearFocus();
            }
        });
        rv_money.setAdapter(money_adapter);
        GridLayoutManager gridLayoutManager4 = new GridLayoutManager(this, 4);
        rv_money.setLayoutManager(gridLayoutManager4);


    }

    // recycle adapter
    public static class HZAdapter extends RecyclerView.Adapter<HZAdapter.HZViewHolder> implements View.OnClickListener {
        // 当前选中的下表
        private int current_index = -1;

        // 数据源
        private List<String> mDatas;

        // 点击接口
        private BetItemClickInterface mBetItemClickListner;

        public BetItemClickInterface getmBetItemClickListner() {
            return mBetItemClickListner;
        }

        public void setmBetItemClickListner(BetItemClickInterface mBetItemClickListner) {
            this.mBetItemClickListner = mBetItemClickListner;
        }

        public HZAdapter(List<String> list) {
            mDatas = list;
        }

        public void refreshData(int index) {
            current_index = index;
            notifyDataSetChanged();
        }

        @Override
        public HZViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            HZViewHolder viewHolder = new HZViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_bet, parent, false));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(HZViewHolder holder, int position) {
            holder.tv_item.setText(mDatas.get(position));
            holder.tv_item.setTag(position);
            holder.tv_item.setOnClickListener(this);
            holder.tv_item.setSelected(current_index == position);


        }

        @Override
        public int getItemCount() {
            return mDatas.size();
        }

        @Override
        public void onClick(View v) {
            if (mBetItemClickListner != null) {
                mBetItemClickListner.onBetItemClick(v, Integer.valueOf(v.getTag().toString()));
            }
        }


        public class HZViewHolder extends RecyclerView.ViewHolder {

            private TextView tv_item;

            public HZViewHolder(View itemView) {
                super(itemView);
                tv_item = (TextView) itemView.findViewById(R.id.tv_item);
            }
        }


        private interface BetItemClickInterface {
            void onBetItemClick(View itemView, int position);
        }

    }
}
