package cn.chenhuamou.im.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.CheckPhoneResponse;
import cn.chenhuamou.im.server.response.RegisterResponse;
import cn.chenhuamou.im.server.response.SendCodeResponse;
import cn.chenhuamou.im.server.response.VerifyCodeResponse;
import cn.chenhuamou.im.server.utils.AMUtils;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.downtime.DownTimer;
import cn.chenhuamou.im.server.utils.downtime.DownTimerListener;
import cn.chenhuamou.im.server.widget.ClearWriteEditText;
import cn.chenhuamou.im.server.widget.LoadDialog;

/**
 * Created by AMing on 16/1/14.
 * Company RongCloud
 */
@SuppressWarnings("deprecation")
public class RegisterActivity extends BaseActivity implements View.OnClickListener, DownTimerListener {

    private static final int CHECK_PHONE = 1;
    private static final int SEND_CODE = 2;
    private static final int VERIFY_CODE = 3;
    private static final int REGISTER = 4;
    private static final int REGISTER_BACK = 1001;
    private ImageView mImgBackground;
    private ClearWriteEditText mPhoneEdit, mCodeEdit, mNickEdit, mPasswordEdit, mComfirmPasswordEdit;
    private Button mGetCode, mConfirm;
    private String mPhone, mCode, mNickName, mPassword, mComfirmPassword, mCodeToken;
    private boolean isRequestCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setHeadVisibility(View.GONE);
        initView();
    }

    private void initView() {
        mPhoneEdit = (ClearWriteEditText) findViewById(R.id.reg_phone);
        mCodeEdit = (ClearWriteEditText) findViewById(R.id.reg_code);
        mNickEdit = (ClearWriteEditText) findViewById(R.id.reg_username);
        mPasswordEdit = (ClearWriteEditText) findViewById(R.id.reg_password);
        mComfirmPasswordEdit = (ClearWriteEditText) findViewById(R.id.comfirm_password);
        mGetCode = (Button) findViewById(R.id.reg_getcode);
        mConfirm = (Button) findViewById(R.id.reg_button);

        mGetCode.setOnClickListener(this);
        mGetCode.setClickable(false);
        mConfirm.setOnClickListener(this);

        TextView goLogin = (TextView) findViewById(R.id.reg_login);
        TextView goForget = (TextView) findViewById(R.id.reg_forget);
        goLogin.setOnClickListener(this);
        goForget.setOnClickListener(this);

        mImgBackground = (ImageView) findViewById(R.id.rg_img_backgroud);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Animation animation = AnimationUtils.loadAnimation(RegisterActivity.this, R.anim.translate_anim);
                mImgBackground.startAnimation(animation);
            }
        }, 200);

        addEditTextListener();

    }

    private void addEditTextListener() {
        mComfirmPasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 5 && mPasswordEdit.getText().length() > 5 && mPhoneEdit.getText().length() > 0) {
                    mConfirm.setClickable(true);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                } else {
                    mConfirm.setClickable(false);
                    mConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CHECK_PHONE:
                return action.checkPhoneAvailable("86", mPhone);
            case SEND_CODE:
                return action.sendCode("86", mPhone);
            case VERIFY_CODE:
                return action.verifyCode("86", mPhone, mCode);
            case REGISTER:
                return action.register(mPhone, mPassword);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case CHECK_PHONE:
                    CheckPhoneResponse cprres = (CheckPhoneResponse) result;
                    if (cprres.getCode() == 200) {
                        if (cprres.isResult()) {
                            mGetCode.setClickable(true);
                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
                            Toast.makeText(mContext, R.string.phone_number_available, Toast.LENGTH_SHORT).show();
                        } else {
                            mGetCode.setClickable(false);
                            mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
                            Toast.makeText(mContext, R.string.phone_number_has_been_registered, Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                case SEND_CODE:
                    SendCodeResponse scrres = (SendCodeResponse) result;
                    if (scrres.getCode() == 200) {
                        NToast.shortToast(mContext, R.string.messge_send);
                    } else if (scrres.getCode() == 5000) {
                        NToast.shortToast(mContext, R.string.message_frequency);
                    }
                    break;

                case VERIFY_CODE:
                    VerifyCodeResponse vcres = (VerifyCodeResponse) result;
                    switch (vcres.getCode()) {
                        case 200:
                            mCodeToken = vcres.getResult().getVerification_token();
                            if (!TextUtils.isEmpty(mCodeToken)) {
                                request(REGISTER);
                            } else {
                                NToast.shortToast(mContext, "code token is null");
                                LoadDialog.dismiss(mContext);
                            }
                            break;
                        case 1000:
                            //验证码错误
                            NToast.shortToast(mContext, R.string.verification_code_error);
                            LoadDialog.dismiss(mContext);
                            break;
                        case 2000:
                            //验证码过期
                            NToast.shortToast(mContext, R.string.captcha_overdue);
                            LoadDialog.dismiss(mContext);
                            break;
                    }
                    break;

                case REGISTER:
                    RegisterResponse registerResponse = (RegisterResponse) result;
                    if (registerResponse.getCode().getCodeId().equals("100")) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, R.string.register_success);
                        Intent data = new Intent();
                        data.putExtra("phone", mPhone);
                        data.putExtra("password", mPassword);
                        setResult(REGISTER_BACK, data);
                        this.finish();
                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, registerResponse.getParameter());
                    }

                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CHECK_PHONE:
                Toast.makeText(mContext, "手机号可用请求失败", Toast.LENGTH_SHORT).show();
                break;
            case SEND_CODE:
                NToast.shortToast(mContext, "获取验证码请求失败");
                break;
            case VERIFY_CODE:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "验证码是否可用请求失败");
                break;
            case REGISTER:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "注册请求失败");
                break;
        }
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentManager() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reg_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.reg_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.reg_getcode:
                if (TextUtils.isEmpty(mPhoneEdit.getText().toString().trim())) {
                    NToast.longToast(mContext, R.string.phone_number_is_null);
                } else {
                    isRequestCode = true;
                    DownTimer downTimer = new DownTimer();
                    downTimer.setListener(this);
                    downTimer.startDown(60 * 1000);
                    request(SEND_CODE);
                }
                break;
            case R.id.reg_button:
                mPhone = mPhoneEdit.getText().toString().trim();
                mPassword = mPasswordEdit.getText().toString().trim();
                mComfirmPassword = mComfirmPasswordEdit.getText().toString().trim();


                if (TextUtils.isEmpty(mPhone)) {
                    NToast.shortToast(mContext, getString(R.string.phone_number_is_null));
                    mPhoneEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mPassword)) {
                    NToast.shortToast(mContext, getString(R.string.password_is_null));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (mPassword.contains(" ")) {
                    NToast.shortToast(mContext, getString(R.string.password_cannot_contain_spaces));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }
                if (TextUtils.isEmpty(mComfirmPassword)) {
                    NToast.shortToast(mContext, getString(R.string.comfirm_password_is_null));
                    mPasswordEdit.setShakeAnimation();
                    return;
                }


                if (!mPassword.equals(mComfirmPassword)) {
                    NToast.shortToast(mContext, "两次密码不一致");
                    mPasswordEdit.setShakeAnimation();
                    mComfirmPasswordEdit.setShakeAnimation();
                    return;
                }

                LoadDialog.show(mContext);
                request(REGISTER);
                break;
        }
    }

    boolean isBright = true;

    @Override
    public void onTick(long millisUntilFinished) {
        mGetCode.setText(String.valueOf(millisUntilFinished / 1000) + "s");
        mGetCode.setClickable(false);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_gray));
        isBright = false;
    }

    @Override
    public void onFinish() {
        mGetCode.setText(R.string.get_code);
        mGetCode.setClickable(true);
        mGetCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.rs_select_btn_blue));
        isBright = true;
    }

}
