package cn.chenhuamou.im.ui.activity;


import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;


import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.GetPicThread;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.GetUserInfoResponse;
import cn.chenhuamou.im.server.response.PublicResponse;
import cn.chenhuamou.im.server.response.QiNiuTokenResponse;
import cn.chenhuamou.im.server.response.SetPortraitResponse;
import cn.chenhuamou.im.server.utils.NLog;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.photo.PhotoUtils;
import cn.chenhuamou.im.server.widget.BottomMenuDialog;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.server.widget.SelectableRoundedImageView;
import cn.chenhuamou.im.utils.UploadUtils;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;


public class MyAccountActivity extends BaseActivity implements View.OnClickListener {

    private static final int UP_LOAD_PORTRAIT = 8;


    private static final int PERMISSIONS_FOR_TAKE_PHOTO = 10;
    private static final int PERMISSIONS_FOR_PICK_IMAGE = 11;
    //图片文件路径
    private String picPath;
    //图片对应Uri
    private Uri photoUri;
    //拍照对应RequestCode
    public static final int SELECT_PIC_BY_TACK_PHOTO = 1;
    //裁剪图片
    private static final int CROP_PICTURE = 3;


    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SelectableRoundedImageView mImageView;
    private TextView mName;
    private BottomMenuDialog dialog;


    private byte[] portraitBytes;   // 上传头像流


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount);
        setTitle(R.string.de_actionbar_myacc);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        initView();

    }

    private void initView() {
        final TextView mPhone = findViewById(R.id.tv_my_phone);
        RelativeLayout portraitItem = findViewById(R.id.rl_my_portrait);
        RelativeLayout nameItem = findViewById(R.id.rl_my_username);
        RelativeLayout phoneItem = findViewById(R.id.rl_my_telephone);
        mImageView = findViewById(R.id.img_my_portrait);
        mName = findViewById(R.id.tv_my_username);

        portraitItem.setOnClickListener(this);
        nameItem.setOnClickListener(this);
        phoneItem.setOnClickListener(this);
        String cacheName = sp.getString(SealConst.Nick_Name, "");
        String cachePortrait = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
        String cachePhone = sp.getString(SealConst.Bind_Phone, "");
        // 设置手机号码
        if (!TextUtils.isEmpty(cachePhone)) {
            mPhone.setText(cachePhone);
        }
        if (!TextUtils.isEmpty(cachePortrait)) {
            mName.setText(cacheName);
            String cacheId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "a");
            String portraitUri = SealUserInfoManager.getInstance().getPortraitUri(new UserInfo(
                    cacheId, cacheName, Uri.parse(cachePortrait)));
            ImageLoader.getInstance().displayImage(portraitUri, mImageView, App.getOptions());
        }

        // 更新昵称
        BroadcastManager.getInstance(mContext).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mName.setText(sp.getString(SealConst.Nick_Name, ""));
            }
        });

        // 绑定手机
        BroadcastManager.getInstance(mContext).addAction(SealConst.Bind_Phone, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPhone.setText(sp.getString(SealConst.Bind_Phone, ""));
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_my_portrait:
                showPhotoDialog();
                break;
            case R.id.rl_my_username:
                Intent intent = new Intent(this, UpdateNameActivity.class);
                intent.putExtra("isUpdateName", true);
                startActivity(intent);
                break;
            case R.id.rl_my_telephone:
                Intent intent2 = new Intent(this, UpdateNameActivity.class);
                intent2.putExtra("isUpdateName", false);
                startActivity(intent2);
                break;
        }
    }


    /*
     * 前台处理图片
     * */
    private byte[] bitMap2StringBase64(Bitmap bit) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);
        return bos.toByteArray();
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case UP_LOAD_PORTRAIT:
                return action.setPortrait(portraitBytes);

        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case UP_LOAD_PORTRAIT:
                    LoadDialog.dismiss(mContext);
                    PublicResponse publicResponse = (PublicResponse) result;
                    if (publicResponse.isResult()) {
                        // 处理返回的头像
                        try {
                            JSONObject jsonObject = new JSONObject(publicResponse.getParameter());
                            String imageUrl = BaseAction.DOMAIN + jsonObject.get("HeaderImage");
                            editor.putString(SealConst.SEALTALK_LOGING_PORTRAIT, imageUrl);
                            editor.commit();
                            ImageLoader.getInstance().displayImage(imageUrl, mImageView, App.getOptions());
                            if (RongIM.getInstance() != null) {
                                RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), sp.getString(SealConst.Nick_Name, ""), Uri.parse(imageUrl)));
                            }
                            BroadcastManager.getInstance(mContext).sendBroadcast(SealConst.CHANGEINFO);
                            NToast.shortToast(mContext, getString(R.string.portrait_update_success));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        NToast.shortToast(mContext, "头像更新失败");
                    }
                    break;
            }
        }
    }


    @Override
    public void onFailure(int requestCode, int state, Object result) {
        LoadDialog.dismiss(mContext);
        switch (requestCode) {
            case UP_LOAD_PORTRAIT:
                NToast.shortToast(mContext, "头像更新失败");
                break;
        }
    }


    /**
     * 弹出底部框
     */
    private void showPhotoDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new BottomMenuDialog(mContext);

        // 拍照
        dialog.setConfirmListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }

                //小于6.0版本直接操作
                if (Build.VERSION.SDK_INT < 23) {
                    takePictures();
                } else {
                    //6.0以后权限处理
                    permissionForM();
                }
            }
        });

        // 从本地导入
        dialog.setMiddleListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                choosePhoto();
            }
        });
        dialog.show();
    }

    /*
     * 从相册获取图片
     * */
    private void choosePhoto() {
        /**
         * 打开选择图片的界面
         */
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");//相片类型
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PERMISSIONS_FOR_PICK_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拍照
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_PIC_BY_TACK_PHOTO) {
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(photoUri, pojo, null, null, null);
                if (cursor != null) {
                    int columnIndex = cursor.getColumnIndexOrThrow(pojo[0]);
                    cursor.moveToFirst();
                    picPath = cursor.getString(columnIndex);
                }
                if (picPath != null && (picPath.endsWith(".png") || picPath.endsWith(".PNG") || picPath.endsWith(".jpg") || picPath.endsWith(".JPG"))) {
                    photoUri = Uri.fromFile(new File(picPath));
                    if (Build.VERSION.SDK_INT > 23) {
                        photoUri = FileProvider.getUriForFile(this, "cn.chenhuamou.im.fileprovider", new File(picPath));
                        cropForN(picPath, CROP_PICTURE);
                    } else {
                        startPhotoZoom(photoUri, CROP_PICTURE);
                    }
                } else {
                    //错误提示
                    NToast.shortToast(mContext, "头像设置失败");
                }
            }

            // 裁剪后的图片
            if (requestCode == CROP_PICTURE) {
                if (photoUri != null) {
                    Bitmap bitmap = BitmapFactory.decodeFile(picPath);
                    if (bitmap != null) {
                        mImageView.setImageBitmap(bitmap);
                        portraitBytes = bitMap2StringBase64(bitmap);
                        LoadDialog.show(mContext);
                        request(UP_LOAD_PORTRAIT);
                    }
                } else {
                    NToast.shortToast(mContext, "头像设置失败");
                }
            }
        }

        // 从相册获取
        if (requestCode == PERMISSIONS_FOR_PICK_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri uri = data.getData();
                    Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                    mImageView.setImageBitmap(bit);
                    portraitBytes = bitMap2StringBase64(bit);
                    LoadDialog.show(mContext);
                    request(UP_LOAD_PORTRAIT);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    NToast.shortToast(mContext, "头像设置失败");
                }
            } else {
                NToast.shortToast(mContext, "头像设置失败");
            }
        }
    }


    /**
     * 拍照获取图片
     */
    private void takePictures() {
        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            ContentValues values = new ContentValues();
            photoUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, SELECT_PIC_BY_TACK_PHOTO);
        } else {
            Toast.makeText(this, "手机未插入内存卡", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * 图片裁剪，参数根据自己需要设置
     *
     * @param uri
     * @param REQUE_CODE_CROP
     */
    private void startPhotoZoom(Uri uri,
                                int REQUE_CODE_CROP) {
        int dp = 500;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);// 去黑边
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 4);//输出是X方向的比例
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 600);//输出X方向的像素
        intent.putExtra("outputY", 450);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra("return-data", false);//设置为不返回数据
        startActivityForResult(intent, REQUE_CODE_CROP);
    }

    /**
     * 7.0以上版本图片裁剪操作
     *
     * @param imagePath
     * @param REQUE_CODE_CROP
     */
    private void cropForN(String imagePath, int REQUE_CODE_CROP) {
        Uri cropUri = getImageContentUri(new File(imagePath));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(cropUri, "image/*");
        intent.putExtra("crop", "true");
        //输出是X方向的比例
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 600);
        intent.putExtra("outputY", 450);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUE_CODE_CROP);
    }


    private Uri getImageContentUri(File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }


    /**
     * 安卓6.0以上版本权限处理
     */
    private void permissionForM() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSIONS_FOR_TAKE_PHOTO);
        } else {
            takePictures();
        }
    }

    /*
     * android 6.0 动态申请权限
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == PERMISSIONS_FOR_TAKE_PHOTO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePictures();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}




