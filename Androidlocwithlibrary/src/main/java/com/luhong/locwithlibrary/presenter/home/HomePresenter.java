package com.luhong.locwithlibrary.presenter.home;

import android.content.Context;
import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.contract.home.HomeContract;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.listener.IRequestListener;
import com.luhong.locwithlibrary.model.ICommandModel;
import com.luhong.locwithlibrary.model.IPermissionModel;
import com.luhong.locwithlibrary.model.home.CheckTokenBind;
import com.luhong.locwithlibrary.model.home.CommandModel;
import com.luhong.locwithlibrary.model.home.DeviceManageModel;
import com.luhong.locwithlibrary.model.home.HomeModel;
import com.luhong.locwithlibrary.model.home.ICheckTokenBind;
import com.luhong.locwithlibrary.model.home.IHomeModel;
import com.luhong.locwithlibrary.model.home.ILoginModel;
import com.luhong.locwithlibrary.model.home.ISettingModel;
import com.luhong.locwithlibrary.model.home.ITrackModel;
import com.luhong.locwithlibrary.model.home.LoginModel;
import com.luhong.locwithlibrary.model.home.PermissionModel;
import com.luhong.locwithlibrary.model.home.SettingModel;
import com.luhong.locwithlibrary.model.home.TrackModel;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.utils.LoginSuccessUtil;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by ITMG on 2018/8/16.
 */
public class HomePresenter extends HomeContract.Presenter {
    private IHomeModel homeModel;
    private ITrackModel trackModel;
    private DeviceManageModel deviceManageModel;
    private IPermissionModel permissionModel;
    private ICommandModel commandModel;
    private ISettingModel settingModel;
    private ILoginModel loginModel;
    private ICheckTokenBind checkTokenBind;

    public HomePresenter() {
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
    public void login(final Context mContext, final String username, final String password) {
        if (loginModel == null) loginModel = new LoginModel();
        ApiClient.getInstance().doSubscribe(loginModel.login(mContext, username, password), new BaseObserver<UserEntity>(mContext, "测试验证中") {
            @Override
            public void onSuccess(UserEntity resultEntity, String msg) {
                ToastUtil.show("测试验证成功");
                LoginSuccessUtil.onLoginSuccess(mContext, resultEntity);
                if (isViewAttached()) baseMvpView.onLoginSuccess(resultEntity);
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                if (isViewAttached()) baseMvpView.onFailure(errCode, errMsg);
            }
        });
    }

    @Override
    public void getHomeData() {
        if (!AppVariable.GIANT_ISBIN) {//是否绑定鹿卫士设备
            return;
        }
        if (homeModel == null) homeModel = new HomeModel();
        ApiClient.getInstance().doSubscribe(homeModel.getHomeData(), new BaseObserver<HomeDataEntity>() {
            @Override
            protected void onSuccess(HomeDataEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onHomeDataSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                //                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

    @Override
    public void getTrackInfo(Context context) {
        if (trackModel == null) trackModel = new TrackModel();
        ApiClient.getInstance().doSubscribe(trackModel.getTrackInfo(AppVariable.currentVehicleId), new BaseObserver<DevicePositionEntity>() {
            @Override
            protected void onSuccess(DevicePositionEntity resultEntity, String msg) {
                if (!TextUtils.isEmpty(AppVariable.currentDeviceId) && resultEntity != null && resultEntity.getLat() != 0.0 && resultEntity.getLon() != 0.0) {
                    SPUtils.putObject(context, AppConstants.LAST_DEVICE_POSITION_KEY + AppVariable.currentDeviceId, resultEntity);
                } else {
                    SPUtils.removeByKey(context, AppConstants.LAST_DEVICE_POSITION_KEY + AppVariable.currentDeviceId);
                }
                if (isViewAttached()) baseMvpView.onTrackInfoSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(1, errResult);
            }
        });
    }

    @Override
    public void sendCommand(Map<String, Object> bodyParams) {
        if (commandModel == null) commandModel = new CommandModel();
        ApiClient.getInstance().doSubscribe(commandModel.sendCommand(bodyParams), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onSendCommandSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(2, errResult);
            }
        });
    }

    @Override
    public void activeBySn(String activateSn) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.activeVehicleBySn(activateSn), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                ToastUtil.show("设备激活成功");
                if (isViewAttached()) baseMvpView.onActiveBySnSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(3, errResult);
            }
        });
    }

    @Override
    public void getSetting(final Context context) {
        if (settingModel == null) settingModel = new SettingModel();
        ApiClient.getInstance().doSubscribe(settingModel.getSetting(), new BaseObserver<List<SettingEntity>>() {
            @Override
            protected void onSuccess(List<SettingEntity> resultEntity, String msg) {
                SPUtils.putObject(context, AppConstants.SETTING_KEY + AppVariable.currentVehicleId, resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
            }
        });
    }

    @Override
    public void getVehicleConfirmPay(String deviceSn, float oweFee) {
        if (deviceManageModel == null) deviceManageModel = new DeviceManageModel();
        ApiClient.getInstance().doSubscribe(deviceManageModel.getVehicleConfirmPay(deviceSn, oweFee), new BaseObserver<Object>() {
            @Override
            protected void onSuccess(Object resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onVehicleConfirmPaySuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(4, errResult);
            }
        });
    }
    @Override
    public void checkTokenBind() {
        if (checkTokenBind == null) checkTokenBind = new CheckTokenBind();
        ApiClient.getInstance().doSubscribe(checkTokenBind.checkTokenBind(), new BaseObserver<UserEntity>() {
            @Override
            protected void onSuccess(UserEntity resultEntity, String msg) {
                if (isViewAttached()) baseMvpView.onCheckTokenBindSuccess(resultEntity);
            }

            @Override
            public void onFailure(int errCode, String errResult) {
                if (isViewAttached()) baseMvpView.onFailure(0, errResult);
            }
        });
    }

    @Override
    public void getDeviceList(Context context) {

    }
}
