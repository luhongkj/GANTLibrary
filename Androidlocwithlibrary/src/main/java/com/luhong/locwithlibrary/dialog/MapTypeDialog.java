package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.listener.SingleClickListener;


/**
 * Created by ITMG on 2019/10/30 0030.
 */
public class MapTypeDialog extends BasePopupWindow {
    private ImageView iv_standard;
    private ImageView iv_satellite;
    private ImageView iv_night;
    private IEventListener eventListener;
    private String isMapTypeSatellitestr="";
    final String MAP_TYPE_NORMAL = "1";
    final String MAP_TYPE_SATELLITE = "2";
    final String MAP_TYPE_NIGHT = "3";

    public MapTypeDialog(Activity context, View parentView, int isMapTypeSatellite, IEventListener eventListener) {
        super(context, parentView, false, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        this.isMapTypeSatellitestr = isMapTypeSatellite+"";
        this.eventListener = eventListener;

        switch (isMapTypeSatellitestr) {
            case MAP_TYPE_NORMAL://  mAMap.setMapType(AMap.MAP_TYPE_NORMAL);// 矢量地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_foc);
                iv_satellite.setImageResource(R.mipmap.map_satellite_nor);
                iv_night.setImageResource(R.mipmap.map_night_no);
                break;
            case MAP_TYPE_SATELLITE://   mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);// 卫星地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_nor);
                iv_satellite.setImageResource(R.mipmap.map_satellite_foc);
                iv_night.setImageResource(R.mipmap.map_night_no);
                break;
            case MAP_TYPE_NIGHT://  mAMap.setMapType(AMap.MAP_TYPE_NIGHT);// 夜间模式地图模式
                iv_standard.setImageResource(R.mipmap.map_standard_nor);
                iv_satellite.setImageResource(R.mipmap.map_satellite_nor);
                iv_night.setImageResource(R.mipmap.map_night_yes);
                break;
        }

    }

    public void setEventListener(IEventListener eventListener) {
        this.eventListener = eventListener;
    }

    @Override
    public int initLayoutId() {
        return R.layout.dialog_map_type;
    }

    @Override
    public void initView() {
        iv_standard = findView(R.id.iv_standard_mapType);
        iv_satellite = findView(R.id.iv_satellite_mapType);
        iv_night = findView(R.id.iv_night_mapType);
    }

    @Override
    public void onEventListener() {
        iv_standard.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (eventListener != null) eventListener.onStandardTypeListener();
                dismiss();
            }
        });
        iv_satellite.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (eventListener != null) eventListener.onSatelliteTypeListener();
                dismiss();
            }
        });
        iv_night.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (eventListener != null) eventListener.onNightTypeListener();
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (eventListener != null) eventListener.onDismiss();
    }

    public interface IEventListener {
        void onStandardTypeListener();

        void onSatelliteTypeListener();

        void onNightTypeListener();

        void onDismiss();
    }
}
