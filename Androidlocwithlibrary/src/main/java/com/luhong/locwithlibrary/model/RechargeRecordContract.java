package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

public interface RechargeRecordContract
{

    interface View extends BaseMvpView
    {
        void onRechargeRecordSuccess(BasePageEntity<RechargeRecordEntity> dataList);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getRechargeRecord(int pageSize, int pageNo);
    }

}
