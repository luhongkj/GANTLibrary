package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

public interface FlowAccountContract
{

    interface View extends BaseMvpView
    {
        void onFlowAccountInfoSuccess(FlowAccountEntity resultEntity);

        void onBillSuccess(BasePageEntity<FlowBillEntity> dataList);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void getFlowAccountInfo();

        public abstract void getFlowBill(int pageSize, int pageNo);
    }
}
