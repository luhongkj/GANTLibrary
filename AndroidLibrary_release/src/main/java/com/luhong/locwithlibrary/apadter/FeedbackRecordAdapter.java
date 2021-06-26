package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseRecyclerAdapter;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.listener.SingleClickListener;
import com.luhong.locwithlibrary.utils.ResUtils;

import java.util.List;

/**
 * Created by ITMG on 2020-01-06.
 */
public class FeedbackRecordAdapter extends BaseRecyclerAdapter<FeedbackRecordEntity>
{
    private IEventListener eventListener;

    public FeedbackRecordAdapter(Context context, List<FeedbackRecordEntity> dataList, IEventListener eventListener)
    {
        super(context, dataList, true);
        this.eventListener = eventListener;
    }

    @Override
    protected int initLayoutId()
    {
        return R.layout.adapter_feedback_record;
    }

    @Override
    protected void convert(RecyclerViewHolder holder, FeedbackRecordEntity data, int position)
    {
        View v_line = holder.findView(R.id.v_line_feedback_record_item);
        LinearLayout ll_content = holder.findView(R.id.ll_content_feedback_record_item);
        TextView tv_type = holder.findView(R.id.tv_type_feedback_record_item);
        TextView tv_status = holder.findView(R.id.tv_status_feedback_record_item);
        TextView tv_question = holder.findView(R.id.tv_question_feedback_record_item);
        TextView tv_questionTime = holder.findView(R.id.tv_questionTime_feedback_record_item);
        LinearLayout ll_answer = holder.findView(R.id.ll_answer_feedback_record_item);
        TextView tv_answer = holder.findView(R.id.tv_answer_feedback_record_item);
        TextView tv_answerTime = holder.findView(R.id.tv_answerTime_feedback_record_item);
        if (position == 0)
        {
            v_line.setVisibility(View.VISIBLE);
        } else
        {
            v_line.setVisibility(View.GONE);
        }
        if (data != null)
        {
            tv_type.setText(data.getTypeShow());
            tv_status.setText(data.getIsReplyShow());
            tv_question.setText(data.getContent());
            tv_questionTime.setText(data.getCreateTime());
            if (data.getIsReply() == 1)
            {
                ll_answer.setVisibility(View.VISIBLE);
                tv_answer.setText(data.getReply());
                tv_answerTime.setText(data.getReplyTime());
            } else
            {
                ll_answer.setVisibility(View.GONE);
            }
            if (data.isOpen())
            {
                ll_content.setVisibility(View.VISIBLE);
                tv_status.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.resToDrawable(mContext, R.mipmap.arrow_up), null);
            } else
            {
                ll_content.setVisibility(View.GONE);
                tv_status.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, ResUtils.resToDrawable(mContext, R.mipmap.arrow_down), null);
            }
            tv_status.setOnClickListener(new SingleClickListener()
            {
                @Override
                public void onSingleClick(View v)
                {
                    if (eventListener != null) eventListener.onStatusClick(data, position);
                }
            });
        }
    }

    public interface IEventListener
    {
        void onStatusClick(FeedbackRecordEntity data, int position);
    }

}
