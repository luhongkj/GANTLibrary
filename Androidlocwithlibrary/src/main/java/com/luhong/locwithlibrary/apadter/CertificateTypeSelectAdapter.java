package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import java.util.List;

/**
 * 选择证件类型
 */
public class CertificateTypeSelectAdapter extends BaseRecyclerAdapter<String>
{

    private IEventListener eventListener;

    public CertificateTypeSelectAdapter(Context context, List<String> dataList, IEventListener eventListener)
    {
        super(context, dataList, false);
        this.eventListener = eventListener;
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_device_select;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, final String data, final int position)
    {
        TextView tv_content = holder.findView(R.id.tv_content_deviceSelect_item);
        if (data != null)
        {
            tv_content.setText(data);
            tv_content.setOnClickListener(new SingleClickListener()
            {
                @Override
                public void onSingleClick(View v)
                {
                    if (eventListener != null)
                    {
                        eventListener.onCheck(data, position);
                    }
                }
            });
        }
    }

    public interface IEventListener
    {
        void onCheck(String data, int position);
    }
}
