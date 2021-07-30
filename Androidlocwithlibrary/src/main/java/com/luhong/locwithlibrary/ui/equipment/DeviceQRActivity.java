package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.dialog.DeviceManageDialog;
import com.luhong.locwithlibrary.dialog.DeviceSavePhotDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.ui.FeesPayActivity;
import com.luhong.locwithlibrary.utils.AppUtils;
import com.luhong.locwithlibrary.utils.BitMapUtile;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.luhong.locwithlibrary.zxing.encode.QRCodeUtil;

import butterknife.BindView;

import static com.luhong.locwithlibrary.api.AppVariable.BACK_ACTIVITE;

public class DeviceQRActivity extends BaseActivity {
    @BindView(R2.id.iv_deviceQR_deviceQR)
    ImageView iv_deviceQR;
    @BindView(R2.id.tv_deviceType_deviceQR)
    TextView tv_deviceType;
    @BindView(R2.id.tv_deviceId_deviceQR)
    TextView tv_deviceId;
    @BindView(R2.id.btn_save_deviceQR)
    Button btn_save;

    @BindView(R2.id.ll_QRphot)
    LinearLayout llQRphot;

    private DeviceEntity deviceEntity;
    private String encryptSn;
    private String deviceSn = "";
    private Bitmap bitmapQR;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_device_qr;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "设备二维码");
        deviceEntity = (DeviceEntity) getIntent().getSerializableExtra(AppConstants.dataKey);
        if (deviceEntity != null) {
            deviceSn = deviceEntity.getOriginalSn();
            if (!TextUtils.isEmpty(deviceSn))
                tv_deviceId.setText("设备ID：" + deviceSn);
            if (!TextUtils.isEmpty(deviceEntity.getUnitModelCn())) {
                tv_deviceType.setText("设备型号：" + deviceEntity.getUnitModelCn());
            }

            if (AppUtils.checkStorageManagerPermission()) {
                encryptSn = deviceEntity.getEncryptSn();
                if (!TextUtils.isEmpty(encryptSn)) {
                    bitmapQR = QRCodeUtil.createQRImage(encryptSn, 250, 250, null, FileUtils.getRootPicDirImg() + "/iv_deviceQR.jpg");
                    if (bitmapQR != null)
                        iv_deviceQR.setImageBitmap(bitmapQR);
                }
            }
        } else {
            showDolag();
        }
    }

    void showDolag() {
        DeviceManageDialog.getInstance(mActivity).showDialog(DeviceManageDialog.DIALOG_JURISDICTION, 0, "", new DeviceManageDialog.IEventListeners() {
            @RequiresApi(api = 30)
            @Override
            public void onConfirm(int type) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.setData(Uri.parse("package:" + mActivity.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, 100);
            }

            @Override
            public void onCancel() {
                mActivity.finish();
            }
        });
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

        btn_save.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (bitmapQR != null && !TextUtils.isEmpty(encryptSn)) {
                    llQRphot.setDrawingCacheEnabled(true);//设置能否缓存图片信息（drawing cache）
                    llQRphot.buildDrawingCache();//如果能够缓存图片，则创建图片缓存
                    Bitmap bit = llQRphot.getDrawingCache();//如果图片已经缓存，返回一个bitmap
                    Bitmap bitmap = BitMapUtile.compressImage(bit);
                    llQRphot.destroyDrawingCache();//释放缓存占用的资源
                    boolean isSuccess = FileUtils.saveBitmap(mActivity, bitmap, FileUtils.getRootPicDir(), encryptSn + ".jpg");
                    if (isSuccess) {
                        showToast("设备二维码保存成功");
                    } else {
                        showToast("该设备二维码图片已保存");
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmapQR != null) {
            bitmapQR.recycle();
            bitmapQR = null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            bitmapQR = QRCodeUtil.createQRImage(encryptSn, 250, 250, null, FileUtils.getRootPicDirImg() + "/iv_deviceQR.jpg");
            if (bitmapQR != null)
                iv_deviceQR.setImageBitmap(bitmapQR);
        }
    }
}
