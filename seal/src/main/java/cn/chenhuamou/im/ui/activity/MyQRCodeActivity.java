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
    private TextView mNicknameTv, mQRTipTv;

    private String portrait, nickName, targetId;
    private boolean isFromGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_qrcode);

        getIntentData();

        initView();

        initData();
    }

    /*
     * 获取传递过来的数据
     * */
    private void getIntentData() {
        portrait = getIntent().getStringExtra(SealConst.Portrait);
        nickName = getIntent().getStringExtra(SealConst.Nick_Name);
        targetId = getIntent().getStringExtra(SealConst.TargetId);
        isFromGroup = getIntent().getBooleanExtra(SealConst.IsFromGroup, false);
    }


    /*
     * 绑定控件
     * */
    private void initView() {
        if (isFromGroup) {
            setTitle("群组二维码");
        } else {
            setTitle("我的二维码");
        }

        mHeaderImg = findViewById(R.id.mine_header);
        mQRCodeImg = findViewById(R.id.img_qr_code);
        mNicknameTv = findViewById(R.id.tv_nickname);
        mQRTipTv = findViewById(R.id.tv_qr_tip);
    }

    /*
     * 初始化数据
     * */
    private void initData() {

        if (isFromGroup) {
            mQRTipTv.setText("扫一扫上面的二维码图案，加入群聊");
        } else {
            mQRTipTv.setText("扫一扫上面的二维码图案，加我微小信");
        }

        ImageLoader.getInstance().displayImage(portrait, mHeaderImg, App.getOptions());
        mNicknameTv.setText(nickName);

        // 二维码
        try {
            String fromWhere = isFromGroup ? SealConst.IsFromGroup : SealConst.IsPrivate;
            Bitmap bitmapQRCode = QRCodeUtils.createQRCode(targetId + "|" + fromWhere , 200);
            mQRCodeImg.setBackground(new BitmapDrawable(bitmapQRCode));
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }
}
