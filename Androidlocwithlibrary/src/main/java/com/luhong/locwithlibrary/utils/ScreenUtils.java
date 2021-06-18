package com.luhong.locwithlibrary.utils;

import android.app.Activity;
import android.view.Window;
import android.view.WindowManager;

/**
 * 屏幕工具类
 * Created by ITMG on 2020-01-16.
 */
public class ScreenUtils {

    /**
     * 是否使屏幕常亮
     *
     * @param activity
     */
    public static void keepScreenLongLight(Activity activity, boolean isOpenLight) {
        Window window = activity.getWindow();
        if (isOpenLight) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

}
