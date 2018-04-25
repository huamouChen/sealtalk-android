package cn.chenhuamou.im.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.db.Groups;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.broadcast.BroadcastManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.CreateGroupResponse;
import cn.chenhuamou.im.server.response.CreateMyGroupResponse;
import cn.chenhuamou.im.server.response.QiNiuTokenResponse;
import cn.chenhuamou.im.server.response.SetGroupPortraitResponse;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.utils.photo.PhotoUtils;
import cn.chenhuamou.im.server.widget.BottomMenuDialog;
import cn.chenhuamou.im.server.widget.ClearWriteEditText;
import cn.chenhuamou.im.server.widget.LoadDialog;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imkit.RongIM;
import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.widget.AsyncImageView;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Created by AMing on 16/1/25.
 * Company RongCloud
 */
public class CreateGroupActivity extends BaseActivity implements View.OnClickListener {


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

    private static final int CREATE_GROUP = 16;

    public static final String REFRESH_GROUP_UI = "REFRESH_GROUP_UI";

    private AsyncImageView asyncImageView;
    private BottomMenuDialog dialog;
    private String mGroupName, mGroupId;
    private ClearWriteEditText mGroupNameEdit;
    private List<String> groupIds = new ArrayList<>();
    private Uri selectUri;

    private String imageUrl;
    private byte[] avatorBytes;   // 上传头像流
    private Button btn_com;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        setTitle(R.string.rc_item_create_group);
        List<Friend> memberList = (List<Friend>) getIntent().getSerializableExtra("GroupMember");
        initView();
        if (memberList != null && memberList.size() > 0) {
            for (Friend f : memberList) {
                groupIds.add(f.getUserId());
            }
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

    private void initView() {
        asyncImageView = (AsyncImageView) findViewById(R.id.img_Group_portrait);
        asyncImageView.setOnClickListener(this);
        mGroupNameEdit = (ClearWriteEditText) findViewById(R.id.create_groupname);
        btn_com = (Button) findViewById(R.id.create_ok);
        btn_com.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_Group_portrait:
                showPhotoDialog();
                break;
            case R.id.create_ok:
                mGroupName = mGroupNameEdit.getText().toString().trim();
                if (TextUtils.isEmpty(mGroupName)) {
                    NToast.shortToast(mContext, getString(R.string.group_name_not_is_null));
                    break;
                }
                if (mGroupName.length() == 1) {
                    NToast.shortToast(mContext, getString(R.string.group_name_size_is_one));
                    break;
                }
                if (AndroidEmoji.isEmoji(mGroupName)) {
                    if (mGroupName.length() <= 2) {
                        NToast.shortToast(mContext, getString(R.string.group_name_size_is_one));
                        break;
                    }
                }
                if (groupIds.size() >= 1) {
                    LoadDialog.show(mContext);
                    request(CREATE_GROUP, true);
                }

                break;
        }
    }


    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case CREATE_GROUP:
                String loninid = getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, "");
                return action.createMyGroup(loninid, mGroupName, groupIds, avatorBytes);
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case CREATE_GROUP:   // 创建群组成功
                    CreateMyGroupResponse createGroupResponse = (CreateMyGroupResponse) result;
                    if (createGroupResponse.getCode() != null && createGroupResponse.getCode().getCodeId().equals("100")) {
                        mGroupId = createGroupResponse.getValue().getGroupId() + "";
                        mGroupName = createGroupResponse.getValue().getGroupName();
                        imageUrl = BaseAction.DOMAIN + createGroupResponse.getValue().getGroupImage();
                        Groups groups = new Groups(mGroupId, mGroupName, imageUrl);
                        groups.setDisplayName(mGroupName);
                        SealUserInfoManager.getInstance().addGroup(groups);
                        BroadcastManager.getInstance(this).sendBroadcast(REFRESH_GROUP_UI, groups);

                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.create_group_success));
                        RongIM.getInstance().startConversation(mContext, Conversation.ConversationType.GROUP, mGroupId, mGroupName);
                        finish();

                    } else {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, getString(R.string.group_create_api_fail));
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case CREATE_GROUP:
                LoadDialog.dismiss(mContext);
                NToast.shortToast(mContext, getString(R.string.group_create_api_fail));
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
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
                        asyncImageView.setImageBitmap(bitmap);
                        avatorBytes = bitMap2StringBase64(bitmap);

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
                    asyncImageView.setImageBitmap(bit);
                    avatorBytes = bitMap2StringBase64(bit);

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


    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }
}
