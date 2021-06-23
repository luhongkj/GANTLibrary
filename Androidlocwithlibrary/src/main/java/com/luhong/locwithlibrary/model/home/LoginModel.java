package com.luhong.locwithlibrary.model.home;

import android.content.Context;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;
import com.luhong.locwithlibrary.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class LoginModel implements ILoginModel {

    @Override
    public Observable<BaseResponse<UserEntity>> login(final Context mContext, String phoneNo, String password) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("phone", phoneNo);
        bodyParams.put("password", StringUtils.md5Hex(password));
        return ApiClient.getInstance().getApiServer().login(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> getCode(String phoneNo) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("phone", phoneNo);
        return ApiClient.getInstance().getApiServer().getLoginCode(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> checkCode(String phoneNo, String code) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("phone", phoneNo);
        bodyParams.put("code", code);
        bodyParams.put("isRegister", true);
        return ApiClient.getInstance().getApiServer().checkCode(bodyParams);
    }

}
