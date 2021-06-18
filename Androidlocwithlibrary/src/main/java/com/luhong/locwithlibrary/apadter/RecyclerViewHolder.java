package com.luhong.locwithlibrary.apadter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


/**
 *
 */
public class RecyclerViewHolder extends RecyclerView.ViewHolder {
    private SparseArray<View> mViews;
    private View mConvertView;

    /**
     * 构造方法
     *
     * @param itemView
     */
    public RecyclerViewHolder(View itemView) {
        super(itemView);
        mConvertView = itemView;
        mViews = new SparseArray<>();
    }

    public static RecyclerViewHolder create(Context context, int layoutId, ViewGroup parent) {
        View itemView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        return new RecyclerViewHolder(itemView);
    }

    public static RecyclerViewHolder create(View itemView) {
        return new RecyclerViewHolder(itemView);
    }

    /**
     * 通过id获得控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T findView(int viewId) {
        View view = mViews.get(viewId);
        if (view == null) {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView() {
        return mConvertView;
    }

    public RecyclerViewHolder setText(int viewId, String text) {
        TextView textView = findView(viewId);
        textView.setText(text);
        return this;
    }

    public RecyclerViewHolder setBgRes(int viewId, int resId) {
        View view = findView(viewId);
        view.setBackgroundResource(resId);
        return this;
    }
}
