package com.luhong.locwithlibrary.ui;


import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.weather.LocalWeatherForecastResult;
import com.amap.api.services.weather.LocalWeatherLive;
import com.amap.api.services.weather.LocalWeatherLiveResult;
import com.amap.api.services.weather.WeatherSearch;
import com.amap.api.services.weather.WeatherSearchQuery;
import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.HomeContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.DevicePromptDialog;
import com.luhong.locwithlibrary.dialog.NavigateTypeDialog;
import com.luhong.locwithlibrary.entity.ArrearsEvent;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.event.NetworkEvent;
import com.luhong.locwithlibrary.listener.IRequestListener;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.model.IPermissionModel;
import com.luhong.locwithlibrary.presenter.BaseMvpView;
import com.luhong.locwithlibrary.presenter.home.HomePresenter;
import com.luhong.locwithlibrary.presenter.server.LocationService;
import com.luhong.locwithlibrary.ui.equipment.SettingActivity;
import com.luhong.locwithlibrary.ui.my.MyActivity;
import com.luhong.locwithlibrary.utils.AMapUtil;
import com.luhong.locwithlibrary.utils.AppUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.LoginSuccessUtil;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.luhong.locwithlibrary.utils.WeatherUtil;
import com.zyq.easypermission.EasyPermissionResult;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_1;
import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_2;
import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_3;
import static com.luhong.locwithlibrary.ui.equipment.LDeviceAddActivity.ADDDEVICE_RESULT_;

public class LHomeActivity extends BaseMvpActivity<HomePresenter> implements BaseMvpView, AMap.OnMarkerClickListener, AMap.InfoWindowAdapter, HomeContract.View, ServiceConnection {
    public static final String GIANT_TOKEN_HOME = "GIANT_TOKEN_HOME";
    public static final String GIANT_PHONE_HOME = "GIANT_PHONE_HOME";

    public final int TYPE_INIT = -1;//初始状态
    public final int TYPE_NORMAL = 0;//未绑定设备状态
    private final int TYPE_FORTIFICATION = 1;//设防状态
    private final int TYPE_UNFORTIFICATION = 2;//解防状态
    public int dataType = TYPE_INIT;//当前设防状态

    @BindView(R2.id.mapView_home)
    MapView mapView;
    @BindView(R2.id.tv_weather_home)
    TextView tv_weather;
    @BindView(R2.id.iv_myLocation_home)
    ImageView iv_myLocation;
    @BindView(R2.id.iv_deviceLocation_home)
    ImageView iv_deviceLocation;
    @BindView(R2.id.iv_follow_home)
    ImageView iv_follow;
    @BindView(R2.id.iv_message_home)
    ImageView iv_message;
    @BindView(R2.id.iv_setting_home)
    ImageView iv_setting;
    @BindView(R2.id.rl_addVehicle_home)
    RelativeLayout rl_addVehicle;
    @BindView(R2.id.tv_addVehicle_home)
    TextView tv_addVehicle;
    private LocationService.LocationBinder locationBinder;
    private AMap mAMap;
    private UiSettings mUiSettings;
    private Marker growMarker = null;
    private MyLocationStyle myLocationStyle;
    private WeatherSearchQuery weatherSearchQuery;
    private WeatherSearch weatherSearch;
    private LatLng phoneLatLng, deviceLatLng;
    private boolean isFollowMove = true, isWeather = false, isUpdateLatlng = false;
    private boolean isRefresh;
    private boolean isHomeInitSuccess, isActivated, isOweFee;
    private volatile DevicePositionEntity positionEntity;
    private DevicePromptDialog devicePromptDialog;
    private String currentCity;
    //    private File textFile;

    @Override
    protected void initData() {
        AppVariable.GIANT_TOKEN = getIntent().getStringExtra(GIANT_TOKEN_HOME);
        AppVariable.GIANT_PHONE = getIntent().getStringExtra(GIANT_PHONE_HOME);
        SPUtils.put(this, BaseConstants.TOKEN, AppVariable.GIANT_TOKEN);
        currentCity = SPUtils.getString(mActivity, AppConstants.CURRENT_CITY, "深圳市");
        String latLng = SPUtils.getString(mActivity, "phoneLatLng", "");
        if (!TextUtils.isEmpty(latLng)) {
            String[] split = latLng.split("/");
            if (split != null && split.length > 1)
                phoneLatLng = new LatLng(Double.valueOf(split[0]), Double.valueOf(split[1]));
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_lhome;
    }

    @SuppressLint("NewApi")
    @Override
    protected void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                String systemModel = AppUtils.getSystemModel();
                if (!TextUtils.isEmpty(systemModel) && systemModel.contains("MP1512")) {
                    boolean isFirst = (boolean) SPUtils.get(mActivity, "systemModel", true);
                    if (isFirst) {
                        requestAllPermissions();
                        SPUtils.put(mActivity, "systemModel", false);
                    }
                } else {
                    requestAllPermissions();
                }
            }
        }, 1500);

        initMap();
        loadCache();
        LocationService.bindService(mActivity, this);
        searchLiveWeather(currentCity);
        initTitleView(true, "鹿卫士", "我的", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startIntentActivity(MyActivity.class);
            }
        });
    }

    @Override
    public void fetchData() {
        SPUtils.put(this, AppVariable.GIANT_TOKEN, BaseConstants.TOKEN);
        //判断是否注册或使用过鹿卫士设备
        mPresenter.checkTokenBind();
        //登录,获取token
    }

    //没有登录,15秒不刷新一次车辆数据
    @SuppressLint("NewApi")
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (!TextUtils.isEmpty(AppVariable.currentVehicleId)) {
                mPresenter.getHomeData();
                mPresenter.getTrackInfo(mActivity);
                if (dataType == TYPE_UNFORTIFICATION) {
                    handler.removeMessages(0);
                    handler.sendEmptyMessageDelayed(0, 15000);
                }
            }
            return false;
        }
    });

    @Override
    protected void onEventListener() {
        //查看轨迹
        iv_message.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                if (dataType <= TYPE_NORMAL) {
                    showToast("未绑定车辆");
                    return;
                }
                startIntentActivity(LSportRecordActivity.class);
            }
        });
        iv_setting.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                if (dataType <= TYPE_NORMAL) {
                    showToast("未绑定车辆");
                    return;
                }
                startIntentActivity(SettingActivity.class);
            }
        });

        iv_myLocation.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                Logger.error("点击手机位置");
                isFollowMove = true;
                isWeather = false;
                mPresenter.checkLocationInit(mActivity);
            }
        });
        iv_deviceLocation.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                if (dataType == TYPE_NORMAL) {
                    showToast("未绑定车辆");
                    return;
                }
                isRefresh = false;
                isFollowMove = false;
                mPresenter.getHomeData();
            }
        });
        iv_follow.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (!AppVariable.GIANT_ISBIN) {
                    ToastUtil.show("请添加设备");
                    return;
                }
                if (dataType <= TYPE_NORMAL) {
                    showToast("未绑定车辆");
                    return;
                }
                if (!isActivated) {//未激活
                    mPresenter.getHomeData();
                    return;
                }
                if (isOweFee) {//欠费
                    mPresenter.getHomeData();
                    return;
                }
                if (deviceLatLng == null || deviceLatLng.latitude == 0.0 || deviceLatLng.longitude == 0.0) {
                    mPresenter.getTrackInfo(mActivity);
                    return;
                }
                if (!isUpdateLatlng || phoneLatLng == null || phoneLatLng.latitude == 0.0 || phoneLatLng.longitude == 0.0) {
                    mPresenter.checkLocationInit(mActivity);
                    return;
                }
                if (!AMapUtil.isLocServiceEnable(mActivity)) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, AppConstants.REQUEST_CODE_OPEN_GPS);
                    return;
                }
                NavigateTypeDialog.getInstance(mActivity).showDialog(new NavigateTypeDialog.INavigateListener() {
                    @Override
                    public void onNavigateType(int type) {
                        Bundle bundle = new Bundle();
                        bundle.putInt(AppConstants.dataTypeKey, type);
                        if (positionEntity != null)
                            bundle.putInt("trackTime", positionEntity.getTrackTime());
                        bundle.putDouble(AppConstants.deviceLatKey, deviceLatLng.latitude);
                        bundle.putDouble(AppConstants.deviceLngKey, deviceLatLng.longitude);
                        bundle.putDouble(AppConstants.phoneLatKey, phoneLatLng.latitude);
                        bundle.putDouble(AppConstants.phoneLngKey, phoneLatLng.longitude);
                        startIntentActivityForResult(NavigateActivity.class, AppConstants.REQUEST_CODE_NAVIGATE, bundle);
                    }
                });
            }
        });

        tv_addVehicle.setOnClickListener(new SingleClickListener() {
            @RequiresApi(api = 30)
            @Override
            public void onSingleClick(View v) {
                if (dataType <= TYPE_NORMAL) {
                    mActivity.requestStorageAndCameraPermissions(new EasyPermissionResult() {
                        @Override
                        public void onPermissionsAccess(int requestCode) {
                            super.onPermissionsAccess(requestCode);
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("isHome", true);
                            startIntentActivityForResult(LVehiclechoiceActivity.class, CaptureActivity.resultDecode,bundle);
                        }

                        @Override
                        public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions) {
                            super.onPermissionsDismiss(requestCode, permissions);
                            showToast("没有权限");
                        }
                    });
                } else {
                    if (!isActivated) {//未激活
                        mPresenter.getHomeData();
                        return;
                    }
                    if (isOweFee) {//欠费
                        mPresenter.getHomeData();
                        return;
                    }
                    showLoading("提交中...");
                    Map<String, Object> bodyParams = new HashMap<>();
                    bodyParams.put("vehicleId", AppVariable.currentVehicleId);
                    bodyParams.put("flowId", "A" + System.currentTimeMillis());
                    isElectrocar = false;
                    if (dataType == TYPE_FORTIFICATION) {//当前设防状态则解防
                        bodyParams.put("msgId", AppConstants.MSG_UNFORTIFICATION);
                        mPresenter.sendCommand(bodyParams);
                    } else if (dataType == TYPE_UNFORTIFICATION) {//当前解防状态则设防
                        bodyParams.put("msgId", AppConstants.MSG_FORTIFICATION);
                        mPresenter.sendCommand(bodyParams);
                    }
                }
            }
        });
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        locationBinder = (LocationService.LocationBinder) service;
        locationBinder.startLocation(locationServiceListener, 500);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        if (locationBinder != null) locationBinder = null;
    }

    @Override
    public void onLocationInitSuccess() {
        if (phoneLatLng != null)
            mAMap.animateCamera(CameraUpdateFactory.newLatLng(phoneLatLng));// 设置指定的可视区域地图
    }

    @Override
    public void onLocationInitFailure(int errCode) {
        Logger.error("定位未就绪");
        if (errCode == BaseConstants.TYPE_NOT_OPEN_GPS) {//打开位置服务
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, AppConstants.REQUEST_CODE_OPEN_GPS);
        } else if (errCode == BaseConstants.TYPE_NOT_FINE_LOCATION) {//打开权限界面
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
            intent.setData(uri);
            try {
                mActivity.startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActiveBySnSuccess(Object resultEntity) {
        if (!AppVariable.GIANT_ISBIN) {
            return;
        }
        mPresenter.getHomeData();
        cancelDevicePromptDialog();
    }

    //登录成功
    @Override
    public void onLoginSuccess(UserEntity userEntity) {
    }

    HomeDataEntity resultEntitys;

    @Override
    public void onHomeDataSuccess(HomeDataEntity resultEntity) {
        mAMap.clear();
        if (resultEntity == null) {
            updateView(TYPE_NORMAL);
            return;
        }
        this.resultEntitys = resultEntity;
        isHomeInitSuccess = true;
        boolean isMatch = false;
        List<HomeDataEntity.Vehicle> vehicleList = resultEntity.getVehicles();
        if (vehicleList != null && vehicleList.size() != 0) {
            for (HomeDataEntity.Vehicle vehicle : vehicleList) {
                if (vehicle == null || TextUtils.isEmpty(vehicle.getSn()) || TextUtils.isEmpty(vehicle.getId()))
                    continue;
                //当前选中的这边为空AppVariable.currentDeviceId
                if (!TextUtils.isEmpty(AppVariable.currentDeviceId) && vehicle.getSn().equals(AppVariable.currentDeviceId)) {//判断设备是否是当前选中的
                    selectVehicle(vehicle, resultEntity.getFirstActiveCost(), resultEntity.getFlowDefaultCost());//getFlowDefaultCost
                    isMatch = true;
                    break;
                }
            }
            if (!isMatch) {
                selectVehicle(vehicleList.get(0), resultEntity.getFirstActiveCost(), resultEntity.getFlowDefaultCost());//getFlowDefaultCost
            }
            mPresenter.getSetting(mActivity);
        } else {
            updateView(TYPE_NORMAL);
        }
    }

    HomeDataEntity.Vehicle vehicle;

    /**
     * 选择设备
     *
     * @param vehicle
     * @param firstActiveCost
     */
    private void selectVehicle(HomeDataEntity.Vehicle vehicle, float firstActiveCost, float flowDefaultCost) {
        this.vehicle = vehicle;
        AppVariable.currentDeviceId = vehicle.getSn();
        SPUtils.put(mActivity, "currentDeviceId", AppVariable.currentDeviceId);
        AppVariable.currentVehicleId = vehicle.getId();
        if (vehicle.getUnitStatus() == HomeDataEntity.Vehicle.TYPE_INACTIVATED) {//未激活
            isActivated = false;
            deviceLatLng = null;
            mAMap.clear();
            updateView(TYPE_UNFORTIFICATION);
            showDevicePromptDialog(DevicePromptDialog.TYPE_ACTIVATING, vehicle.getSn(), firstActiveCost, flowDefaultCost);
        } else {//1已激活
            isActivated = true;
            if (vehicle.getOweFee() < 0) {//欠费
                isOweFee = true;
                deviceLatLng = null;
                mAMap.clear();
                updateView(TYPE_UNFORTIFICATION);
                Logger.error("1582080获取主页数据");
                showDevicePromptDialog(vehicle.getOweFeeType() == 1 ? DevicePromptDialog.TYPE_ARREARS_CONFIRM : DevicePromptDialog.TYPE_ARREARS, vehicle.getSn(), vehicle.getOweFee(), flowDefaultCost);
            } else {//获取位置信息
                isOweFee = false;
                deviceLatLng = null;
                mPresenter.getTrackInfo(mActivity);
            }
        }
    }

    @Override
    public void onTrackInfoSuccess(DevicePositionEntity positionEntity) {
        this.positionEntity = positionEntity;
        updateDevicePosition(false);
    }

    boolean isElectrocar = false;//false为响应式    true 为解/设防  指令发送不等待响应

    //指令传输结果
    @Override
    public void onSendCommandSuccess(Object dataList) {
        if (isElectrocar) return;
        cancelLoading();
        if (dataType == TYPE_FORTIFICATION) {//4设防
            showToast("解防成功");
            updateView(TYPE_UNFORTIFICATION);
        } else if (dataType == TYPE_UNFORTIFICATION) {//5解防
            showToast("设防成功");
            updateView(TYPE_FORTIFICATION);
        }
    }


    @Override
    public void onVehicleConfirmPaySuccess(Object resultEntity) {//弃用
        //        showToast("补缴成功");
        mPresenter.getHomeData();
        showDevicePromptDialog(DevicePromptDialog.TYPE_PAY_ARREARS_SUCCESS, "", 0, 0);
    }

    //校验是否使用鹿卫士
    @Override
    public void onCheckTokenBindSuccess(UserEntity result) {
        AppVariable.GIANT_ISBIN = result.getBind();
        LocationService.bindService(mActivity, this);
        mPresenter.checkLocationInit(mActivity);
        if (result.getBind()) {
            LoginSuccessUtil.onLoginSuccess(this, result);
            mPresenter.getHomeData();
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        Logger.error("onFailure执行？？errType=" + errType);
        cancelLoading();
        if (errType == 1) {
            //            onTrackInfoSuccess(null);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Logger.error("执行？requestCode= " + requestCode);
        if (requestCode == ADDDEVICE_RESULT_) {//添加设备
            mPresenter.getHomeData();
        } else if (requestCode == AppConstants.REQUEST_CODE_ADD) {
            mPresenter.getHomeData();
        } else if (resultCode == FeesPayActivity.RECHARGE_SUCCESS_CODE) {
            mPresenter.getHomeData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    float yue = resultEntitys.getAccountMoney() / resultEntitys.getFlowDefaultCost();//流量账号余额///月租
                    showDevicePromptDialog(DevicePromptDialog.TYPE_PAY_ARREARS_SUCCESS, "", yue, 0);
                }
            }, 2000);
        } else if (resultCode == ActivatePayActivity.TYPE_ACTIVE) {
            mPresenter.getHomeData();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    float yue = resultEntitys.getAccountMoney() / resultEntitys.getFlowDefaultCost();//流量账号余额///月租
                    showDevicePromptDialog(DevicePromptDialog.TYPE_PAY_ACTIVE_SUCCESS, "", yue, 0);
                }
            }, 2000);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(ArrearsEvent eventMessage) {//欠费/补缴成功
        if (eventMessage == null) return;
        Logger.error("欠费/补缴ArrearsEvent=" + eventMessage.getType());
        if (eventMessage.getType() == ArrearsEvent.TYPE_ARREARS) {//欠费
            //  cancelDevicePromptDialog();
            updateView(TYPE_UNFORTIFICATION);
            //  mPresenter.getHomeData();
        } else if (eventMessage.getType() == ArrearsEvent.TYPE_REPAY) {//补缴成功

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscribeEvent(NetworkEvent networkEvent) {//网络监听
        Logger.error("网络变化=" + networkEvent.getNetType());
        if (networkEvent.getNetType() >= NetworkEvent.NETWORK_MOBILE) {
            if (!isHomeInitSuccess)
                reFetchData();
        }
    }

    private void cancelDevicePromptDialog() {
        if (devicePromptDialog != null && devicePromptDialog.isShowing())
            devicePromptDialog.cancel();
    }

    /**
     * 重新绑定数据
     */
    public void reFetchData() {
        if (!AppVariable.GIANT_ISBIN) {
            return;
        }
        loadCache();
        mPresenter.getHomeData();
    }

    /**
     * @param type
     * @param activateSn   設備SN
     * @param amount       缴费费用
     * @param serverLength 每月费用
     */
    private void showDevicePromptDialog(int type, String activateSn, float amount,
                                        float serverLength) {
        cancelDevicePromptDialog();
        devicePromptDialog = DevicePromptDialog.getInstance(mActivity);
        devicePromptDialog.showDialog(type, AppVariable.currentDeviceId, "" + Math.abs(amount), "" + serverLength, new BaseDialog.IEventListener() {
            @Override
            public void onConfirm() {
                if (type == DevicePromptDialog.TYPE_ACTIVATING) {
                    //                    mPresenter.activeBySn(activateSn);//需要激活的设备sn
                    Bundle bundle = new Bundle();
                    bundle.putString("SN", activateSn);
                    bundle.putString("money", amount + "");
                    startIntentActivityForResult(ActivatePayActivity.class, ActivatePayActivity.TYPE_ACTIVE, bundle);
                } else {
                    if (type == DevicePromptDialog.TYPE_ARREARS_CONFIRM) {//1确认补缴
                        mPresenter.getVehicleConfirmPay(AppVariable.currentDeviceId, amount);
                    } else if (type == DevicePromptDialog.TYPE_ARREARS) {//2立即补缴
                        Bundle bundle = new Bundle();
                        bundle.putString("SN", activateSn);
                        bundle.putString("money", "" + Math.abs(amount));
                        bundle.putString("serverLength", "" + serverLength);
                        startIntentActivityForResult(FeesPayActivity.class, FeesPayActivity.RECHARGE_SUCCESS_CODE, bundle);
                    }
                }
            }

            @Override
            public void onCancel() {

            }
        });
    }

    //获取缓存数据
    private void loadCache() {
        positionEntity = SPUtils.getObject(mActivity,
                AppConstants.LAST_DEVICE_POSITION_KEY + AppVariable.currentDeviceId,
                new TypeToken<DevicePositionEntity>() {
                }.getType());
        updateDevicePosition(true);
    }

    /**
     * @param isCache 缓存
     */
    private void updateDevicePosition(boolean isCache) {
        if (positionEntity == null) {
            deviceLatLng = null;
            if (!isCache) {
                mAMap.clear();
                updateView(TYPE_UNFORTIFICATION);//默认解防状态
            }
            return;
        }

        String safy = positionEntity.getSafy();
        if (!TextUtils.isEmpty(safy) && safy.equals(DevicePositionEntity.TYPE_FORTIFICATION)) {
            updateView(TYPE_FORTIFICATION);
        } else if (!TextUtils.isEmpty(safy) && safy.equals(DevicePositionEntity.TYPE_UNFORTIFICATION)) {
            updateView(TYPE_UNFORTIFICATION);
        } else {
            updateView(TYPE_UNFORTIFICATION);//默认解防状态
        }
    }

    @SuppressLint("NewApi")
    private void updateView(int type) {
        dataType = type;
        if (dataType == TYPE_FORTIFICATION) {//车辆设防22
            handler.removeMessages(0);
            rl_addVehicle.setBackgroundResource(R.mipmap.home_fortification_bg);
            tv_addVehicle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ResUtils.resToDrawable(mActivity, R.mipmap.home_fortification), null, null);
            tv_addVehicle.setText("已设防");
            isFollowMove = false;
        } else if (dataType == TYPE_UNFORTIFICATION) {//车辆撤防32
            rl_addVehicle.setBackgroundResource(R.mipmap.home_fortification_bg);
            tv_addVehicle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ResUtils.resToDrawable(mActivity, R.mipmap.home_unfortification), null, null);
            tv_addVehicle.setText("已解防");
            isFollowMove = false;
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 15000);
        } else {
            handler.removeMessages(0);
            rl_addVehicle.setBackgroundResource(R.mipmap.home_add_bg);
            tv_addVehicle.setCompoundDrawablesRelativeWithIntrinsicBounds(null, ResUtils.resToDrawable(mActivity, R.mipmap.home_add), null, null);
            tv_addVehicle.setText("添加设备");
            isFollowMove = true;
            mAMap.clear();
            return;
        }
        addGrowMarker();
    }

    private void initMap() {//初始化AMap对象
        if (mAMap == null) {
            mAMap = mapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mAMap.animateCamera(phoneLatLng == null ? CameraUpdateFactory.zoomTo(18) : CameraUpdateFactory.newLatLngZoom(phoneLatLng, 18f));
            myLocationStyle = new MyLocationStyle();
            //            myLocationStyle.interval(10 * 1000);
            ImageView imageView = new ImageView(mActivity);
            imageView.setImageResource(R.mipmap.map_mylocation);
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromView(imageView));
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
            myLocationStyle.radiusFillColor(ResUtils.resToColor(mActivity, R.color.transparent));// 设置圆形的填充颜色
            myLocationStyle.strokeColor(ResUtils.resToColor(mActivity, R.color.transparent));// 设置圆形的边框颜色
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
            mUiSettings.setRotateGesturesEnabled(false);
            mUiSettings.setZoomControlsEnabled(false);
            mUiSettings.setMyLocationButtonEnabled(false); // 是否显示默认的定位按钮
            mAMap.setInfoWindowAdapter(this);
            mAMap.setOnMarkerClickListener(this);// 设置点击marker事件监听器
            mAMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    //用户拖动地图后，不再跟随移动，直到用户点击定位按钮
                    //                Logger.error("地图拖拽");
                    if (phoneLatLng != null && phoneLatLng.latitude != 0.0 && phoneLatLng.longitude != 0.0) {
                        isFollowMove = false;
                    }
                }
            });
        }
    }

    LocationService.ILocationServiceListener locationServiceListener = new LocationService.ILocationServiceListener() {
        @Override
        public void onLocationChanged(AMapLocation location) {
            if (location != null) {
                if (location.getErrorCode() == 0) { //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                    if (location.getLatitude() == 0.0 || location.getLongitude() == 0.0) {
                        return;
                    }
                    AppVariable.phoneLat = location.getLatitude();
                    AppVariable.phoneLng = location.getLongitude();
                    phoneLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    isUpdateLatlng = true;
                    if (isFollowMove) {
                        SPUtils.put(mActivity, "phoneLatLng", location.getLatitude() + "/" + location.getLongitude());
                        mAMap.animateCamera(CameraUpdateFactory.newLatLng(phoneLatLng));// 设置指定的可视区域地图
                        if (dataType > TYPE_NORMAL) {
                            isFollowMove = false;
                        }
                        if (!isWeather) {
                            String city = location.getCity();
                            if (TextUtils.isEmpty(city)) return;
                            SPUtils.put(mActivity, AppConstants.CURRENT_CITY, city);
                            Logger.error("onLocationChanged定位地址city=" + city);
                            searchLiveWeather(city);
                            isWeather = true;
                        }
                    }
                }
            } else {
                Logger.error("定位失败，loc is null");
            }
        }
    };

    /**
     * 实时天气查询
     */
    @SuppressLint("NewApi")
    private void searchLiveWeather(String cityAdCode) {
        if (TextUtils.isEmpty(cityAdCode)) return;
        weatherSearchQuery = new WeatherSearchQuery(cityAdCode, WeatherSearchQuery.WEATHER_TYPE_LIVE);//检索参数为城市和天气类型，WEATHER_TYPE_LIVE实时天气为1、WEATHER_TYPE_FORECAST天气预报为2
        weatherSearch = new WeatherSearch(mActivity);
        weatherSearch.setOnWeatherSearchListener(new WeatherSearch.OnWeatherSearchListener() {
            @Override
            public void onWeatherLiveSearched(LocalWeatherLiveResult weatherLiveResult, int rCode) {
                if (rCode == AMapException.CODE_AMAP_SUCCESS) {
                    if (weatherLiveResult != null && weatherLiveResult.getLiveResult() != null) {
                        LocalWeatherLive weatherLive = weatherLiveResult.getLiveResult();
                        //                        Logger.error("天气数据= " + weatherLive.getWeather() + "   " + weatherLive.getTemperature() + "℃");
                        tv_weather.setText(weatherLive.getWeather() + "   " + weatherLive.getTemperature() + "℃");
                        int resId = WeatherUtil.getWeather(weatherLive.getWeather());
                        if (resId != -1)
                            tv_weather.setCompoundDrawablesRelativeWithIntrinsicBounds(ResUtils.resToDrawable(mActivity, resId), null, null, null);
                    }
                } else {
                    Logger.error("天气查询错误码=" + rCode);
                }
            }

            @Override
            public void onWeatherForecastSearched(LocalWeatherForecastResult localWeatherForecastResult, int i) {

            }
        });
        weatherSearch.setQuery(weatherSearchQuery);
        weatherSearch.searchWeatherAsyn(); //异步搜索
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (growMarker != null && marker.getId().equals(growMarker.getId())) {
            marker.showInfoWindow();
        } else {
            marker.hideInfoWindow();
        }
        return true;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        if (dataType <= TYPE_NORMAL) {
            return null;
        }
        return getInfoWindowView();
    }

    @Override
    public View getInfoContents(Marker marker) {
        Logger.error("点击执行？？");
        //        if (marker == growMarker) {
        //            return getInfoWindowView();
        //        }
        return null;
    }

    private View getInfoWindowView() {
        View infoWindow = getLayoutInflater().inflate(R.layout.view_map_home, null);
        TextView tv_electricity = infoWindow.findViewById(R.id.tv_electricity_home);
        TextView tv_satellite = infoWindow.findViewById(R.id.tv_satellite_home);
        ImageView iv_signal = infoWindow.findViewById(R.id.iv_signal_home);
        if (positionEntity != null) {
            tv_electricity.setText("电量：" + positionEntity.getBattery() + "%");
            tv_satellite.setText("卫星：" + positionEntity.getGnss() + "颗");
            int deviceNetwork = positionEntity.getNetwork() * 100 / 31;
            int net = -1;
            if (deviceNetwork > 0 && deviceNetwork < 25) {
                net = 0;
            } else if (deviceNetwork >= 25 && deviceNetwork < 50) {
                net = 1;
            } else if (deviceNetwork >= 50 && deviceNetwork < 75) {
                net = 2;
            } else if (deviceNetwork >= 75) {
                net = 3;
            }
            iv_signal.setImageResource(ResUtils.getDrawableId(mActivity, "map_signal" + net));
        }
        return infoWindow;
    }

    /**
     * 添加一个从地上生长的Marker
     */
    public void addGrowMarker() {
        mAMap.clear();
        if (positionEntity != null && positionEntity.getLat() != 0.0 && positionEntity.getLon() != 0.0) {
            deviceLatLng = AMapUtil.convertLatLng(mActivity, new LatLng(positionEntity.getLat(), positionEntity.getLon()), CoordinateConverter.CoordType.GPS);
        } else {
            deviceLatLng = null;
            isFollowMove = true;
            return;
        }

        mAMap.animateCamera(CameraUpdateFactory.newLatLng(deviceLatLng));// 设置指定的可视区域地图

        if (dataType == TYPE_NORMAL) {
            isFollowMove = true;
            return;
        }

        ImageView imageView = new ImageView(mActivity);
        if (dataType == TYPE_FORTIFICATION) {//设防状态
            imageView.setImageResource(R.mipmap.map_device_safeguard);
            if (vehicle != null) {
                switch (vehicle.getVehicleType()) {
                    case DEVICETYPE_1:// 自行车
                        break;
                    case DEVICETYPE_2: // 电动车
                    case DEVICETYPE_3: { // 锂电助力车
                        imageView.setImageResource(R.mipmap.map_ele_device_safeguard);
                    }
                    break;
                }
            }
        } else {//解防状态
            imageView.setImageResource(R.mipmap.map_device_location);
            if (vehicle != null) {
                switch (vehicle.getVehicleType()) {
                    // 自行车
                    case DEVICETYPE_1:
                        break;
                    case DEVICETYPE_2: // 电动车
                    case DEVICETYPE_3: {  // 锂电助力车
                        imageView.setImageResource(R.mipmap.map_ele_device_location);
                    }
                    break;
                }
            }
        }
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory.fromView(imageView)).position(deviceLatLng);
        growMarker = mAMap.addMarker(markerOptions);
        growMarker.showInfoWindow();
        // mAMap.animateCamera(deviceLatLng == null ? CameraUpdateFactory.zoomTo(18) : CameraUpdateFactory.newLatLngZoom(deviceLatLng, 18f));
        mAMap.animateCamera(CameraUpdateFactory.changeLatLng(deviceLatLng));// 设置指定的可视区域地图

        if (!isRefresh) {
            jumpPoint(growMarker);
        } else
            isRefresh = false;
    }

    /**
     * marker点击时跳动一下
     */
    @SuppressLint("NewApi")
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;
       final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatlng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }


    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (locationBinder != null) {
            locationBinder.startLocation(locationServiceListener, 1000);
        }
        //        setUserVisibleHint(true);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        mPresenter.getHomeData();
        if (dataType == TYPE_UNFORTIFICATION) {
            handler.removeMessages(0);
            handler.sendEmptyMessageDelayed(0, 15000);
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
        handler.removeMessages(0);
        if (locationBinder != null) locationBinder.stopService();
    }

    @Override
    public void onStop() {
        if (handler != null) {
            handler.removeMessages(0);
        }
        super.onStop();
    }
}
