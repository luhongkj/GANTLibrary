package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.CountDownTimer;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.BitMapUtile;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.luhong.locwithlibrary.view.ClearEditText;
import com.luhong.locwithlibrary.view.ScaleImageView;
import com.luhong.locwithlibrary.zxing.encode.QRCodeUtil;

import butterknife.BindView;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * 设备二维码保存本地
 */
public class VerifyPhoneDialog extends BaseDialog {
    private static VerifyPhoneDialog deviceRecordDialog;

    @BindView(R2.id.tv_confirm_deviceRecord)
    TextView tvConfirmDeviceRecord;

    @BindView(R2.id.iv_close_deviceRecord)
    ImageView ivCloseDeviceRecord;

    @BindView(R2.id.cePhoneCode)
    EditText cePhoneCode;

    @BindView(R2.id.tvPhone)
    TextView tvPhone;

    @BindView(R2.id.tvGetCode)
    TextView tvGetCode;

    private IEventListener confirmListener;
    String phone;

    public static VerifyPhoneDialog getInstance(Context context) {
        deviceRecordDialog = new VerifyPhoneDialog(context);
        return deviceRecordDialog;
    }

    public void showDialog(String phone, IEventListener onConfirmListener) {
        this.confirmListener = onConfirmListener;
        this.phone = phone;
        if (deviceRecordDialog != null) show();
    }

    public VerifyPhoneDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_verify_phone;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        setCancelable(true);
        tvPhone.setText(phone);
        setView(cePhoneCode);
       //调用系统输入法
        ((InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE))
                .toggleSoftInput(0,
                        InputMethodManager.HIDE_NOT_ALWAYS);

        tvConfirmDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                confirmListener.onConfirm(cePhoneCode.getText().toString());
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });
        //获取验证码
        tvGetCode.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                confirmListener.ongetCode(tvPhone.getText().toString());
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });

        ivCloseDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                confirmListener.onCancel();
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });
    }

    private CountDownTimer countDownTimer;

    public void getCodeCallBack() {
        tvGetCode.setEnabled(false);
        ToastUtil.show("验证码获取成功");
        if (countDownTimer != null) countDownTimer.start();
        countDownTimer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvGetCode.setText((millisUntilFinished / 1000) + "秒后可重试");
                tvGetCode.setEnabled(false);
                tvGetCode.setTextColor(mContext.getResources().getColor(R.color.colorPrimary2));
            }

            @Override
            public void onFinish() {
                tvGetCode.setText("获取验证码");
                tvGetCode.setEnabled(true);
                tvGetCode.setTextColor(mContext.getResources().getColor(R.color.tool_bar_color));
            }
        };
    }

    @Override
    protected void onEventListener() {

    }

    public interface IEventListener {
        void onConfirm(String code);

        void ongetCode(String phone);

        void onCancel();
    }
}
