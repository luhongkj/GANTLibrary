package com.luhong.locwithlibrary.presenter;


import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.FlowAccountContract;
import com.luhong.locwithlibrary.contract.IFlowAccountModel;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.model.FlowAccountModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;

/**
 *
 */
public class FlowAccountPresenter extends FlowAccountContract.Presenter
{
    private IFlowAccountModel flowAccountModel;

    public FlowAccountPresenter()
    {
    }

    @Override
    public void getFlowAccountInfo()
    {
        if (flowAccountModel == null) flowAccountModel = new FlowAccountModel();
        ApiClient.getInstance().doSubscribe(flowAccountModel.getFlowAccountInfo(), new BaseObserver<FlowAccountEntity>()
        {
            @Override
            protected void onSuccess(FlowAccountEntity resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onFlowAccountInfoSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

    @Override
    public void getFlowBill(int pageSize, int pageNo)
    {
        if (flowAccountModel == null) flowAccountModel = new FlowAccountModel();
        ApiClient.getInstance().doSubscribe(flowAccountModel.getFlowBill(pageSize, pageNo), new BaseObserver<BasePageEntity<FlowBillEntity>>()
        {
            @Override
            protected void onSuccess(BasePageEntity<FlowBillEntity> resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onBillSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });

    }

}
