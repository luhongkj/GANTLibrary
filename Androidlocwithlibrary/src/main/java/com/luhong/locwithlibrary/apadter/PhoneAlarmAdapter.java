package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.utils.BaseUtils;

import java.util.List;

/**
 * 电话告警
 */
public class PhoneAlarmAdapter extends BaseRecyclerAdapter<PhoneAlarmEntity>
{

    public PhoneAlarmAdapter(Context context, List<PhoneAlarmEntity> dataList, boolean isOpenLoadMore)
    {
        super(context, dataList, isOpenLoadMore);
    }


    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_phone_alarm;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, PhoneAlarmEntity data, int position)
    {
        TextView tv_phoneNo = holder.findView(R.id.tv_phoneNo_alarmRecord_item);
        TextView tv_date = holder.findView(R.id.tv_date_alarmRecord_item);
        if (data != null)
        {
            tv_phoneNo.setText("" + BaseUtils.phoneEncrypt(data.getMobile()));
            tv_date.setText(data.getCreateTime());
        }
    }

}
