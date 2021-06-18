package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.R2;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.DateUtils;
import com.luhong.locwithlibrary.utils.Logger;

import java.util.List;

public class SportRecordAdapter extends BaseRecyclerAdapter<SportEntity.TrackList> {
    private ISportRecordListener sportRecordListener;

    public SportRecordAdapter(Context context, List<SportEntity.TrackList> dataList, ISportRecordListener sportRecordListener) {
        super(context, dataList, true);
        this.sportRecordListener = sportRecordListener;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_sport_record;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, final SportEntity.TrackList data, final int position) {
        ImageView iv_type = holder.findView(R.id.iv_type_sportRecord_item);
        TextView tv_date = holder.findView(R.id.tv_date_sportRecord_item);
        TextView tv_from = holder.findView(R.id.tv_from_sportRecord_item);
        TextView tv_mileage = holder.findView(R.id.tv_mileage_sportRecord_item);
        TextView tv_trackName = holder.findView(R.id.tv_trackName_sportRecord_item);

        iv_type.setImageResource(R.mipmap.icon_m_cycle);
        if (!TextUtils.isEmpty(data.getSn()))
            tv_from.setText("来自设备-" + data.getSn().substring(data.getSn().length() - 4));

        if (!TextUtils.isEmpty(data.getStartTime()))
            tv_date.setText(DateUtils.formatSportDateTime(data.getStartTime()));
        if (!TextUtils.isEmpty(data.getName())) {
            tv_trackName.setText(data.getName());
        } else {
            if (!TextUtils.isEmpty(data.getStartTime()))
                tv_trackName.setText("我的轨迹" + data.getStartTime().substring(5, 10).replace("-", ""));
        }
        tv_mileage.setText(data.getMileage() + "");
        tv_trackName.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                sportRecordListener.onEdit(data, position);
            }
        });
    }

    public int getPositionForSection(String firstYear) {
        for (int i = 0; i < getDatasSize(); i++) {
            String sortStr = getDataList().get(i).getStartTime();
            if (firstYear.equalsIgnoreCase(sortStr)) {
                return i;
            }
        }
        return -1;
    }

    public interface ISportRecordListener {
        void onEdit(SportEntity.TrackList data, int position);
    }
}
