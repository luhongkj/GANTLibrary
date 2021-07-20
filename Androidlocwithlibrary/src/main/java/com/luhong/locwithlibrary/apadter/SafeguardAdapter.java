package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.fragment.SafeguardFragment;
import com.luhong.locwithlibrary.utils.ImageLoadUtils;

import java.util.List;

import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_CLAIMED;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_EXPIRED;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_FAILED;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_FINAL_TRIAL;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_FIRST_TRIAL;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_GUARANTEED;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_OVERDUE;
import static com.luhong.locwithlibrary.entity.SafeguardEntity.TYPE_PAY;

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
                /**
                 *    public static final int TYPE_FIRST_TRIAL = 1;//1待初审
                 *     public static final int TYPE_FINAL_TRIAL = 2;//2待终审
                 *     public static final int TYPE_FAILED = 3;//3审核未过
                 *     public static final int TYPE_PAY = 4;//4待支付
                 *     public static final int TYPE_GUARANTEED = 5;//5已保障
                 *     public static final int TYPE_EXPIRED = 6;//6已过期
                 *     public static final int TYPE_CLAIMED = 7;//7已理赔
                 *     public static final int TYPE_OVERDUE = 8;//8过期未支付
                 */
                switch (data.getStatus()) {
                    case TYPE_FIRST_TRIAL:
                        tv_description.setTextColor(Color.parseColor("#FD6935"));
                        break;
                    case TYPE_FINAL_TRIAL:
                        tv_description.setTextColor(Color.parseColor("#FD6935"));
                        break;
                    case TYPE_FAILED:
                        tv_description.setTextColor(Color.parseColor("#FD6935"));
                        break;
                    case TYPE_PAY:
                        tv_description.setTextColor(Color.parseColor("#3FA7F5"));
                        break;

                    case TYPE_GUARANTEED:
                        tv_description.setTextColor(Color.parseColor("#43DBC1"));
                        break;

                    case TYPE_EXPIRED:
                        tv_description.setTextColor(Color.parseColor("#8A8888"));
                        break;
                    case TYPE_CLAIMED:
                        tv_description.setTextColor(Color.parseColor("#8A8888"));
                        break;
                    case TYPE_OVERDUE:
                        tv_description.setTextColor(Color.parseColor("#8A8888"));
                        break;
                    default:
                        tv_description.setTextColor(Color.parseColor("#FF0000"));
                        break;
                }
            }
        }
    }

}
