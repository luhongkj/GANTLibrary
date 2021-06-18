package com.luhong.locwithlibrary.model.home;

import android.content.Context;

import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ILoginModel {
    Observable<BaseResponse<UserEntity>> login(Context mContext, String username, String password);

    Observable<BaseResponse<Object>> getCode(String phoneNo);

    Observable<BaseResponse<Object>> checkCode(String phoneNo, String code);
}
