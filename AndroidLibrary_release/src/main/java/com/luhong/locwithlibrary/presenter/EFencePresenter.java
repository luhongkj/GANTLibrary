package com.luhong.locwithlibrary.presenter;

import android.text.TextUtils;


import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.EFenceContract;
import com.luhong.locwithlibrary.contract.IServerModel;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.model.ICommandModel;
import com.luhong.locwithlibrary.model.ServerModel;
import com.luhong.locwithlibrary.model.home.CommandModel;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.IDeviceManageModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.Map;

/**
 * Created by ITMG on 2020-01-03.
 */
public class EFencePresenter extends EFenceContract.Presenter
{
    private IServerModel serverModel;
    private IDeviceManageModel deviceManageModel;
    private ICommandModel commandModel;

    public EFencePresenter()
    {
    }

    @Override
    public void getFenceRadius()
    {
        if (serverModel == null) serverModel = new ServerModel();
        ApiClient.getInstance().doSubscribe(serverModel.getFenceRadius(), new BaseObserver<FenceRadiusEntity>()
        {
            @Override
            protected void onSuccess(FenceRadiusEntity resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onGetFenceRadiusSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
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

    @Override
    public void sendCommand(Map<String, Object> bodyParams)
    {
        if (commandModel == null) commandModel = new CommandModel();
        ApiClient.getInstance().doSubscribe(commandModel.sendCommand(bodyParams), new BaseObserver<Object>()
        {
            @Override
            protected void onSuccess(Object resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onSendCommandSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }
}
