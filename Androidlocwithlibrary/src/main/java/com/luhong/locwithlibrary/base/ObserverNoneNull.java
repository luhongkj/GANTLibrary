package com.luhong.locwithlibrary.base;



import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

public abstract class ObserverNoneNull<T> implements Observer<T>
{
    public abstract void changed(@NonNull T data);

    @Override
    public void onChanged(T t)
    {
        if (t != null)
        {
            changed(t);
        }
    }
}
