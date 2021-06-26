package com.luhong.locwithlibrary.model;


import com.luhong.locwithlibrary.base.BaseActivity;
import com.luhong.locwithlibrary.listener.IRequestListener;

/**
 * 权限、初始化检测
 * Created by ITMG on 2020-01-16.
 */
public interface IPermissionModel
{
    void checkLocationInit(BaseActivity mActivity, IRequestListener<Object> requestListener);
}
