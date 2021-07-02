package com.luhong.locwithlibrary.listener;


import com.luhong.locwithlibrary.apadter.RecyclerViewHolder;

/**
 * Created by ITMG on 2018/9/30.
 */
public interface OnRecyclerClickListener<T> {
    void onItemClick(RecyclerViewHolder viewHolder, T data, int position);

    void onItemLongClick(RecyclerViewHolder viewHolder, T data, int position);
}
