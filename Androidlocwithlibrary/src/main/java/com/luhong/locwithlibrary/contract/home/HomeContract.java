package com.luhong.locwithlibrary.contract.home;

import android.content.Context;

import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.HistoryInfoEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.BasePresenter;

import java.util.List;
import java.util.Map;

public interface HomeContract {

    interface View extends BaseMvpView {
        void onLocationInitFailure(int errCode);

        void onLocationInitSuccess();

        void onHomeDataSuccess(HomeDataEntity resultEntity);

        void onTrackInfoSuccess(DevicePositionEntity dataList);

        void onSendCommandSuccess(Object resultEntity);

        void onActiveBySnSuccess(Object resultEntity);

        void onLoginSuccess(UserEntity userEntity);

        void onVehicleConfirmPaySuccess(Object resultEntity);

        void onCheckTokenBindSuccess(UserEntity resultEntity);

        void onDeviceListSuccess(List<DeviceEntity> dataList);
        void onVehicleListSuccess( List<VehicleListEntity> resultEntity);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void checkLocationInit(BaseActivity mActivity);

        public abstract void getHomeData();

        public abstract void getTrackInfo(Context context);

        public abstract void sendCommand(Map<String, Object> bodyParams);

        public abstract void getSetting(Context context);

        public abstract void activeBySn(String activateSn);

        public abstract void getVehicleConfirmPay(String deviceSn, float oweFee);

        public abstract void login(Context mContext, String username, String password);

        public abstract void checkTokenBind();

        public abstract void getDeviceList(Context context);

        public abstract void getVehicle();
    }

}
