package cn.chenhuamou.im.ui.activity.wallet;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.chenhuamou.im.R;
import cn.chenhuamou.im.SealConst;

/**
 * Created by Rex on 2018/4/25.
 * Email chenhm4444@gmail.com
 */
public class SelectDateActivity extends Activity implements View.OnClickListener {


    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private int mYear = 0, mMonth = 0, mDay = 0;
    private boolean isStart = false;

    private LinearLayout mMaskView;

    private Button btn_start_date, btn_end_date, btn_today, btn_pass_three_day, btn_last_week, btn_date_comfirm, btn_date_cancle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_date);
        initData();
        initView();
    }

    private void initData() {
        Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);

    }

    private void initView() {
        mMaskView = findViewById(R.id.ll_mask);

        btn_start_date = findViewById(R.id.btn_start_date);
        btn_end_date = findViewById(R.id.btn_end_date);
        btn_today = findViewById(R.id.btn_today);
        btn_pass_three_day = findViewById(R.id.btn_pass_three_day);
        btn_last_week = findViewById(R.id.btn_last_week);
        btn_date_comfirm = findViewById(R.id.btn_date_comfirm);
        btn_date_cancle = findViewById(R.id.btn_date_cancle);

        btn_start_date.setText(getCurrentTime());
        btn_end_date.setText(getTomorrowTime());

        addListener();
    }

    private void addListener() {
        mMaskView.setOnClickListener(this);
        btn_start_date.setOnClickListener(this);
        btn_end_date.setOnClickListener(this);
        btn_today.setOnClickListener(this);
        btn_pass_three_day.setOnClickListener(this);
        btn_last_week.setOnClickListener(this);
        btn_date_comfirm.setOnClickListener(this);
        btn_date_cancle.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_mask:
                finish();
                break;
            case R.id.btn_start_date:
                isStart = true;
                showDatePicker();
                break;
            case R.id.btn_end_date:
                isStart = false;
                showDatePicker();
                break;
            case R.id.btn_today:
                btn_start_date.setText(getCurrentTime());
                btn_end_date.setText(getTomorrowTime());
                break;
            case R.id.btn_pass_three_day:
                btn_start_date.setText(getPassThreeDate());
                btn_end_date.setText(getTomorrowTime());
                break;
            case R.id.btn_last_week:
                btn_start_date.setText(getLastWeek());
                btn_end_date.setText(getTomorrowTime());
                break;
            case R.id.btn_date_comfirm:
                clickComfirmButton();
                break;
            case R.id.btn_date_cancle:
                finish();
                break;
        }
    }

    /*
     * 获取日期选择器
     * */
    private void showDatePicker() {
        new DatePickerDialog(this, onDateSetListener, mYear, mMonth, mDay).show();
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            mYear = year;
            mMonth = month + 1;
            mDay = dayOfMonth;

            String selectedDate = String.format("%d-%02d-%02d", mYear, mMonth, mDay);
            // 起始日期
            if (isStart) {
                btn_start_date.setText(selectedDate);
            } else {    // 截止日期
                btn_end_date.setText(selectedDate);
            }
        }
    };

    /*
     *
     * 点击确认按钮*/
    private void clickComfirmButton() {
        Intent intent = new Intent();
        intent.putExtra(SealConst.StartDate, btn_start_date.getText().toString());
        intent.putExtra(SealConst.EndDate, btn_end_date.getText().toString());
        setResult(RESULT_OK, intent);
        this.finish();
    }


    // 获取系统的当前时间
    private String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    // 获取明天的时间
    private String getTomorrowTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return simpleDateFormat.format(calendar.getTime());
    }

    // 获取系统的前3填时间
    private String getPassThreeDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -3);
        return simpleDateFormat.format(calendar.getTime());
    }

    // 获取系统前一周时间
    private String getLastWeek() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -7);
        return simpleDateFormat.format(calendar.getTime());
    }

}
