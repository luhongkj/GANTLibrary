package com.luhong.locwithlibrary.presenter;

import android.app.Activity;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.ClaimContract;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.model.ClaimModel;
import com.luhong.locwithlibrary.model.IUploadModel;
import com.luhong.locwithlibrary.model.UploadModel;
import com.luhong.locwithlibrary.model.home.IClaimModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.Map;

/**
 * Created by ITMG on 2020-01-04.
 */
public class ClaimPresenter extends ClaimContract.Presenter {
    private IClaimModel claimModel;
    private IUploadModel uploadModel;

    public ClaimPresenter() {
    }

    @Override
    public void uploadFile(Activity context, int picType, int position, String filePath/*List<PicEntity> pathList*/) {
        if (uploadModel == null) uploadModel = new UploadModel();
        ApiClient.getInstance().doSubscribe(uploadModel.uploadFile(context, filePath/*pathList*/), new BaseObserver<UrlEntity>() {
            @Override
            protected void onSuccess(UrlEntity urlEntity, String msg) {
                if (isViewAttached() && urlEntity != null) {
                    urlEntity.setPicType(picType);
                    baseMvpView.onUploadSuccess(urlEntity, position);
                }
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(0, errMsg);
            }
        });
    }

    @Override
    public void getClaim(int type, String claimId) {
        if (claimModel == null) claimModel = new ClaimModel();
        ApiClient.getInstance().doSubscribe(claimModel.getClaim(claimId), new BaseObserver<ClaimEntity>() {
            @Override
            protected void onSuccess(ClaimEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onGetClaimSuccess(type, resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void saveClaim(String insuranceId) {
        if (claimModel == null) claimModel = new ClaimModel();
        ApiClient.getInstance().doSubscribe(claimModel.saveClaim(insuranceId), new BaseObserver<ClaimEntity>() {
            @Override
            protected void onSuccess(ClaimEntity resultEntity, String msg) {
                ToastUtil.show("报失成功");
                if (isViewAttached()) baseMvpView.onSaveClaimSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }


    @Override
    public void updateClaim(Map<String, Object> bodyParams) {
        if (claimModel == null) claimModel = new ClaimModel();
        ApiClient.getInstance().doSubscribe(claimModel.updateClaim(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("提交成功");
                if (isViewAttached()) baseMvpView.onUpdateClaimSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
            }
        });
    }

    @Override
    public void cancelClaim(String claimId) {
        if (claimModel == null) claimModel = new ClaimModel();
        ApiClient.getInstance().doSubscribe(claimModel.cancelClaim(claimId), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("取消成功");
                if (isViewAttached()) baseMvpView.onCancelClaimSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(4, errResult);
            }
        });
    }
}
