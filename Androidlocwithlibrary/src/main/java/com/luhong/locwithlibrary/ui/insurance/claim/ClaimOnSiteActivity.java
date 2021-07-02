package com.luhong.locwithlibrary.ui.insurance.claim;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.Projection;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.ApiServer;
import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.SCallBack;
import com.luhong.locwithlibrary.base.BaseMvpActivity;
import com.luhong.locwithlibrary.contract.ClaimOnSiteContract;
import com.luhong.locwithlibrary.dialog.PhotoDialog;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.presenter.ClaimOnSitePresenter;
import com.luhong.locwithlibrary.utils.AMapUtil;
import com.luhong.locwithlibrary.utils.BaseUtils;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.FileUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.LuBanUtils;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.ToastUtil;
import com.zyq.easypermission.EasyPermissionResult;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import retrofit2.Call;
import top.zibin.luban.OnCompressListener;

import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_1;
import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_2;
import static com.luhong.locwithlibrary.api.AppConstants.DEVICETYPE_3;


/**
 * 理赔进度-现场拍照取证
 */
public class ClaimOnSiteActivity extends BaseMvpActivity<ClaimOnSitePresenter> implements ClaimOnSiteContract.View/*, AMap.OnMyLocationChangeListener*/
{
    public static final int TYPE_NOT_OPEN_GPS = 2;//GPS未打开
    public static final int TYPE_NOT_FINE_LOCATION = 3;//未授予定位权限
    public static final int REQUEST_CODE_OPEN_GPS = 1;
    @BindView(R2.id.mapView_claimOnSite)
    MapView mapView;
    @BindView(R2.id.iv_zoomIn_home)
    ImageView iv_zoomIn;
    @BindView(R2.id.iv_zoomOut_home)
    ImageView iv_zoomOut;
    @BindView(R2.id.iv_deviceLocation_claimOnSite)
    ImageView iv_deviceLocation;
    @BindView(R2.id.btn_confirm_claimOnSite)
    Button btn_confirm;

    private AMap mAMap;
    private MarkerOptions markerOptions;
    private UiSettings mUiSettings;
    private MyLocationStyle myLocationStyle;
    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private Uri picUri;
    private File picFile;
    private LatLng phoneLatLng, deviceLatLng;
    private final int TYPE_NORMAL = 0;//未绑定设备状态
    private final int TYPE_FORTIFICATION = 1;//设防状态
    private final int TYPE_UNFORTIFICATION = 2;//解防状态
    private String claimId, vehicleId, evidencePath, evidenceTime;
    private float distance;
    private ApiServer apiServer = ApiClient.getInstance().getApiServer();
    private DeviceEntity device = null;

    @Override
    protected int initLayoutId()
    {
        return R.layout.activity_claim_onsite;
    }

    @Override
    protected void initView(Bundle savedInstanceState)
    {
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        initTitleView(true, "拍照取证");
        initMap();
        initLocation();
        Bundle bundle = getIntent().getExtras();
        claimId = bundle.getString(AppConstants.dataKey);
        vehicleId = bundle.getString(AppConstants.vehicleIdKey);

    }

    @Override
    protected void initData()
    {

    }

    @Override
    protected void onEventListener()
    {
        iv_zoomIn.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                mAMap.animateCamera(CameraUpdateFactory.zoomIn(), 200, null);
            }
        });
        iv_zoomOut.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                mAMap.animateCamera(CameraUpdateFactory.zoomOut(), 200, null);
            }
        });
        iv_deviceLocation.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                fetchData();
            }
        });
        btn_confirm.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                //                startIntentActivity(ClaimForensicsActivity.class);
                mActivity.requestStorageAndCameraPermissions(new EasyPermissionResult()
                {
                    @Override
                    public void onPermissionsAccess(int requestCode)
                    {
                        super.onPermissionsAccess(requestCode);
                        picFile = FileUtils.createSDFile(FileUtils.getRootPicDirImg(), DateUtils.formatCurrentDateTime() + ".jpg");
                        FileUtils.deleteFile(picFile.getAbsolutePath());
                        picUri = FileUtils.getUriForFile(mActivity, picFile);
                        Logger.error("拍照file=" + picFile);
                        PhotoDialog.takePhoto(mActivity, picUri);
                    }

                    @Override
                    public void onPermissionsDismiss(int requestCode, @NonNull List<String> permissions)
                    {
                        super.onPermissionsDismiss(requestCode, permissions);
                        ToastUtil.show("没有权限");
                    }
                });

            }
        });
    }

    @Override
    protected void fetchData()
    {
        mPresenter.checkLocationInit(mActivity);
        Call<BaseResponse<List<DeviceEntity>>> call = apiServer.getDeviceListCall(new HashMap<>());
        call.enqueue(new SCallBack<BaseResponse<List<DeviceEntity>>>()
        {
            @Override
            public void onSuccess(@NonNull BaseResponse<List<DeviceEntity>> result)
            {
                if (result.isSuccess())
                {
                    List<DeviceEntity> devices = result.getData();
                    for (DeviceEntity d : devices)
                    {
                        if (d.getId().equals(vehicleId))
                        {
                            device = d;
                            mPresenter.getTrackInfo(ClaimOnSiteActivity.this, vehicleId);
                            break;
                        }
                    }
                } else
                {
                    showToast(result.getMsg());
                    finish();
                }
            }
        });
    }

    @Override
    public void onLocationInitFailure(int errCode)
    {
        Logger.error("定位未就绪");
        if (errCode == TYPE_NOT_OPEN_GPS)
        {//打开位置服务
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, REQUEST_CODE_OPEN_GPS);
        } else if (errCode == TYPE_NOT_FINE_LOCATION)
        {//打开权限界面
            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", mActivity.getPackageName(), null);
            intent.setData(uri);
            try
            {
                mActivity.startActivity(intent);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onLocationInitSuccess()
    {
    }

    @Override
    public void onTrackInfoSuccess(DevicePositionEntity positionEntity)
    {
        if (positionEntity != null && positionEntity.getLat() != 0.0 && positionEntity.getLon() != 0.0)
        {
            deviceLatLng = AMapUtil.convertLatLng(mActivity, new LatLng(positionEntity.getLat(), positionEntity.getLon()), CoordinateConverter.CoordType.GPS);
            if (deviceLatLng == null || deviceLatLng.latitude == 0.0 || deviceLatLng.longitude == 0.0)
            {
                mAMap.clear();
                btn_confirm.setEnabled(false);
                return;
            }
            String safy = positionEntity.getSafy();
            if (!TextUtils.isEmpty(safy) && safy.equals(DevicePositionEntity.TYPE_FORTIFICATION))
            {
                addGrowMarker(TYPE_FORTIFICATION, deviceLatLng);
            } else if (!TextUtils.isEmpty(safy) && safy.equals(DevicePositionEntity.TYPE_UNFORTIFICATION))
            {
                addGrowMarker(TYPE_UNFORTIFICATION, deviceLatLng);
            } else
            {
                mAMap.clear();
                btn_confirm.setEnabled(false);
            }
        } else
        {
            mAMap.clear();
            btn_confirm.setEnabled(false);
        }
    }

    @Override
    public void onUploadSuccess(UrlEntity resultEntity, int position)
    {
        String evidenceUrl = resultEntity.getUrl();
        if (TextUtils.isEmpty(evidenceUrl)) return;

        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", claimId);//理赔id
        bodyParams.put("evidence", evidenceUrl);//取证地址
        bodyParams.put("evidenceTime", evidenceTime);//拍照取证时间
        bodyParams.put("lat", deviceLatLng.latitude);//设备经纬度
        bodyParams.put("lon", deviceLatLng.longitude);//
        bodyParams.put("evidenceLat", phoneLatLng.latitude);//手机经纬度
        bodyParams.put("evidenceLon", phoneLatLng.longitude);//
        bodyParams.put("distance", distance);//距离
        bodyParams.put("commitType", 2);//1正式提交 2草稿提交

        //        bodyParams.put("filingReceipt", filingReceiptUrl);//立案回执
        //        bodyParams.put("filingTime", filingTime);//立案时间
        mPresenter.updateClaim(bodyParams);
    }

    @Override
    public void onUpdateClaimSuccess()
    {
        cancelLoading();
        Intent intent = new Intent();
        intent.putExtra(AppConstants.dataTypeKey, evidencePath);
        intent.putExtra("evidencePath", evidencePath);
        intent.putExtra("evidenceTime", evidenceTime);
        setResult(AppConstants.RESULT_CODE_QUERY, intent);
        finish();
    }

    @Override
    public void onFailure(int errType, String errMsg)
    {
        cancelLoading();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQUEST_CODE_OPEN_GPS)
        {
            if (BaseUtils.isGpsEnabled(mActivity))
            {//GPS已打开
                mPresenter.checkLocationInit(mActivity);
            }
        }
        switch (resultCode)
        {
            case RESULT_OK:
                if (requestCode == PhotoDialog.REQ_CODE_CAMERA)
                {// 从相机返回,从设置相机图片的输出路径中提取数据
                    if (picFile == null) return;
                    String path = picFile.getAbsolutePath();
                    Logger.error("拍照图片路径= " + evidencePath);

                    if (!TextUtils.isEmpty(path))
                    {
                        LuBanUtils.load(mActivity, path, new OnCompressListener()
                        {
                            @Override
                            public void onStart()
                            {

                            }

                            @Override
                            public void onSuccess(File file)
                            {
                                evidencePath = file.getAbsolutePath();
                                Logger.error(" 压缩后文件路径= " + evidencePath);
                                evidenceTime = DateUtils.formatCurrentDateTime();
                                showLoading("提交中...");
                                mPresenter.uploadFile(mActivity, 0, evidencePath);
                            }

                            @Override
                            public void onError(Throwable e)
                            {

                            }
                        });
                    }
                    break;
                }
        }
    }

    /**
     * 计算手机-设备的距离
     */
    private void calculateLineDistance()
    {
        if (deviceLatLng != null && deviceLatLng.longitude != 0.0 && deviceLatLng.latitude != 0.0 && phoneLatLng != null && phoneLatLng.longitude != 0.0 && phoneLatLng.latitude != 0.0)
        {
            distance = AMapUtils.calculateLineDistance(phoneLatLng, deviceLatLng);
            Logger.error("距离=" + distance);
            if (distance > 300)
            {//300
                btn_confirm.setEnabled(false);
            } else
            {
                btn_confirm.setEnabled(true);
            }
        } else
        {
            btn_confirm.setEnabled(false);
        }
    }

    /**
     * 添加一个从地上生长的Marker
     */
    public void addGrowMarker(int dataType, LatLng latLng)
    {
        calculateLineDistance();

        mAMap.clear();
        if (deviceLatLng != null)
            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(deviceLatLng, 16f));// 设置指定的可视区域地图
        // 绘制一个圆形
        mAMap.addCircle(new CircleOptions().center(deviceLatLng)
                .radius(300).strokeColor(ResUtils.resToColor(mActivity, R.color.theme_color))
                .fillColor(Color.argb(40, 140, 228, 204)).strokeWidth(4));

        BitmapDescriptor bitmapDescriptor = null;
        if (dataType == TYPE_FORTIFICATION)
        {
            int imgId = R.mipmap.map_device_safeguard;
            switch (device.getVehicleType())
            {
                // 自行车
                case DEVICETYPE_1:
                    imgId = R.mipmap.map_device_safeguard;
                    break;
                // 电动车
                case DEVICETYPE_2:
                    // 锂电助力车
                case DEVICETYPE_3:
                    imgId = R.mipmap.map_ele_device_safeguard;
                    break;
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(imgId);
        } else /*if (dataType == TYPE_UNFORTIFICATION)*/
        {
            int imgId = R.mipmap.map_device_location;
            switch (device.getVehicleType())
            {
                // 自行车
                case DEVICETYPE_1:
                    imgId = R.mipmap.map_device_location;
                    break;
                // 电动车
                case DEVICETYPE_2:
                    // 锂电助力车
                case DEVICETYPE_3:
                    imgId = R.mipmap.map_ele_device_location;
                    break;
            }
            bitmapDescriptor = BitmapDescriptorFactory.fromResource(imgId);
        }
        markerOptions = new MarkerOptions().icon(bitmapDescriptor).position(latLng);
        Marker growMarker = mAMap.addMarker(markerOptions);

        jumpPoint(growMarker);

    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker)
    {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mAMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable()
        {
            @Override
            public void run()
            {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed / duration);
                double lng = t * markerLatlng.longitude + (1 - t) * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t) * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0)
                {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void initMap()
    {//初始化AMap对象
        if (mAMap == null)
        {
            mAMap = mapView.getMap();
            mUiSettings = mAMap.getUiSettings();
            mAMap.animateCamera(CameraUpdateFactory.zoomTo(16f));
            myLocationStyle = new MyLocationStyle();
            myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0)/*ResUtils.resToColor(mActivity, R.color.radius_color)*/);// 设置圆形的填充颜色
            myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0)/*ResUtils.resToColor(mActivity, R.color.radius_color)*/);// 设置圆形的边框颜色
            ImageView imageView = new ImageView(mActivity);
            imageView.setImageResource(R.mipmap.map_mylocation);
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromView(imageView));
            //            myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.mipmap.map_mylocation)); // 自定义定位蓝点图标
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            mAMap.setMyLocationStyle(myLocationStyle);
            mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false

        }
        mUiSettings.setZoomControlsEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(false);
        mUiSettings.setMyLocationButtonEnabled(true); // 是否显示默认的定位按钮
    }

    /**
     * 初始化定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void initLocation()
    {
        //初始化client
        locationClient = new AMapLocationClient(getApplicationContext());
        locationOption = getDefaultOption();
        //设置定位参数
        locationClient.setLocationOption(locationOption);
        // 设置定位监听
        locationClient.setLocationListener(locationListener);
        startLocation();
    }

    /**
     * 默认的定位参数
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private AMapLocationClientOption getDefaultOption()
    {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(1000);//可选，设置定位间隔。默认为2秒
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
     * 定位监听
     */
    AMapLocationListener locationListener = new AMapLocationListener()
    {
        @Override
        public void onLocationChanged(AMapLocation location)
        {
            if (location != null && location.getErrorCode() == 0 && location.getLatitude() != 0.0 || location.getLongitude() != 0.0)
            { //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                phoneLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                calculateLineDistance();
            } else
            {
                btn_confirm.setEnabled(false);
            }
        }
    };

    /**
     * 开始定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void startLocation()
    {
        //根据控件的选择，重新设置定位参数
        resetOption();
        // 设置定位参数
        locationClient.setLocationOption(locationOption);
        // 启动定位
        locationClient.startLocation();
    }

    /**
     * 停止定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void stopLocation()
    {
        // 停止定位
        locationClient.stopLocation();
    }

    /**
     * 销毁定位
     *
     * @author hongming.wang
     * @since 2.8.0
     */
    private void destroyLocation()
    {
        if (null != locationClient)
        {
            /**
             * 如果AMapLocationClient是在当前Activity实例化的，
             * 在Activity的onDestroy中一定要执行AMapLocationClient的onDestroy
             */
            locationClient.onDestroy();
            locationClient = null;
            locationOption = null;
        }
    }

    // 根据控件的选择，重新设置定位参数
    private void resetOption()
    {
        // 设置是否需要显示地址信息
        locationOption.setNeedAddress(false);
        /**
         * 设置是否优先返回GPS定位结果，如果30秒内GPS没有返回定位结果则进行网络定位
         * 注意：只有在高精度模式下的单次定位有效，其他方式无效
         */
        locationOption.setGpsFirst(true);
        // 设置是否开启缓存
        locationOption.setLocationCacheEnable(true);
        // 设置是否单次定位
        locationOption.setOnceLocation(false);
        //设置是否等待设备wifi刷新，如果设置为true,会自动变为单次定位，持续定位时不要使用
        locationOption.setOnceLocationLatest(false);
        //设置是否使用传感器
        locationOption.setSensorEnable(true);
        //设置是否开启wifi扫描，如果设置为false时同时会停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        try
        {
            // 设置发送定位请求的时间间隔,最小值为1000，如果小于1000，按照1000算
            locationOption.setInterval(2 * 1000);
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
        try
        {
            // 设置网络请求超时时间
            locationOption.setHttpTimeOut(10 * 1000);
        } catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume()
    {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause()
    {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mapView != null) mapView.onDestroy();
        destroyLocation();
    }
}
