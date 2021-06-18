package com.luhong.locwithlibrary.api;


import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Super Interface CallBack
 * Created by xiaolei on 2017/7/9.
 */

public abstract class SICallBack<T> implements Callback<T>
{
    // 统一的出错处理方式
    public static IFailEvent failEvent;

    public abstract void onSuccess(@NonNull T result);

    public abstract void onFinally();


    public void onFail(@NonNull Call<T> call, @NonNull Throwable t)
    {
        if (failEvent != null)
        {
            failEvent.onFail(call, t);
        }else 
        {
            t.printStackTrace();
        }
    }


    @Override
    public void onResponse(Call<T> call, Response<T> response)
    {
        try
        {
            if (response.isSuccessful())
            {
                T body = response.body();
                if (body != null)
                {
                    onSuccess(body);
                } else
                {
                    onFail(call, new Exception("response.body() == NULL"));
                }
            } else
            {
                onFail(call, new Exception(response.message()));
            }
        } finally
        {
            onFinally();
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t)
    {
        try
        {
            onFail(call, t);
        } finally
        {
            onFinally();
        }
    }
}
