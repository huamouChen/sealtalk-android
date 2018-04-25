package cn.chenhuamou.im.ui.activity.wallet;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import cn.chenhuamou.im.R;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class AccountInfoActivity extends Activity {


    private TitleBar titleBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("账户信息");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
