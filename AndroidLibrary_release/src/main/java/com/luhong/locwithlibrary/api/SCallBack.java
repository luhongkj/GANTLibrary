package com.luhong.locwithlibrary.api;



import androidx.annotation.NonNull;

import retrofit2.Call;

/**
 * SimpleICallBack
 * Created by xiaolei on 2017/4/6.
 */
public abstract class SCallBack<T> extends SICallBack<T>
{
    @Override
    public abstract void onSuccess(@NonNull T result);
    
    @Override
    public void onFail(Call<T> call, Throwable t)
    {
        super.onFail(call, t);
    }

    @Override
    public void onFinally()
    {
    }
}
