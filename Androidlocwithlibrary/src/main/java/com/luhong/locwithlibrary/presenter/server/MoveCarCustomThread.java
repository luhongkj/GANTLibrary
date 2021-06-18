package com.luhong.locwithlibrary.presenter.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;
import com.amap.api.maps.model.animation.ScaleAnimation;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.enums.MOVE_STATE;
import com.luhong.locwithlibrary.ui.MapTrackActivity;
import com.luhong.locwithlibrary.utils.TrackMoveUtil;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Description: <MoveCarCustomThread><br>
 * Author:      mxdl<br>
 * Date:        2019/7/10<br>
 * Version:     V1.0.0<br>
 * Update:     <br>
 */
public class MoveCarCustomThread extends Thread {
    public static final String TAG = MoveCarCustomThread.class.getSimpleName();
    private Handler moveCarHandler;//发送数据的异步消息处理器
    private Object lock = new Object();//线程锁
    private boolean moveing = false;//是否线程正在移动
    private boolean pause = false;//暂停状态，为true则暂停
    private boolean stop = false;//停止状态，为true则停止移动
    private final int ZOOM = 15;
    private WeakReference<MapTrackActivity> mActivityWeakReference;//防止内存Activity导致的内容泄漏
    private MOVE_STATE currMoveState = MOVE_STATE.START_STATUS;

    public void setCurrMoveState(MOVE_STATE currMoveState) {
        this.currMoveState = currMoveState;
    }

    public MOVE_STATE getCurrMoveState() {
        return currMoveState;
    }

    AMap aMap;

    public MoveCarCustomThread(MapTrackActivity activity, AMap aMap) {
        mActivityWeakReference = new WeakReference<>(activity);
        this.aMap = aMap;
    }

    //暂停移动
    public void pauseMove() {
        pause = true;
    }

    //设置暂停之后，再次移动调用它
    public void reStartMove() {
        synchronized (lock) {
            pause = false;
            lock.notify();
        }
    }

    public void stopMove() {
        stop = true;
        if (moveCarHandler != null) {
            moveCarHandler.removeCallbacksAndMessages(null);
        }
        if (mActivityWeakReference.get() != null) {
            mActivityWeakReference.get().mLatLngList.clear();
            mActivityWeakReference.get().mMainHandler.removeCallbacksAndMessages(null);
        }
    }

    public Handler getMoveCarHandler() {
        return moveCarHandler;
    }

    public boolean isMoveing() {
        return moveing;
    }

    @Override
    public void run() {
        super.run();
        //设置该线程为loop线程
        Looper.prepare();
        moveCarHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                //通过锁保证发过来的数据同步入列
                synchronized (lock) {
                    if (msg.obj != null && msg.obj instanceof List) {
                        List<LatLng> latLngList = (List<LatLng>) msg.obj;
                        moveCoarseTrack(latLngList, aMap);
                    }
                }
                return false;
            }
        });
        //启动loop线程
        Looper.loop();
    }

    private void moveCoarseTrack(List<LatLng> latLngList, AMap mAMap) {
        if (latLngList == null || latLngList.size() == 0 || latLngList.size() == 1) {
            return;
        }
        MapTrackActivity maptrackactivity = mActivityWeakReference.get();
        try {
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngList.get(0), ZOOM, 0, 30)), mAMap, null);
            Thread.sleep(500);//休眠3秒
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngList.get(0), ZOOM, 50, 80)), mAMap, null);
            Thread.sleep(1000);//休眠3秒
            startMarker(latLngList.get(0));
            Thread.sleep(1000);//休眠3秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.v(TAG, "moveCoarseTrack start.........................................................");
        long startTime = System.currentTimeMillis();
        int step = TrackMoveUtil.getStep(latLngList);// 通过距离,计算轨迹动画运动步数
        Log.v(TAG, "move step:" + step);
        float distance = TrackMoveUtil.getDistance(latLngList);
        Log.v(TAG, "move distance:" + distance);
        double mTimeInterval = TrackMoveUtil.getMoveTime(distance, step);// 通过距离,计算轨迹动画时间间隔
        mTimeInterval = 5;// 每走一步停止10毫秒
        Log.v(TAG, "move mTimeInterval:" + mTimeInterval);
        CameraPosition cameraPosition = new CameraPosition(latLngList.get(0), ZOOM, 50, 100);
        mAMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        moveing = true;
        for (int i = 0; i < latLngList.size() - 1; i++) {
            // 暂停状态，线程停止了
            if (pause) {
                movePause();
            }
            if (stop) {
                break;
            }
            moveing = true;
            LatLng startLatLng = latLngList.get(i);
            LatLng endLatLng = latLngList.get(i + 1);
            moveCar(startLatLng, endLatLng, maptrackactivity);
            moveLine(startLatLng, maptrackactivity);
            moveCamera(startLatLng, maptrackactivity);
            double slope = TrackMoveUtil.getSlope(startLatLng, endLatLng);// 计算两点间的斜率
            double intercept = TrackMoveUtil.getInterception(slope, startLatLng);// 根据点和斜率算取截距
            boolean isReverse = (startLatLng.latitude > endLatLng.latitude);// 是不是正向的标示（向上设为正向）
            double xMoveDistance = isReverse ? TrackMoveUtil.getXMoveDistance(slope) : -1 * TrackMoveUtil.getXMoveDistance(slope);
            maptrackactivity.playProgress(i);
            // 应该对经纬度同时处理
            double sleep = 0;
            int flag = 0;
            for (double j = startLatLng.latitude; !((j >= endLatLng.latitude) ^ isReverse); j = j - xMoveDistance) {
                // 非暂停状态地图才进行跟随移动
                if (pause) {
                    movePause();
                }
                if (stop) {
                    break;
                }
                moveing = true;
                flag++;
                if (slope != Double.MAX_VALUE) {
                    startLatLng = new LatLng(j, (j - intercept) / slope);
                } else {
                    startLatLng = new LatLng(j, startLatLng.longitude);
                }
                moveCar(startLatLng, maptrackactivity);
                moveLine(startLatLng, maptrackactivity);
                if (flag % 100 == 0) {
                    moveCamera(startLatLng, maptrackactivity);
                }
                // 如果间隔时间小于1毫秒,则略过当前休眠,累加直到休眠时间到1毫秒:会损失精度
                if (mTimeInterval < 1) {
                    sleep += mTimeInterval;
                    if (sleep >= 1) {
                        Log.v(TAG, "sleep:" + sleep);
                        SystemClock.sleep((long) sleep);
                        sleep = 0;
                    }
                } else {
                    SystemClock.sleep((long) mTimeInterval);
                }
            }
        }
        long endTime = System.currentTimeMillis();
        moveing = false;
        endMarker(latLngList.get(latLngList.size() - 1));
        newLatLngBoundss(latLngList, mAMap);
  //      stopMove();
        maptrackactivity.playTcomplete();
        Log.v(TAG, "endTime:" + endTime);   //结束
        Log.v(TAG, "run mTimeInterval:" + (endTime - startTime));
        Log.v(TAG, "moveCoarseTrack end.........................................................");
    }

    /**
     * 添加一个从地上生长的Marker
     */
    public void startMarker(LatLng latlng) {
        Marker growMarker;
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.map_start))
                .position(latlng);
        growMarker = aMap.addMarker(markerOptions);
        startGrowAnimation(growMarker);
    }

    /**
     * 添加一个从地上生长的Marker
     */
    public void endMarker(LatLng latlng) {
        Marker growMarker;
        MarkerOptions markerOptions = new MarkerOptions().icon(BitmapDescriptorFactory
                .fromResource(R.mipmap.map_end))
                .position(latlng);
        growMarker = aMap.addMarker(markerOptions);
        startGrowAnimation(growMarker);
    }

    /**
     * 地上生长的Marker
     */
    private void startGrowAnimation(Marker growMarker) {
        if (growMarker != null) {
            Animation animation = new ScaleAnimation(0, 1, 0, 1);
            animation.setInterpolator(new LinearInterpolator());
            //整个移动所需要的时间
            animation.setDuration(1000);
            //设置动画
            growMarker.setAnimation(animation);
            //开始动画
            growMarker.startAnimation();
        }
    }

    private LatLngBounds latLngBounds;

    private void newLatLngBoundss(List<LatLng> list, AMap aMap) {
        // 构建 轨迹的显示区域
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : list) {
            builder.include(latLng);
        }
        latLngBounds = builder.build();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*
                 this.zoom = var2;
                this.tilt = var3;
                this.bearing = ((double)var4 <= 0.0D ? var4 % 360.0F + 360.0F : var4) % 360.0F;
                changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngList.get(0), 17, 0, 30)), mAMap, null);
            Thread.sleep(500);//休眠3秒
            changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLngList.get(0), 17, 50, 80)), mAMap, null);
                 */
                changeCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(list.get(list.size() - 1), ZOOM, 0, 0)), aMap, null);
                try {
                    Thread.sleep(1000);//休眠3秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                aMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)); // 设置指定的可视区域地图
            }
        }, 1000);
    }

    private void moveLine(LatLng startLatLng, MapTrackActivity MapTrackActivity) {
        MapTrackActivity.mLatLngList.add(startLatLng);// 向轨迹集合增加轨迹点
        MapTrackActivity.mMovePolyline.setPoints(MapTrackActivity.mLatLngList);// 轨迹画线开始
    }

    boolean siHandle;

    private void moveCar(final LatLng startLatLng, final LatLng endLatLng, MapTrackActivity MapTrackActivity) {
        moveCar(startLatLng, MapTrackActivity);
        if (MapTrackActivity.mCarMarker != null) {
            MapTrackActivity.mCarMarker.setRotateAngle((float) TrackMoveUtil.getAngle(startLatLng, endLatLng));// 设置小车车头的方向
            CameraPosition newCamPos2 = new CameraPosition(startLatLng, aMap.getMaxZoomLevel(), 70, 0);
            aMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos2), 4000, null);
            if (siHandle) {//延时转动地图
                siHandle = !siHandle;
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        CameraPosition cameraPosition = new CameraPosition.Builder().bearing((float) getDirection(startLatLng, endLatLng)).build();
                        //  CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng1).tilt(70).build();
                        aMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        //要执行的操作
                        siHandle = !siHandle;
                    }
                };
                Timer timer = new Timer();
                timer.schedule(task, 1500);//2秒后执行TimeTask的run方法
            }
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 100)); // 设置指定的可视区域地图
        }
    }

    /**
     * 计算方向
     * @param p1
     * @param p2
     * @return
     */
    private double getDirection(LatLng p1, LatLng p2) {
        double lat1 = p1.latitude;
        double lon1 = p1.longitude;
        double lat2 = p2.latitude;
        double lon2 = p2.longitude;
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);
        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        double deltaFI = Math.log(Math.tan(lat2 / 2 + Math.PI / 4) / Math.tan(lat1 / 2 + Math.PI / 4));
        double deltaLON = Math.abs(lon1 - lon2) % 180;
        double theta = Math.atan2(deltaLON, deltaFI);
        double endDirection = Math.toDegrees(theta);
        return Math.toDegrees(theta);
    }

    private void moveCar(LatLng startLatLng, MapTrackActivity MapTrackActivity) {
        if (MapTrackActivity.mCarMarker != null) {
            MapTrackActivity.mCarMarker.setPosition(startLatLng);// 小车移动
            aMap.moveCamera(CameraUpdateFactory.changeLatLng(startLatLng));
        }
    }

    private void movePause() {
        try {
            lock.wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void moveCamera(LatLng startLatLng, MapTrackActivity MapTrackActivitys) {
        Message message = Message.obtain();
        message.what = MapTrackActivity.EventType.MapMove;
        message.obj = startLatLng;
        MapTrackActivitys.mMainHandler.sendMessage(message);
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update, AMap mAMap, AMap.CancelableCallback callback) {
        mAMap.animateCamera(update, 1000, callback);
    }

}
