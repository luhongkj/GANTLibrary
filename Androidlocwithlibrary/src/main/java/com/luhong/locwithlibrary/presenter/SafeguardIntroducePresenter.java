package com.luhong.locwithlibrary.presenter;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.SafeguardIntroduceContract;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.model.ISafeguardModel;
import com.luhong.locwithlibrary.model.SafeguardModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;

import java.util.List;
import java.util.Map;

/**
 *
 */
public class SafeguardIntroducePresenter extends SafeguardIntroduceContract.Presenter
{
    private ISafeguardModel safeguardModel;

    public SafeguardIntroducePresenter()
    {
    }

    @Override
    public void getElectricIntroduce(Map<String, Object> bodyParams)
    {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getElectricIntroduce(bodyParams), new BaseObserver<List<SafeguardIntroduceEntity>>()
        {
            @Override
            protected void onSuccess(List<SafeguardIntroduceEntity> resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onGetElectricIntroduceSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

    @Override
    public void getBicycleIntroduce(Map<String, Object> bodyParams)
    {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getBicycleIntroduce(bodyParams), new BaseObserver<List<SafeguardIntroduceEntity>>()
        {
            @Override
            protected void onSuccess(List<SafeguardIntroduceEntity> resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onGetBicycleIntroduceSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });

    }

}
