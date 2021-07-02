package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import io.reactivex.Observable;

public class CheckTokenBind implements ICheckTokenBind {

    @Override
    public Observable<BaseResponse<UserEntity>> checkTokenBind() {
        return ApiClient.getInstance().getApiServer().checkTokenBind();
    }

}
