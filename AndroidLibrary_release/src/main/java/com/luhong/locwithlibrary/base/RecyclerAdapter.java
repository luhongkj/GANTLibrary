package com.luhong.locwithlibrary.base;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.luhong.locwithlibrary.R;

import java.util.LinkedList;
import java.util.List;

public abstract class RecyclerAdapter<DataType> extends RecyclerView.Adapter<BaseHolder>
{
    private View.OnClickListener listener = new View.OnClickListener()
    {
        @Override
        public void onClick(View view)
        {
            if (itemClickListener != null)
                itemClickListener.onClick(view, (int) view.getTag(R.id.recycleView_position));
        }
    };
    private OnItemClickListener itemClickListener;
    public List<DataType> list;

    public RecyclerAdapter()
    {
        this(null);
    }

    public RecyclerAdapter(List<DataType> list)
    {
        if (list == null)
        {
            list = new LinkedList<>();
        }
        this.list = list;
    }


    public abstract BaseHolder onCreate(@NonNull ViewGroup parent, int viewType);

    public abstract void onBindView(@NonNull BaseHolder holder,@NonNull DataType data, int position);


    @NonNull
    @Override
    public BaseHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        BaseHolder holder = onCreate(parent, viewType);
        if (!holder.itemView.hasOnClickListeners())
        {
            holder.itemView.setOnClickListener(listener);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseHolder holder, int position)
    {
        holder.itemView.setTag(R.id.recycleView_position, position);
        onBindView(holder, list.get(position), position);
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }

    public void setItemClickListener(OnItemClickListener listener)
    {
        this.itemClickListener = listener;
    }

    public interface OnItemClickListener
    {
        void onClick(View view, int position);
    }
}
