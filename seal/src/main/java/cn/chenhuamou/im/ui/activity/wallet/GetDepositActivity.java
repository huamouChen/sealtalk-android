package cn.chenhuamou.im.ui.activity.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.jrmf360.rylib.wallet.widget.ClearEditText;
import com.jrmf360.rylib.wallet.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.BankListResponse;
import cn.chenhuamou.im.server.response.PublicResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.activity.BaseActivity;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class GetDepositActivity extends BaseActivity implements View.OnClickListener {


    private static final int GET_DRAW_MONEY = 1000;   // 提现
    private static final int GET_USER_DETAIL_BALANCE = 2000;   // 获取用户余额详情
    private static final int GET_USER_Banks = 3000;   // 获取用户绑定的银行卡

    private List<BankListResponse> bankList = new ArrayList<>();
    private List<String> userBankNamesList = new ArrayList<>();


    private TitleBar titleBar;
    private ClearEditText cwe_draw, cwe_moneypwd, cwe_banknum;
    private Button btn_comfirm;
    private Spinner spinner;
    private String bankId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdeposit);
        initView();

        LoadDialog.show(mContext);
        // 获取用户银行卡
        request(GET_USER_Banks);
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);

        titleBar = findViewById(R.id.titlebar);
        titleBar.setTitle("提现");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        cwe_draw = findViewById(R.id.cwe_draw);
        cwe_moneypwd = findViewById(R.id.cwe_moneypwd);
        cwe_banknum = findViewById(R.id.cwe_banknum);
        btn_comfirm = findViewById(R.id.btn_comfirm);

        // 银行ID
        spinner = findViewById(R.id.spinner_bank);


        addListener();
    }

    /*
     * 设置点击监听
     * */
    private void addListener() {
        btn_comfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (TextUtils.isEmpty(cwe_draw.getText().toString())) {
            NToast.shortToast(mContext, "取现金额不能为空");
            cwe_draw.setShakeAnimation();
            return;
        }

        if (TextUtils.isEmpty(cwe_moneypwd.getText().toString())) {
            NToast.shortToast(mContext, "资金密码不能为空");
            cwe_moneypwd.setShakeAnimation();
            return;
        }

        if (TextUtils.isEmpty(cwe_banknum.getText().toString())) {
            NToast.shortToast(mContext, "银行ID不能为空");
            cwe_banknum.setShakeAnimation();
            return;
        }

        LoadDialog.show(mContext);
        request(GET_DRAW_MONEY);
    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {


        switch (requestCode) {
            case GET_USER_Banks:
                return action.getUserBanks(1, 100);
            case GET_DRAW_MONEY:
                return action.getWithDrawMoney(
                        cwe_draw.getText().toString(),
                        cwe_moneypwd.getText().toString(),
                        bankId
                );

        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        LoadDialog.dismiss(mContext);

        switch (requestCode) {
            case GET_USER_Banks:
                List<BankListResponse> getUserDetailBalanceResponse = (List<BankListResponse>) result;
                // 接口返回不规范，根本没法判断，草
                if (getUserDetailBalanceResponse.size() <= 0) {
                    NToast.shortToast(mContext, "没有获取到绑定的银行卡，请先绑定银行卡");
                    return;
                }
                setSpinnerAdaper(getUserDetailBalanceResponse);
                break;
            case GET_DRAW_MONEY:
                PublicResponse publicResponse = (PublicResponse) result;
                if (publicResponse.isResult()) {
                    NToast.shortToast(mContext, "提现已提交，请耐心等候");
                    // 发送通知，刷新我的钱包界面
                    BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.RefreshMyWallet);

                } else {
                    NToast.shortToast(mContext, publicResponse.getError());
                }
                break;
        }
    }

    // 设置下拉框数据
    private void setSpinnerAdaper(List<BankListResponse> listResponses) {
        for (BankListResponse item : listResponses) {
            userBankNamesList.add(item.getBankCodeName() != null ? item.getBankCodeName() : "未知银行");
        }
        bankList.addAll(listResponses);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, userBankNamesList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cwe_banknum.setText(bankList.get(position).getID() + "");
                bankId = bankList.get(position).getID() + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case GET_USER_Banks:
                NToast.shortToast(mContext, "获取绑定的银行卡失败");
                break;
            case GET_DRAW_MONEY:
                NToast.shortToast(mContext, "提现申请失败");
                break;
        }

    }
}
