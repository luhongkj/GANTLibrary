package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.CarEntity;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import java.util.List;

public class VehiclechoiceAdapter extends BaseRecyclerAdapter<VehicleListEntity> {
    private ISportRecordListener sportRecordListener;

    public VehiclechoiceAdapter(Context context, List<VehicleListEntity> dataList, ISportRecordListener sportRecordListener) {
        super(context, dataList, true);
        this.sportRecordListener = sportRecordListener;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_vehicle_chioce;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, final VehicleListEntity data, final int position) {
        TextView tv_left_title = holder.findView(R.id.tv_left_title);
        tv_left_title.setText(data.getVehicleName());
        ImageView iv_check_alarmRadius_item = holder.findView(R.id.iv_check_alarmRadius_item);
        if (data.getFlag()) {
            iv_check_alarmRadius_item.setImageResource(R.mipmap.alarm_radius_select_foc);
        } else {
            iv_check_alarmRadius_item.setImageResource(R.mipmap.alarm_radius_select_nor);
        }
        LinearLayout ll_flow = holder.findView(R.id.ll_flow);
        ll_flow.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                sportRecordListener.onEdit(data, position);
            }
        });
    }


    public interface ISportRecordListener {
        void onEdit(VehicleListEntity data, int position);
    }
}
