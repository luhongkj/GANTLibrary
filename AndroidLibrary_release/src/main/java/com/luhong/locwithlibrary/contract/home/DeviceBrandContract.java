package com.luhong.locwithlibrary.contract.home;


import com.luhong.locwithlibrary.entity.DeviceBrandEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface DeviceBrandContract
{

    interface View extends BaseMvpView
    {
        void onVehicleBrandsSuccess(List<DeviceBrandEntity> dataList);

        void onVehicleTypeSuccess(List<VehicleModelEntity> deviceEntity);

        //        void onVehicleModelsSuccess(List<VehicleModelEntity> deviceEntity);

    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getVehicleBrands(int vehicleType);

        public abstract void getVehicleType();

        //        public abstract void getVehicleModels();
    }

}
