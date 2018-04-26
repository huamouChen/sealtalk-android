package cn.chenhuamou.im.ui.activity.wallet;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jrmf360.rylib.wallet.widget.ClearEditText;
import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.PublicResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.activity.BaseActivity;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {


    private static final int AliPay_Check = 1000;

    private TitleBar titleBar;
    private TextView tv_account, tv_cardnum_copy, tv_cardname_copy, tv_cardNum, tv_cardName;
    private ClearEditText cet_amount, cet_time, cet_bank_code;
    private Button btn_next;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {

        mHeadLayout.setVisibility(View.GONE);

        titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("充值");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 当前账号
        tv_account = findViewById(R.id.tv_account);
        tv_account.setText(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""));
        // 复制
        tv_cardnum_copy = findViewById(R.id.tv_cardnum_copy);
        tv_cardname_copy = findViewById(R.id.tv_cardname_copy);
        // 要复制的两个控件
        tv_cardNum = findViewById(R.id.tv_cardNum);
        tv_cardName = findViewById(R.id.tv_cardName);

        // 转账金额
        cet_amount = findViewById(R.id.cet_amount);

        // 银行编码
        cet_bank_code = findViewById(R.id.cet_bank_code);

        // 转账时间
        cet_time = findViewById(R.id.cet_time);

        // 已转账，提交审核
        btn_next = findViewById(R.id.btn_charge);

        addListener();
    }

    private void addListener() {
        tv_cardnum_copy.setOnClickListener(this);
        tv_cardname_copy.setOnClickListener(this);

        // 点击提交审核按钮
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:  // 提交审核
                if (cet_amount.getText().toString().isEmpty()) {
                    NToast.shortToast(mContext, "转账金额不能为空");
                    cet_amount.setShakeAnimation();
                    return;
                }
                if (cet_bank_code.getText().toString().isEmpty()) {
                    NToast.shortToast(mContext, "银行代码不能为空");
                    cet_bank_code.setShakeAnimation();
                    return;
                }
                if (cet_time.getText().toString().isEmpty()) {
                    NToast.shortToast(mContext, "转账时间不能为空");
                    cet_time.setShakeAnimation();
                    return;
                }
                LoadDialog.show(mContext);
                request(AliPay_Check);
                break;
            case R.id.tv_cardnum_copy:  // 复制卡号
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(tv_cardNum.getText().toString());
                NToast.shortToast(mContext, "已复制：" + tv_cardNum.getText().toString());
                break;
            case R.id.tv_cardname_copy:   // 复制卡主的名字
                ClipboardManager cm2 = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm2.setText(tv_cardName.getText().toString());
                NToast.shortToast(mContext, "已复制：" + tv_cardName.getText().toString());
                break;
        }

    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return action.getRechargeAlipay(cet_amount.getText().toString(), cet_time.getText().toString(), cet_bank_code.getText().toString());
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        PublicResponse publicResponse = (PublicResponse) result;
        if (publicResponse.isResult()) {
            NToast.shortToast(mContext, "审核成功，请耐心等候");
        } else {
            NToast.shortToast(mContext, publicResponse.getError());
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "审核失败，请重试");
    }
}
