package com.luhong.locwithlibrary.presenter.home;

import android.content.Context;
import android.view.View;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.contract.home.DeviceAddContract;
import com.luhong.locwithlibrary.contract.home.LVehicleChoiceContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.model.home.DeviceBrandModel;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.HomeModel;
import com.luhong.locwithlibrary.model.home.IDeviceBrandModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.model.home.ILVehicleChioce;
import com.luhong.locwithlibrary.model.home.LCehicleChioceMode;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;

/**
 * 车辆添加
 * Created by ITMG on 2020-01-21.
 */
public class LVehicleChoicePresenter extends LVehicleChoiceContract.Presenter {
    ILVehicleChioce ilVehicleChioce;
    private IDeviceManageModel deviceManageModel;
    @Override
    public void getVehicle() {
        if (ilVehicleChioce == null) ilVehicleChioce = new LCehicleChioceMode();
        ApiClient.getInstance().doSubscribe(ilVehicleChioce.getVehicle(), new BaseObserver< List<VehicleListEntity>>() {
            @Override
            protected void onSuccess( List<VehicleListEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onVehicleListSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }


    @Override
    public void getDeviceList(Context context) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getDeviceList(), new BaseObserver<List<DeviceEntity>>() {
            @Override
            protected void onSuccess(List<DeviceEntity> resultEntity, String msg) {
                SPUtils.putObject(context, AppConstants.DEVICE_MANAGE_KEY, resultEntity);
                if (isViewAttached()) baseMvpView.onDeviceListSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }
}
