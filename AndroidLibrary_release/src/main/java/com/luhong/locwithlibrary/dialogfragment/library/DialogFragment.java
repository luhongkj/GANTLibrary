package com.luhong.locwithlibrary.dialogfragment.library;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.luhong.locwithlibrary.R;

import java.util.LinkedList;
import java.util.List;

/**
 * 基于Fragment重写的DialogFragment
 */
public abstract class DialogFragment extends BaseFragment
{
    private Config config = new Config();
    private List<Dialog> dialogs = new LinkedList<>();

    public DialogFragment()
    {
    }

    public void dismiss()
    {
        for (Dialog dialog : dialogs)
        {
            if (dialog.isShowing())
            {
                dialog.dismiss();
            }
        }
    }

    @Override
    public void onDestroy()
    {
        this.dismiss();
        super.onDestroy();
    }


    public Dialog show(FragmentManager manager, ConfigCallBack onConfig)
    {
        if (!this.isAdded())
        {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(this, DialogFragment.class.getName());
            transaction.commitAllowingStateLoss();
            manager.executePendingTransactions();
        }
        onConfig.onConfig(this.config);
        Context context = this.requireContext();
        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.Base_Theme_Light_Dialog);
        builder.setTitle("");
        builder.setMessage("");
        builder.setOnDismissListener(new DialogInterface.OnDismissListener()
        {
            @Override
            public void onDismiss(DialogInterface dialog)
            {
                dialogs.remove(dialog);
            }
        });
        builder.setCancelable(this.config.cancelable);
        AlertDialog alertDialog = builder.create();
        try
        {
            Window window = alertDialog.getWindow();
            if (window != null)
            {
                WindowManager.LayoutParams params = window.getAttributes();
                // 去除四角黑色背景  
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                window.setAttributes(params);
            }
            alertDialog.show();
            View view = this.getView();
            if (view == null)
                throw new RuntimeException("View没有初始化");
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null)
            {
                parent.removeView(view);
            }
            alertDialog.setContentView(view);

            window = alertDialog.getWindow();
            if (window != null)
            {
                window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
                WindowManager.LayoutParams params = window.getAttributes();
                window.setGravity(this.config.gravity);
                DisplayMetrics dm = new DisplayMetrics();
                window.getWindowManager().getDefaultDisplay().getMetrics(dm);
                window.setWindowAnimations(this.config.animStyle);
                if (this.config.width != -1)
                {
                    params.width = this.config.width;
                }
                if (this.config.heigh != -1)
                {
                    params.height = this.config.heigh;
                }
                // 设置周围的暗色系数  
                params.dimAmount = this.config.dimAmount;
                window.setAttributes(params);
            }
            this.dialogs.add(alertDialog);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return alertDialog;
    }

    public static class Config
    {
        /**
         * 设置宽度
         */
        public int width = -1;
        /**
         * 设置高度
         */
        public int heigh = -1;
        /**
         * 设置显示的时候，背景的暗色系数
         */
        public float dimAmount = 0.2f;
        /**
         * 设置是否可以点击侧边取消
         */
        public boolean cancelable = true;
        /**
         * 设置位置
         */
        public int gravity = Gravity.CENTER;
        /**
         * 设置动画进出动画
         */
        public int animStyle = R.style.BaseAnimStyle;
    }

    public interface ConfigCallBack
    {
        public void onConfig(Config config);
    }
}