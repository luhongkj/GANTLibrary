package com.luhong.locwithlibrary;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

import androidx.annotation.RequiresApi;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.IFailEvent;
import com.luhong.locwithlibrary.api.SICallBack;
import com.luhong.locwithlibrary.base.ActivityLifes;
import com.luhong.locwithlibrary.utils.ToastUtil;

import retrofit2.Call;

public class Locwith {
    private static Context mContext;

    public static Context getContext() {
        return mContext;
    }
    @SuppressLint("NewApi")
    public static void intit(Application application) {
        if (isMainProcess(application)) {
            mContext = application.getApplicationContext();
            ApiClient.getInstance().initRetrofit(mContext);
         //   MultiDex.install(mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                webviewSetPath(application);
            }
            //定义前台服务的默认样式。即标题、描述和图标
            //定义前台服务的通知点击事件
            // 设置统一的网络请求失败提示语
            SICallBack.failEvent = new IFailEvent() {
                private long lastTime = 0;

                @Override
                public void onFail(Call<?> callBack, Throwable t) {
                    long now = System.currentTimeMillis();
                    if (now - lastTime < 10 * 1000) {
                        ToastUtil.show("网络出小差了～");
                        lastTime = now;
                    }
                }
            };
            application.registerActivityLifecycleCallbacks(ActivityLifes.getInstance());
        }
    }

    @SuppressLint("NewApi")
    protected static boolean isMainProcess(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return context.getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    @RequiresApi(api = 28)
    public static void webviewSetPath(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            String processName = getProcessName(context);
            if (!context.getApplicationContext().getPackageName().equals(processName)) {//判断不等于默认进程名称
                WebView.setDataDirectorySuffix(processName);
            }
        }
    }

    @SuppressLint("NewApi")
    public static String getProcessName(Context context) {
        if (context == null) return null;
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == android.os.Process.myPid()) {
                return processInfo.processName;
            }
        }
        return null;
    }
}
