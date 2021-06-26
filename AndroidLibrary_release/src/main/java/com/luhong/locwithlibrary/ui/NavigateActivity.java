package com.luhong.locwithlibrary.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.home.NavigateContract;
import com.luhong.locwithlibrary.dialog.BaseDialog;
import com.luhong.locwithlibrary.dialog.PromptDialog;
import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.presenter.NavigatePresenter;
import com.luhong.locwithlibrary.utils.AMapUtil;
import com.luhong.locwithlibrary.utils.ErrorInfo;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;

import static com.amap.api.maps.AMap.MAP_TYPE_NORMAL;

public class NavigateActivity extends BaseMvpActivity<NavigatePresenter> implements NavigateContract.View, AMapNaviListener, AMapNaviViewListener {
    @BindView(R2.id.aMapNaviView_navigate)
    AMapNaviView mAMapNaviView;
    @BindView(R2.id.iv_mapType_navigate)
    ImageView iv_mapType;
    private AMapNavi mAMapNavi;
    private AMap mAMap;
    private final List<NaviLatLng> endLatLngList = new ArrayList<NaviLatLng>();
    private List<NaviLatLng> mWayPointList = new ArrayList<NaviLatLng>();
    private LatLng deviceLastLatLng, deviceLatLng;
    private int isMapTypeSatellite;
    private int dataType;

    private PromptDialog promptDialog;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private boolean isResponded = true;
    private int trackTime = 5;
    private PowerManager.WakeLock wakeLock;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mPresenter != null) mPresenter.getTrackInfoRefresh(mActivity);
        }
    };

    private void exitNavi() {
        try {
            if (promptDialog == null) promptDialog = new PromptDialog(mActivity);
            promptDialog.showDialog("确定退出导航？", new BaseDialog.IEventListener() {
                @Override
                public void onConfirm() {
                    showLoading("正在退出");
                    if (mHandler != null) {
                        mHandler.removeMessages(1);
                    }
                    if (timerTask != null) timerTask.cancel();
                    if (timer != null) timer.cancel();
                    if (mAMapNavi != null) {
                        mAMapNavi.stopNavi();
                        mAMapNavi.destroy();
                    }
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 1500);
                }

                @Override
                public void onCancel() {

                }
            });
        } catch (Exception e) {
            Logger.error("退出异常？：" + e);
        }
    }

    @Override
    protected int initLayoutId() {
        return R.layout.activity_navigate;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mAMapNaviView.onCreate(savedInstanceState);
        mAMapNaviView.setAMapNaviViewListener(this);

        AMapNaviViewOptions options = new AMapNaviViewOptions();
        options.setScreenAlwaysBright(false);
        mAMapNaviView.setViewOptions(options);

        mAMapNavi = AMapNavi.getInstance(getApplicationContext());
        mAMapNavi.addAMapNaviListener(this);
        mAMapNavi.setUseInnerVoice(true);

        mAMap = mAMapNaviView.getMap();
        mAMap.setTrafficEnabled(false);

        initTitleView(true, "导航追车");
        //锁屏CPU继续运行
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, NavigateActivity.class.getName());// this.getClass().getCanonicalName()
        wakeLock.acquire();

        ScreenUtils.keepScreenLongLight(this, true);

        Bundle bundle = getIntent().getExtras();
        dataType = bundle.getInt(AppConstants.dataTypeKey);
        trackTime = bundle.getInt("trackTime", 5);
        double deviceLat = bundle.getDouble(AppConstants.deviceLatKey, -1);
        double deviceLng = bundle.getDouble(AppConstants.deviceLngKey, -1);
        double phoneLat = bundle.getDouble(AppConstants.phoneLatKey, -1);
        double phoneLng = bundle.getDouble(AppConstants.phoneLngKey, -1);
        if (deviceLat != -1 && deviceLng != -1) {
            endLatLngList.add(new NaviLatLng(deviceLat, deviceLng));
        }
        showLoading();
    }

    @Override
    protected void initData() {
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (isResponded) {
                    mHandler.sendEmptyMessage(1);
                    isResponded = false;
                }
            }
        };
        timer.schedule(timerTask, 1000, trackTime * 1000);
    }

    @Override
    protected void fetchData() {
        mPresenter.getTrackInfoRefresh(this);
    }

    @Override
    public void onTrackInfoRefreshSuccess(TrackInfoRefreshEntity trackInfoRefreshEntity) {
        isResponded = true;
        if (trackInfoRefreshEntity == null) return;
        try {
            if (trackInfoRefreshEntity.getLat() != 0.0 && trackInfoRefreshEntity.getLon() != 0.0) {
                deviceLatLng = AMapUtil.convertLatLng(mActivity, new LatLng(trackInfoRefreshEntity.getLat(), trackInfoRefreshEntity.getLon()), CoordinateConverter.CoordType.GPS);
                if (deviceLastLatLng == null) {
                    deviceLastLatLng = deviceLatLng;
                    return;
                }
                float distance = AMapUtils.calculateLineDistance(deviceLastLatLng, deviceLatLng);
                Logger.error("两定位点距离=" + distance + "米");
                if (distance >= 25f) {
                    deviceLastLatLng = deviceLatLng;
                    endLatLngList.clear();
                    endLatLngList.add(new NaviLatLng(deviceLatLng.latitude, deviceLatLng.longitude));
                    onInitNaviSuccess();
                }
            }
        } catch (Exception e) {
            Logger.error("自定义重算路线失败=" + e);
            cancelLoading();
        }
    }

    @Override
    public void onFailure(int errType, String errMsg) {
        cancelLoading();
        if (errType == 1) isResponded = true;
    }

    @Override
    protected void onEventListener() {
        if (tv_title_left != null)
            tv_title_left.setOnClickListener(new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                    exitNavi();
                }
            });
        iv_mapType.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                iv_mapType.setImageResource(R.mipmap.map_type_foc);
                showWindow(mActivity, iv_mapType);
            }
        });
    }

    private void showWindow(Context context, View parentView) {
        View child = LayoutInflater.from(context).inflate(R.layout.dialog_map_type, null);
        PopupWindow window = new PopupWindow(child, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setOutsideTouchable(true);
        window.setFocusable(false);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.DialogTopAnimStyle);
        window.showAsDropDown(parentView, 0, 0);

        ImageView iv_standard = child.findViewById(R.id.iv_standard_mapType);
        ImageView iv_satellite = child.findViewById(R.id.iv_satellite_mapType);
        ImageView iv_night = child.findViewById(R.id.iv_night_mapType);

        switch (isMapTypeSatellite) {
            case AMap.MAP_TYPE_NORMAL://  mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_foc);
                iv_satellite.setImageResource(R.mipmap.map_satellite_nor);
                iv_night.setImageResource(R.mipmap.map_night_no);
                break;
            case AMap.MAP_TYPE_SATELLITE://   mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_nor);
                iv_satellite.setImageResource(R.mipmap.map_satellite_foc);
                iv_night.setImageResource(R.mipmap.map_night_no);
                break;
            case AMap.MAP_TYPE_NIGHT://  mAMap.setMapType(AMap.MAP_TYPE_NIGHT);// 夜间模式地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_nor);
                iv_satellite.setImageResource(R.mipmap.map_satellite_nor);
                iv_night.setImageResource(R.mipmap.map_night_yes);
                break;
        }

        iv_standard.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isMapTypeSatellite = MAP_TYPE_NORMAL;
                mAMap.setMapType(MAP_TYPE_NORMAL);// 矢量地图模式
                window.dismiss();
            }
        });
        iv_satellite.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isMapTypeSatellite = AMap.MAP_TYPE_SATELLITE;
                mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                window.dismiss();
            }
        });
        iv_night.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                isMapTypeSatellite = AMap.MAP_TYPE_NIGHT;
                mAMap.setMapType(AMap.MAP_TYPE_NIGHT);// 夜间模式地图模式
                window.dismiss();
            }
        });

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_mapType.setImageResource(R.mipmap.map_type_nor);
                mAMapNaviView.invalidate();
            }
        });
    }

    @Override
    public void onBackPressed() {
        exitNavi();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAMapNaviView.onPause();
        //        停止导航之后，会触及底层stop，然后就不会再有回调了，但是讯飞当前还是没有说完的半句话还是会说完
        //        mAMapNavi.stopNavi();
    }

    @Override
    protected void onDestroy() {
        ScreenUtils.keepScreenLongLight(this, false);
        if (wakeLock != null && wakeLock.isHeld()) {
            wakeLock.release();
            wakeLock = null;
        }
        try {
            isResponded = false;
            if (mAMapNaviView != null) mAMapNaviView.onDestroy();
            //since 1.6.0 不再在naviview destroy的时候自动执行AMapNavi.stopNavi();请自行执行
            if (mAMapNavi != null) {
                mAMapNavi.stopNavi();
                mAMapNavi.destroy();
            }
            cancelLoading();
        } catch (Exception e) {
            Logger.error("退出导航出错= " + e);
        }
        super.onDestroy();
    }

    @Override
    public void finish() {
        Intent intent = new Intent();
        setResult(AppConstants.REQUEST_CODE_NAVIGATE, intent);
        super.finish();
    }

    @Override
    public void onInitNaviSuccess() {//初始化成功
        if (dataType == 1) {//骑行模式
            mAMapNavi.calculateRideRoute(endLatLngList.get(0));
        } else if (dataType == 2) {//步行
            mAMapNavi.calculateWalkRoute(endLatLngList.get(0));
        } else {//驾车
            /**
             * 方法: int strategy=mAMapNavi.strategyConvert(congestion, avoidhightspeed, cost, hightspeed, multipleroute); 参数:
             *
             * @congestion 躲避拥堵
             * @avoidhightspeed 不走高速
             * @cost 避免收费
             * @hightspeed 高速优先
             * @multipleroute 多路径
             *
             *  说明: 以上参数都是boolean类型，其中multipleroute参数表示是否多条路线，如果为true则此策略会算出多条路线。
             *  注意: 不走高速与高速优先不能同时为true 高速优先与避免收费不能同时为true
             */
            int strategy = 0;
            try {
                //再次强调，最后一个参数为true时代表多路径，否则代表单路径
                strategy = mAMapNavi.strategyConvert(true, false, false, false, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            mAMapNavi.calculateDriveRoute(endLatLngList, mWayPointList, strategy);
        }
    }

    @Override
    public void onCalculateRouteSuccess(int[] ids) {//多路径算路成功回调
        mAMapNavi.startNavi(NaviType.GPS);//NaviType.GPS;//EMULATOR
        cancelLoading();
    }

    @Override
    public void onInitNaviFailure() {
        Logger.error("初始化失败");
        showToast("导航初始化失败，请重试");
        cancelLoading();
    }

    @Override
    public void onStartNavi(int i) {//开始导航状态
        Logger.error("onStartNavi()=" + i);
    }

    @Override
    public void onTrafficStatusUpdate() {
        Logger.error("onTrafficStatusUpdate()");
    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {
        Logger.error("onLocationChange()");
    }

    @Override
    public void onGetNavigationText(int i, String s) {
        Logger.error("onGetNavigationText(),i=" + i + ",s=" + s);
    }

    @Override
    public void onGetNavigationText(String s) {
        Logger.error("onGetNavigationText()" + s);
    }

    @Override
    public void onEndEmulatorNavi() {
        Logger.error("onEndEmulatorNavi()");
    }

    @Override
    public void onArriveDestination() {
        Logger.error("onEndEmulatorNavi()");
    }

    @Override
    public void onCalculateRouteFailure(int errorInfo) {//路线计算失败
        cancelLoading();
        Logger.error("路线计算失败：错误码=" + errorInfo + ",Error Message= " + ErrorInfo.getError(errorInfo));
        Logger.error("错误码详细链接见：http://lbs.amap.com/api/android-navi-sdk/guide/tools/errorcode/");
        if (errorInfo == 20 /*|| errorInfo == 25 */ || errorInfo == 26) {
            showToast("距离太远，请选择其他导航模式");
        } else if (errorInfo == 23) {
            showToast("访问过于频繁，请稍后重试");
        } else {
            showToast("路线计算失败，请重试");
        }
    }

    @Override
    public void onReCalculateRouteForYaw() {//偏航重算
        Logger.error("onReCalculateRouteForYaw()");
    }

    @Override
    public void onReCalculateRouteForTrafficJam() {//拥堵重算
        Logger.error("onReCalculateRouteForTrafficJam()");
    }

    @Override
    public void onArrivedWayPoint(int i) {
        Logger.error("onArrivedWayPoint()" + i);
    }

    @Override
    public void onGpsOpenStatus(boolean b) {
        Logger.error("onGpsOpenStatus()" + b);
        if (!b) showToast("GPS信号不可用");
    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {
        Logger.error("onNaviInfoUpdate()");
    }


    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {
        Logger.error("updateCameraInfo()");
    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {
        Logger.error("updateIntervalCameraInfo()" + i);
    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {
        Logger.error("onServiceAreaUpdate()");
    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {
        Logger.error("showCross()");
    }

    @Override
    public void hideCross() {
        Logger.error("hideCross()");
    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {
        Logger.error("showModeCross()");
    }

    @Override
    public void hideModeCross() {
        Logger.error("hideModeCross()");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {
        Logger.error("showLaneInfo()");
    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {
        Logger.error("showLaneInfo()");
    }

    @Override
    public void hideLaneInfo() {
        Logger.error("hideLaneInfo()");
    }

    @Override
    public void notifyParallelRoad(int i) {
        Logger.error("notifyParallelRoad()" + i);
        if (i == 0) {
            //            Toast.makeText(this, "当前在主辅路过渡", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主辅路过渡");
            return;
        }
        if (i == 1) {
            //            Toast.makeText(this, "当前在主路", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在主路");
            return;
        }
        if (i == 2) {
            //            Toast.makeText(this, "当前在辅路", Toast.LENGTH_SHORT).show();
            Log.d("wlx", "当前在辅路");
        }
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {
        Logger.error("OnUpdateTrafficFacility()");
    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {
        Logger.error("OnUpdateTrafficFacility()");
    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {
        Logger.error("updateAimlessModeStatistics()");
    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {
        Logger.error("updateAimlessModeCongestionInfo()");
    }

    @Override
    public void onPlayRing(int i) {
        Logger.error("onPlayRing()" + i);
    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {
        Logger.error("onCalculateRouteSuccess()");
    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {
        Logger.error("onCalculateRouteFailure()");
    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {
        Logger.error("onNaviRouteNotify()");
    }

    @Override
    public void onGpsSignalWeak(boolean b) {

    }

    @Override
    public void onNaviSetting() {
        Logger.error("onNaviSetting()");
    }

    @Override
    public void onNaviCancel() {
        Logger.error("onNaviCancel()");
        finish();
    }

    @Override
    public boolean onNaviBackClick() {
        Logger.error("onNaviBackClick()");
        exitNavi();
        return true;
    }

    @Override
    public void onNaviMapMode(int i) {
        Logger.error("onNaviMapMode()" + i);
    }

    @Override
    public void onNaviTurnClick() {
        Logger.error("onNaviTurnClick()");
    }

    @Override
    public void onNextRoadClick() {
        Logger.error("onNextRoadClick()");
    }

    @Override
    public void onScanViewButtonClick() {
        Logger.error("onScanViewButtonClick()");
    }

    @Override
    public void onLockMap(boolean b) {
        Logger.error("onLockMap()" + b);
    }

    @Override
    public void onNaviViewLoaded() {
        Logger.error("onNaviViewLoaded()导航页面加载成功");
    }

    @Override
    public void onMapTypeChanged(int i) {
        Logger.error("onMapTypeChanged()" + i);
    }

    @Override
    public void onNaviViewShowMode(int i) {
        Logger.error("onNaviViewShowMode()" + i);
    }

}
