package cn.chenhuamou.im.ui.activity.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

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
public class AddCardFirstActivity extends BaseActivity {

    private static final int Bind_BankCard = 1000;

    private TitleBar titleBar;

    private ClearEditText cet_bank_code, cet_bankCardNum, cet_bankCardNum_com, cet_card_username, cet_pwd, cet_province;
    private Button btn_next;


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        String userId = getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
        return action.addUserBank(userId,
                cet_bank_code.getText().toString(),
                cet_bankCardNum.getText().toString(),
                cet_card_username.getText().toString(),
                cet_pwd.getText().toString(),
                cet_bankCardNum_com.getText().toString(),
                cet_province.getText().toString());
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);
        PublicResponse response = (PublicResponse) result;
        if (response.isResult()) {
            NToast.shortToast(mContext, "绑定银行卡成功");
            finish();
        } else {
            NToast.shortToast(mContext, response.getError());
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "绑定银行卡失败");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_first);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitle("添加银行卡");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cet_bank_code =  findViewById(R.id.cet_bank_code);
        cet_bankCardNum =  findViewById(R.id.cet_bankCardNum);
        cet_bankCardNum_com =  findViewById(R.id.cet_bankCardNum_com);
        cet_card_username =  findViewById(R.id.cet_card_username);
        cet_pwd =  findViewById(R.id.cet_pwd);
        cet_province = findViewById(R.id.cet_province);

        btn_next =  findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDialog.show(mContext);
                request(Bind_BankCard);
            }
        });
    }
}
