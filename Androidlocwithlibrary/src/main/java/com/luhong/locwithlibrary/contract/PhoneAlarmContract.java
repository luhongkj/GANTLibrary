package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.text.ParseException;

public interface PhoneAlarmContract
{

    interface View extends BaseMvpView
    {
        void onGetVehicleBySnSuccess(DeviceServerEntity deviceServerEntity) ;
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getVehicleBySn(String deviceSn);
    }

}
