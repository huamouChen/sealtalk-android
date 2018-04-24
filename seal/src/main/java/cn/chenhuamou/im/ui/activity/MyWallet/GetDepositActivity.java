package cn.chenhuamou.im.ui.activity.MyWallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;
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
public class GetDepositActivity extends BaseActivity implements View.OnClickListener {


    private static final int GET_DRAW_MONEY = 1000;   // 提现

    private TitleBar titleBar;
    private ClearWriteEditText cwe_draw, cwe_moneypwd, cwe_banknum;
    private Button btn_comfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdeposit);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);

        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("提现");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cwe_draw = (ClearWriteEditText) findViewById(R.id.cwe_draw);
        cwe_moneypwd = (ClearWriteEditText) findViewById(R.id.cwe_moneypwd);
        cwe_banknum = (ClearWriteEditText) findViewById(R.id.cwe_banknum);
        btn_comfirm = (Button) findViewById(R.id.btn_comfirm);

        addListener();
    }

    /*
    * 设置点击监听
    * */
    private void addListener() {
        cwe_draw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2 && cwe_banknum.getText().length() > 2 && cwe_moneypwd.getText().length() > 2) {
                    btn_comfirm.setClickable(true);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    btn_comfirm.setClickable(false);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cwe_moneypwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2 && cwe_banknum.getText().length() > 2 && cwe_draw.getText().length() > 2) {
                    btn_comfirm.setClickable(true);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    btn_comfirm.setClickable(false);
                    btn_comfirm.setClickable(false);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        cwe_banknum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 2 && cwe_draw.getText().length() > 2 && cwe_moneypwd.getText().length() > 2) {
                    btn_comfirm.setClickable(true);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    btn_comfirm.setClickable(false);
                    btn_comfirm.setClickable(false);
                    btn_comfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_comfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LoadDialog.show(mContext);
        request(GET_DRAW_MONEY);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        return action.getWithDrawMoney(cwe_draw.getText().toString(),
                cwe_moneypwd.getText().toString(),
                cwe_banknum.getText().toString());
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        PublicResponse publicResponse = (PublicResponse) result;
        if (publicResponse.isResult()) {
            NToast.shortToast(mContext, "提现已提交，请耐心等候");
        } else {
            NToast.shortToast(mContext, publicResponse.getError());
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "提现申请失败");
    }
}
