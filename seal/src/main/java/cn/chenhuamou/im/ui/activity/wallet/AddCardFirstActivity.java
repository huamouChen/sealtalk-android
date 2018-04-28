package cn.chenhuamou.im.ui.activity.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.jrmf360.rylib.wallet.widget.ClearEditText;
import com.jrmf360.rylib.wallet.widget.TitleBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetBankstResponse;
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
    private static final int GET_CHARGE_WAY = 2000;


    private TitleBar titleBar;

    private ClearEditText cet_bankCardNum, cet_bankCardNum_com, cet_card_username, cet_pwd, cet_province;
    private Button btn_next;
    private TextView tv_bank_code;

    // 银行编码数组
    private String[] bankCodeList;
    private String[] bankNameList;
    private String bankCodeString = "";

    private Spinner spinner;


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {

        switch (requestCode) {
            case Bind_BankCard:
                String userId = getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
                return action.addUserBank(userId,
                        bankCodeString,
                        cet_bankCardNum.getText().toString(),
                        cet_card_username.getText().toString(),
                        cet_pwd.getText().toString(),
                        cet_bankCardNum_com.getText().toString(),
                        cet_province.getText().toString());

            case GET_CHARGE_WAY:
                return action.getChargeWay();
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);

        switch (requestCode) {
            case Bind_BankCard:
                PublicResponse response = (PublicResponse) result;
                if (response.isResult()) {
                    NToast.shortToast(mContext, "绑定银行卡成功");
                    finish();
                } else {
                    NToast.shortToast(mContext, response.getError());
                }
                break;
            case GET_CHARGE_WAY:
                List<GetBankstResponse> chargeWayLists = (List<GetBankstResponse>) result;
                break;
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case Bind_BankCard:
                NToast.shortToast(mContext, "绑定银行卡失败");
                break;
            case GET_CHARGE_WAY:
                NToast.shortToast(mContext, "获取充值方式失败");
                break;
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card_first);
        initView();

//        request(GET_CHARGE_WAY);
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);
        titleBar = (TitleBar) findViewById(R.id.titleBar);
        titleBar.setTitle("添加银行卡");

        bankCodeList = getResources().getStringArray(R.array.bank_code);
        bankNameList = getResources().getStringArray(R.array.bank_name);

        // 银行编码
        spinner = findViewById(R.id.spinner_bank);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bankNameList);
        //这里设置的是Spinner的样式 ， 输入 simple_之后会提示有4人，如果专属spinner的话应该是俩种，在特殊情况可自己定义样式
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_bank_code.setText(bankNameList[position]);
                bankCodeString = bankCodeList[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        tv_bank_code = findViewById(R.id.tv_bank_code);
        cet_bankCardNum = findViewById(R.id.cet_bankCardNum);
        cet_bankCardNum_com = findViewById(R.id.cet_bankCardNum_com);
        cet_card_username = findViewById(R.id.cet_card_username);
        cet_pwd = findViewById(R.id.cet_pwd);
        cet_province = findViewById(R.id.cet_province);

        btn_next = findViewById(R.id.btn_next);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadDialog.show(mContext);
                request(Bind_BankCard);
            }
        });
    }
}
