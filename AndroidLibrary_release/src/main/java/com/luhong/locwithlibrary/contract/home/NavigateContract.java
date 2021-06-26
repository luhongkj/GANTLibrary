package com.luhong.locwithlibrary.contract.home;

import android.content.Context;

import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;


public interface NavigateContract {

    interface View extends BaseMvpView {
        //        void onLocationInitFailure(int errCode);
        //
        //        void onLocationInitSuccess();

        void onTrackInfoRefreshSuccess(TrackInfoRefreshEntity trackInfoRefreshEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        //        public abstract void checkLocationInit(BaseActivity mActivity);

        public abstract void getTrackInfoRefresh(Context context);

    }

}
