package com.luhong.locwithlibrary.presenter;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.SafeguardDetailContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.model.ISafeguardModel;
import com.luhong.locwithlibrary.model.SafeguardModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;

import java.util.Map;

/**
 *
 */
public class SafeguardDetailPresenter extends SafeguardDetailContract.Presenter
{
    private ISafeguardModel safeguardModel;

    public SafeguardDetailPresenter()
    {
    }

    @Override
    public void getSafeguardDetail(Map<String, Object> bodyParams)
    {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getSafeguardInfo(bodyParams), new BaseObserver<SafeguardEntity>()
        {
            @Override
            protected void onSuccess(SafeguardEntity resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onSafeguardDetailSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

}
