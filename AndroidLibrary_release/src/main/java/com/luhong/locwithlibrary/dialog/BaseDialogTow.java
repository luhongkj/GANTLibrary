package com.luhong.locwithlibrary.dialog;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.FloatRange;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import androidx.appcompat.app.AlertDialog;

import com.luhong.locwithlibrary.R;


public abstract class BaseDialogTow
{
    private Context context;
    private int layout = -1;

    private AlertDialog lastDialog = null;
    private boolean isBackContext = false;
    private AlertDialog.Builder builder;

    private int width = -1;
    private int heigh = -1;
    private float dimAmount = 0.2f;
    private boolean cancelable = false;
    private int gravity = Gravity.CENTER;

    private int animStyle = R.style.BaseAnimStyle;

    public BaseDialogTow(Context context, @LayoutRes int layout)
    {
        this.context = context;
        this.layout = layout;
        if (context instanceof Activity && !((Activity) context).isFinishing())
        {
            builder = new AlertDialog.Builder(context);
        } else
        {
            isBackContext = true;
            builder = new AlertDialog.Builder(context, R.style.Base_Theme_Light_Dialog);
        }
    }

    /**
     * 设置宽度，绝对的值
     *
     * @param width
     * @return
     */
    public void setWidth(int width)
    {
        this.width = width;
    }

    /**
     * 设置宽度，屏幕的百分比的
     *
     * @param widthPercent
     * @return
     */
    public void setWidth(@FloatRange(from = 0.0, to = 1.0) float widthPercent)
    {
        this.width = (int) (getScreenWidth(context) * widthPercent);
    }

    /**
     * 设置高度
     *
     * @param heigh
     * @return
     */
    public void setHeigh(int heigh)
    {
        this.heigh = heigh;
    }

    /**
     * 设置背景透明度
     *
     * @param dimAmount
     * @return
     */
    public void setDimAmount(@FloatRange(from = 0.0, to = 1.0) float dimAmount)
    {
        this.dimAmount = dimAmount;
    }

    /**
     * 设置是否可以点击侧边取消
     *
     * @param cancelable
     * @return
     */
    public void setCancelable(boolean cancelable)
    {
        this.cancelable = cancelable;
    }

    /**
     * 设置位置
     *
     * @return
     */
    public void setGravity(int gravity)
    {
        this.gravity = gravity;
    }

    /**
     * 设置动画进出动画
     */
    public void setAnimStyle(@StyleRes int animStyle)
    {
        this.animStyle = animStyle;
    }

    public void dismiss()
    {
        if (lastDialog != null)
        {
            lastDialog.dismiss();
        }
    }

    public abstract void initView(AlertDialog alertDialog, View view);

    public abstract void initEvent(AlertDialog alertDialog, View view);


    public AlertDialog show()
    {
        builder.setTitle("");
        builder.setMessage("");
        builder.setCancelable(cancelable);
        AlertDialog alertDialog = builder.create();
        try
        {
            Window window = alertDialog.getWindow();
            if (window != null)
            {
                WindowManager.LayoutParams params = window.getAttributes();
                // 去除四角黑色背景  
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                if (isBackContext)
                {
                    window.setType(WindowManager.LayoutParams.TYPE_TOAST);
                }
                window.setAttributes(params);
            }
            alertDialog.show();

            View view = View.inflate(alertDialog.getContext(), layout, null);
            alertDialog.setContentView(view);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

            initView(alertDialog, view);
            initEvent(alertDialog, view);
            window = alertDialog.getWindow();
            if (window != null)
            {
                WindowManager.LayoutParams params = window.getAttributes();
                window.setGravity(gravity);
                DisplayMetrics dm = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(dm);
                window.setWindowAnimations(animStyle);
                if (width != -1)
                {
                    params.width = width;
                } else
                {
                    setWidth(0.8f);
                    params.width = width;
                }
                if (heigh != -1)
                {
                    params.height = heigh;
                }
                // 设置周围的暗色系数  
                params.dimAmount = dimAmount;
                window.setAttributes(params);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        lastDialog = alertDialog;
        return alertDialog;
    }


    /**
     * 获取屏幕宽度
     */
    private static int getScreenWidth(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();
    }
}
