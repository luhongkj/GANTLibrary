package com.luhong.locwithlibrary.presenter;

import android.app.Activity;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.SafeguardEditContract;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.model.ISafeguardModel;
import com.luhong.locwithlibrary.model.IUploadModel;
import com.luhong.locwithlibrary.model.SafeguardModel;
import com.luhong.locwithlibrary.model.UploadModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.Map;

/**
 *
 */
public class SafeguardEditPresenter extends SafeguardEditContract.Presenter {
    private ISafeguardModel safeguardModel;
    private IUploadModel uploadModel;


    public SafeguardEditPresenter() {
    }

    @Override
    public void uploadFile(Activity context, final int dataType, String filePath/*List<PicEntity> pathList*/) {
        if (uploadModel == null) uploadModel = new UploadModel();
        ApiClient.getInstance().doSubscribe(uploadModel.uploadFile(context, filePath/*pathList*/), new BaseObserver<UrlEntity>() {
            @Override
            protected void onSuccess(UrlEntity dataList, String msg) {
                if (isViewAttached()) baseMvpView.onUploadSuccess(dataType, dataList);
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(0, errMsg);
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

    @Override
    public void saveSafeguard(Map<String, Object> bodyParams) {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.saveSafeguard(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("提交成功");
                if (isViewAttached()) baseMvpView.onSaveSafeguardSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });

    }

    @Override
    public void updateSafeguard(Map<String, Object> bodyParams) {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.updateSafeguard(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("提交成功");
                if (isViewAttached()) baseMvpView.onUpdateSafeguardSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
            }
        });
    }

    @Override
    public void getSafeguardInfo(Map<String, Object> bodyParams) {
        if (safeguardModel == null) safeguardModel = new SafeguardModel();
        ApiClient.getInstance().doSubscribe(safeguardModel.getSafeguardInfo(bodyParams), new BaseObserver<SafeguardEntity>() {
            @Override
            protected void onSuccess(SafeguardEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onGetSafeguardInfoSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(4, errResult);
            }
        });
    }


}
