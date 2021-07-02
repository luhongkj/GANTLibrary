package com.luhong.locwithlibrary.presenter;

import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.PhoneAlarmContract;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.ToastUtil;

/**
 *
 */
public class PhoneAlarmPresenter extends PhoneAlarmContract.Presenter
{
    private IDeviceManageModel deviceManageModel;

    public PhoneAlarmPresenter()
    {
    }

    @Override
    public void getVehicleBySn(String deviceSn)
    {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getVehicleBySn(deviceSn), new BaseObserver<DeviceServerEntity>()
        {
            @Override
            protected void onSuccess(DeviceServerEntity resultEntity, String msg)
            {
                if (!TextUtils.isEmpty(msg)) ToastUtil.show(msg);
                if (isViewAttached()) baseMvpView.onGetVehicleBySnSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

}
