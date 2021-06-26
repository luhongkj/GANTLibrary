package com.luhong.locwithlibrary.net.interceptor;

import android.os.Build;
import android.text.TextUtils;
import android.webkit.WebSettings;

import androidx.annotation.RequiresApi;

import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.utils.AppUtils;
import com.luhong.locwithlibrary.utils.SPUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * appv:APP版本; appm:手机参数
 * 全局头文件header拦截器
 */
public class HeaderInterceptor implements Interceptor {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        builder.removeHeader("User-Agent");
        builder.addHeader("User-Agent", WebSettings.getDefaultUserAgent(Locwith.getContext()));
        builder.addHeader("Content-Type", "application/json;charset=UTF-8");

        String token = SPUtils.getString(Locwith.getContext(), BaseConstants.TOKEN);
        if (TextUtils.isEmpty(token)) {
            token = "Invalid token";
        }
        builder.addHeader("token", token);
        builder.addHeader("appType", "giant");
        String phoneModel = AppUtils.getDeviceBrand() + "--" + AppUtils.getSystemModel() + "--" + AppUtils.getSystemVersion();
        if (!TextUtils.isEmpty(phoneModel)) {
            builder.addHeader("appm", phoneModel);//手机机型
        }
        return chain.proceed(builder.build());
    }
}
