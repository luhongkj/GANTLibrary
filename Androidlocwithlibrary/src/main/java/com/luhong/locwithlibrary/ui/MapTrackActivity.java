package com.luhong.locwithlibrary.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.reflect.TypeToken;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.base.CheckPermissionsActivity;
import com.luhong.locwithlibrary.dialog.MapTypeDialog;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.model.IMainView;
import com.luhong.locwithlibrary.presenter.TrackDetailPresenter;
import com.luhong.locwithlibrary.presenter.server.MoveCarCustomThread;
import com.luhong.locwithlibrary.utils.AMapUtil;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ResUtils;
import com.luhong.locwithlibrary.utils.SPUtils;
import com.zyq.easypermission.EasyPermissionResult;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

//setContentView(R.layout.activity_map_demo);
public class MapTrackActivity extends CheckPermissionsActivity implements IMainView, TrackDetailPresenter.View {
    public static final String dataTypeKey = "dataTypeKey";
    @BindView(R2.id.rl_share)
    RelativeLayout rlShare;
    @BindView(R2.id.mapView_trackDetail)
    MapView mapView;
    @BindView(R2.id.rl_top_trackDetail)
    RelativeLayout rlTopTrackDetail;
    @BindView(R2.id.iv_mapType_trackDetail)
    ImageView ivMapTypeTrackDetail;
    @BindView(R2.id.tv_mileage_track)
    TextView tvMileageTrack;
    @BindView(R2.id.tv_uphillMileage_track)
    TextView tvUphillMileageTrack;
    @BindView(R2.id.tv_duration_track)
    TextView tvDurationTrack;
    @BindView(R2.id.tv_averageSpeed_track)
    TextView tvAverageSpeedTrack;
    @BindView(R2.id.tv_maxSpeed_track)
    TextView tvMaxSpeedTrack;

    @BindView(R2.id.tv_time_track)
    TextView tvTimeTrack;

    @BindView(R2.id.tv_speed_track)
    TextView tvSpeedTrack;


    @BindView(R2.id.tv_distributionSpeed_track)
    TextView tvDistributionSpeedTrack;
    @BindView(R2.id.tv_energy_track)
    TextView tvEnergyTrack;
    @BindView(R2.id.tv_calories_track)
    TextView tvCaloriesTrack;

    @BindView(R2.id.iv_start_track)
    ImageView ivStartTrack;

    @BindView(R2.id.progressBar_track)
    SeekBar progressBarTrack;
    @BindView(R2.id.tv_speed_track_contnet)
    TextView tvSpeedTrackContnet;
    @BindView(R2.id.show_energy_layout)
    LinearLayout showEnergyLayout;
    @BindView(R2.id.ll_bottom_track)
    LinearLayout llBottomTrack;
    @BindView(R2.id.ll_bottomContent_trackDetail)
    LinearLayout llBottomContentTrackDetail;
    private Typeface fromAsset;
    private SportEntity.TrackList sportEntity;
    public Marker mCarMarker;
    public AMap mAMap;
    public List<LatLng> mLatLngList = new ArrayList<>();// 轨迹总线集合
    private int isMapTypeSatellite = AMap.MAP_TYPE_NORMAL;
    public Polyline mMovePolyline;// 移动轨迹线
    private PolylineOptions polylineOptions;
    public MoveCarCustomThread mSingleMoveCarCustomThread;
    List<LatLng> conLatLon = new ArrayList<>();

    private int normalStatus = 0, playingStatus = 1, pauseStatus = 2;//0 正常状态  1播放状态  2暂停状态
    private int mPlayState = normalStatus;

    @Override
    public void error(String s) {
    }

    public interface EventType {
        int MapMove = 0;// 0:地图实时跟随；1：小车停止运动；2：小车运动;3：地图加载成功;4:熄火后地图全览
    }


    public boolean isCarFirstMove = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_demo);
        ButterKnife.bind(this);
        setTranslucentStatus(false);
        //   EventBus.getDefault().register(this);
        fromAsset = ResUtils.getTypeface(this, "fonts/impact_0.ttf");
        //可以获得AMap 继续对其另外的操作
        //  mAMap.setTrafficEnabled(true);
        // mAMap.setOnInfoWindowClickListener(marker -> VideoDetailActivity.intentTo(this, mVideos.get(index)));
        Bundle bundle = getIntent().getExtras();
        sportEntity = (SportEntity.TrackList) bundle.getSerializable(dataTypeKey);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        mAMap = mapView.getMap();
        //日间 切换 夜间
        mAMap.setMapType(isMapTypeSatellite);//夜景地图，aMap是地图控制器对象。
        fromAsset = ResUtils.getTypeface(this, "fonts/impact_0.ttf");
        tvMileageTrack.setTypeface(fromAsset);
        tvUphillMileageTrack.setTypeface(fromAsset);
        tvDurationTrack.setTypeface(fromAsset);
        tvAverageSpeedTrack.setTypeface(fromAsset);
        tvMaxSpeedTrack.setTypeface(fromAsset);
        tvDistributionSpeedTrack.setTypeface(fromAsset);
        tvEnergyTrack.setTypeface(fromAsset);
        tvTimeTrack.setTypeface(fromAsset);
        tvSpeedTrack.setTypeface(fromAsset);
        tvCaloriesTrack.setTypeface(fromAsset);
        TrackDetailPresenter presenter2;
        progressBarTrack.setProgress(0);
        presenter2 = new TrackDetailPresenter();
        presenter2.getSportLocationsNew(this, sportEntity.getId(), this);
        ivStartTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPlayState == normalStatus) {//正常状态
                    progressBarTrack.setProgress(0);
                    progressBarTrack.setClickable(false);
                    progressBarTrack.setEnabled(false);
                    progressBarTrack.setSelected(false);
                    progressBarTrack.setFocusable(false);
                    mPlayState = playingStatus;
                    mAMap.clear(true);
                    mapView.onCreate(savedInstanceState);// 此方法必须重写
                    mAMap = mapView.getMap();
                    //日间 切换 夜间
                    mAMap.setMapType(isMapTypeSatellite);//夜景地图，aMap是地图控制器对象。
                    ivStartTrack.setImageResource(R.mipmap.track_pause);
                    initMapView(conLatLon);
                    Observable.just(0).delay(1, TimeUnit.SECONDS).subscribe(new Consumer<Integer>() {
                        @Override
                        public void accept(Integer aLong) throws Exception {
                            //模拟从后台每间隔5秒的时间取一次数据，从取了20次
                            Message obtain = Message.obtain();
                            Log.e("1582080", "accept: mSingleMoveCarCustomThread");
                            obtain.obj = conLatLon;
                            mSingleMoveCarCustomThread.getMoveCarHandler().sendMessage(obtain);
                        }
                    });
                    mSingleMoveCarCustomThread = null;
                    mSingleMoveCarCustomThread = new MoveCarCustomThread(MapTrackActivity.this, mAMap);
                    mSingleMoveCarCustomThread.start();
                } else if (mPlayState == playingStatus) {//播放状态
                    mPlayState = pauseStatus;
                    ivStartTrack.setImageResource(R.mipmap.track_play);
                    mSingleMoveCarCustomThread.pauseMove();
                } else if (mPlayState == pauseStatus) {//暂停状态
                    ivStartTrack.setImageResource(R.mipmap.track_pause);
                    mPlayState = playingStatus;
                    mSingleMoveCarCustomThread.reStartMove();
                }
            }
        });

        progressBarTrack.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
                if (mPlayState == normalStatus) {//正常状态
                    if (dataList == null || dataList.size() == 0 || conLatLon == null || conLatLon.size() == 0) {
                        return;
                    }
                    if (mCarMarker != null) {
                        mCarMarker.remove();
                    }
                    mAMap.animateCamera(CameraUpdateFactory.changeLatLng(conLatLon.get(progress)));
                    playProgress(progress);//滑动到当前经纬度位置
                    addCarMaker(conLatLon.get(progress));
                }
            }

            //按住seekbar时会触发
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            //放开seekbar时会触发
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mainHandler.removeCallbacksAndMessages(null);
            }
        });

        ivMapTypeTrackDetail.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                ivMapTypeTrackDetail.setImageResource(R.mipmap.map_type_foc);
                new MapTypeDialog(MapTrackActivity.this, ivMapTypeTrackDetail, isMapTypeSatellite, new MapTypeDialog.IEventListener() {
                    @Override
                    public void onStandardTypeListener() {
                        isMapTypeSatellite = AMap.MAP_TYPE_NORMAL;
                        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                        tvTimeTrack.setTextColor(getResources().getColor(R.color.tvtimetrack2));
                        tvSpeedTrackContnet.setTextColor(getResources().getColor(R.color.tvtimetrack2));
                        tvSpeedTrack.setTextColor(getResources().getColor(R.color.tvtimetrack2));
                    }

                    @Override
                    public void onSatelliteTypeListener() {
                        isMapTypeSatellite = AMap.MAP_TYPE_SATELLITE;
                        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                        tvTimeTrack.setTextColor(getResources().getColor(R.color.tvtimetrack1));
                        tvSpeedTrack.setTextColor(getResources().getColor(R.color.tvtimetrack1));
                        tvSpeedTrackContnet.setTextColor(getResources().getColor(R.color.tvtimetrack1));
                    }

                    @Override
                    public void onNightTypeListener() {
                        isMapTypeSatellite = AMap.MAP_TYPE_NIGHT;
                        mAMap.setMapType(AMap.MAP_TYPE_NIGHT);// 夜间模式地图模式
                        tvTimeTrack.setTextColor(getResources().getColor(R.color.tvtimetrack));
                        tvSpeedTrack.setTextColor(getResources().getColor(R.color.tvtimetrack));
                        tvSpeedTrackContnet.setTextColor(getResources().getColor(R.color.tvtimetrack));
                    }

                    @Override
                    public void onDismiss() {
                        ivMapTypeTrackDetail.setImageResource(R.mipmap.map_type_nor);
                    }
                });
            }
        });
    }

    //画全部轨迹
    private void allTrack() {
        ImageView ivStart = new ImageView(this);
        ivStart.setImageResource(R.mipmap.map_start);
        mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromView(ivStart))).setPosition(conLatLon.get(0));
        ImageView ivEnd = new ImageView(this);
        ivEnd.setImageResource(R.mipmap.map_end);
        mAMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromView(ivEnd))).setPosition(conLatLon.get(conLatLon.size() - 1));
        //画线 有需要可以参照其他画线功能添加图片
        polylineOptions = new PolylineOptions().addAll(conLatLon).color(Color.argb(255, 124, 252, 0)).useGradient(true).width(15);
        mAMap.addPolyline(polylineOptions);
        moveCamera(conLatLon);
    }

    List<SportLocationEntity> dataList;

    @Override
    public void onTrackDetailSuccess(List<SportLocationEntity> dataList) {
        this.dataList = dataList;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onTrackDetailNewSuccess(SportLocationPageEntity<SportLocationEntity> resultEntity) {
        if (resultEntity == null) {
            return;
        }
        try {
            initTitleView(true, DateUtils.formatConversionTime("yyyy年MM月dd日", resultEntity.getStartTime()), "", new SingleClickListener() {
                @Override
                public void onSingleClick(View v) {
                }
            });
            tvMileageTrack.setText(resultEntity.getMileage());
            progressBarTrack.setMax(resultEntity.getList().size() - 1);
            tvUphillMileageTrack.setText(resultEntity.getAscent());
            tvAverageSpeedTrack.setText("" + resultEntity.getAvgSpeed());
            tvMaxSpeedTrack.setText("" + resultEntity.getMaxSpeed());
            tvDurationTrack.setText(resultEntity.getRideTime() + "");
            tvDistributionSpeedTrack.setText("" + resultEntity.getPlanSpeed());
            tvEnergyTrack.setText(resultEntity.getReduction() + " kg");
            tvCaloriesTrack.setText(resultEntity.getCalorie() + " kcal");
            for (SportLocationEntity sportLocationEntity : resultEntity.getList()) {
                // LatLng latLng = new LatLng(sportLocationEntity.getLat(), sportLocationEntity.getLon());
                LatLng latLng = AMapUtil.convertLatLng(this, new LatLng(sportLocationEntity.getLat(), sportLocationEntity.getLon()), CoordinateConverter.CoordType.GPS);
                conLatLon.add(latLng);
            }
            //  addStartMaker(conLatLon.get(0));
            allTrack();
            onTrackDetailSuccess(resultEntity.getList());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int errCode, String errResult) {
    }

    public Handler mMainHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case EventType.MapMove: // 小车挪动
                    if (msg.obj instanceof LatLng) {
                        // 第一次小车挪动的时候给一个默认级别14，以后移动就不管了，避免第一次地图级别太大而导致的显示不合理问题
                        LatLng latLng = (LatLng) msg.obj;
                        if (isCarFirstMove) {
                            mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                        } else {
                            mAMap.animateCamera(CameraUpdateFactory.changeLatLng(latLng));
                        }
                        isCarFirstMove = false;
                    }
                    break;
            }
            return false;
        }
    });

    //单次普通移动
    @SuppressLint("CheckResult")
    private void startSingleCustomMove() {
    }

    private List<LatLng> initMapView(List<LatLng> listLatLng) {
        //在小车移动之前一定要把小车移动到第一个点的位置，否则地图会闪屏
        mLatLngList.clear();
        animateCamera(listLatLng.get(0));
        //创建小车
        addCarMaker(listLatLng.get(0));
        //创建轨迹线
        addCarMovePolyLine();
        return listLatLng;
    }

    private Marker addCarMaker(LatLng latLng) {
        int height = 60;
        int width = 60;
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.mipmap.car_loatonig);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.setFlat(true);
        markerOptions.anchor(0.5f, 0.5f);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker));
        mCarMarker = mAMap.addMarker(markerOptions);
        mCarMarker.setPosition(latLng);
        return mCarMarker;
    }

    //跟随点移动,添加轨迹
    private void addCarMovePolyLine() {
        mMovePolyline = mAMap.addPolyline(new PolylineOptions().width(12).color(Color.argb(255, 124, 252, 0)));// 地图上增加一条默认的轨迹线
        mMovePolyline.setPoints(null);
    }

    private void animateCamera(LatLng latLng) {
        mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void moveCamera(List<LatLng> a) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (int i = 0; i < a.size(); i++) {
            builder.include(a.get(i));
        }
        if (!builder.build().contains(mAMap.getCameraPosition().target)) {
            mAMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));
        }
    }


    //播放结束
    public void playTcomplete() {
        ivStartTrack.postDelayed(new Runnable() {//回到主线程
            @Override
            public void run() {
                ivStartTrack.setImageResource(R.mipmap.track_play);
                mPlayState = normalStatus;
                mSingleMoveCarCustomThread = null;
                progressBarTrack.setClickable(true);
                progressBarTrack.setEnabled(true);
                progressBarTrack.setSelected(true);
                progressBarTrack.setFocusable(true);
                if (mCarMarker != null) {
                    mCarMarker.remove();
                }
            }
        }, 1000);
    }

    Handler mainHandler;

    //播放中,更新进度条
    public void playProgress(int lastLength) {
        if (dataList != null) {
            if (mainHandler == null) {
                mainHandler = new Handler(Looper.getMainLooper());
            }
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    //已在主线程中，可以更新UI
                    progressBarTrack.setProgress(lastLength);
                    String times = DateUtils.formatConversionTime("HH:mm:ss", dataList.get(lastLength).getTime());
                    tvTimeTrack.setText(times);
                    tvSpeedTrack.setText(dataList.get(lastLength).getSpeed() + "");
                    Logger.error(times);
                }
            });
        }
    }

    public void addStartMaker(LatLng latLng) {
        Marker marker = null;
        if (marker == null) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.setFlat(true);
            markerOptions.anchor(0.5f, 0.5f);
            marker = mAMap.addMarker(markerOptions);
        }
        marker.setPosition(latLng);
        //    growInto(marker);
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        mAMap.animateCamera(update, 1000, callback);
    }

}
