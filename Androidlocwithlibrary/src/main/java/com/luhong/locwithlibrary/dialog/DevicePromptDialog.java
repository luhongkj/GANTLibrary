package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ToastUtil;

import butterknife.BindView;

/**
 * 欠费补缴/设备激活
 * Created by ITMG on 2019/12/11.
 */
public class DevicePromptDialog extends BaseDialog {
    public static final int TYPE_ACTIVATING = 0;//设备激活提示
    public static final int TYPE_ARREARS = 1;//欠费充值提示(立即补缴)
    public static final int TYPE_ARREARS_CONFIRM = 3;//欠费补缴确认提示
    public static final int TYPE_SAFEGUARD = 2;//购买保险提示
    public static final int TYPE_VIN = 10;//添加设备VIN提示

    public static final int TYPE_PAY_FLOW_SUCCESS = 4;//流量套餐支付成功
    public static final int TYPE_PAY_PHONE_SUCCESS = 5;//电话告警服务支付成功
    public static final int TYPE_PAY_FENCE_SUCCESS = 6;//电子围栏告警服务支付成功
    public static final int TYPE_PAY_SAFEGUARD_SUCCESS = 7;//保险购买支付成功
    public static final int TYPE_PAY_ARREARS_SUCCESS = 8;//补缴支付成功
    public static final int TYPE_PAY_ACTIVE_SUCCESS = 9;//激活预充值支付成功

    @BindView(R2.id.tv_confirm_devicePrompt)
    TextView tvConfirm;
    @BindView(R2.id.tv_title_devicePrompt)
    TextView tvTitle;
    @BindView(R2.id.rl_contentBg_devicePrompt)
    RelativeLayout rl_contentBg;
    @BindView(R2.id.tv_content_devicePrompt)
    TextView tvContent;

    private static DevicePromptDialog devicePromptDialog;
    private IEventListener confirmListener;
    private String deviceId;
    private String amount = "0";
    private String cost = "0";
    private int dataType;

    public static DevicePromptDialog getInstance(Context context) {
        devicePromptDialog = new DevicePromptDialog(context);
        return devicePromptDialog;
    }

    public void showDialog(int dataType, String deviceId, String amount, IEventListener onConfirmListener) {
        this.dataType = dataType;
        this.deviceId = deviceId;
        this.amount = amount;
        this.confirmListener = onConfirmListener;
        if (devicePromptDialog != null) show();
    }

    public void showDialog(int dataType, String deviceId, String amount, String cost, IEventListener onConfirmListener) {
        this.dataType = dataType;
        this.deviceId = deviceId;
        this.amount = amount;
        this.cost = cost;
        this.confirmListener = onConfirmListener;
        if (devicePromptDialog != null) show();
    }


    public void showDialog(int dataType, IEventListener onConfirmListener) {
        this.dataType = dataType;
        this.confirmListener = onConfirmListener;
        if (devicePromptDialog != null) show();
    }

    public DevicePromptDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_device_prompt;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        if (dataType == TYPE_ACTIVATING) {
            setCancelable(true);
            tvTitle.setText("设备激活");
            rl_contentBg.setBackgroundResource(R.mipmap.prompt_activating);
            tvContent.setText(String.format(getContext().getString(R.string.active), deviceId, amount));
            tvConfirm.setText("确认激活");
        } else if (dataType == TYPE_ARREARS) {
            setCancelable(true);
            tvTitle.setText("欠费补缴");
            rl_contentBg.setBackgroundResource(R.mipmap.prompt_arrears);
            int con = (int) ((Float.parseFloat(amount)) / (Float.parseFloat(cost)));
            tvContent.setText(String.format(getContext().getString(R.string.recharge_supplement_text), deviceId, con+""));
            tvConfirm.setText("立即补缴");
        } else if (dataType == TYPE_ARREARS_CONFIRM) {
            setCancelable(true);
            tvTitle.setText("欠费补缴");
            rl_contentBg.setBackgroundResource(R.mipmap.prompt_arrears);
            int con = (int) ((Float.parseFloat(amount)) / (Float.parseFloat(cost)));
            tvContent.setText(String.format(getContext().getString(R.string.arrears_confirm_text), deviceId, con+""));
            tvConfirm.setText("确认补缴");
        } else if (dataType == TYPE_SAFEGUARD) {
            setCancelable(true);
            tvTitle.setText("温馨提示");
            rl_contentBg.setBackgroundResource(R.mipmap.end_riding_bg);
            tvContent.setText("为了保障您的权益，购买前请确认本人已阅读并同意《丢车保障服务协议》。");
            tvConfirm.setText("立即购买");
        } else if (dataType == TYPE_VIN) {
            setCancelable(true);
            tvTitle.setText("温馨提示");
            rl_contentBg.setBackgroundResource(R.mipmap.end_riding_bg);
            tvContent.setText("车架号码为本车唯一识别码，用于统计车辆轨迹及保障服务，请据实填写");
            tvConfirm.setText("我知道了");
        } else {
            setCancelable(true);
            tvTitle.setText("支付成功");
            tvConfirm.setText("知道了");
            rl_contentBg.setBackgroundResource(R.mipmap.prompt_pay_success);
            tvContent.setGravity(Gravity.CENTER_HORIZONTAL);
            if (dataType == TYPE_PAY_FLOW_SUCCESS) {
                tvContent.setText("恭喜您已成功充值流量账户～\n当前余额为：" + amount + "个月");
            } else if (dataType == TYPE_PAY_PHONE_SUCCESS) {
                tvContent.setText("恭喜您已成功购买电话告警服务～\n有效期至：" + amount);//2020年12月20日
            } else if (dataType == TYPE_PAY_FENCE_SUCCESS) {
                tvContent.setText("恭喜您已成功购买电子围栏服务～\n有效期至：" + amount);//2020年12月20日
                //            } else if (dataType == TYPE_PAY_SAFEGUARD_SUCCESS) {
                //                tvContent.setText("恭喜您已成功充值流量账户～\n当前余额为：" + amount + "元");
            } else if (dataType == TYPE_PAY_ARREARS_SUCCESS) {
                tvContent.setText("恭喜您已成功补缴所欠流量费～\n当前设备功能可正常使用");
            } else if (dataType == TYPE_PAY_ACTIVE_SUCCESS) {
                tvTitle.setText("激活成功");
                tvContent.setText("恭喜您已成功激活设备～\n当前流量账户余额：" + amount + "个月");
            }
        }
    }

    @Override
    protected void onEventListener() {
        tvConfirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onConfirm();
                cancel();
            }
        });
    }

    private long firstPressedTime = 0;

    @Override
    public void onBackPressed() {
        if (dataType == TYPE_SAFEGUARD || dataType >= TYPE_PAY_FLOW_SUCCESS) {
            cancel();
            return;
        }
        if (System.currentTimeMillis() - firstPressedTime > 2000) {
            ToastUtil.show(R.string.exit_app);
            firstPressedTime = System.currentTimeMillis();
        } else {
            BaseActivity.exitApp();
        }
    }
}
