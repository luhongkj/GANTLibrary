package com.luhong.locwithlibrary.ui.equipment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.base.BasePopupWindow;
import com.luhong.locwithlibrary.contract.home.DeviceAddContract;
import com.luhong.locwithlibrary.dialog.VerifyPhoneDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.home.DeviceAddPresenter;
import com.luhong.locwithlibrary.ui.CaptureActivity;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.LoginSuccessUtil;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ITMG on 2020-01-20.
 */
public class LDeviceAddActivity extends BaseMvpActivity<DeviceAddPresenter> implements DeviceAddContract.View {
    public static final String VEHICLECHOICE_DATA = "VEHICLECHOICE_DATA";

    public static final int ADDDEVICE_RESULT_ = 50;
    @BindView(R2.id.tv_left_title)
    TextView tvLeftTitle;
    @BindView(R2.id.tv_center_title)
    TextView tvCenterTitle;
    @BindView(R2.id.iv_icon_title)
    ImageView ivIconTitle;
    @BindView(R2.id.tv_right_title)
    TextView tvRightTitle;
    @BindView(R2.id.ll_titleBar_title)
    LinearLayout llTitleBarTitle;
    @BindView(R2.id.tv_bicycleTypeInfo_deviceAdd)
    TextView tvBicycleTypeInfoDeviceAdd;
    @BindView(R2.id.tv_bicycleBrandInfo_deviceAdd)
    TextView tvBicycleBrandInfoDeviceAdd;
    @BindView(R2.id.tv_bicycleModelInfo_deviceAdd)
    TextView tvBicycleModelInfoDeviceAdd;
    @BindView(R2.id.tv_bicycleFrameNoInfo_deviceAdd)
    TextView tvBicycleFrameNoInfoDeviceAdd;
    @BindView(R2.id.ll_bicycleInfo_deviceAdd)
    LinearLayout llBicycleInfoDeviceAdd;
    @BindView(R2.id.tvCarName)
    TextView tvCarName;
    @BindView(R2.id.ad_rl)
    LinearLayout adRl;
    @BindView(R2.id.tvCarCode)
    TextView tvCarCode;
    @BindView(R2.id.tvCarType)
    TextView tvCarType;
    @BindView(R2.id.tvCarTime)
    TextView tvCarTime;
    @BindView(R2.id.ll_bicycleEdit_deviceAdd)
    LinearLayout llBicycleEditDeviceAdd;
    @BindView(R2.id.tv_deviceModel_deviceAdd)
    TextView tvDeviceModelDeviceAdd;
    @BindView(R2.id.tv_deviceId_deviceAdd)
    TextView tvDeviceIdDeviceAdd;
    @BindView(R2.id.iv_scan)
    ImageView ivScan;
    @BindView(R2.id.btn_confirm_deviceAdd)
    Button btnConfirmDeviceAdd;

    @BindView(R2.id.tv_title_null)
    TextView tv_title_null;

    VehicleListEntity cheVehicleList;
    private DeviceEntity deviceParams;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_device_add;
    }

    BasePopupWindow myPopupWindow;

    @Override
    protected void initView(Bundle savedInstanceState) {
        initTitleView(true, "添加设备");
        cheVehicleList = (VehicleListEntity) getIntent().getSerializableExtra(VEHICLECHOICE_DATA);
        tvCarName.setText(cheVehicleList.getVehicleName());
        tvCarCode.setText(cheVehicleList.getVin());
        tvCarType.setText(cheVehicleList.getVehicleType());
        ivScan.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                startIntentActivityForResult(CaptureActivity.class, CaptureActivity.resultDecode);
            }
        });

        btnConfirmDeviceAdd.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (modeEntity == null) {
                    ToastUtil.show("请扫描设备二维码");
                    return;
                }
                if (!AppVariable.GIANT_ISBIN) {
                    myPopupWindow = new BasePopupWindow(mActivity, new BasePopupWindow.IEventListener() {
                        @Override
                        public void onConfirm(String code) {
                            showLoading("数据提交中...");
                            mPresenter.checkCode(AppVariable.GIANT_PHONE, code);
                            myPopupWindow.dismiss();
                        }

                        @Override
                        public void ongetCode(String phone) {
                            mPresenter.getCode(mActivity, phone);
                        }

                        @Override
                        public void onCancel() {
                            myPopupWindow.dismiss();
                        }
                    }, AppVariable.GIANT_PHONE);
                    myPopupWindow.showAtLocation(btnConfirmDeviceAdd, Gravity.CENTER, 0, 0);
                    return;
                } else {
                    putData();
                }
            }
        });
    }

    void putData() {
        deviceParams = new DeviceEntity();
        deviceParams.setSn(deviceSn);
        deviceParams.setVin(cheVehicleList.getVin());
        //                    if (isAfterEmpty) {
        deviceParams.setBrandId(cheVehicleList.getBrand());
        deviceParams.setVehicleModel(cheVehicleList.getVehicleType());
        deviceParams.setVehicleType(cheVehicleList.getVehicleModel());
        //                    }
        showLoading("数据提交中...", false);
        mPresenter.addVehicle(deviceParams, btnConfirmDeviceAdd);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onEventListener() {

    }


    @Override
    protected void fetchData() {

    }

    @Override
    public void onVehicleAddSuccess(Object resultEntity) {
        mPresenter.checkTokenBind();
    }

    InstallModeEntity modeEntity;
    String deviceSn;

    @Override
    public void onInstallModeSuccess(InstallModeEntity installModeEntity) {
        if (installModeEntity == null) {
            showToast("设备不存在");
            return;
        }
        tv_title_null.setVisibility(View.GONE);
        tvDeviceModelDeviceAdd.setVisibility(View.VISIBLE);
        tvDeviceIdDeviceAdd.setVisibility(View.VISIBLE);
        this.modeEntity = installModeEntity;
        deviceSn = installModeEntity.getSn();
        tvDeviceModelDeviceAdd.setText("型号：" + (TextUtils.isEmpty(installModeEntity.getUnitModel()) ? "" : installModeEntity.getUnitModel()));
        tvDeviceIdDeviceAdd.setText("ID：" + (TextUtils.isEmpty(installModeEntity.getOriginalSn()) ? "" : installModeEntity.getOriginalSn()));
    }

    @Override
    public void onVehicleTypeSuccess(List<VehicleModelEntity> deviceEntity) {

    }

    @Override
    public void onVehicleByVinSuccess(VehicleDtoEntity deviceEntity) {

    }

    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {

    }

    //获取验证码回调
    @Override
    public void onGetCodeSuccess(Object resultEntity) {
        if (myPopupWindow != null) {
            myPopupWindow.getCodeCallBack();
        }
    }

    //提交验证结果回调
    @Override
    public void onCheckCodeSuccess(Object resultEntity) {
        putData();
    }

    //获取用户信息
    @Override
    public void onCheckTokenBindSuccess(UserEntity resultEntity) {
        AppVariable.GIANT_ISBIN = resultEntity.getBind();
        LoginSuccessUtil.onLoginSuccess(this, resultEntity);
        setResult(ADDDEVICE_RESULT_);
        finish();
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.error("执行？requestCode= " + requestCode);
        if (requestCode == CaptureActivity.resultDecode) {
            if (data == null) return;
            String content = data.getStringExtra(CaptureActivity.dataTypeKey);
            if (TextUtils.isEmpty(content)) return;
            Logger.error("二维码内容=" + content);
            if (content.length() == AppConstants.DEVICE_SN_LENGTH) {
                //    ToastUtil.show(content);
                mPresenter.getInstallMode(content);
            } else {
                showToast(content);
            }
        }
    }
}
