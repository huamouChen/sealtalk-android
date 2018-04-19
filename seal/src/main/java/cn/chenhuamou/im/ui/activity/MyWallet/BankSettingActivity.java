package cn.chenhuamou.im.ui.activity.MyWallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.BankListResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.ui.activity.BaseActivity;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class BankSettingActivity extends BaseActivity {


    private static final int Get_Exist_BankCard = 2000;  // 是否绑定银行卡
    private static final int Get_BankCard_List = 1000;  // 获取银行卡列表

    private TitleBar titleBar;
    private ListView mListView;
    private BankCardAdapter adapterBankCard;
    private List<BankListResponse> datas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_setting);
        initView();

        initData();

        LoadDialog.show(mContext);
        request(Get_Exist_BankCard);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case Get_Exist_BankCard:
                return action.existUserBank();
            case Get_BankCard_List:
                return action.getUserBanks(1, 100);

        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {


        switch (requestCode) {
            case Get_Exist_BankCard:
                String isHave = (String) result;
                if (isHave.equals("true")) {
                    request(Get_BankCard_List);
                } else {
                    LoadDialog.dismiss(mContext);
                    NToast.shortToast(mContext, "暂未绑定银行卡");
                }
                break;
            case Get_BankCard_List:
                LoadDialog.dismiss(mContext);
                List<BankListResponse> list = (List<BankListResponse>) result;
                if (list != null && list.size() > 0) {
                    datas.addAll(list);
                    adapterBankCard.notifyDataSetChanged();
                }
                break;
        }


    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, "获取用户银行卡接口请求失败");
    }

    /*
     * 绑定控件
     * */
    private void initView() {

        mHeadLayout.setVisibility(View.GONE);

        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("银行卡设置");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mListView = (ListView) findViewById(R.id.lv_bank_card);
    }


    private void initData() {
        datas = new ArrayList<>();
        datas.add(new BankListResponse()); // 是为了显示添加银行卡item
        adapterBankCard = new BankCardAdapter();
        mListView.setAdapter(adapterBankCard);
    }


    class BankCardAdapter extends BaseAdapter {

        public BankCardAdapter() {
        }

        @Override
        public int getCount() {
            return datas == null ? 0 : datas.size();
        }

        @Override
        public Object getItem(int i) {
            return datas == null ? null : datas.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public int getItemViewType(int position) {
            return position == datas.size() - 1 ? 1 : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            int type = getItemViewType(i);
            BankCarViewHolder viewHolder = null;

            if (type == 0) {
                BankListResponse bankListResponse = datas.get(i);
                if (view == null) {
                    viewHolder = new BankCarViewHolder();
                    view = LayoutInflater.from(BankSettingActivity.this).inflate(R.layout.item_setting_bank_cark_list, null);
                    viewHolder.tv_tv_bankCardName = (TextView) view.findViewById(R.id.tv_bankCardName);
                    viewHolder.tv_bankCardNum = (TextView) view.findViewById(R.id.tv_bankCardNum);
                    view.setTag(viewHolder);
                } else {
                    viewHolder = (BankCarViewHolder) view.getTag();
                }

                viewHolder.tv_tv_bankCardName.setText(bankListResponse.getBankName());
                viewHolder.tv_bankCardNum.setText(bankListResponse.getBankNum());

                return view;
            } else { // 添加银行卡
                View cell = LayoutInflater.from(BankSettingActivity.this).inflate(R.layout.item_add_bank_card, null);
                LinearLayout linearLayout = (LinearLayout) cell.findViewById(R.id.ll_add_card);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(BankSettingActivity.this, AddCardFirstActivity.class));
                    }
                });
                return cell;
            }
        }
    }


    class BankCarViewHolder {
        TextView tv_tv_bankCardName, tv_bankCardNum;
    }


}
