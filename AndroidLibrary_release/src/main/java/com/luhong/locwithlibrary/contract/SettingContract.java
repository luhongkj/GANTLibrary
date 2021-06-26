package com.luhong.locwithlibrary.contract;


import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;

public interface SettingContract
{

    interface View extends BaseMvpView
    {
        void onDeviceListSuccess(List<DeviceEntity> dataList);
        
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getDeviceList();
    }
}
