package com.luhong.locwithlibrary.contract.home;


import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface LVehicleChoiceContract {

    interface View extends BaseMvpView {
        void onVehicleListSuccess( List<VehicleListEntity> resultEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void getVehicle();
    }
}
