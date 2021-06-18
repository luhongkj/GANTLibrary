package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

public interface AlarmRecordContract
{

    interface View extends BaseMvpView
    {
        void onPhoneAlarmSuccess(BasePageEntity<PhoneAlarmEntity> dataList);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getPhoneAlarm(int dataType, Map<String, Object> bodyParams);
    }

}
