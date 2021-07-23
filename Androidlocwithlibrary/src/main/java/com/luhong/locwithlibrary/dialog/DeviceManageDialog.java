package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ToastUtil;

import butterknife.BindView;
import butterknife.Unbinder;

import static com.luhong.locwithlibrary.api.AppVariable.FEEMONTHLY;

/**
 * 温馨提示
 * Created by ITMG on 2019/12/11.
 */
public class DeviceManageDialog extends BaseDialog {

    @BindView(R2.id.tv_confirm_devicePrompt)
    TextView tvConfirm;

    @BindView(R2.id.tv_clo_devicePrompt)
    TextView tv_clo_devicePrompt;

    @BindView(R2.id.tv_title_devicePrompt)
    TextView tvTitle;
    @BindView(R2.id.rl_contentBg_devicePrompt)
    RelativeLayout rl_contentBg;
    @BindView(R2.id.tv_content_devicePrompt)
    TextView tvContent;

    @BindView(R2.id.view_lin)
    View view_lin;

    @BindView(R2.id.iv_close_deviceRecord)
    ImageView iv_close_deviceRecord;

    private static DeviceManageDialog devicePromptDialog;
    private IEventListeners confirmListener;
    private int type;//1  欠费   2  正常解绑  3 已购保
    private float feemoney;
    private String feeType;
    public final static int DIALOG_ARREARAGE = 1;//欠费解绑
    public final static int DIALOG_NORMAL = 2;//正常解绑
    public final static int DIALOG_BUY_INSURANCE = 3;//已购保解绑
    public final static int DIALOG_ADDRESS_NULL = 4;//车辆列表给空
    public final static int DIALOG_ADDRESS_OFF_LIN = 5;//设备离线
    public static DeviceManageDialog getInstance(Context context) {
        devicePromptDialog = new DeviceManageDialog(context);
        return devicePromptDialog;
    }

    /**
     * @param type              弹窗类型 1  欠费   2  正常解绑  3 已购保
     * @param feemoney          欠费金额
     * @param feeType           流量账户是否购钱补缴
     * @param onConfirmListener
     */
    public void showDialog(int type, float feemoney, String feeType, IEventListeners onConfirmListener) {
        this.confirmListener = onConfirmListener;
        this.type = type;
        this.feemoney = feemoney;
        this.feeType = feeType;
        if (devicePromptDialog != null) show();
    }

    public DeviceManageDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_device_manage;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        //1  欠费   2  正常解绑  3 已购保
        switch (type) {
            case DIALOG_ARREARAGE://1  欠费
//oweFeeType 欠费类型（1：确认补缴，2、立即补缴（充钱去））
                int feecon = (int) (feemoney / FEEMONTHLY);
                tvContent.setText("设备已欠费" + feecon + "个月,请补缴后再解绑。");
                view_lin.setVisibility(View.GONE);
                iv_close_deviceRecord.setVisibility(View.GONE);
                tvTitle.setText("确定解绑设备");
                if (feeType.equals("1"))
                    tvConfirm.setText("确认补缴");
                else if (feeType.equals("2"))
                    tvConfirm.setText("立即补缴");
                break;

            case DIALOG_NORMAL://2  正常解绑
                tvContent.setText("设备解绑后,会持续扣除设备的月租费用,再次使用该设备,需补缴所欠费费用。");
                tvTitle.setText("温馨提示");
                view_lin.setVisibility(View.GONE);
                iv_close_deviceRecord.setVisibility(View.GONE);
                tvConfirm.setText("确认解绑");
                break;

            case DIALOG_BUY_INSURANCE://3 已购保
                tvContent.setText("1.您已购买保险,解绑设备后保障服务将失效\n" +
                        "2.设备解绑后,会持续扣除设备的月租费用,再次使用该设备,需补缴所欠费费用。");
                tvTitle.setText("确定解绑设备");
                tvConfirm.setText("确认解绑");
                view_lin.setVisibility(View.GONE);
                iv_close_deviceRecord.setVisibility(View.GONE);
                break;
            case DIALOG_ADDRESS_NULL:
                tvTitle.setText("温馨提示");
                tvConfirm.setText("去添加");
                tvContent.setText("当前您未添加车辆，请添加车辆再添加设备。");
                tv_clo_devicePrompt.setVisibility(View.GONE);
                view_lin.setVisibility(View.VISIBLE);
                iv_close_deviceRecord.setVisibility(View.VISIBLE);
                break;
            case DIALOG_ADDRESS_OFF_LIN:
                tvContent.setText(feeType);
                tvTitle.setText("温馨提示");
                view_lin.setVisibility(View.GONE);
                tv_clo_devicePrompt.setVisibility(View.GONE);
                iv_close_deviceRecord.setVisibility(View.GONE);
                tvConfirm.setText("知道了");
                break;
        }
    }

    @Override
    protected void onEventListener() {
        tvConfirm.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onConfirm(type);
                cancel();
            }
        });
        tv_clo_devicePrompt.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onCancel();
                cancel();
            }
        });

        iv_close_deviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (confirmListener != null) confirmListener.onCancel();
                cancel();
            }
        });
    }

    /**
     * 事件监听
     */

    public interface IEventListeners {
        void onConfirm(int type);

        void onCancel();
    }
}
