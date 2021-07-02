package com.luhong.locwithlibrary.net.request;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.luhong.locwithlibrary.Locwith;
import com.luhong.locwithlibrary.R;
import com.luhong.locwithlibrary.dialog.LoadingDialog;
import com.luhong.locwithlibrary.entity.ArrearsEvent;
import com.luhong.locwithlibrary.entity.LogoutEvent;
import com.luhong.locwithlibrary.net.exception.ApiException;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.utils.EventBusUtils;
import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.ToastUtil;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeoutException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.HttpException;


/**
 * Created by ITMG on 2019-03-30.
 */
public abstract class BaseObserver<T> implements Observer<BaseResponse<T>> {
    private Context mContext;
    private LoadingDialog mLoadingDialog;
    private String mLoadingMsg = "加载中...";
    private boolean mIsShowLoading;//是否显示加载loading
    private boolean mIsShowToast = true;//是否直接弹出Toast
    private Disposable disposable;// d.dispose(); //通过判断解除订阅关系

    public BaseObserver() {
        this.mContext = Locwith.getContext();
        this.mIsShowLoading = false;
        this.mIsShowToast = true;
    }

    public BaseObserver(boolean mIsShowToast) {
        this.mContext = Locwith.getContext();
        this.mIsShowLoading = false;
        this.mIsShowToast = mIsShowToast;
    }

    public BaseObserver(Context mContext) {
        this.mContext = mContext;
        this.mIsShowLoading = false;
    }

    public BaseObserver(Context mContext, boolean isShowLoading) {
        this.mContext = mContext;
        this.mIsShowLoading = isShowLoading;
    }

    public BaseObserver(Context mContext, boolean isShowLoading, boolean mIsShowToast) {
        this.mContext = mContext;
        this.mIsShowLoading = isShowLoading;
        this.mIsShowToast = mIsShowToast;
    }

    public BaseObserver(Context mContext, String loadingMsg) {
        this.mContext = mContext;
        this.mIsShowLoading = true;
        this.mLoadingMsg = loadingMsg;
        this.mIsShowToast = true;
    }

    public BaseObserver(Context mContext, String loadingMsg, boolean mIsShowToast) {
        this.mContext = mContext;
        this.mIsShowLoading = true;
        this.mLoadingMsg = loadingMsg;
        this.mIsShowToast = mIsShowToast;
    }


    @Override
    public void onSubscribe(Disposable d) {
        this.disposable = d;
        showLoading();
    }

    @Override
    public void onComplete() {//onCompleted
        cancelLoading();
    }

    @Override
    public void onError(Throwable e) { //TODO: 统一处理请求异常的情况
        int errorCode = -1;
        String errorMsg = e.getMessage();
        Logger.error(" http请求异常信息: " + e.toString());
        if (e instanceof ApiException) {
            onErrorMsg(e.getMessage());
        } else if (e instanceof ConnectException || e instanceof UnknownHostException) {
            errorMsg = mContext.getString(R.string.net_error);
            onErrorMsg(errorMsg);
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
            errorMsg = mContext.getString(R.string.time_out);
            onErrorMsg(errorMsg);
//        } else if (e instanceof WeakNetworkException) {//弱网
//            onWeakNetWorkError();
        } else if (e instanceof JsonSyntaxException) {//数据解析异常com.google.gson.JsonSyntaxException:
            errorMsg = mContext.getString(R.string.parsing_error);
            onErrorMsg(errorMsg);
        } else if (e instanceof HttpException) {//服务器响应异常
            try {
                ResponseBody response = ((HttpException) e).response().errorBody();
                errorCode = ((HttpException) e).code();
                String errorJson = getResponseErrorBodyJson(response);
                Logger.error("服务器响应异常errorCode:" + errorCode + " ,errorJson: " + errorJson);
                onFailHasSuccess(new Gson().fromJson(errorJson, BaseResponse.class));
                return;
            } catch (Exception e1) {
                e1.printStackTrace();
                onFailure(errorCode, mContext.getString(R.string.server_error));
                onErrorMsg(mContext.getString(R.string.server_error));
            }
        } else {
            onErrorMsg(mContext.getString(R.string.server_error));
        }
        onFailure(errorCode, errorMsg);
    }

    @Override
    public void onNext(BaseResponse<T> baseResponse) {
        if (baseResponse != null) {
            if (baseResponse.getCode() == 1) {
                onSuccess(baseResponse.getData(), baseResponse.getMsg());
            } else {
                onFailHasSuccess(baseResponse);
            }
        }
    }

    /**
     * 可在此处统一显示loading view
     */
    private void showLoading() {
        if (mIsShowLoading && mContext != null) {
            mLoadingDialog = new LoadingDialog.Builder((Activity) mContext).setMessage(mLoadingMsg).setCancelable(true).create();
            mLoadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    if (disposable != null) disposable.dispose();
                }
            });
            mLoadingDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (disposable != null) disposable.dispose();
                }
            });
            mLoadingDialog.show();
        }
    }

    private void cancelLoading() {
        if (mIsShowLoading && mContext != null) {
            if (mLoadingDialog != null) mLoadingDialog.cancel();
        }
    }

    private void onLoginError() {
//        SPUtils.removeByKey(mContext, BaseConstants.LOGIN_KEY);
        EventBusUtils.post(new LogoutEvent());
    }

    protected abstract void onSuccess(T t, String msg);

    protected abstract void onFailure(int errCode, String errMsg);

    public void onFailHasSuccess(BaseResponse baseResponse) {
        if (baseResponse == null) return;
        if (baseResponse.getCode() == 401) {//登录失效
            onLoginError();
        } else if (baseResponse.getCode() == 430) {//欠费
            EventBusUtils.post(new ArrearsEvent(ArrearsEvent.TYPE_ARREARS));
        }
        onErrorMsg(!TextUtils.isEmpty(baseResponse.getMsg()) ? baseResponse.getMsg() : "");
        onFailure(baseResponse.getCode(), !TextUtils.isEmpty(baseResponse.getMsg()) ? baseResponse.getMsg() : "");
    }

    public void onErrorMsg(String errMsg) {
        onComplete();
        if (mIsShowToast)
            if (errMsg.contains("登录过期")) {
            } else {
                ToastUtil.show(errMsg);
            }
    }

    /**
     * @param responseBody
     * @return
     */
    public String getResponseErrorBodyJson(ResponseBody responseBody) {
        Charset UTF8 = Charset.forName("UTF-8");
        BufferedSource source = responseBody.source();
        try {
            source.request(Long.MAX_VALUE); // Buffer the entire body.
        } catch (IOException e) {
            e.printStackTrace();
        }
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                e.printStackTrace();
            }
        }
        return buffer.clone().readString(charset);
    }
}
