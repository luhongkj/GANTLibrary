package com.luhong.locwithlibrary.api;


import retrofit2.Call;

/**
 * 设置统一的网络失败处理方式<br/>
 * 处理流程：onFail -> IFailEvent.onFail
 * 这里需要注意的是，如果你重写了SICallBack的onFail方法，并且不调用super.onFail，
 * 那么，则不会走统一的处理方式。
 * Created by xiaolei on 2017/11/14.
 */

public interface IFailEvent
{
    public abstract void onFail(Call<?> callBack, Throwable t);
}
