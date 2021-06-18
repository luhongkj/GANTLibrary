package com.luhong.locwithlibrary.apadter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.base.BaseHolder;
import com.luhong.locwithlibrary.base.RecyclerAdapter;
import com.luhong.locwithlibrary.entity.RescueBean;

public class RescueAdapter extends RecyclerAdapter<RescueBean> {
    // 删除事件
    private OnDeleteItemClickListener deleteItemClickListener;
    // 添加事件
    private OnAddClickListener addClickListener;

    /**
     * 设置点击删除事件
     */
    public void setOnDeleteItemClickListener(OnDeleteItemClickListener clickListener) {
        this.deleteItemClickListener = clickListener;
    }

    /**
     * 设置点击添加事件
     */
    public void setOnAddClickListener(OnAddClickListener addClickListener) {
        this.addClickListener = addClickListener;
    }

    @Override
    public BaseHolder onCreate(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rescue_contact, parent, false);
        return new BaseHolder(view);
    }

    @Override
    public void onBindView(@NonNull BaseHolder holder, @NonNull RescueBean data, int position) {
        View title_view = holder.get(R.id.title_view);
        View contact_layout = holder.get(R.id.contact_layout);
        View add_layout = holder.get(R.id.add_layout);
        View delete_btn = holder.get(R.id.delete_btn);
        TextView name_and_phone = holder.get(R.id.name_and_phone);
        delete_btn.setTag(position);
        delete_btn.setOnClickListener(deleteItemClickListener);
        add_layout.setOnClickListener(addClickListener);
        title_view.setVisibility(View.GONE);
        contact_layout.setVisibility(View.GONE);
        add_layout.setVisibility(View.GONE);
        if (data.getType() == null) {
            return;
        }
        switch (data.getType()) {
            case TITLE:
                title_view.setVisibility(View.VISIBLE);
                break;
            case DATA:
                contact_layout.setVisibility(View.VISIBLE);
                name_and_phone.setText(String.format("%s（%s）", data.getPhone(), data.getContactName()));
                break;
            case ADD:
                add_layout.setVisibility(View.VISIBLE);
                break;
        }
    }

    public static abstract class OnDeleteItemClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Object tagObj = v.getTag();
            if (tagObj != null) {
                onClick(v, (int) tagObj);
            }
        }

        public abstract void onClick(View v, int position);
    }

    public static abstract class OnAddClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            onClick();
        }

        public abstract void onClick();
    }
}
