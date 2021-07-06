package com.luhong.locwithlibrary.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
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
import com.luhong.locwithlibrary.dialog.DeviceManageDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.dialog.DeviceSavePhotDialog;
import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.dialog.PromptDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.home.DeviceManagePresenter;
import com.luhong.locwithlibrary.ui.FeesPayActivity;
import com.luhong.locwithlibrary.ui.equipment.DeviceManageActivity;
import com.luhong.locwithlibrary.ui.equipment.DeviceQRActivity;
import com.luhong.locwithlibrary.ui.equipment.EditActivity;
import com.luhong.locwithlibrary.ui.equipment.electronic.EFenceActivity;
import com.luhong.locwithlibrary.ui.equipment.electronic.PhoneAlarmActivity;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;

import static com.luhong.locwithlibrary.api.AppVariable.FEEMONTHLY;

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
        mPresenter.getSafeguardMine();
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

                if (deviceEntity.getOweFee() < 0) {//欠费
                    DeviceManageDialog.getInstance(mActivity).showDialog(DeviceManageDialog.DIALOG_ARREARAGE,Math.abs( deviceEntity.getOweFee()), deviceEntity.getOweFeeType(), new DeviceManageDialog.IEventListeners() {
                        @Override
                        public void onConfirm(int type) {

                            //欠费类型（1：确认补缴，2、立即补缴（充钱去））
                            if (deviceEntity.getOweFeeType().equals("2")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("SN", deviceEntity.getSn());
                                bundle.putString("money", "" + Math.abs( deviceEntity.getOweFee()));
                                bundle.putString("serverLength", "" + FEEMONTHLY);
                                startIntentActivityForResult(FeesPayActivity.class, FeesPayActivity.RECHARGE_SUCCESS_CODE, bundle);
                            } else if (deviceEntity.getOweFeeType().equals("1")) {
                                showLoading("加载中...");
                                mPresenter.getVehicleConfirmPay(AppVariable.currentDeviceId, deviceEntity.getOweFee());
                            }
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                } else {//是否购保
                    boolean flag = false;
                    for (SafeguardEntity safeguardEntity : list.getRecords()) {
                        if (safeguardEntity.getSn().equals(deviceEntity.getSn()) && safeguardEntity.getStatus() == SafeguardEntity.TYPE_GUARANTEED) {//车辆ID相同且是保障中的
                            flag = true;
                        }
                    }
                    if (list == null || flag) {
                        //  DeviceManageDialog.getInstance(mActivity).showDialog();
                        DeviceManageDialog.getInstance(mActivity).showDialog(DeviceManageDialog.DIALOG_BUY_INSURANCE, Math.abs( deviceEntity.getOweFee()), deviceEntity.getOweFeeType(), new DeviceManageDialog.IEventListeners() {
                            @Override
                            public void onConfirm(int type) {
                                showLoading("加载中...");
                                mPresenter.deleteDevice(deviceEntity);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    } else {
                        //  DeviceManageDialog.getInstance(mActivity).showDialog();
                        DeviceManageDialog.getInstance(mActivity).showDialog(DeviceManageDialog.DIALOG_NORMAL, Math.abs( deviceEntity.getOweFee()), deviceEntity.getOweFeeType(), new DeviceManageDialog.IEventListeners() {
                            @Override
                            public void onConfirm(int type) {
                                showLoading("加载中...");
                                mPresenter.deleteDevice(deviceEntity);
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }

                }

              /*  PromptDialog.getInstance(mActivity).showDialog("确定解除绑定?", new BaseDialog.IEventListener() {
                    @Override
                    public void onConfirm() {
                        dialog.setTitle("正在解绑");
                        dialog.show();
                        mPresenter.deleteDevice(deviceEntity);
                    }

                    @Override
                    public void onCancel() {

                    }
                });*/


            }
        });
        //电子围栏
        rl_electronic.setOnClickListener(v ->

        {
            Bundle bundle = new Bundle();
            bundle.putString("SN", deviceEntity.getSn());
            startIntentActivity(EFenceActivity.class, bundle);
        });
        //电话告警
        rl_emergency.setOnClickListener(v ->

        {
            Bundle bundle = new Bundle();
            bundle.putString("SN", deviceEntity.getSn());
            startIntentActivity(PhoneAlarmActivity.class, bundle);

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
        cancelLoading();
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

    BasePageEntity<SafeguardEntity> list;

    @Override
    public void onSafeguardMineSuccess(BasePageEntity<SafeguardEntity> pageList) {
        cancelLoading();
        this.list = pageList;
    }

    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {
        cancelLoading();
        ((DeviceManageActivity) mActivity).onRefresh();
        ToastUtil.show("补缴成功!");
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == AppConstants.RESULT_CODE_UPDATE) {
            if (data == null) return;
            String deviceName = data.getStringExtra(AppConstants.dataKey);
            tv_name.setText(deviceName);
            deviceEntity.setNickName(deviceName);
            mPresenter.updateDevice(deviceEntity);
        }else if (resultCode == FeesPayActivity.RECHARGE_SUCCESS_CODE) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.show("补缴成功!");
                    ((DeviceManageActivity) mActivity).onRefresh();
                }
            }, 2000);
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
