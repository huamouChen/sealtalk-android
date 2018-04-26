package cn.chenhuamou.im.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.response.GetUserCashResponse;
import cn.chenhuamou.im.server.response.GetUserChargeListResponse;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 */
public class TakeCashHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<GetUserCashResponse.TakeCashBean> mDatas;

    private static final int TYPE_NORMAL = 1000;
    private static final int TYPE_FOOTER = 2000;


    //脚布局当前的状态,默认为没有更多
    private int footer_state = 1;


    /*
     * 改变加载更多的布局
     * */
    public void changeState(int state) {
        this.footer_state = state;
        notifyDataSetChanged();
    }

    public TakeCashHistoryAdapter(Context context, List<GetUserCashResponse.TakeCashBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_trade_history, parent, false);
            return new TakeCashHistoryAdapter.TradeHistoryViewHolder(cell);

        } else {
            View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_load_more, parent, false);
            return new TakeCashHistoryAdapter.LoadMoreViewHolder(cell);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (position != mDatas.size()) {
            TradeHistoryViewHolder viewHolder = (TradeHistoryViewHolder) holder;
            GetUserCashResponse.TakeCashBean takeCashBean = mDatas.get(position);
            viewHolder.tv_record_code.setText(takeCashBean.getRecordCode());
            viewHolder.tv_date.setText(takeCashBean.getAddTime());
            viewHolder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.green));
            double doubleString = Double.valueOf(takeCashBean.getWithDrawMoney());
            viewHolder.tv_amount.setText(String.format("¥ %.2f", doubleString));

        } else {
            LoadMoreViewHolder viewHolder = (LoadMoreViewHolder) holder;
            if (footer_state == SealConst.PULL_LOAD_MORE) {
                viewHolder.tv_load_more.setText("上拉加载更多...");
            }
            if (footer_state == SealConst.LOADING_MORE) {
                viewHolder.tv_load_more.setText("正在努力加载中...");
            }
            if (footer_state == SealConst.NO_MORE) {
                viewHolder.tv_load_more.setText("——————我是有底线的——————");
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size() + 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position >= mDatas.size() ? TYPE_FOOTER : TYPE_NORMAL;
    }


    /*
     * viewHolder
     * */
    public class TradeHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_record_code, tv_date, tv_amount;


        public TradeHistoryViewHolder(View itemView) {
            super(itemView);
            tv_record_code = itemView.findViewById(R.id.tv_record_code);
            tv_date = itemView.findViewById(R.id.tv_trade_date);
            tv_amount = itemView.findViewById(R.id.tv_trade_amount);
        }
    }

    private class LoadMoreViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_load_more;

        public LoadMoreViewHolder(View itemView) {
            super(itemView);
            tv_load_more = itemView.findViewById(R.id.tv_load_more);
        }
    }
}

