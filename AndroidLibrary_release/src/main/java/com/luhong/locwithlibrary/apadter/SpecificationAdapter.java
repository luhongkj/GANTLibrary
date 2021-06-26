package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.DateUtils;

import java.util.List;

public class SpecificationAdapter extends BaseRecyclerAdapter<DeviceEntity> {
    private ISportRecordListener sportRecordListener;

    public SpecificationAdapter(Context context, List<DeviceEntity> dataList, ISportRecordListener sportRecordListener) {
        super(context, dataList, true);
        this.sportRecordListener = sportRecordListener;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_specifi;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, final DeviceEntity data, final int position) {
        TextView tv_left_title = holder.findView(R.id.tv_left_title);
        TextView light_work_model_name = holder.findView(R.id.light_work_model_name);
        light_work_model_name.setText(data.getUnitModelCn());
        tv_left_title.setText(data.getNickName());
        LinearLayout ll_flow = holder.findView(R.id.ll_flow);
        ll_flow.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                sportRecordListener.onEdit(data, position);
            }
        });
    }


    public interface ISportRecordListener {
        void onEdit(DeviceEntity data, int position);
    }
}
