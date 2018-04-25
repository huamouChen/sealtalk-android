package cn.chenhuamou.im.ui.fragment.wallet;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetUserCashResponse;
import cn.chenhuamou.im.server.response.GetUserChargeListResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.ui.adapter.ChargeHistoryAdapter;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 * 充值记录fragment
 */
public class RechargeHistoryFragment extends Fragment implements OnDataListener {

    private Context mContext;
    public AsyncTaskManager mAsyncTaskManager;
    protected SealAction action;

    private String userId;
    private static final int GET_USER_TAKE_CASH = 1000;

    private RecyclerView mRecyclerView;
    private ChargeHistoryAdapter mChargeHistoryAdapter;
    private List<GetUserChargeListResponse.ChargeBean> mDatas;

    private TextView tv_no_data;
    private int pageIndex = 1;
    private int pageSize = 20;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View contentView = inflater.inflate(R.layout.fragment_recharge_history, container, false);
        initData();
        initView(contentView);
        return contentView;
    }

    /*
     * 绑定控件
     * */
    private void initView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recyclerview_recharge);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mChargeHistoryAdapter = new ChargeHistoryAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mChargeHistoryAdapter);

        tv_no_data = contentView.findViewById(R.id.tv_no_data);
    }

    /*
     * 获取数据
     * */
    private void initData() {
        userId = mContext.getSharedPreferences(SealConst.SharedPreferencesName,  Context.MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
        mAsyncTaskManager = AsyncTaskManager.getInstance(mContext);
        // Activity管理
        action = new SealAction(mContext);

        mDatas = new ArrayList<>();

        mAsyncTaskManager.request(GET_USER_TAKE_CASH, this);
    }

    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        return action.getUserChargeLists(userId, "2018-04-25", "2018-04-25", pageSize, pageIndex);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        GetUserChargeListResponse cashResponse = (GetUserChargeListResponse) result;
        if (cashResponse.getCode().getCodeId().equals("100")) {
            List<GetUserChargeListResponse.ChargeBean> resultList = cashResponse.getList();
            mDatas.addAll(resultList);
            // 判断是否有数据
            if (pageIndex == 1 && (resultList == null || resultList.size() == 0)) {
                tv_no_data.setVisibility(View.VISIBLE);
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
            mChargeHistoryAdapter.notifyDataSetChanged();
        } else {
            NToast.shortToast(mContext, cashResponse.getCode().getDescription());
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        NToast.shortToast(mContext, "获取充值记录失败");
    }
}
