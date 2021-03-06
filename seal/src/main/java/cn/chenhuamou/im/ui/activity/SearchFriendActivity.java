package cn.chenhuamou.im.ui.activity;

import android.content.Context;
import android.net.Uri;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.chenhuamou.im.App;
import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealUserInfoManager;
import cn.chenhuamou.im.SealAppContext;
import cn.chenhuamou.im.SealConst;
import cn.chenhuamou.im.db.Friend;
import cn.chenhuamou.im.server.BaseAction;
import cn.chenhuamou.im.server.network.async.AsyncTaskManager;
import cn.chenhuamou.im.server.network.http.HttpException;
import cn.chenhuamou.im.server.response.ApplyFriendResponse;
import cn.chenhuamou.im.server.response.FindUserInfoResponse;
import cn.chenhuamou.im.server.response.FriendInvitationResponse;
import cn.chenhuamou.im.server.response.GetUserInfoByPhoneResponse;
import cn.chenhuamou.im.server.response.GetUserInfoResponse;
import cn.chenhuamou.im.server.utils.AMUtils;
import cn.chenhuamou.im.server.utils.CommonUtils;
import cn.chenhuamou.im.server.utils.NToast;
import cn.chenhuamou.im.server.widget.DialogWithYesOrNoUtils;
import cn.chenhuamou.im.server.widget.LoadDialog;
import cn.chenhuamou.im.server.widget.SelectableRoundedImageView;
import io.rong.imageloader.core.ImageLoader;
import io.rong.imlib.model.UserInfo;

public class SearchFriendActivity extends BaseActivity implements View.OnClickListener {

    private static final int CLICK_CONVERSATION_USER_PORTRAIT = 1;
    private static final int SEARCH_PHONE = 10;
    private static final int ADD_FRIEND = 11;
    private EditText mEtSearch;
    private LinearLayout searchItem;
    private RelativeLayout searchBtn;
    private TextView searchName;
    private TextView tv_id;
    private SelectableRoundedImageView searchHeader;
    private String mPhone;
    private String addFriendMessage;
    private String mFriendId;

    private Friend mFriend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle((R.string.search_friend));

        searchBtn = (RelativeLayout) findViewById(R.id.rl_search);
        tv_id = (TextView) findViewById(R.id.tv_id);
        mEtSearch = (EditText) findViewById(R.id.search_edit);
        searchItem = (LinearLayout) findViewById(R.id.search_result);
        searchName = (TextView) findViewById(R.id.search_name);
        searchHeader = (SelectableRoundedImageView) findViewById(R.id.search_header);

        searchBtn.setOnClickListener(this);
        mEtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    searchBtn.setVisibility(View.VISIBLE);
                    searchItem.setVisibility(View.GONE);
                    mPhone = s.toString().trim();
                    tv_id.setText(s);
                } else {
                    searchBtn.setVisibility(View.GONE);
                    tv_id.setText("");
                    searchItem.setVisibility(View.GONE);
                    searchName.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public Object doInBackground(int requestCode, String id) throws HttpException {
        switch (requestCode) {
            case SEARCH_PHONE:
                return action.findUserInfoByUserId(mPhone);
            case ADD_FRIEND:
                return action.applyFriend(mPhone, addFriendMessage);
        }
        return super.doInBackground(requestCode, id);
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case SEARCH_PHONE:
                    LoadDialog.dismiss(mContext);
                    FindUserInfoResponse findUserInfoResponse = (FindUserInfoResponse) result;
                    if (findUserInfoResponse.getCode().getCodeId().equals("100") && !findUserInfoResponse.getValue().isExist()) {
                        LoadDialog.dismiss(mContext);
                        NToast.shortToast(mContext, "不存在此用户");
                        searchItem.setVisibility(View.GONE);
                    } else {
                        LoadDialog.dismiss(mContext);
                        mFriendId = tv_id.getText().toString();
                        searchItem.setVisibility(View.VISIBLE);
                        searchName.setText(tv_id.getText().toString());
                        // 设置头像
                        if (findUserInfoResponse.getValue().getHeadimg() != null && !TextUtils.isEmpty(findUserInfoResponse.getValue().getHeadimg())) {
                            String portraitUri = BaseAction.DOMAIN + findUserInfoResponse.getValue().getHeadimg();
                            ImageLoader.getInstance().displayImage(portraitUri, searchHeader, App.getOptions());
                        }

                        searchItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (isFriendOrSelf(mFriendId)) {
                                    Intent intent = new Intent(SearchFriendActivity.this, UserDetailActivity.class);
                                    intent.putExtra("friend", mFriend);
                                    intent.putExtra("type", CLICK_CONVERSATION_USER_PORTRAIT);
                                    startActivity(intent);
                                    SealAppContext.getInstance().pushActivity(SearchFriendActivity.this);
                                    return;
                                }
                                DialogWithYesOrNoUtils.getInstance().showEditDialog(mContext, getString(R.string.add_text), getString(R.string.add_friend), new DialogWithYesOrNoUtils.DialogCallBack() {
                                    @Override
                                    public void executeEvent() {

                                    }

                                    @Override
                                    public void updatePassword(String oldPassword, String newPassword) {

                                    }

                                    @Override
                                    public void executeEditEvent(String editText) {
                                        if (!CommonUtils.isNetworkConnected(mContext)) {
                                            NToast.shortToast(mContext, R.string.network_not_available);
                                            return;
                                        }
                                        addFriendMessage = editText;
                                        if (TextUtils.isEmpty(editText)) {
                                            addFriendMessage = "我是" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.Nick_Name, "");
                                        }
                                        if (!TextUtils.isEmpty(mFriendId)) {
                                            LoadDialog.show(mContext);
                                            request(ADD_FRIEND);
                                        } else {
                                            NToast.shortToast(mContext, "id is null");
                                        }
                                    }
                                });
                            }
                        });
                    }
                    break;
                case ADD_FRIEND:
                    ApplyFriendResponse applyFriendResponse = (ApplyFriendResponse) result;
                    if (applyFriendResponse.getCode() != null) {
                        NToast.shortToast(mContext, getString(R.string.request_success));
                        LoadDialog.dismiss(mContext);
                    } else {
                        NToast.shortToast(mContext, "请求失败 发生错误:");
                        LoadDialog.dismiss(mContext);
                    }
                    break;
            }
        }
    }

    @Override
    public void onFailure(int requestCode, int state, Object result) {
        switch (requestCode) {
            case ADD_FRIEND:
                NToast.shortToast(mContext, "你们已经是好友");
                LoadDialog.dismiss(mContext);
                break;
            case SEARCH_PHONE:
                if (state == AsyncTaskManager.HTTP_ERROR_CODE || state == AsyncTaskManager.HTTP_NULL_CODE) {
                    super.onFailure(requestCode, state, result);
                } else {
                    NToast.shortToast(mContext, "用户不存在");
                }
                LoadDialog.dismiss(mContext);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        hintKbTwo();
        finish();
        return super.onOptionsItemSelected(item);
    }

    private void hintKbTwo() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive() && getCurrentFocus() != null) {
            if (getCurrentFocus().getWindowToken() != null) {
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private boolean isFriendOrSelf(String id) {
        String inputPhoneNumber = mEtSearch.getText().toString().trim();
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String selfPhoneNumber = sp.getString(SealConst.SEALTALK_LOGING_PHONE, "");
        if (inputPhoneNumber != null) {
            if (inputPhoneNumber.equals(selfPhoneNumber)) {
                mFriend = new Friend(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""),
                        sp.getString(SealConst.Nick_Name, ""),
                        Uri.parse(sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "")));
                return true;
            } else {
                mFriend = SealUserInfoManager.getInstance().getFriendByID(id);
                if (mFriend != null) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        LoadDialog.show(mContext);
        request(SEARCH_PHONE);

    }
}
