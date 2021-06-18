package com.luhong.locwithlibrary.net.interceptor;

import android.text.TextUtils;


import com.luhong.locwithlibrary.utils.Logger;

import java.io.IOException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 网络请求Log拦截器
 */
public class LoggerInterceptor implements Interceptor {
    private String TAG = LoggerInterceptor.class.getSimpleName();

    public LoggerInterceptor() {
    }

    public LoggerInterceptor(String TAG) {
        if (!TextUtils.isEmpty(TAG)) {
            this.TAG = TAG;
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        MediaType mediaType = response.body().contentType();
        String responseBody = response.body().string();
        Logger.error(request.method() + "请求地址= " + request.url() + "\n,请求响应responseBody=\n " + responseBody);
        Response responseBuild = response.newBuilder().body(ResponseBody.create(mediaType, responseBody)).build();
        Headers headers = responseBuild.networkResponse().request().headers();
        Logger.error("请求头参数=" + headers.toString());

        return responseBuild;
    }
}
