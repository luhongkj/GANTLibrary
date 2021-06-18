package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

public interface EFenceContract
{

    interface View extends BaseMvpView
    {
        void onGetVehicleBySnSuccess(DeviceServerEntity installModeEntity);

        void onGetFenceRadiusSuccess(FenceRadiusEntity resultEntity);

        void onSendCommandSuccess(Object resultEntity);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getVehicleBySn(String deviceSn);

        public abstract void getFenceRadius();

        public abstract void sendCommand(Map<String, Object> bodyParams);
    }
}
