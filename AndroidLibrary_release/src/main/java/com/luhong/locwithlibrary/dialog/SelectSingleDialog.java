package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.entity.OptionsSingleEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.zyyoona7.picker.OptionsPickerView;
import com.zyyoona7.picker.listener.OnOptionsSelectedListener;

import java.util.List;

import butterknife.BindView;

/**
 * 单选
 * Created by ITMG on 2019/9/24 0024.
 */
public class SelectSingleDialog extends BaseDialog {
    @BindView(R2.id.btn_cancel_optionsPicker)
    TextView tv_cancel;
    @BindView(R2.id.btn_confirm_optionsPicker)
    TextView tv_confirm;
    @BindView(R2.id.optionsPickerView_optionsPicker)
    OptionsPickerView<OptionsSingleEntity> optionsPickerView;

    private static SelectSingleDialog typeDialog;
    private IResultListener resultListener;
    private List<OptionsSingleEntity> dataList;

    private OptionsSingleEntity selectOptionsEntity;
    private int selectPosition = 0;

    public static SelectSingleDialog getInstance(Context context) {
        typeDialog = new SelectSingleDialog(context);
        return typeDialog;
    }

    public void showDialog(List<OptionsSingleEntity> dataList, int initPosition, IResultListener resultListener) {
        this.dataList = dataList;
        this.selectPosition = initPosition;
        this.resultListener = resultListener;
        show();
    }


    public SelectSingleDialog(Context context) {
        super(context);
    }

    @Override
    public int initLayoutId() {
        return R.layout.dialog_options_picker;
    }

    @Override
    protected int setGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        tv_cancel = findViewById(R.id.btn_cancel_optionsPicker);
        tv_confirm = findViewById(R.id.btn_confirm_optionsPicker);
        optionsPickerView = findViewById(R.id.optionsPickerView_optionsPicker);
        optionsPickerView.setData(dataList);
        optionsPickerView.setVisibleItems(7);
        optionsPickerView.setOpt1SelectedPosition(selectPosition);
        optionsPickerView.setResetSelectedPosition(true);
        optionsPickerView.setShowDivider(true);
        optionsPickerView.setDividerHeight(1, true);
        optionsPickerView.setLineSpacing(20);
        optionsPickerView.setDividerColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        optionsPickerView.setDrawSelectedRect(false);
        optionsPickerView.setSelectedItemTextColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        optionsPickerView.setNormalItemTextColor(ResUtils.resToColor(getContext(), R.color.text_3));
        optionsPickerView.setTextSize(22f, true);
        optionsPickerView.setSoundEffect(true);
        optionsPickerView.setSoundEffectResource(R.raw.options_choose);

        selectOptionsEntity = dataList.get(selectPosition);
        optionsPickerView.setOnOptionsSelectedListener(new OnOptionsSelectedListener<OptionsSingleEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable OptionsSingleEntity opt1Data, int opt2Pos, @Nullable OptionsSingleEntity opt2Data, int opt3Pos, @Nullable OptionsSingleEntity opt3Data) {
                if (opt1Data == null) {
                    return;
                }
                selectOptionsEntity = opt1Data;
                selectPosition = opt1Pos;
            }
        });
    }

    @Override
    public void onEventListener() {
        tv_cancel.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                cancel();
            }
        });
        tv_confirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (resultListener == null) return;
                resultListener.onConfirm(selectOptionsEntity, selectPosition);
                cancel();
            }
        });
    }

    public interface IResultListener {
        void onConfirm(OptionsSingleEntity selectOptionsEntity, int position);
    }
}
