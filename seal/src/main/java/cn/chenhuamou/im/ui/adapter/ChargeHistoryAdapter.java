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
import cn.chenhuamou.im.server.response.GetUserCashResponse;
import cn.chenhuamou.im.server.response.GetUserChargeListResponse;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 */
public class ChargeHistoryAdapter extends RecyclerView.Adapter<ChargeHistoryAdapter.TradeHistoryViewHolder> {
    private Context mContext;
    private List<GetUserChargeListResponse.ChargeBean> mDatas;

    public ChargeHistoryAdapter(Context context, List<GetUserChargeListResponse.ChargeBean> datas) {
        mContext = context;
        mDatas = datas;
    }

    @NonNull
    @Override
    public TradeHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cell = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_trade_history, parent, false);
        TradeHistoryViewHolder viewHolder = new TradeHistoryViewHolder(cell);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TradeHistoryViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
    }


    /*
    * viewHolder
    * */
    public class TradeHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_date, tv_type, tv_amount;


        public TradeHistoryViewHolder(View itemView) {
            super(itemView);
        }


    }
}

