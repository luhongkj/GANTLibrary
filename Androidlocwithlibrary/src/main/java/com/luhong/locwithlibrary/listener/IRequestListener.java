package com.luhong.locwithlibrary.listener;

/**
 * 请求回调接口
 * Created by ITMG on 2018/9/30.
 */
public interface IRequestListener<T> {
    /**
     * 请求成功
     */
     void onSuccess(T resultEntity);

    /**
     * 请求失败
     */
    void onFailure(int errCode, String errResult);
}
