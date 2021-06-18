package com.luhong.locwithlibrary.presenter.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.SettingContract;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;

import java.util.List;

/**
 * Created by ITMG on 2018/8/16.
 */
public class SettingPresenter extends SettingContract.Presenter {
    private IDeviceManageModel deviceManageModel;

    public SettingPresenter() {
    }

    @Override
    public void getDeviceList() {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getDeviceList(), new BaseObserver<List<DeviceEntity>>() {
            @Override
            protected void onSuccess(List<DeviceEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onDeviceListSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }
}
