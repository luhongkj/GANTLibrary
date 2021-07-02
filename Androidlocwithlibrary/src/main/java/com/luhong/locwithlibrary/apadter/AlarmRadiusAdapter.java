package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.AlarmRadiusEntity;

import java.util.List;

/**
 * 围栏半径
 */
public class AlarmRadiusAdapter extends BaseRecyclerAdapter<AlarmRadiusEntity>
{


    public AlarmRadiusAdapter(Context context, List<AlarmRadiusEntity> dataList)
    {
        super(context, dataList, false);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_alarm_radius;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, AlarmRadiusEntity data, int position)
    {
        holder.setText(R.id.tv_content_alarmRadius_item, data.getContent() + "m");
        ImageView iv_check = holder.findView(R.id.iv_check_alarmRadius_item);
        if (data.isCheck())
        {
            iv_check.setImageResource(R.mipmap.alarm_radius_select_foc);
        } else
        {
            iv_check.setImageResource(R.mipmap.alarm_radius_select_nor);
        }
    }

    public void releaseData()
    {
        for (AlarmRadiusEntity radiusEntity : getDataList())
        {
            radiusEntity.setCheck(false);
        }
    }
}
