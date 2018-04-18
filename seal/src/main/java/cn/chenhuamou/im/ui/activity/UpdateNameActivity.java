package cn.chenhuamou.im.ui.activity;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.SetNameResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.ClearWriteEditText;
import cn.chenhuamou.im.server.widget.LoadDialog;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/6/23.
 * Company RongCloud
 */
public class UpdateNameActivity extends BaseActivity implements View.OnClickListener {

    private static final int UPDATE_NAME = 7;
    private static final int Bind_Phone = 8;
    private ClearWriteEditText mNameEditText;
    private TextView tv_remind;
    private String newName;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private boolean isUpdateName = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        // 是否是绑定手机
        isUpdateName = getIntent().getBooleanExtra("isUpdateName", false);

        tv_remind = (TextView) findViewById(R.id.tv_remind);

        if (isUpdateName) {
            setTitle(getString(R.string.update_name));
            tv_remind.setText("好的名字可以让你的朋友更容易记住");
        } else {
            setTitle(getString(R.string.bind_phone));
            tv_remind.setText("绑定手机可以方便找回密码和账号");
        }

        Button rightButton = getHeadRightButton();
        rightButton.setVisibility(View.GONE);
        mHeadRightText.setVisibility(View.VISIBLE);
        mHeadRightText.setText(getString(R.string.confirm));
        mHeadRightText.setOnClickListener(this);
        mNameEditText = (ClearWriteEditText) findViewById(R.id.update_name);



        // 设置默认的显示项
        sp = getSharedPreferences("config", MODE_PRIVATE);
        if (isUpdateName) {
            mNameEditText.setText(sp.getString(SealConst.Nick_Name, ""));
            mNameEditText.setSelection(sp.getString(SealConst.SEALTALK_LOGIN_NAME, "").length());
            editor = sp.edit();
        } else {
            mNameEditText.setText(sp.getString(SealConst.Bind_Phone, ""));
            mNameEditText.setSelection(sp.getString(SealConst.Bind_Phone, "").length());
            editor = sp.edit();
        }

    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        if (isUpdateName)
            return action.setName(newName);
        else
            return action.bindPhone(newName);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        SetNameResponse sRes = (SetNameResponse) result;
        switch (requestCode) {
            case UPDATE_NAME:
                if (sRes.isResult()) {
                    editor.putString(SealConst.Nick_Name, newName);
                    editor.commit();

                    BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.CHANGEINFO);

                    RongIM.getInstance().refreshUserInfoCache(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), newName, Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, ""))));
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), newName, Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, ""))));

                    LoadDialog.dismiss(mContext);
                    NToast.shortToast(mContext, "昵称更改成功");
                    finish();
                }
                break;
            case Bind_Phone:
                // 更新手机
                editor.putString(SealConst.Bind_Phone, newName);
                editor.apply();
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, "手机绑定成功");
                BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.Bind_Phone);
                finish();
                break;
        }



    }

    @Override
    public void onClick(View v) {
        newName = mNameEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(newName)) {
            LoadDialog.show(mContext);

            if (isUpdateName)
                request(UPDATE_NAME, true);
            else
                request(Bind_Phone, true);

        } else {
            NToast.shortToast(mContext, "输入不能为空");
            mNameEditText.setShakeAnimation();
        }
    }
}
