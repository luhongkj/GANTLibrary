package com.luhong.locwithlibrary.base;

import android.os.Bundle;

import androidx.annotation.Nullable;


public interface BaseCycle
{
    // 初始化与生命周期无关的对象
    public void initObj();

    // 初始化传递的数据
    public void initData(@Nullable Bundle savedInstanceState);

    // 初始化界面
    public void initView();

    // 初始化各种事件
    public void initEvent();

    // 开始加载数据
    public void loadData();
}
