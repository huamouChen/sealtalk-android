package cn.chenhuamou.im.ui.activity.MyWallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import org.w3c.dom.Text;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.server.BaseAction;
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
    private TextView tv_balance;  // 余额
    private LinearLayout mRechargeView, mGetDepositView;  // 充值 提现
    private RelativeLayout rl_account_info, rl_tradeDetail, rl_secureSetting, rl_mybank;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        initView();
        initData();
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

        tv_balance = (TextView) findViewById(R.id.tv_change);
        mRechargeView = (LinearLayout) findViewById(R.id.ll_wallet_recharge);
        mGetDepositView = (LinearLayout) findViewById(R.id.ll_getDeposit);
        rl_account_info = (RelativeLayout) findViewById(R.id.rl_accountInfo);
        rl_tradeDetail = (RelativeLayout) findViewById(R.id.rl_tradeDetail);
        rl_secureSetting = (RelativeLayout) findViewById(R.id.rl_secureSetting);
        rl_mybank = (RelativeLayout) findViewById(R.id.rl_mybank);

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
        UserBalanceResponse userBalanceResponse = (UserBalanceResponse) result;
        if (userBalanceResponse != null) {
            int balance = userBalanceResponse.getAgMoney() + userBalanceResponse.getPtMoney() + userBalanceResponse.getTexMoney() + userBalanceResponse.getUserMoney();
            tv_balance.setText("¥ " + balance + "");
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
}
