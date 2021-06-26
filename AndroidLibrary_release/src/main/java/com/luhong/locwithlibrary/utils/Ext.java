package com.luhong.locwithlibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;


import java.lang.reflect.Field;

public class Ext
{
    /**
     * 快捷对ViewModel进行实例化
     *
     * @param klass viewmodel的类
     * @param <T>
     * @return 实例
     */
    public static <T extends ViewModel> T create(ViewModelStoreOwner owner, Class<T> klass)
    {
        return new ViewModelProvider(owner, new ViewModelProvider.NewInstanceFactory()).get(klass);
    }

    /**
     * 获取状态栏的高度
     */
    public static int getStatbarHeight(Context context)
    {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
        {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        if (result == 0)
        {
            try
            {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object obj = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = Integer.parseInt(field.get(obj).toString());
                result = context.getResources().getDimensionPixelSize(x);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 测量自定义View的高度
     */
    public static int measureHeight(int heightMeasureSpec)
    {
        int heightResult = 0;
        int heightSpecMode = View.MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = View.MeasureSpec.getSize(heightMeasureSpec);
        switch (heightSpecMode)
        {
            case View.MeasureSpec.UNSPECIFIED:
            {
                heightResult = heightSpecSize;
            }
            break;
            case View.MeasureSpec.AT_MOST:
            {
                heightResult = View.MeasureSpec.getSize(heightMeasureSpec);
            }
            break;
            case View.MeasureSpec.EXACTLY:
            {
                heightResult = View.MeasureSpec.getSize(heightMeasureSpec);
            }
        }
        return heightResult;
    }


    /**
     * 测量自定义View的宽度
     */
    public static int measureWidth(int widthMeasureSpec)
    {
        int widthResult = 0;
        int widthSpecMode = View.MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = View.MeasureSpec.getSize(widthMeasureSpec);
        switch (widthSpecMode)
        {
            case View.MeasureSpec.UNSPECIFIED:
            {
                widthResult = widthSpecSize;
            }
            break;
            case View.MeasureSpec.AT_MOST:
            {
                widthResult = View.MeasureSpec.getSize(widthMeasureSpec);
            }
            break;
            case View.MeasureSpec.EXACTLY:
            {
                widthResult = View.MeasureSpec.getSize(widthMeasureSpec);
            }
        }
        return widthResult;
    }

    /**
     * 单位换算 px 转 sp
     *
     * @param context 上下文
     * @param px      px像素单位
     * @return sp的单位
     */
    public static int px2sp(Context context, int px)
    {
        return DensityUtils.px2sp(context, px);
    }

    /**
     * 单位换算 sp 转 px
     *
     * @param context 上下文
     * @param sp      sp单位
     * @return px像素单位
     */
    public static int sp2px(Context context, int sp)
    {
        return DensityUtils.sp2px(context, sp);
    }

    /**
     * 单位换算 px 转 dp
     *
     * @param context 上下文
     * @param px      px像素
     * @return dp单位
     */
    public static int px2dp(Context context, int px)
    {
        return DensityUtils.px2dip(context, px);
    }

    /**
     * 单位换算 dp 转 px
     *
     * @param context 上下文
     * @param dp      dp单位
     * @return px像素单位
     */
    public static int dp2px(Context context, int dp)
    {
        return DensityUtils.dip2px(context, dp);
    }


    /**
     * 设置界面为黑色标题栏文字的模式
     */
    public static void darkStatusModel(Activity activity)
    {
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    /**
     * 设置界面为白色标题栏文字的模式
     */
    public static void lightStatusModel(Activity activity)
    {
        View decor = activity.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}
