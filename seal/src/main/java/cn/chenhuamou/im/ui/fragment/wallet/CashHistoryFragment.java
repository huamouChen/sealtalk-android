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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetUserCashResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.ui.adapter.TakeCashHistoryAdapter;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 * 提现记录fragment
 */
public class CashHistoryFragment extends Fragment implements OnDataListener {

    private Context mContext;
    public AsyncTaskManager mAsyncTaskManager;
    protected SealAction action;

    private String userId;
    private static final int GET_USER_TAKE_CASH = 1000;

    private RecyclerView mRecyclerView;
    private TakeCashHistoryAdapter mTakeCashHistoryAdapter;
    private List<GetUserCashResponse.TakeCashBean> mDatas;

    private TextView tv_no_data;
    private int pageIndex = 1;
    private int pageSize = 20;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private String startDate, endDate;


    /*
    * 刷新数据
    * */
    public void refreshData(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate =endDate;
        mAsyncTaskManager.request(GET_USER_TAKE_CASH, this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        View contentView = inflater.inflate(R.layout.fragment_cash_history, container, false);
        initData();
        initView(contentView);
        return contentView;
    }

    /*
     * 绑定控件
     * */
    private void initView(View contentView) {
        mRecyclerView = contentView.findViewById(R.id.recyclerview_cash);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mTakeCashHistoryAdapter = new TakeCashHistoryAdapter(getContext(), mDatas);
        mRecyclerView.setAdapter(mTakeCashHistoryAdapter);

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

        // 默认获取今天的
        startDate = getCurrentTime();
        endDate = getCurrentTime();
        mAsyncTaskManager.request(GET_USER_TAKE_CASH, this);
    }

    // 获取系统的当前时间
    private String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    @Override
    public Object doInBackground(int requestCode, String parameter) throws HttpException {
        return action.getUserCashLists(userId, startDate, endDate, pageSize, pageIndex);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        GetUserCashResponse cashResponse = (GetUserCashResponse) result;
        if (cashResponse.getCode().getCodeId().equals("100")) {
            List<GetUserCashResponse.TakeCashBean> resultList = cashResponse.getList();
            mDatas.addAll(resultList);
            // 判断是否有数据
            if (pageIndex == 1 && (resultList == null || resultList.size() == 0)) {
                tv_no_data.setVisibility(View.VISIBLE);
            } else {
                tv_no_data.setVisibility(View.GONE);
            }
            mTakeCashHistoryAdapter.notifyDataSetChanged();
        } else {
            NToast.shortToast(mContext, cashResponse.getCode().getDescription());
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        NToast.shortToast(mContext, "获取提现记录失败");
    }
}
