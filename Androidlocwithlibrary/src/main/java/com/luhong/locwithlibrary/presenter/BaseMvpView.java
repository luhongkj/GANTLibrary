package com.luhong.locwithlibrary.presenter;

/**
 * 出错回调
 * Created by ITMG on 2018/8/14.
 */
public interface BaseMvpView {
    /**
     *
     * @param errType 区分错误类型
     * @param errMsg 请求错误信息
     */
    void onFailure(int errType, String errMsg);
}
