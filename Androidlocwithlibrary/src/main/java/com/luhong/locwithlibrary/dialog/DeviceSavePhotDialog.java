package com.luhong.locwithlibrary.dialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.BitMapUtile;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.view.ScaleImageView;
import com.luhong.locwithlibrary.zxing.encode.QRCodeUtil;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设备二维码保存本地
 */
public class DeviceSavePhotDialog extends BaseDialog {
    private static DeviceSavePhotDialog deviceRecordDialog;
    @BindView(R2.id.iv_deviceQR_deviceQR)
    ScaleImageView ivDeviceQRDeviceQR;


    @BindView(R2.id.tv_deviceType_deviceQR)
    TextView tvDeviceTypeDeviceQR;

    @BindView(R2.id.tv_deviceId_deviceQR)
    TextView tvDeviceIdDeviceQR;

    @BindView(R2.id.tv_confirm_deviceRecord)
    TextView tvConfirmDeviceRecord;

    @BindView(R2.id.ll_QRphot)
    LinearLayout llQRphot;

    @BindView(R2.id.iv_close_deviceRecord)
    ImageView ivCloseDeviceRecord;

    private IEventListener confirmListener;

    private DeviceEntity deviceEntity;

    private Bitmap bitmapQR;

    public static DeviceSavePhotDialog getInstance(Context context) {
        deviceRecordDialog = new DeviceSavePhotDialog(context);
        return deviceRecordDialog;
    }

    public void showDialog(DeviceEntity deviceEntity, IEventListener onConfirmListener) {
        this.deviceEntity = deviceEntity;
        this.confirmListener = onConfirmListener;
        if (deviceRecordDialog != null) show();
    }

    public DeviceSavePhotDialog(Context context) {
        super(context);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.dialog_save_device_phot;
    }

    @Override
    protected int setGravity() {
        return Gravity.CENTER;
    }

    @Override
    protected void initAlertDialogView(Window window) {
        setCancelable(false);
        tvConfirmDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                llQRphot.setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
                llQRphot.buildDrawingCache();//如果能够缓存图片，则创建图片缓存
                Bitmap bit = llQRphot.getDrawingCache();//如果图片已经缓存，返回一个bitmap
                Bitmap bitmap = BitMapUtile.compressImage(bit);
                llQRphot.destroyDrawingCache();//释放缓存占用的资源
                confirmListener.onConfirm(deviceEntity, bitmap);
                cancel();
            }
        });
        ivCloseDeviceRecord.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                confirmListener.onCancel();
                cancel();
            }
        });
        if (deviceEntity != null) {
            if (!TextUtils.isEmpty(deviceEntity.getOriginalSn()))
                tvDeviceIdDeviceQR.setText("设备ID：" + deviceEntity.getOriginalSn());
            if (!TextUtils.isEmpty(deviceEntity.getUnitModelCn())) {
                tvDeviceTypeDeviceQR.setText("设备型号：" + deviceEntity.getUnitModelCn());
            }
            if (!TextUtils.isEmpty(deviceEntity.getEncryptSn())) {
                bitmapQR = QRCodeUtil.createQRImage(deviceEntity.getEncryptSn(), 250, 250, null, FileUtils.getRootPicDirImg() + "/iv_deviceQR.jpg");
                if (bitmapQR != null)
                    ivDeviceQRDeviceQR.setImageBitmap(bitmapQR);
            }

        } else {
            cancel();
        }
    }

    @Override
    protected void onEventListener() {

    }

    public interface IEventListener {
        void onConfirm(DeviceEntity deviceEntity, Bitmap btimap);

        void onCancel();
    }

}
