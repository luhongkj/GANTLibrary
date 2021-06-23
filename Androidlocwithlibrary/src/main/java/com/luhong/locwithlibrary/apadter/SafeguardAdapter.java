package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.fragment.SafeguardFragment;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;

import java.util.List;

public class SafeguardAdapter extends BaseRecyclerAdapter<SafeguardEntity> {
    private int dataType;

    public SafeguardAdapter(Context context, List<SafeguardEntity> dataList, boolean isOpenLoadMore, int dataType) {
        super(context, dataList, isOpenLoadMore);
        this.dataType = dataType;
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_safeguard;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, SafeguardEntity data, int position) {
        ImageView iv_icon = holder.findView(R.id.iv_Icon_safeguard_item);
        TextView tv_name = holder.findView(R.id.tv_name_safeguard_item);
        TextView tv_description = holder.findView(R.id.tv_description_safeguard_item);
        if (data != null) {
            ImageLoadUtils.loadCircle(data.getPicture(), R.mipmap.safeguard_icon, R.mipmap.safeguard_icon, iv_icon);
            if (dataType == SafeguardFragment.dataType_home) {
                tv_name.setText(data.getName());
                tv_description.setText(data.getRemark());
                tv_description.setTextColor(mContext.getResources().getColor(R.color.text_6));
            } else if (dataType == SafeguardFragment.dataType_min) {
                tv_name.setText(data.getInsureSetName());
                tv_description.setText(data.getStatusShow());
                tv_description.setTextColor(mContext.getResources().getColor(R.color.safeguard_text));
            }
        }
    }

}
