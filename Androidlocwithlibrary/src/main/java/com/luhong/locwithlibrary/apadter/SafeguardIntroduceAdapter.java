package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;

import java.util.List;

public class SafeguardIntroduceAdapter extends BaseRecyclerAdapter<SafeguardIntroduceEntity>
{

    public SafeguardIntroduceAdapter(Context context, List<SafeguardIntroduceEntity> dataList)
    {
        super(context, dataList, false);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_safeguard_introduce;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, SafeguardIntroduceEntity data, int position)
    {
        TextView tv_name = holder.findView(R.id.tv_name_safeguard_introduce);
        TextView tv_feeRate = holder.findView(R.id.tv_feeRate_safeguard_introduce);
        TextView tv_payRate = holder.findView(R.id.tv_payRate_safeguard_introduce);
        if (data != null)
        {
            tv_name.setText(data.getName());
            if (data.getFeeRate() > 1)
            {
                tv_feeRate.setText("" + data.getFeeRate());
            } else
            {
                tv_feeRate.setText("车辆估值*" + data.getFeeRate() * 100 + "%");
            }
            tv_payRate.setText("车辆估值*" + data.getPayRate() * 100 + "%");
        }
    }

}
