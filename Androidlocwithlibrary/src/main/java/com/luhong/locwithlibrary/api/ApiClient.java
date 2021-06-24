package com.luhong.locwithlibrary.api;

import android.app.Activity;


import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.listener.IRequestListener;
import com.luhong.locwithlibrary.net.request.BaseObserver;
import com.luhong.locwithlibrary.net.request.RetrofitClient;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.io.File;
import java.util.Map;

import io.reactivex.Observable;

public class ApiClient extends RetrofitClient {
    private static ApiClient mInstance;
    private volatile ApiServer apiServer = null;//注解请求参数

    private ApiClient() {
    }

    public static ApiClient getInstance() {
        if (mInstance == null) {
            synchronized (ApiClient.class) {
                if (mInstance == null) {
                    mInstance = new ApiClient();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected String getBaseUrl() {
        return HttpVariable.BASE_API_URL;
    }

    public ApiServer getApiServer() {
        if (apiServer == null) {
            synchronized (ApiServer.class) {
                if (apiServer == null) {
                    apiServer = getRetrofit().create(ApiServer.class);
                }
            }
        }
        return apiServer;
    }

    public Observable<BaseResponse<UrlEntity>> uploadForPath(Map<String, String> bodyParams, Map<String, String> filePathParams) {
        return getApiServer().uploadFile(getRequestBodyForPath(bodyParams, filePathParams));
    }

    public Observable<BaseResponse<UrlEntity>> uploadForFile(Map<String, String> bodyParams, Map<String, File> fileParams) {
        return getApiServer().uploadFile(getRequestBodyForFile(bodyParams, fileParams));
    }

    public void uploadForPath(Activity context, Map<String, String> bodyParams, Map<String, String> fileParams, final IRequestListener<UrlEntity> requestListener) {
        doSubscribe(getApiServer().uploadFile(getRequestBodyForPath(bodyParams, fileParams)), new BaseObserver<UrlEntity>(context, "上传中") {
            @Override
            protected void onSuccess(UrlEntity imgUrl, String msg) {
                requestListener.onSuccess(imgUrl);
            }

            @Override
            protected void onFailure(int errCode, String errMsg) {
                requestListener.onFailure(errCode, errMsg);
            }
        });
    }

}
