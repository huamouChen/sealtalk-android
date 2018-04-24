package cn.chenhuamou.im.ui.activity.MyWallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
 * 创建资金密码
 */
public class SetPayPwdActivity extends BaseActivity implements View.OnClickListener {


    private boolean isCreatwPayPwd = false;   // 是创建资金密码 还是修改资金密码
    private static final int CREATE_PAY_PASSWORD = 1000;
    private static final int MODIFY_PAY_PASSWORD = 2000;

    private String userId = "";

    private TitleBar titleBar;

    private LinearLayout mModifyPwdView, mCreatePayPwdView;

    private ClearEditText mCreatePwdView, mCreatePwdComView;   // 创建密码
    private ClearEditText mModifyOldPwdView, mModifyNewPwdView, mModifyNewComPwdView;  // 修改密码
    private Button btn_com;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_pay_pwd);
        initIntentData();
        initView();
    }

    /*
    * 获取intent传过来的数据
    * */
    private void initIntentData() {
        isCreatwPayPwd = getIntent().getBooleanExtra(SealConst.IsCreatwPayPwd, false);

        userId = getSharedPreferences(SealConst.SharedPreferencesName, MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
    }


    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);
        titleBar = (TitleBar) findViewById(R.id.pay_pwd_titlebar);
        if (isCreatwPayPwd) {
            titleBar.setTitle("创建资金密码");
        } else {
            titleBar.setTitle("修改资金密码");
        }

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mModifyPwdView = (LinearLayout) findViewById(R.id.ll_modify_pwd);
        mCreatePayPwdView = (LinearLayout) findViewById(R.id.ll_create_pwd);

        mCreatePwdView = (ClearEditText)findViewById(R.id.cet_pwd);
        mCreatePwdComView = (ClearEditText)findViewById(R.id.cet_confirm_pwd);

        mModifyOldPwdView = (ClearEditText)findViewById(R.id.cet_old_pwd);
        mModifyNewPwdView = (ClearEditText)findViewById(R.id.cet_new_pwd);
        mModifyNewComPwdView = (ClearEditText)findViewById(R.id.cet_new_pwd_com);

        // 是修改资金密码 还是 创建资金密码
        if (isCreatwPayPwd) {
            mCreatePayPwdView.setVisibility(View.VISIBLE);
            mModifyPwdView.setVisibility(View.GONE);
        } else {
            mCreatePayPwdView.setVisibility(View.GONE);
            mModifyPwdView.setVisibility(View.VISIBLE);
        }

        btn_com = (Button) findViewById(R.id.btn_pay_pwd_comfirm);

        addListener();
    }

    /*
    * 添加点击监听
    * */
    private void addListener() {
        btn_com.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        LoadDialog.show(mContext);
        if (isCreatwPayPwd)
            request(CREATE_PAY_PASSWORD);
        else
            request(MODIFY_PAY_PASSWORD);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CREATE_PAY_PASSWORD:
                return action.createBankPassword(userId, mCreatePwdView.getText().toString());
            case MODIFY_PAY_PASSWORD:
                return action.modifyBankPassword(userId, mModifyOldPwdView.getText().toString(), mModifyNewPwdView.getText().toString());
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case CREATE_PAY_PASSWORD:
                PublicResponse createResponse = (PublicResponse) result;
                if (createResponse.isResult()) {
                    NToast.shortToast(mContext, "资金密码创建成功");
                } else {
                    NToast.shortToast(mContext, createResponse.getError());
                }
                break;
            case MODIFY_PAY_PASSWORD:
                PublicResponse modifyResponse = (PublicResponse) result;
                if (modifyResponse.isResult()) {
                    NToast.shortToast(mContext, "修改资金密码成功");
                } else {
                    NToast.shortToast(mContext, modifyResponse.getError());
                }
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case MODIFY_PAY_PASSWORD:
                NToast.shortToast(mContext, "修改资金密码失败");
                break;
            case CREATE_PAY_PASSWORD:
                NToast.shortToast(mContext, "创建资金密码失败");
                break;
        }
    }
}
