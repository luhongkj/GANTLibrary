package com.luhong.locwithlibrary.net.request;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.luhong.locwithlibrary.net.cache.CacheInterceptor;
import com.luhong.locwithlibrary.net.interceptor.HeaderInterceptor;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * API初始化类
 * Created by ITMG on 2019/3/28.
 */
public abstract class RetrofitClient {
    private static final int DEFAULT_TIMEOUT = 20;
    private OkHttpClient okHttpClient;
    private Retrofit retrofit;

    /**
     * 网络日志打印框架
     */
    private HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(@NotNull String message) {
            Log.d("HttpRetrofit", message);
        }
    });

    @NonNull
    protected abstract String getBaseUrl();

    /**
     * 初始化必要对象和参数
     */
    public void initRetrofit(Context context) { // 初始化Retrofit
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").serializeNulls().create();
        retrofit = new Retrofit.Builder()
                .client(getOkHttpClient(context))
                .baseUrl(getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public <S> S create(Context context, Class<S> service) {
        return getRetrofit().create(service);
    }

    public <T> void doSubscribe(Observable<T> observable, Observer<? super T> subscriber) {
        //设置线程调度
        observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    private OkHttpClient getOkHttpClient(Context context) {
        if (okHttpClient == null) {
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            okHttpClient = HttpsUtils.getHttpsOkHttpClient()
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)//允许失败重试
                    //.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    //.addInterceptor(new LoggerInterceptor())
                    .addInterceptor(new HeaderInterceptor())
                    .addInterceptor(loggingInterceptor)
                    .addInterceptor(new CacheInterceptor(context, CacheInterceptor.Type.SQLITE))
                    .build();
        }
        return okHttpClient;
    }

    public RequestBody getRequestBodyForFile(Map<String, String> bodyParams, Map<String, File> fileParams) {
        RequestBody requestBody = null;
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
            }
        }
        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                File file = fileParams.get(key);
                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("*/*"/*"application/octet-stream"*/), file);
                multipartBodyBuilder.addFormDataPart(/*key*/"file", file.getName(), fileRequestBody);
            }
        }
        requestBody = multipartBodyBuilder.build();
        return requestBody;
    }

    public RequestBody getRequestBodyForPath(Map<String, String> bodyParams, Map<String, String> fileParams) {
        RequestBody requestBody;
        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        if (bodyParams != null) {
            Iterator<String> iterator = bodyParams.keySet().iterator();
            String key = "";
            while (iterator.hasNext()) {
                key = iterator.next();
                multipartBodyBuilder.addFormDataPart(key, bodyParams.get(key));
            }
        }

        if (fileParams != null) {
            Iterator<String> iterator = fileParams.keySet().iterator();
            String key = "";
            int i = 0;
            while (iterator.hasNext()) {
                key = iterator.next();
                i++;
                String filePath = fileParams.get(key);
                File file = new File(filePath);
                RequestBody fileRequestBody = RequestBody.create(MediaType.parse("*/*"/*"application/octet-stream"*/), file);
                multipartBodyBuilder.addFormDataPart(/*key*/"file", file.getName(), fileRequestBody);
            }
        }
        requestBody = multipartBodyBuilder.build();
        return requestBody;
    }
}
