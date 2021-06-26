package com.luhong.locwithlibrary.presenter;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.SafeguardContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.model.ISafeguardModel;
import com.luhong.locwithlibrary.model.SafeguardModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;

import java.util.List;

/**
 *
 */
public class SafeguardPresenter extends SafeguardContract.Presenter {
    private ISafeguardModel safeguardModel;

    public SafeguardPresenter() {
    }

    @Override
    public void getSafeguardHome() {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getSafeguardHome(), new BaseObserver<List<SafeguardEntity>>() {
            @Override
            protected void onSuccess(List<SafeguardEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onSafeguardHomeSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });

    }

    @Override
    public void getSafeguardMine() {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getSafeguardMine(), new BaseObserver<BasePageEntity<SafeguardEntity>>() {
            @Override
            protected void onSuccess(BasePageEntity<SafeguardEntity> resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onSafeguardMineSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

}
