package com.luhong.locwithlibrary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseMvpFragment;
import com.luhong.locwithlibrary.contract.home.DeviceManageContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DeviceSavePhotDialog;
import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.dialog.PromptDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.home.DeviceManagePresenter;
import com.luhong.locwithlibrary.ui.equipment.DeviceManageActivity;
import com.luhong.locwithlibrary.ui.equipment.DeviceQRActivity;
import com.luhong.locwithlibrary.ui.equipment.EditActivity;
import com.luhong.locwithlibrary.ui.equipment.electronic.EFenceActivity;
import com.luhong.locwithlibrary.ui.equipment.electronic.PhoneAlarmActivity;
import com.luhong.locwithlibrary.utils.FileUtils;

import java.util.List;

import butterknife.BindView;

/**
 * 设备信息framgnet
 */
public class DeviceManageFragment extends BaseMvpFragment<DeviceManagePresenter> implements DeviceManageContract.View/*, CancelAdapt*/ {
    @BindView(R2.id.rl_qr_deviceManage)
    RelativeLayout rl_qr;
    @BindView(R2.id.tv_name_deviceManage)
    TextView tv_name;
    @BindView(R2.id.tv_type_deviceManage)
    TextView tv_type;
    @BindView(R2.id.tv_brand_deviceManage)
    TextView tv_brand;
    @BindView(R2.id.tv_bicycleModel_deviceManage)
    TextView tv_bicycleModel;
    @BindView(R2.id.tv_frameNo_deviceManage)
    TextView tv_frameNo;
    @BindView(R2.id.tv_deviceModel_deviceManage)
    TextView tv_deviceModel;
    @BindView(R2.id.tv_deviceId_deviceManage)
    TextView tv_deviceId;
    @BindView(R2.id.tv_unbind_deviceManage)
    TextView tv_unbind;

    @BindView(R2.id.rl_electronic)
    RelativeLayout rl_electronic;

    @BindView(R2.id.rl_emergency)
    RelativeLayout rl_emergency;

    @BindView(R2.id.tv_emergency)//电话告警
            TextView tv_emergency;

    @BindView(R2.id.tv_rail)
    TextView tv_rail;

    private int dataType;
    private DeviceEntity deviceEntity;
    private LoadingDialog dialog;

    public DeviceManageFragment() {
    }

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_device_manage;
    }

    @Override
    protected void initData() {
        if (getArguments() != null) {
            dataType = getArguments().getInt(AppConstants.dataTypeKey);
            deviceEntity = (DeviceEntity) getArguments().getSerializable(AppConstants.dataKey);
        }
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        dialog = new LoadingDialog(getActivity());
        if (deviceEntity != null) {
            /**
             *   * fenceAlarmIsOpen:电子围栏服务是否开通：1是 0否
             *      * fenceAlarmDeadline:电子围栏服务到期时间
             *      * telAlarmIsOpen:电话告警服务是否开通：1是 0否
             *      * telAlarmDeadline:电话告警服务到期时间
             */
            if (TextUtils.isEmpty(deviceEntity.getFenceAlarmIsOpen()) || deviceEntity.getFenceAlarmIsOpen().equals("0")) {

                tv_rail.setText("立即开通");
            } else {
                tv_rail.setText("有效期至 :" + deviceEntity.getFenceAlarmDeadline());
            }

            if (TextUtils.isEmpty(deviceEntity.getTelAlarmIsOpen()) || deviceEntity.getTelAlarmIsOpen().equals("0")) {
                tv_emergency.setText("立即开通");
            } else {
                tv_emergency.setText("有效期至 :" + deviceEntity.getTelAlarmDeadline());

            }
            tv_name.setText(deviceEntity.getNickName());
            tv_type.setText(deviceEntity.getVehicleTypeCn());
            tv_brand.setText(deviceEntity.getBrandName());
            tv_bicycleModel.setText(deviceEntity.getVehicleModel());
            tv_frameNo.setText(deviceEntity.getVin());
            tv_deviceModel.setText(deviceEntity.getUnitModelCn());
            tv_deviceId.setText(deviceEntity.getOriginalSn());
        }
    }

    @Override
    protected void fetchData() {

    }

    @Override
    protected void onEventListener() {
        tv_name.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString(AppConstants.dataKey, tv_name.getText().toString().trim());
                bundle.putInt(AppConstants.dataTypeKey, 101);
                startIntentActivityForResult(EditActivity.class, AppConstants.REQUEST_CODE_UPDATE, bundle);
            }
        });
        rl_qr.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (deviceEntity != null)
                    startIntentActivity(DeviceQRActivity.class, AppConstants.dataKey, deviceEntity);
            }
        });
        tv_unbind.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                PromptDialog.getInstance(mActivity).showDialog("确定解除绑定?", new BaseDialog.IEventListener() {
                    @Override
                    public void onConfirm() {
                        dialog.setTitle("正在解绑");
                        dialog.show();
                        mPresenter.deleteDevice(deviceEntity);
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        });
        //电子围栏
        rl_electronic.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("SN", deviceEntity.getSn());
            startIntentActivity(EFenceActivity.class, bundle);
        });
        //电话告警
        rl_emergency.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("SN",deviceEntity.getSn());
            startIntentActivity(PhoneAlarmActivity.class,bundle);

        });
    }

    @Override
    public void onDeviceListSuccess(List<DeviceEntity> dataList) {

    }

    @Override
    public void onDeviceUpdateSuccess(DeviceEntity deviceEntity) {
        showToast("修改成功");
        ((DeviceManageActivity) mActivity).onRefresh();
    }

    @Override
    public void onDeviceDeleteSuccess(boolean isCurrentDevice, DeviceEntity deviceEntity) {
        dialog.dismiss();
        showToast("解绑成功");
        if (isCurrentDevice) {
            AppVariable.currentDeviceId = "";
            AppVariable.currentVehicleId = "";
        }
        ((DeviceManageActivity) mActivity).onRefresh();
        //判断该二维码是否在本地,在本地不弹出保存提示
        if (FileUtils.isFiel(FileUtils.getRootPicDir(), deviceEntity.getEncryptSn() + ".jpg")) {
            return;
        }
        DeviceSavePhotDialog.getInstance(getActivity()).showDialog(deviceEntity, new DeviceSavePhotDialog.IEventListener() {
            @Override
            public void onConfirm(DeviceEntity deviceEntity, Bitmap btimap) {
                if (btimap != null && !TextUtils.isEmpty(deviceEntity.getEncryptSn())) {
                    boolean isSuccess = FileUtils.saveBitmap(mActivity, btimap, FileUtils.getRootPicDir(), deviceEntity.getEncryptSn() + ".jpg");
                    if (isSuccess) {
                        showToast("设备二维码保存成功");
                    } else {
                        showToast("该设备二维码图片已保存");
                    }
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    @Override
    public void onProductDescriptionSuccess(PdfEntity resultEntity) {

    }

    @Override
    public void onFailure(int errType, String errMsg) {
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) return;
        if (resultCode == AppConstants.RESULT_CODE_UPDATE) {
            String deviceName = data.getStringExtra(AppConstants.dataKey);
            tv_name.setText(deviceName);
            deviceEntity.setNickName(deviceName);
            mPresenter.updateDevice(deviceEntity);
        }
    }

    public static DeviceManageFragment newInstance(int dataType, DeviceEntity deviceEntity) {
        DeviceManageFragment fragment = new DeviceManageFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(AppConstants.dataTypeKey, dataType);
        arguments.putSerializable(AppConstants.dataKey, deviceEntity);
        fragment.setArguments(arguments);
        return fragment;
    }
}
