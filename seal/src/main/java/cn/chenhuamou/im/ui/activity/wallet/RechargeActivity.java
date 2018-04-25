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

import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.PublicResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.ClearWriteEditText;
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
    private ClearWriteEditText cet_amount, cet_time;
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

        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("充值");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // 当前账号
        tv_account = (TextView) findViewById(R.id.tv_account);
        tv_account.setText(getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""));
        // 复制
        tv_cardnum_copy = (TextView) findViewById(R.id.tv_cardnum_copy);
        tv_cardname_copy = (TextView) findViewById(R.id.tv_cardname_copy);
        // 要复制的两个控件
        tv_cardNum = (TextView) findViewById(R.id.tv_cardNum);
        tv_cardName = (TextView) findViewById(R.id.tv_cardName);

        // 转账金额
        cet_amount = (ClearWriteEditText) findViewById(R.id.cet_amount);

        // 转账时间
        cet_time = (ClearWriteEditText) findViewById(R.id.cet_time);

        // 已转账，提交审核
        btn_next = (Button) findViewById(R.id.btn_charge);

        addListener();
    }

    private void addListener() {
        cet_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2 && cet_time.getText().length() > 2) {
                    btn_next.setClickable(true);
                } else {
                    btn_next.setClickable(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        cet_time.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2 && cet_amount.getText().length() > 2) {
                    btn_next.setClickable(true);
                    btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    btn_next.setClickable(false);
                    btn_next.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_cardnum_copy.setOnClickListener(this);
        tv_cardname_copy.setOnClickListener(this);

        // 点击提交审核按钮
        btn_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_charge:  // 提交审核
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
        return action.getRechargeAlipay(cet_amount.getText().toString(), cet_time.getText().toString(), "104AAAABBBBC");
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
