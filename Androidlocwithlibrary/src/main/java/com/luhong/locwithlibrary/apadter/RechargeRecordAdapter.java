package com.luhong.locwithlibrary.apadter;

import android.content.Context;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;

import java.util.List;

/**
 * 充值记录
 */
public class RechargeRecordAdapter extends BaseRecyclerAdapter<RechargeRecordEntity> {

    public RechargeRecordAdapter(Context context, List<RechargeRecordEntity> dataList, boolean isOpenLoadMore) {
        super(context, dataList, isOpenLoadMore);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_recharge_record;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, RechargeRecordEntity data, int position) {
        holder.setText(R.id.tv_orderNO_recharge_record, data.getOrderNo());
        holder.setText(R.id.tv_amount_recharge_record, "￥" + data.getAccount());
        holder.setText(R.id.tv_realPrice_recharge_record, "￥" + data.getRealPrice());
        holder.setText(R.id.tv_payType_recharge_record, data.getTypeShow());
        holder.setText(R.id.tv_payDate_recharge_record, data.getRechargeTime());
    }

}
