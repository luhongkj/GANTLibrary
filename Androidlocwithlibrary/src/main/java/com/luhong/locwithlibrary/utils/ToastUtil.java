package com.luhong.locwithlibrary.utils;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.StringRes;

import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.R;


public class ToastUtil {
    private static Toast toast;

    /**
     * 默认 短时间Toast
     *
     * @param message
     */
    public static void show(CharSequence message) {//默认
        showShort(message);
    }

    /**
     * 默认 短时间Toast
     *
     * @param strResId
     */
    public static void show(int strResId) {//默认
        showShort(strResId);
    }

    /**
     * 自定义时间Toast
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        showToast(message.toString(), duration);
    }

    /**
     * 自定义时间Toast
     *
     * @param strResId
     * @param duration
     */
    public static void show(@StringRes int strResId, int duration) {
        showToast(Locwith.getContext().getString(strResId), duration);
    }

    /**
     * 短时间Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {
        showToast(message.toString(), Toast.LENGTH_SHORT);
    }


    /**
     * 短时间Toast
     *
     * @param strResId
     */
    public static void showShort(@StringRes int strResId) {
        showToast(Locwith.getContext().getString(strResId), Toast.LENGTH_SHORT);
    }

    /**
     * 长时间Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        showToast(message.toString(), Toast.LENGTH_LONG);
    }

    /**
     * 长时间Toast
     *
     * @param strResId
     */
    public static void showLong(@StringRes int strResId) {
        showToast(Locwith.getContext().getString(strResId), Toast.LENGTH_LONG);
    }

    /**
     * 自定义toast
     *
     * @param content
     * @return
     */
    public static void showToast(String content, int duration) {
        if (TextUtils.isEmpty(content)) return;
        if (toast != null) {
            toast.cancel();
        }
        toast = new Toast(Locwith.getContext());
        View view = LayoutInflater.from(Locwith.getContext()).inflate(R.layout.view_toast, null);
        TextView tv_content = view.findViewById(R.id.tv_content_toast);
        tv_content.setText(content);
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(duration);
        toast.show();
    }
}
