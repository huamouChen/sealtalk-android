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
        GetUserChargeListResponse.ChargeBean chargehBean = mDatas.get(position);
        holder.tv_record_code.setText(chargehBean.getRecordCode());
        holder.tv_date.setText(chargehBean.getAddTime());
        holder.tv_amount.setTextColor(mContext.getResources().getColor(R.color.red));
        holder.tv_amount.setText("¥ " +  chargehBean.getChargeMoney());
    }

    @Override
    public int getItemCount() {
        return mDatas != null ? mDatas.size() : 0;
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
}

