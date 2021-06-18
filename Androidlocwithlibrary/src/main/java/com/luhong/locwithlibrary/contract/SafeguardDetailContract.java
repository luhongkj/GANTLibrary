package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

public interface SafeguardDetailContract
{

    interface View extends BaseMvpView
    {
        void onSafeguardDetailSuccess(SafeguardEntity resultEntity);

    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getSafeguardDetail(Map<String, Object> bodyParams);

    }
}
