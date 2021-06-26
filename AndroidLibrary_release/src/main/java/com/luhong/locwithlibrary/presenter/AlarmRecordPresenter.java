package com.luhong.locwithlibrary.presenter;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.AlarmRecordContract;
import com.luhong.locwithlibrary.contract.IServerModel;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.model.ServerModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;

import java.util.Map;

/**
 *
 */
public class AlarmRecordPresenter extends AlarmRecordContract.Presenter
{
    private IServerModel serverModel;

    public AlarmRecordPresenter()
    {
    }

    @Override
    public void getPhoneAlarm(int dataType, Map<String, Object> bodyParams)
    {
        if (serverModel == null) serverModel = new ServerModel();
        ApiClient.getInstance().doSubscribe(serverModel.getPhoneAlarm(dataType, bodyParams), new BaseObserver<BasePageEntity<PhoneAlarmEntity>>()
        {
            @Override
            protected void onSuccess(BasePageEntity<PhoneAlarmEntity> resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onPhoneAlarmSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

}
