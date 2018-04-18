package cn.chenhuamou.im.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetRongGroupResponse;
import io.rong.imkit.RongIM;

/**
 * Created by AMing on 16/8/5.
 * Company RongCloud
 */
public class SplashActivity extends Activity {


    private Context context;
    private android.os.Handler handler = new android.os.Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        context = this;
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cacheToken = sp.getString(SealConst.Rong_Token, "");
        if (!TextUtils.isEmpty(cacheToken)) {
            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMain();
                }
            }, 800);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin();
                }
            }, 800);
        }
    }


    private void goToMain() {
        startActivity(new Intent(context, MainActivity.class));
        finish();
    }

    private void goToLogin() {
        startActivity(new Intent(context, LoginActivity.class));
        finish();
    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }
}
