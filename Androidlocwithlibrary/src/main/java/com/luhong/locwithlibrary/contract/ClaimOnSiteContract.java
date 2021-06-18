package com.luhong.locwithlibrary.contract;

import android.app.Activity;
import android.content.Context;

import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

/**
 * 理赔拍照
 * Created by ITMG on 2020-01-04.
 */
public interface ClaimOnSiteContract
{

    interface View extends UploadContract.View
    {

        void onLocationInitFailure(int type);

        void onLocationInitSuccess();

        void onTrackInfoSuccess(DevicePositionEntity dataList);

        void onUpdateClaimSuccess();
    }

    abstract class Presenter extends BasePresenter<View>
    {

        public abstract void checkLocationInit(BaseActivity mActivity);

        public abstract void getTrackInfo(Context context, String vehicleId);

        public abstract void updateClaim(Map<String, Object> bodyParams);

        public abstract void uploadFile(Activity context, int position, String filePath);
    }

}
