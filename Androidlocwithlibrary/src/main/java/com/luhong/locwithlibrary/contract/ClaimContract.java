package com.luhong.locwithlibrary.contract;

import android.app.Activity;

import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.Map;

/**
 * 理赔
 * Created by ITMG on 2020-01-04.
 */
public interface ClaimContract
{

    interface View extends UploadContract.View
    {
        void onGetClaimSuccess(int type, ClaimEntity resultEntity);

        void onSaveClaimSuccess(ClaimEntity resultEntity);

        void onUpdateClaimSuccess(Object resultEntity);

        void onCancelClaimSuccess(Object resultEntity);
    }

    abstract class Presenter extends BasePresenter<View>
    {
        public abstract void uploadFile(Activity context, int picType, int position, String filePath/*List<PicEntity> pathList*/);

        public abstract void getClaim(int type, String claimId);

        public abstract void saveClaim(String insuranceId);

        public abstract void updateClaim(Map<String, Object> bodyParams);

        public abstract void cancelClaim(String claimId);
    }

}
