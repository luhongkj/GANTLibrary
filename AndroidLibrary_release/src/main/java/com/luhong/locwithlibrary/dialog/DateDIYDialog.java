package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.text.format.Time;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.zyyoona7.picker.DatePickerView;
import com.zyyoona7.picker.base.BaseDatePickerView;
import com.zyyoona7.picker.ex.DayWheelView;
import com.zyyoona7.picker.ex.MonthWheelView;
import com.zyyoona7.picker.ex.YearWheelView;
import com.zyyoona7.picker.listener.OnDateSelectedListener;
import com.zyyoona7.wheel.WheelView;

import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;

/**
 * Created by ITMG on 2018/6/25.
 */
public class DateDIYDialog extends BaseDialog {
    @BindView(R2.id.btn_cancel_dateDialog)
    TextView tv_cancel;
    @BindView(R2.id.btn_confirm_dateDialog)
    TextView tv_confirm;
    @BindView(R2.id.datePicker_dateDialog)
    DatePickerView datePicker;
    private static DateDIYDialog typeDialog;
    private IResultListener resultListener;
    private int initYear;
    private int initMonth;
    private int initDay;
    private boolean isHideDayItem = false;
    private boolean isInit = false;

    public static DateDIYDialog getInstance(Context context) {
        typeDialog = new DateDIYDialog(context);
        return typeDialog;
    }

    public void showDialog(boolean isInit, boolean isHideDayItem, IResultListener resultListener) {
        this.isInit = isInit;
        this.isHideDayItem = isHideDayItem;
        this.resultListener = resultListener;
        show();
    }

    public void showDialog(boolean isHideDayItem, IResultListener resultListener) {
        this.isHideDayItem = isHideDayItem;
        this.resultListener = resultListener;
        show();
    }

    public void showDialog(IResultListener resultListener) {
        showDialog(false, resultListener);
    }

    public DateDIYDialog(Context context) {
        super(context);
    }

    @Override
    public int initLayoutId() {
        return R.layout.dialog_date_diy;
    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        tv_cancel = findViewById(R.id.btn_cancel_dateDialog);
        tv_confirm = findViewById(R.id.btn_confirm_dateDialog);
        datePicker = findViewById(R.id.datePicker_dateDialog);
        Time time = new Time("GMT+8");
        time.setToNow();
        if (isInit) {
            initYear = 1990;
            initMonth = 6;
            initDay = 1;
        }else {
            initYear = time.year;
            initMonth = time.month + 1;
            initDay = time.monthDay;
        }
        datePicker.setSelectedYear(initYear);
        datePicker.setSelectedMonth(initMonth);
        datePicker.setSelectedDay(initDay);
        //设置最大选择日期
        Calendar maxCalendar = Calendar.getInstance();
        maxCalendar.set(Calendar.YEAR, time.year);
        maxCalendar.set(Calendar.MONTH, time.month);
        maxCalendar.set(Calendar.DAY_OF_MONTH, time.monthDay);
        datePicker.setMaxDate(maxCalendar);
        //设置最小选择日期
        Calendar minCalendar = Calendar.getInstance();
        minCalendar.set(Calendar.YEAR, 1900);
        minCalendar.set(Calendar.MONTH, 0);
        minCalendar.set(Calendar.DAY_OF_MONTH, 1);
        datePicker.setMinDate(minCalendar);
        datePicker.setYearRange(1900, time.year);

        datePicker.setTextSize(24, true);
        datePicker.setShowLabel(false);
        if (isHideDayItem) {
            datePicker.hideDayItem();
        }
        datePicker.getYearWv().setTextBoundaryMargin(30, true);
        datePicker.getMonthWv().setTextBoundaryMargin(40, true);
        datePicker.getDayWv().setTextBoundaryMargin(40, true);
        datePicker.setResetSelectedPosition(true);
        datePicker.setShowDivider(true);
        datePicker.setDividerHeight(1, true);
        datePicker.setLineSpacing(20);
        datePicker.setDrawSelectedRect(true);
        datePicker.setDividerType(WheelView.DIVIDER_TYPE_FILL);
        datePicker.setDividerColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        datePicker.setSelectedItemTextColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        datePicker.setNormalItemTextColor(ResUtils.resToColor(getContext(), R.color.text_3));

        YearWheelView yearWv3 = datePicker.getYearWv();
        MonthWheelView monthWv3 = datePicker.getMonthWv();
        DayWheelView dayWv3 = datePicker.getDayWv();
        yearWv3.setIntegerNeedFormat("%d");//%d年
        monthWv3.setIntegerNeedFormat("%01d");
        dayWv3.setIntegerNeedFormat("%02d");

        datePicker.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(BaseDatePickerView datePickerView, int year, int month, int day, @Nullable Date date) {
                initYear = year;
                initMonth = month;
                initDay = day;
            }
        });
    }

    @Override
    public void onEventListener() {
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultListener == null) return;
                resultListener.onConfirm(initYear, initMonth, initDay);
                cancel();
            }
        });

    }


    public interface IResultListener {
        void onConfirm(int year, int month, int day);
    }
}
