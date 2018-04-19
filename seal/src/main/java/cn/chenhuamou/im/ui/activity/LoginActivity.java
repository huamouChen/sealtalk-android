package cn.chenhuamou.im.ui.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.network.GetPicThread;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetRongTokenResponse;
import cn.chenhuamou.im.server.response.GetTokenResponse;
import cn.chenhuamou.im.server.response.GetUserInfoResponse;
import cn.chenhuamou.im.server.response.LoginResponse;
import cn.chenhuamou.im.server.utils.CommonUtils;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.ClearWriteEditText;
import cn.chenhuamou.im.server.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/1/15.
 * Company RongCloud
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private final static String TAG = "LoginActivity";
    private static final int LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int GET_RONG_TOKEN = 600;
    private static final int GET_RONG_GROUPS = 700;
    private static final int SYNC_USER_INFO = 9;

    private ImageView mImg_Background;
    private ClearWriteEditText mPhoneEdit, mPasswordEdit;
    private String phoneString;
    private String passwordString;
    private String connectResultId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String loginToken;
    private String rong_token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setHeadVisibility(View.GONE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        initView();
    }

    private void initView() {
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.de_login_phone);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.de_login_password);

        Button mConfirm = (Button) findViewById(R.id.de_login_sign);
        TextView mRegister = (TextView) findViewById(R.id.de_login_register);
        TextView forgetPassword = (TextView) findViewById(R.id.de_login_forgot);
        forgetPassword.setOnClickListener(this);
        mConfirm.setOnClickListener(this);
        mRegister.setOnClickListener(this);
        mImg_Background = (ImageView) findViewById(R.id.de_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(LoginActivity.this, R.anim.translate_anim);
                mImg_Background.startAnimation(animation);
            }
        }, 200);

        String oldPhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        String oldPassword = sp.getString(SealConst.SEALTALK_LOGING_PASSWORD, "");

        if (!TextUtils.isEmpty(oldPhone) && !TextUtils.isEmpty(oldPassword)) {
            mPhoneEdit.setText(oldPhone);
            mPasswordEdit.setText(oldPassword);
        }

        // 其他设备登录，被踢下线
        if (getIntent().getBooleanExtra("kickedByOtherClient", false)) {
            final AlertDialog dlg = new AlertDialog.Builder(LoginActivity.this).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setContentView(R.layout.other_devices);
            TextView text = (TextView) window.findViewById(R.id.ok);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.cancel();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.de_login_sign:
                phoneString = mPhoneEdit.getText().toString().trim();
                passwordString = mPasswordEdit.getText().toString().trim();

                if (TextUtils.isEmpty(phoneString)) {
                    NToast.shortToast(mContext, R.string.phone_number_is_null);
                    mPhoneEdit.setShakeAnimation();
                    return;
                }

                if (TextUtils.isEmpty(passwordString)) {
                    NToast.shortToast(mContext, R.string.password_is_null);
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (passwordString.contains(" ")) {
                    NToast.shortToast(mContext, R.string.password_cannot_contain_spaces);
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                LoadDialog.show(mContext);
                editor.putBoolean("exit", false);
                editor.commit();
                String oldPhone = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
                request(LOGIN, true);
                break;
            case R.id.de_login_register:
                startActivityForResult(new Intent(this, RegisterActivity.class), 1);
                break;
            case R.id.de_login_forgot:
                startActivityForResult(new Intent(this, ForgetPasswordActivity.class), 2);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 2 && data != null) {
            String phone = data.getStringExtra("phone");
            String password = data.getStringExtra("password");
            mPhoneEdit.setText(phone);
            mPasswordEdit.setText(password);
        } else if (data != null && requestCode == 1) {
            String phone = data.getStringExtra("phone");
            String password = data.getStringExtra("password");
            String id = data.getStringExtra("id");
            String nickname = data.getStringExtra("nickname");
            if (!TextUtils.isEmpty(phone) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(id) && !TextUtils.isEmpty(nickname)) {
                mPhoneEdit.setText(phone);
                mPasswordEdit.setText(password);
                editor.putString(SealConst.SEALTALK_LOGING_PHONE, phone);
                editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, password);
                editor.putString(SealConst.SEALTALK_LOGIN_ID, id);
                editor.putString(SealConst.SEALTALK_LOGIN_NAME, nickname);
                editor.commit();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case LOGIN:
                return action.login(phoneString, passwordString, getCurrentTime(), false, "");
            case GET_TOKEN:
                return action.getToken();
            case SYNC_USER_INFO:
                return action.getUserInfo();
            case GET_RONG_TOKEN:
                return action.getRongToken();
            case GET_RONG_GROUPS:
                return action.getRongGroups(phoneString);

        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {

                case LOGIN:
                    final LoginResponse loginResponse = (LoginResponse) result;
                    if (loginResponse.getResult() == 0) {
                        loginToken = loginResponse.getToken();
                        editor.putString(SealConst.SEALTALK_LOGIN_ID, phoneString);
                        editor.putString(SealConst.TOKEN, loginToken);
                        editor.commit();
                        // 打开数据库
                        SealUserInfoManager.getInstance().openDB();
                        // 登录成功，获取融云 token
                        request(GET_RONG_TOKEN);
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, loginResponse.getError());
                    }
                    break;

                case SYNC_USER_INFO:
                    GetUserInfoResponse userInfoByIdResponse = (GetUserInfoResponse) result;
                    if (userInfoByIdResponse.getUserName() != null) {
                        LoadDialog.dismiss(mContext);
                        String nickName = userInfoByIdResponse.getNickName();
                        String phone = userInfoByIdResponse.getPhoneNum();
                        editor.putString(SealConst.Nick_Name, nickName.isEmpty() ? phoneString : nickName);
                        editor.putString(SealConst.Bind_Phone, phone);
                        editor.commit();
                        goToMain();
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(phone, nickName, Uri.parse("")));
                    } else {
                        LoadDialog.dismiss(mContext);
                        NLog.e("GetUserInfoResponse", "获取用户信息失败，无法登录");
                    }

                    break;

                case GET_RONG_TOKEN:
                    final GetRongTokenResponse rongTokenResponse = (GetRongTokenResponse) result;
                    if (rongTokenResponse.getValue() == null) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.get_token_api_fail);
                    }
                    rong_token = rongTokenResponse.getValue().getRongToken();
                    editor.putString(SealConst.Rong_Token, rong_token);
                    editor.commit();
                    // 连接 融云 服务器
                    if (!TextUtils.isEmpty(rong_token)) {
                        RongIM.connect(rong_token, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                NLog.e("connect", "onTokenIncorrect");
                                request(GET_RONG_TOKEN);
                            }

                            @Override
                            public void onSuccess(String s) {
                                connectResultId = s;
                                editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                editor.putString(SealConst.Rong_Token, rong_token);
                                editor.commit();
                                // 获取用户信息
                                request(SYNC_USER_INFO, true);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                            }
                        });
                    }
                    break;
            }
        }
    }

    private void reGetToken() {
        request(GET_TOKEN);
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        if (!CommonUtils.isNetworkConnected(mContext)) {
            LoadDialog.dismiss(mContext);
            NToast.shortToast(mContext, getString(R.string.network_not_available));
            return;
        }
        switch (requestCode) {
            case LOGIN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.login_api_fail);
                break;
            case SYNC_USER_INFO:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.sync_userinfo_api_fail);
                break;
            case GET_TOKEN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.get_token_api_fail);
                break;
            case GET_RONG_TOKEN:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, R.string.get_token_api_fail);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void goToMain() {
        // 要保存的是融云的 token
        editor.putString(SealConst.Rong_Token, rong_token);
        editor.putString(SealConst.SEALTALK_LOGING_PHONE, phoneString);
        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, passwordString);
        editor.commit();
        LoadDialog.dismiss(mContext);
        NToast.shortToast(mContext, R.string.login_success);
        startActivity(new Intent(LoginActivity.this, MainActivity.class));
        finish();
    }

    // 获取系统的当前时间
    private String getCurrentTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }
}
