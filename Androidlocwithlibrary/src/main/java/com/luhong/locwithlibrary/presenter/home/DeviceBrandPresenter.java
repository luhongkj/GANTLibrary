package com.luhong.locwithlibrary.presenter.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.home.DeviceBrandContract;
import com.luhong.locwithlibrary.entity.DeviceBrandEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.model.home.DeviceBrandModel;
import com.luhong.locwithlibrary.model.home.IDeviceBrandModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;

import java.util.Collections;
import java.util.List;

/**
 * 车辆品牌
 * Created by ITMG on 2020-01-21.
 */
public class DeviceBrandPresenter extends DeviceBrandContract.Presenter {
    private IDeviceBrandModel deviceBrandModel;

    public DeviceBrandPresenter() {
    }

    @Override
    public void getVehicleBrands(int vehicleType) {
        if (deviceBrandModel == null) deviceBrandModel = new DeviceBrandModel();
        ApiClient.getInstance().doSubscribe(deviceBrandModel.getVehicleBrands(vehicleType), new BaseObserver<List<DeviceBrandEntity>>() {
            @Override
            protected void onSuccess(List<DeviceBrandEntity> dataList, String msg) {
                if (isViewAttached()) {
                    if (dataList != null && dataList.size() > 1)
                        Collections.sort(dataList); // 按通过率排序
                    baseMvpView.onVehicleBrandsSuccess(dataList);
                }
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
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
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }
}
