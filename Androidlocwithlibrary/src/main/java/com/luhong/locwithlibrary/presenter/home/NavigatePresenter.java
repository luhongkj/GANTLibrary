package com.luhong.locwithlibrary.presenter.home;

import android.content.Context;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.home.NavigateContract;
import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.model.home.ITrackModel;
import com.luhong.locwithlibrary.model.home.TrackModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;


/**
 * Created by ITMG on 2019/8/16.
 */
public class NavigatePresenter extends NavigateContract.Presenter
{
    private ITrackModel trackModel;

    public NavigatePresenter()
    {
    }

    //    @Override
    //    public void checkLocationInit(BaseActivity mActivity) {
    //        if (permissionModel == null) permissionModel = new PermissionModel();
    //        permissionModel.checkLocationInit(mActivity, new IRequestListener<Object>() {
    //            @Override
    //            public void onSuccess(Object resultEntity) {
    //                if (isViewAttached()) baseMvpView.onLocationInitSuccess();
    //            }
    //
    //            @Override
    //            public void onFailure(int errCode, String errMsg) {
    //                if (isViewAttached()) baseMvpView.onLocationInitFailure(errCode);
    //            }
    //        });
    //    }

    @Override
    public void getTrackInfoRefresh(Context context)
    {
        if (trackModel == null) trackModel = new TrackModel();
        ApiClient.getInstance().doSubscribe(trackModel.getTrackInfoRefresh(), new BaseObserver<TrackInfoRefreshEntity>()
        {
            @Override
            protected void onSuccess(TrackInfoRefreshEntity resultEntity, String msg)
            {
                if (isViewAttached()) baseMvpView.onTrackInfoRefreshSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult)
            {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }


}
