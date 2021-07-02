package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;

import java.util.List;

/**
 * 反馈
 */
public class FeedbackAdapter extends BaseRecyclerAdapter<FeedbackEntity>
{
    private IEventListener eventListener;

    public FeedbackAdapter(Context context, List<FeedbackEntity> dataList, IEventListener<FeedbackEntity> eventListener)
    {
        super(context, dataList, false);
        this.eventListener = eventListener;
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_feedback;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, FeedbackEntity data, int position)
    {
        TextView cb_content = holder.findView(R.id.cb_content_feedback_item);
        cb_content.setText(data.getName());
        cb_content.setEnabled(!data.isCheck());
        cb_content.setOnClickListener(new SingleClickListener()
        {
            @Override
            public void onSingleClick(View v)
            {
                if (eventListener != null) eventListener.onCheck(data, position);
            }
        });
    }

    public void releaseData()
    {
        for (FeedbackEntity entity : getDataList())
        {
            entity.setCheck(false);
        }
    }
}
