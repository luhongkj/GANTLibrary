package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.entity.OptionsMultipleEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.OptionsParseHelper;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.zyyoona7.picker.OptionsPickerView;
import com.zyyoona7.picker.listener.OnOptionsSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 *
 * Created by ITMG on 2019/9/24 0024.
 */
public class SelectMultipleDialog extends BaseDialog {
    private String TAG = SelectMultipleDialog.class.getSimpleName();
    @BindView(R2.id.btn_cancel_optionsPicker)
    TextView tv_cancel;
    @BindView(R2.id.btn_confirm_optionsPicker)
    TextView tv_confirm;
    @BindView(R2.id.optionsPickerView_optionsPicker)
    OptionsPickerView<OptionsMultipleEntity> optionsPickerView;

    private static SelectMultipleDialog typeDialog;
    private IResultListener resultListener;
    List<OptionsMultipleEntity> p3List = new ArrayList<>(1);
    List<List<OptionsMultipleEntity>> c3List = new ArrayList<>(1);
    List<List<List<OptionsMultipleEntity>>> d3List = new ArrayList<>(1);

    private OptionsMultipleEntity selectOptions1, selectOptions2, selectOptions3;
    private int selectPosition1 = 0, selectPosition2 = 0, selectPosition3 = 0;

    public static SelectMultipleDialog getInstance(Context context) {
        typeDialog = new SelectMultipleDialog(context);
        return typeDialog;
    }

    public void showDialog(IResultListener resultListener) {
        this.resultListener = resultListener;
        show();
    }

    public SelectMultipleDialog(Context context) {
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
        OptionsParseHelper.initThreeLevelCityList(getContext(), p3List, c3List, d3List);
        optionsPickerView = findViewById(R.id.optionsPickerView_optionsPicker);
        optionsPickerView.setLinkageData(p3List, c3List, d3List);
        optionsPickerView.setVisibleItems(7);
        optionsPickerView.setResetSelectedPosition(true);
        optionsPickerView.setShowDivider(true);
        optionsPickerView.setDividerHeight(4);
        optionsPickerView.setLineSpacing(20);
        optionsPickerView.setDividerColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        optionsPickerView.setDrawSelectedRect(false);
        optionsPickerView.setSelectedItemTextColor(ResUtils.resToColor(getContext(), R.color.theme_color));
        optionsPickerView.setNormalItemTextColor(ResUtils.resToColor(getContext(), R.color.text_3));
        optionsPickerView.setTextSize(22f, true);
        optionsPickerView.setSoundEffect(true);
        optionsPickerView.setSoundEffectResource(R.raw.options_choose);
        optionsPickerView.setOpt3SelectedPosition(6);//0

        selectOptions1 = p3List.get(0);
        selectOptions2 = c3List.get(0).get(0);
        selectOptions3 = d3List.get(0).get(0).get(6);//0
        optionsPickerView.setOnOptionsSelectedListener(new OnOptionsSelectedListener<OptionsMultipleEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable OptionsMultipleEntity opt1Data, int opt2Pos, @Nullable OptionsMultipleEntity opt2Data, int opt3Pos, @Nullable OptionsMultipleEntity opt3Data) {
                if (opt1Data == null || opt2Data == null || opt3Data == null) {
                    return;
                }
                selectOptions1 = opt1Data;
                selectPosition1 = opt1Pos;
                selectOptions2 = opt2Data;
                selectPosition2 = opt2Pos;
                selectOptions3 = opt3Data;
                selectPosition3 = opt3Pos;
                Log.e(TAG, "onOptionsSelected: three Linkage op1Pos=" + opt1Pos + ",op1Data=" + opt1Data.getName() + ",op2Pos=" + opt2Pos
                        + ",op2Data=" + opt2Data.getName() + ",op3Pos=" + opt3Pos + ",op3Data=" + opt3Data.getName());
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
                resultListener.onConfirm(selectOptions1, selectPosition1, selectOptions2, selectPosition2, selectOptions3, selectPosition3);
                cancel();
            }
        });
    }

    public interface IResultListener {
        void onConfirm(@Nullable OptionsMultipleEntity selectOptions1, int position1, @Nullable OptionsMultipleEntity selectOptions2, int position2, @Nullable OptionsMultipleEntity selectOptions3, int position3);
    }
}
