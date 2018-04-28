package cn.chenhuamou.im.ui.activity.wallet;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.UserBalanceResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.activity.BaseActivity;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener {


    private static final int GET_BALANCE = 1000;

    private TitleBar titleBar;
    private TextView tv_balance, tv_userMoney, tv_agMoney, tv_texMoney, tv_ptMoney;  // 余额
    private LinearLayout mRechargeView, mGetDepositView;  // 充值 提现
    private RelativeLayout rl_account_info, rl_tradeDetail, rl_secureSetting, rl_mybank;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        initView();
        initData();

        BroadcastManager.getInstance(mContext).addAction(SealConst.RefreshMyWallet, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initData();
            }
        });
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("我的钱包");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_balance = findViewById(R.id.tv_wallet_balance);
        mRechargeView = findViewById(R.id.ll_wallet_recharge);
        mGetDepositView = findViewById(R.id.ll_getDeposit);
        rl_account_info = findViewById(R.id.rl_accountInfo);
        rl_tradeDetail = findViewById(R.id.rl_tradeDetail);
        rl_secureSetting = findViewById(R.id.rl_secureSetting);
        rl_mybank = findViewById(R.id.rl_mybank);

        tv_userMoney = findViewById(R.id.tv_userMoney);
        tv_agMoney = findViewById(R.id.tv_agMoney);
        tv_texMoney = findViewById(R.id.tv_texMoney);
        tv_ptMoney = findViewById(R.id.tv_ptMoney);

        initListener();
    }

    /*
     * 设置监听
     * */
    private void initListener() {
        mRechargeView.setOnClickListener(this);
        mGetDepositView.setOnClickListener(this);
        rl_account_info.setOnClickListener(this);
        rl_tradeDetail.setOnClickListener(this);
        rl_secureSetting.setOnClickListener(this);
        rl_mybank.setOnClickListener(this);
    }

    /*
     * 获取账户余额
     * */
    private void initData() {
        // 获取用户账户余额
        LoadDialog.show(mContext);
        request(GET_BALANCE);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return action.getUserBalance();
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        String balanceString = (String) result;

        if (balanceString != null) {
            double balance = Double.valueOf(balanceString);
            String moneyString = String.format("¥" + "%.2f", balance);
            tv_balance.setText(moneyString);
        } else {
            NToast.shortToast(mContext, "获取用户余额返回为空");
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "获取用户余额失败");
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_wallet_recharge:
                startActivity(new Intent(this, RechargeActivity.class));
                break;
            case R.id.ll_getDeposit:
                startActivity(new Intent(this, GetDepositActivity.class));
                break;
            case R.id.rl_accountInfo:
                startActivity(new Intent(this, AccountInfoActivity.class));
                break;
            case R.id.rl_tradeDetail:
                startActivity(new Intent(this, TradeHistoryActivity.class));
                break;
            case R.id.rl_secureSetting:
                startActivity(new Intent(this, SecureSettingActivity.class));
                break;
            case R.id.rl_mybank:
                startActivity(new Intent(this, BankSettingActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        BroadcastManager.getInstance(mContext).destroy(SealConst.RefreshMyWallet);
        super.onDestroy();

    }
}
