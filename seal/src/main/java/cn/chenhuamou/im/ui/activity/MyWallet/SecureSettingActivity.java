package cn.chenhuamou.im.ui.activity.MyWallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.RelativeLayout;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.ui.activity.BaseActivity;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class SecureSettingActivity extends BaseActivity implements View.OnClickListener {


    private TitleBar titleBar;
    private RelativeLayout mModifyPwdView, mCreatePwdView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_setting);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);

        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("安全设置");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mModifyPwdView = (RelativeLayout) findViewById(R.id.rl_modify_money_pwd);
        mCreatePwdView = (RelativeLayout) findViewById(R.id.rl_creat_money_pwd);

        addListener();
    }

    /*
    * 设置点击监听
    * */
    private void addListener() {
        mModifyPwdView.setOnClickListener(this);
        mCreatePwdView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_modify_money_pwd:
                Intent intent = new Intent(mContext, SetPayPwdActivity.class);
                intent.putExtra(SealConst.IsCreatwPayPwd, false);
                startActivity(intent);
                break;
            case R.id.rl_creat_money_pwd:
                Intent intent2 = new Intent(mContext, SetPayPwdActivity.class);
                intent2.putExtra(SealConst.IsCreatwPayPwd, true);
                startActivity(intent2);
                break;
        }
    }
}
