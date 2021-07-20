package com.luhong.locwithlibrary.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.luhong.locwithlibrary.BuildConfig;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.ui.BasePdfActivity;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BasePopupWindow extends PopupWindow {
    protected Unbinder mUnbinder;
    Context mContext;
    private LayoutInflater mInflater;
    private View mContentView;
    @BindView(R2.id.tv_confirm_deviceRecord)
    TextView tvConfirmDeviceRecord;

    @BindView(R2.id.iv_close_deviceRecord)
    ImageView ivCloseDeviceRecord;

    @BindView(R2.id.tv_checkProtocol_verification)
    TextView tv_checkProtocol_verification;

    @BindView(R2.id.tv_privacy_verification)
    TextView tv_privacy_verification;

    @BindView(R2.id.cePhoneCode)
    EditText cePhoneCode;

    @BindView(R2.id.tvPhone)
    TextView tvPhone;

    @BindView(R2.id.tvGetCode)
    TextView tvGetCode;

    private IEventListener confirmListener;
    String phone;

    boolean isCheckProtocol = false;

    public BasePopupWindow(Context context, IEventListener confirmListener, String phone) {
        super(context);
        this.mContext = context;
        this.confirmListener = confirmListener;
        this.phone = phone;
        //打气筒
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //打气
        mContentView = mInflater.inflate(R.layout.dialog_verify_phone, null);
        //设置View
        setContentView(mContentView);
        mUnbinder = ButterKnife.bind(this, mContentView);
        //设置宽与高
        setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        setHeight(WindowManager.LayoutParams.MATCH_PARENT);

        setBackgroundDrawable(context.getResources().getDrawable(R.color.translucent_05));
        /**
         * 设置进出动画
         */
        setAnimationStyle(R.style.MyPopupWindow);
        /**
         * 设置背景只有设置了这个才可以点击外边和BACK消失
         */
        setBackgroundDrawable(new ColorDrawable());
        /**
         * 设置可以获取集点
         */
        setFocusable(true);

        /**
         * 设置点击外边可以消失
         */
        setOutsideTouchable(false);

        /**
         *设置可以触摸
         */
        setTouchable(true);


        /**
         * 设置点击外部可以消失
         */

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /**
                 * 判断是不是点击了外部
                 */
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    return false;
                }
                //不是点击外部
                return false;
            }
        });

        /**
         * 初始化View与监听器
         */
        initView();
        initListener();
    }

    private CountDownTimer countDownTimer;

    private void initView() {
        tvPhone.setText(phone);
        tvConfirmDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (TextUtils.isEmpty(cePhoneCode.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (!isCheckProtocol){
                    ToastUtil.show("请你勾选并阅读隐私协议");
                    return;
                }

                confirmListener.onConfirm(cePhoneCode.getText().toString());
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });

        tv_checkProtocol_verification.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isCheckProtocol = !isCheckProtocol;
                if (isCheckProtocol) {
                    tv_checkProtocol_verification.setCompoundDrawablesRelativeWithIntrinsicBounds(ResUtils.resToDrawable(mContext, R.mipmap.verification_protocol_foc), null, null, null);
                } else {
                    tv_checkProtocol_verification.setCompoundDrawablesRelativeWithIntrinsicBounds(ResUtils.resToDrawable(mContext, R.mipmap.verification_protocol_nor), null, null, null);
                }
                tvConfirmDeviceRecord.setEnabled(isCheckProtocol);
            }
        });
        tv_privacy_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmListener.onPrivacy();
            }
        });
        //获取验证码
        tvGetCode.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!isCheckProtocol){
                    ToastUtil.show("请你勾选并阅读隐私协议");
                    return;
                }
                confirmListener.ongetCode(tvPhone.getText().toString());
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });

        ivCloseDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                confirmListener.onCancel();
                cancel();
                if (countDownTimer != null) countDownTimer.cancel();
            }
        });
    }

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
        countDownTimer.start();
    }

    private void initListener() {

    }

    public interface IEventListener {
        void onConfirm(String code);

        void ongetCode(String phone);

        void onPrivacy();

        void onCancel();
    }

    public void cancel() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}
