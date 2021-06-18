package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.TextView;


import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.FlowBillEntity;

import java.util.List;

/**
 * 流水账单
 */
public class FlowBillAdapter extends BaseRecyclerAdapter<FlowBillEntity> {

    public FlowBillAdapter(Context context, List<FlowBillEntity> dataList, boolean isOpenLoadMore) {
        super(context, dataList, isOpenLoadMore);
    }

    @Override
    protected int initLayoutId() {
        return R.layout.adapter_flow_bill;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, FlowBillEntity data, int position) {
        TextView tv_rechargeType = holder.findView(R.id.tv_rechargeType_flowBill_item);
        TextView tv_amount = holder.findView(R.id.tv_amount_flowBill_item);
        if (data != null) {
            holder.setText(R.id.tv_date_flowBill_item, data.getOptime());
            if (data.getRechargeAccount().equals("null")) {
                data.setRechargeAccount("0.00");
            }
            if (data.getSourceType() == 1) {
                if (!TextUtils.isEmpty(data.getContent())) {
                    tv_rechargeType.setText(data.getContent());
                } else {
                    tv_rechargeType.setText(data.getRechargeType());
                }
                double moneys = Double.parseDouble(data.getRechargeAccount());
                float flowFee = (float) data.getFlowFee();
                float jg = (float) (moneys / flowFee);
                tv_amount.setText("+" + (int) jg + "个月");

            } else if (data.getSourceType() == 2) {
                tv_rechargeType.setText("设备(ID:" + data.getSn() + ")" + data.getMonth() + "月" + data.getFeeType());
                double moneys = Double.parseDouble(data.getCostMoney());
                float flowFee = (float) data.getFlowFee();
                float jg = (int) (moneys / flowFee);
                tv_amount.setText("-" + (int) jg + "个月");
            } else if (data.getSourceType() == 3) {
                tv_rechargeType.setText(data.getContent());
                double moneys = Double.parseDouble(data.getRechargeAccount());
                float flowFee = (float) data.getFlowFee();
                float jg = (float) (moneys / flowFee);
                tv_amount.setText("-" + (int) jg + "个月");
            }
        }
    }

}
