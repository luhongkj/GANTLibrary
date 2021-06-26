package com.luhong.locwithlibrary.base;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class BaseHolder extends RecyclerView.ViewHolder
{
    private SparseArray<View> store = new SparseArray<>();

    public BaseHolder(@NonNull View itemView)
    {
        super(itemView);
    }
    
    public Context getContext()
    {
        return itemView.getContext();
    }
    
    public <T extends View> T get(@IdRes int id)
    {
        View view = store.get(id);
        if (view == null)
        {
            view = itemView.findViewById(id);
            store.put(id, view);
        }
        return (T) view;
    }
}
