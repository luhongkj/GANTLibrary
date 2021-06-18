package com.luhong.locwithlibrary.base;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.luhong.locwithlibrary.utils.Ext;

public abstract class BaseActivityTow extends AppCompatActivity implements BaseCycle
{
    private boolean hasResumed = false;
    private BaseCycleViewModel _cycleViewModel = null;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState)
    {
        // 初始化
        _cycleViewModel = Ext.create(this, BaseCycleViewModel.class);
        // 发送初始化数据的信息
        _cycleViewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_OBJ);
        setTranslucentStatus(true);
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        // 监听各个事件
        _cycleViewModel.cycleStatus.observe(this, new ObserverNoneNull<BaseCycleViewModel.Status>()
        {
            @Override
            public void changed(@NonNull BaseCycleViewModel.Status status)
            {
                switch (status)
                {
                    case INIT_OBJ:
                        initObj();
                        break;
                    case INIT_DATA:
                        initData(savedInstanceState);
                        break;
                    case INIT_VIEW:
                        initView();
                        break;
                    case INIT_EVENT:
                        initEvent();
                        break;
                    case LOAD_DATA:
                        loadData();
                        break;
                }
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState)
    {
        super.onPostCreate(savedInstanceState);
        _cycleViewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_DATA);
        _cycleViewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_VIEW);
        _cycleViewModel.cycleStatus.setValue(BaseCycleViewModel.Status.INIT_EVENT);
    }
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus(boolean isLight) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            updateStatusColor(isLight);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);//状态栏为透明

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
    }
    protected void updateStatusColor(boolean isLight) {
        View decorView = getWindow().getDecorView();
        if (isLight) {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        if (!hasResumed)
        {
            hasResumed = true;
            _cycleViewModel.cycleStatus.setValue(BaseCycleViewModel.Status.LOAD_DATA);
        }
    }

    // 初始化与生命周期无关的对象
    @Override
    public void initObj()
    {
    }

    // 初始化传递的数据
    @Override
    public abstract void initData(@Nullable Bundle savedInstanceState);

    // 初始化界面
    @Override
    public abstract void initView();

    // 初始化各种事件
    @Override
    public abstract void initEvent();

    // 开始加载数据
    @Override
    public abstract void loadData();

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _cycleViewModel.cycleStatus.removeObservers(this);
    }

}
