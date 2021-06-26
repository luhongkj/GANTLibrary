package com.luhong.locwithlibrary.presenter.home;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;


import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.home.DeviceAddContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.model.home.CheckTokenBind;
import com.luhong.locwithlibrary.model.home.DeviceBrandModel;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.ICheckTokenBind;
import com.luhong.locwithlibrary.model.home.IDeviceBrandModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.model.home.ILoginModel;
import com.luhong.locwithlibrary.model.home.LoginModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;

/**
 * 车辆添加
 * Created by ITMG on 2020-01-21.
 */
public class DeviceAddPresenter extends DeviceAddContract.Presenter {
    private IDeviceManageModel deviceManageModel;
    private IDeviceBrandModel deviceBrandModel;
    private ILoginModel loginModel;
    private ICheckTokenBind checkTokenBind;
    public DeviceAddPresenter() {
    }

    @Override
    public void addVehicle(DeviceEntity deviceEntity, View view) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        view.setEnabled(false);
        ApiClient.getInstance().doSubscribe(deviceManageModel.addVehicle(deviceEntity), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("设备添加成功");
                view.setEnabled(true);
                if (isViewAttached()) baseMvpView.onVehicleAddSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                view.setEnabled(true);
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

    @Override
    public void getInstallMode(String deviceSn) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getInstallMode(deviceSn), new BaseObserver<InstallModeEntity>() {
            @Override
            protected void onSuccess(InstallModeEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onInstallModeSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void getVehicleType() {
        if (deviceBrandModel == null) deviceBrandModel = new DeviceBrandModel();
        ApiClient.getInstance().doSubscribe(deviceBrandModel.getVehicleType(), new BaseObserver<List<VehicleModelEntity>>() {
            @Override
            protected void onSuccess(List<VehicleModelEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onVehicleTypeSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }

    @Override
    public void getVehicleByVin(String vehicleVin) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getVehicleByVin(vehicleVin), new BaseObserver<VehicleDtoEntity>() {
            @Override
            protected void onSuccess(VehicleDtoEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onVehicleByVinSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
            }
        });
    }

    @Override
    public void getVehicleConfirmPay(String deviceSn, float oweFee) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getVehicleConfirmPay(deviceSn, oweFee), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onVehicleConfirmPaySuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(4, errResult);
            }
        });
    }

    @Override
    public void getCode(Context mContext, String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (loginModel == null) loginModel = new LoginModel();
        ApiClient.getInstance().doSubscribe(loginModel.getCode(phoneNo), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onGetCodeSuccess(resultEntity);
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(0, errMsg);
            }
        });
    }

    @Override
    public void checkCode(String phoneNo, String code) {
        if (TextUtils.isEmpty(phoneNo)) {
            ToastUtil.show("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            ToastUtil.show("请输入验证码");
            return;
        }
        if (loginModel == null) loginModel = new LoginModel();
        ApiClient.getInstance().doSubscribe(loginModel.checkCode(phoneNo, code), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onCheckCodeSuccess(resultEntity);
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(1, errMsg);
            }
        });
    }

    @Override
    public void checkTokenBind() {
        if (checkTokenBind == null) checkTokenBind = new CheckTokenBind();
        ApiClient.getInstance().doSubscribe(checkTokenBind.checkTokenBind(), new BaseObserver<UserEntity>() {
            @Override
            protected void onSuccess(UserEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onCheckTokenBindSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }
}
