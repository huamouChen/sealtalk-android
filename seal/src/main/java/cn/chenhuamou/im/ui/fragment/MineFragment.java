package cn.chenhuamou.im.ui.fragment;

import android.app.AlertDialog;
import android.app.Notification;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import java.util.Hashtable;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.SealAction;
import cn.chenhuamou.im.server.UpdateService;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.async.OnDataListener;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.VersionResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.SelectableRoundedImageView;
import cn.chenhuamou.im.ui.activity.AccountSettingActivity;
import cn.chenhuamou.im.ui.activity.MyAccountActivity;
import cn.chenhuamou.im.ui.activity.wallet.MyWalletActivity;
import cn.chenhuamou.im.utils.QRCodeUtils;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.CSCustomServiceInfo;
import io.rong.imlib.model.UserInfo;

/**
 * Created by AMing on 16/6/21.
 * Company RongCloud
 */
public class MineFragment extends Fragment implements View.OnClickListener {
    private static final int COMPARE_VERSION = 54;
    public static final String SHOW_RED = "SHOW_RED";
    private SharedPreferences sp;
    private SelectableRoundedImageView imageView;
    private TextView mName, mCurrent_version, tv_account;
    private ImageView mNewVersionView, mQRCodeImg;
    private boolean isHasNewVersion;
    private String url;
    private boolean isDebug;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.seal_mine_fragment, container, false);
        isDebug = getContext().getSharedPreferences("config", getContext().MODE_PRIVATE).getBoolean("isDebug", false);
        initViews(mView);
        initData();
        BroadcastManager.getInstance(getActivity()).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUserInfo();
            }
        });

        // 比较版本
        compareVersion();
        return mView;
    }

    private void compareVersion() {
        AsyncTaskManager.getInstance(getActivity()).request(COMPARE_VERSION, new OnDataListener() {
            @Override
            public Object doInBackground(int requestCode, String parameter) throws HttpException {
                return new SealAction(getActivity()).getSealTalkVersion();
            }

            @Override
            public void onSuccess(int requestCode, Object result) {
                if (result != null) {

                    VersionResponse response = (VersionResponse) result;
                    if (response.getCode() != null && response.getCode().getCodeId().equals("100")) {
                        // 服务器版本
                        String serviceVersion = response.getValue().getVersionCode();
                        String[] s1 = serviceVersion.split("\\.");
                        StringBuilder sb1 = new StringBuilder();
                        for (int i = 0; i < s1.length; i++) {
                            sb1.append(s1[i]);
                        }
                        // 当前版本
                        String[] s2 = getVersionInfo()[1].split("\\.");
                        StringBuilder sb2 = new StringBuilder();
                        for (int i = 0; i < s2.length; i++) {
                            sb2.append(s2[i]);
                        }
                        if (Integer.parseInt(sb1.toString()) > Integer.parseInt(sb2.toString())) {
                            mNewVersionView.setVisibility(View.VISIBLE);
                            url = (response.getValue().getFileUrl() != null && !response.getValue().getFileUrl().isEmpty()) ? (BaseAction.DOMAIN + response.getValue().getFileUrl()) : "";
                            isHasNewVersion = true;
                            BroadcastManager.getInstance(getActivity()).sendBroadcast(SHOW_RED);
                        } else {
                            mNewVersionView.setVisibility(View.GONE);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int requestCode, int state, Object result) {

            }
        });
    }

    private void initData() {
        sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        updateUserInfo();
    }

    private void initViews(View mView) {
        mQRCodeImg = mView.findViewById(R.id.img_qr_code);
        mNewVersionView = mView.findViewById(R.id.new_version_icon);
        mCurrent_version = mView.findViewById(R.id.tv_current_version);
        mCurrent_version.setText("当前版本 " + getVersionInfo()[1]);
        imageView = mView.findViewById(R.id.mine_header);
        mName = mView.findViewById(R.id.mine_name);
        tv_account = mView.findViewById(R.id.mine_account);
        RelativeLayout mUserProfile = mView.findViewById(R.id.start_user_profile);
        LinearLayout mMineSetting = mView.findViewById(R.id.mine_setting);
        LinearLayout mMineService = mView.findViewById(R.id.mine_service);
        LinearLayout mMineXN = mView.findViewById(R.id.mine_xiaoneng);
        LinearLayout mMineAbout = mView.findViewById(R.id.mine_about);
        if (isDebug) {
            mMineXN.setVisibility(View.VISIBLE);
        } else {
            mMineXN.setVisibility(View.GONE);
        }
        mUserProfile.setOnClickListener(this);
        mMineSetting.setOnClickListener(this);
        mMineService.setOnClickListener(this);
        mMineAbout.setOnClickListener(this);
        mMineXN.setOnClickListener(this);
        mView.findViewById(R.id.my_wallet).setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_user_profile:
                startActivity(new Intent(getActivity(), MyAccountActivity.class));
                break;
            case R.id.mine_setting:
                startActivity(new Intent(getActivity(), AccountSettingActivity.class));
                break;
            case R.id.mine_service:
                CSCustomServiceInfo.Builder builder = new CSCustomServiceInfo.Builder();
                CSCustomServiceInfo csinfo = builder.nickName("微小信").build();
                RongIM.getInstance().startCustomerServiceChat(getActivity(), "KEFU152410101210011", "在线客服", csinfo);
                // KEFU146001495753714 正式  KEFU145930951497220 测试  小能: zf_1000_1481459114694   zf_1000_1480591492399
                break;
            case R.id.mine_xiaoneng:
                CSCustomServiceInfo.Builder builder1 = new CSCustomServiceInfo.Builder();
                builder1.province("北京");
                builder1.city("北京");
                RongIM.getInstance().startCustomerServiceChat(getActivity(), "zf_1000_1481459114694", "在线客服", builder1.build());
                break;
            case R.id.mine_about:  // 当前版本
                if (mNewVersionView.getVisibility() != View.GONE) {
                    downNewVersion();
                }

                break;
            case R.id.my_wallet:
                startActivity(new Intent(getActivity(), MyWalletActivity.class));
//                JrmfClient.intentWallet(getActivity());
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateUserInfo() {
        String userId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");
        String username = sp.getString(SealConst.Nick_Name, "");
        String userPortrait = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        mName.setText(username);
        tv_account.setText("iM账号：" + userId);
        if (!TextUtils.isEmpty(userPortrait)) {
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri
                    (new UserInfo(userId, username, Uri.parse(userPortrait)));
            ImageLoader.getInstance().displayImage(portraitUri, imageView, App.getOptions());
        }

        // 二维码
        try {
            Bitmap bitmapQRCode = QRCodeUtils.createQRCode(userId, 30);
            mQRCodeImg.setImageBitmap(bitmapQRCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }

    private String[] getVersionInfo() {
        String[] version = new String[2];

        PackageManager packageManager = getActivity().getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(getActivity().getPackageName(), 0);
            version[0] = String.valueOf(packageInfo.versionCode);
            version[1] = packageInfo.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return version;
    }


    private void downNewVersion() {
        mNewVersionView.setVisibility(View.GONE);
        final AlertDialog dlg = new AlertDialog.Builder(getContext()).create();
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_download);
        TextView text = window.findViewById(R.id.friendship_content1);
        TextView photo = window.findViewById(R.id.friendship_content2);
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(url);
                intent.setData(content_url);
                startActivity(intent);
                dlg.cancel();
            }
        });
        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // 6.0 以下
                if (url == null || url.isEmpty()) return;
                NToast.shortToast(getContext(), getString(R.string.downloading_apk));
                UpdateService.Builder.create(url)
                        .setStoreDir("update/flag")
                        .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                        .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                        .build(getContext());
                dlg.cancel();
//                } else {
//                    getActivity().requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
//                }
            }
        });
        isHasNewVersion = false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (url == null || url.isEmpty()) return;
                    NToast.shortToast(getContext(), getString(R.string.downloading_apk));
                    UpdateService.Builder.create(url)
                            .setStoreDir("update/flag")
                            .setDownloadSuccessNotificationFlag(Notification.DEFAULT_ALL)
                            .setDownloadErrorNotificationFlag(Notification.DEFAULT_ALL)
                            .build(getContext());
                } else {
                    NToast.shortToast(getContext(), "暂无读写SD卡权限");
                }
                break;
        }
    }

}
