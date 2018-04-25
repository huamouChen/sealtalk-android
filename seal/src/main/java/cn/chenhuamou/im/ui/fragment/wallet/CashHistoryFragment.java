package cn.chenhuamou.im.ui.fragment.wallet;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.chenhuamou.im.R;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 * 提现记录fragment
 */
public class CashHistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_cash_history, container, false);
        return contentView;
    }
}
