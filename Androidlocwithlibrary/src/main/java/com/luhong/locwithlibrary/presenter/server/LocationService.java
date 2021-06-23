package com.luhong.locwithlibrary.presenter.server;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;

/**
 * Created by ITMG on 2020-03-07.
 */
public class LocationService extends Service {
    public static boolean mIsRunning = false;//服务是否启动
    private ILocationServiceListener locationServiceListener;
    private AMapLocationClient mLocationClient;
    private AMapLocationClientOption mLocationOption;
    private LocationBinder downloadBinder = new LocationBinder();
    private long locationInterval =1000;

    public static void bindService(Context context, ServiceConnection connection) {
        Intent intent = new Intent(context, LocationService.class);
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mIsRunning = true;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return downloadBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mIsRunning = false;
        return super.onUnbind(intent);
    }

    public class LocationBinder extends Binder {
        public void startLocation(ILocationServiceListener locationListeners, long interval) {
            locationServiceListener = locationListeners;
            locationInterval = interval;
            LocationService.this.startLocation(null);//开始
        }

        /**
         * 开始定位
         *
         * @param locationOption 定位参数
         */
        public void startLocation(AMapLocationClientOption locationOption) {
            LocationService.this.startLocation(locationOption);
        }

        public void stopLocation() {
            LocationService.this.stopLocation();
        }

        public void destroyLocation() {
            LocationService.this.destroyLocation();
        }

        public void stopService() {
            stopLocation();
            destroyLocation();
            LocationService.this.close();
        }
    }

    /**
     * 关闭
     */
    public void close() {
        stopSelf();
    }

    /**
     * 启动定位
     */
    void startLocation(AMapLocationClientOption locationOption) {
        stopLocation();
        if (mLocationClient == null) {
            mLocationClient = new AMapLocationClient(this.getApplicationContext());
        }
        mLocationOption = locationOption == null ? getDefaultOption() : locationOption;
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.setLocationListener(locationListener);
        mLocationClient.startLocation();
    }

    /**
     * 停止定位
     */
    void stopLocation() {
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation() {
        if (null != mLocationClient) {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            mLocationClient.onDestroy();
            mLocationClient = null;
            mLocationOption = null;
        }
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30 * 1000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(locationInterval);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(false);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(true);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）
        return mOption;
    }

    /**
     * 获取GPS状态的字符串
     *
     * @param statusCode GPS状态码
     * @return
     */
    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                //                showToast("请打开GPS");
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                //                showToast(str);
                break;
        }
        return str;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            //Logger.error("定位服务回调经纬度="+aMapLocation.getLatitude()+"/"+aMapLocation.getLongitude()+",地址="+aMapLocation.getAddress());
            if (locationServiceListener != null)
                locationServiceListener.onLocationChanged(aMapLocation);
        }
    };

    public interface ILocationServiceListener {
        void onLocationChanged(AMapLocation aMapLocation);
    }
}
