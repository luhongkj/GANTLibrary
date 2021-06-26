package com.luhong.locwithlibrary.ui.equipment;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.IFlowAccountModel;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.model.FlowAccountModel;
import com.luhong.locwithlibrary.model.RechargeRecordContract;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;

/**
 *
 */
public class RechargeRecordPresenter extends RechargeRecordContract.Presenter
{
    private IFlowAccountModel flowAccountModel;

    public RechargeRecordPresenter()
    {
    }

    @Override
    public void getRechargeRecord(int pageSize, int pageNo)
    {
        if (flowAccountModel == null) flowAccountModel = new FlowAccountModel();
        ApiClient.getInstance().doSubscribe(flowAccountModel.getRechargeRecord(pageSize, pageNo), new BaseObserver<BasePageEntity<RechargeRecordEntity>>()
        {
            @Override
            protected void onSuccess(BasePageEntity<RechargeRecordEntity> resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onRechargeRecordSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

}
