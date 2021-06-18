package com.luhong.locwithlibrary.presenter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.contract.ClaimOnSiteContract;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.listener.IRequestListener;
import com.luhong.locwithlibrary.model.ClaimModel;
import com.luhong.locwithlibrary.model.IPermissionModel;
import com.luhong.locwithlibrary.model.IUploadModel;
import com.luhong.locwithlibrary.model.UploadModel;
import com.luhong.locwithlibrary.model.home.IClaimModel;
import com.luhong.locwithlibrary.model.home.ITrackModel;
import com.luhong.locwithlibrary.model.home.PermissionModel;
import com.luhong.locwithlibrary.model.home.TrackModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.SPUtils;

import java.util.Map;

/**
 * Created by ITMG on 2018/8/16.
 */
public class ClaimOnSitePresenter extends ClaimOnSiteContract.Presenter {
    private IPermissionModel permissionModel;
    private ITrackModel trackModel;
    private IClaimModel claimModel;
    private IUploadModel uploadModel;

    public ClaimOnSitePresenter() {
    }

    @Override
    public void checkLocationInit(BaseActivity mActivity) {
        if (permissionModel == null) permissionModel = new PermissionModel();
        permissionModel.checkLocationInit(mActivity, new IRequestListener<Object>() {
            @Override
            public void onSuccess(Object resultEntity) {
                if (isViewAttached()) baseMvpView.onLocationInitSuccess();
            }

            @Override
            public void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onLocationInitFailure(errCode);
            }
        });
    }

    @Override
    public void getTrackInfo(Context context, String vehicleId) {
        if (trackModel == null) trackModel = new TrackModel();
        ApiClient.getInstance().doSubscribe(trackModel.getTrackInfo(vehicleId), new BaseObserver<DevicePositionEntity>() {
            @Override
            protected void onSuccess(DevicePositionEntity resultEntity, String msg) {
                if (!TextUtils.isEmpty(AppVariable.currentDeviceId) && vehicleId.equals(AppVariable.currentDeviceId) && resultEntity != null && resultEntity.getLat() != 0.0 && resultEntity.getLon() != 0.0)
                    SPUtils.putObject(context, AppConstants.LAST_DEVICE_POSITION_KEY + AppVariable.currentDeviceId, resultEntity);
                if (isViewAttached()) baseMvpView.onTrackInfoSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void updateClaim(Map<String, Object> bodyParams) {
        if (claimModel == null) claimModel = new ClaimModel();
        ApiClient.getInstance().doSubscribe(claimModel.updateClaim(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onUpdateClaimSuccess();
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
            }
        });
    }

    @Override
    public void uploadFile(Activity context, int position, String filePath) {
        if (uploadModel == null) uploadModel = new UploadModel();
        ApiClient.getInstance().doSubscribe(uploadModel.uploadFile(context, filePath), new BaseObserver<UrlEntity>() {
            @Override
            protected void onSuccess(UrlEntity urlEntity, String msg) {
                if (isViewAttached() && urlEntity != null) {
                    baseMvpView.onUploadSuccess(urlEntity, position);
                }
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(0, errMsg);
            }
        });
    }
}
