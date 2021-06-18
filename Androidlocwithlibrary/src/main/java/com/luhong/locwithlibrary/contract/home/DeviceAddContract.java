package com.luhong.locwithlibrary.contract.home;


import android.content.Context;

import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface DeviceAddContract {

    interface View extends BaseMvpView {
        void onVehicleAddSuccess(Object resultEntity);

        void onInstallModeSuccess(InstallModeEntity installModeEntity);

        void onVehicleTypeSuccess(List<VehicleModelEntity> deviceEntity);

        void onVehicleByVinSuccess(VehicleDtoEntity deviceEntity);

        void onVehicleConfirmPaySuccess(Object resultEntity);

        void onGetCodeSuccess(Object resultEntity);

        void onCheckCodeSuccess(Object resultEntity);
        void onCheckTokenBindSuccess(UserEntity resultEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void addVehicle(DeviceEntity deviceEntity, android.view.View view);

        public abstract void getInstallMode(String deviceSn);

        public abstract void getVehicleType();

        public abstract void getVehicleByVin(String vehicleVin);

        public abstract void getVehicleConfirmPay(String deviceSn, float oweFee);

        public abstract void getCode(Context mContext, String phoneNo);

        public abstract void checkCode( String phoneNo, String code);
        public abstract void checkTokenBind();
    }
}
