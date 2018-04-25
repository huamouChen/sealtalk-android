package cn.chenhuamou.im.ui.activity.wallet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.jrmf360.rylib.wallet.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.ui.activity.BaseActivity;
import cn.chenhuamou.im.ui.fragment.wallet.CashHistoryFragment;
import cn.chenhuamou.im.ui.fragment.wallet.RechargeHistoryFragment;

/**
 * Created by Rex on 2018/4/19.
 * Email chenhm4444@gmail.com
 */
public class TradeHistoryActivity extends BaseActivity {

    private TitleBar titleBar;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private MyTradeFragmentPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_history);
        initView();
    }

    /*
     * 绑定控件
     * */
    private void initView() {
        mHeadLayout.setVisibility(View.GONE);

        titleBar = (TitleBar) findViewById(R.id.titlebar);
        titleBar.setTitle("交易记录");

        // 返回按钮
        titleBar.getIvBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTabLayout = findViewById(R.id.tab_main);
        mViewPager = findViewById(R.id.vp_main);

        // 设置 adapter
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new RechargeHistoryFragment());
        fragmentList.add(new CashHistoryFragment());
        List<String> titles = new ArrayList<>();
        titles.add("充值");
        titles.add("提现");
        mViewPagerAdapter = new MyTradeFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setAdapter(mViewPagerAdapter);

        // 将 tabLayout 和 ViewPager 绑定在一起
        mTabLayout.setupWithViewPager(mViewPager);
    }



    /*
    * viewPagerAdapter
    * */
    public class MyTradeFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;    // fragment数组
        private List<String> titles;            // 标题数组

        public MyTradeFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> titles) {
            super(fm);
            this.fragmentList = fragmentList;
            this.titles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }
}

