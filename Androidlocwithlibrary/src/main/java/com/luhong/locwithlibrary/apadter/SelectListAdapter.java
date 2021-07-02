package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.SelectListEntity;

import java.util.List;

/**
 * 列表单选
 */
public class SelectListAdapter extends BaseRecyclerAdapter<SelectListEntity>
{

    public SelectListAdapter(Context context, List<SelectListEntity> dataList)
    {
        super(context, dataList, false);
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_select_list;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, SelectListEntity data, int position)
    {
        if (!TextUtils.isEmpty(data.getContent()))
            holder.setText(R.id.tv_content_selectList_item, data.getContent());
        if (!TextUtils.isEmpty(data.getVehicleBrand()))
            holder.setText(R.id.tv_brand_selectList_item, data.getVehicleBrand());
        View v_line = holder.findView(R.id.v_line_selectList_item);
        if (position == getDatasSize() - 1)
        {
            v_line.setVisibility(View.GONE);
        } else
        {
            v_line.setVisibility(View.VISIBLE);
        }
    }

}
