package cn.chenhuamou.im.ui.activity;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.WriterException;

import org.w3c.dom.Text;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.utils.QRCodeUtils;
import io.rong.imageloader.core.ImageLoader;

/**
 * Created by Rex on 2018/4/27.
 * Email chenhm4444@gmail.com
 */
public class MyQRCodeActivity extends BaseActivity {

    private ImageView mHeaderImg, mQRCodeImg;
    private TextView mNicknameTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);

        initView();

        initData();
    }


    /*
    * 绑定控件
    * */
    private void initView() {
        setTitle("我的二维码");
        mHeaderImg = findViewById(R.id.mine_header);
        mQRCodeImg = findViewById(R.id.img_qr_code);
        mNicknameTv = findViewById(R.id.tv_nickname);
    }

    /*
    * 初始化数据
    * */
    private void initData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SealConst.SharedPreferencesName, MODE_PRIVATE);
        String portraitString = sharedPreferences.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        String nickName = sharedPreferences.getString(SealConst.Nick_Name, "");
        String userId = sharedPreferences.getString(SealConst.SEALTALK_LOGIN_ID, "");

        ImageLoader.getInstance().displayImage(portraitString, mHeaderImg, App.getOptions());
        mNicknameTv.setText(nickName);

        // 二维码
        try {
            Bitmap bitmapQRCode = QRCodeUtils.createQRCode(userId, 200);
            mQRCodeImg.setBackground(new BitmapDrawable(bitmapQRCode));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
