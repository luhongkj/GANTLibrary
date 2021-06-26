package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;
import java.util.Map;

public interface SafeguardIntroduceContract
{

    interface View extends BaseMvpView
    {
        void onGetElectricIntroduceSuccess(List<SafeguardIntroduceEntity> dataList);

        void onGetBicycleIntroduceSuccess(List<SafeguardIntroduceEntity> dataList);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getElectricIntroduce(Map<String, Object> bodyParams);

        public abstract void getBicycleIntroduce(Map<String, Object> bodyParams);
    }
}
