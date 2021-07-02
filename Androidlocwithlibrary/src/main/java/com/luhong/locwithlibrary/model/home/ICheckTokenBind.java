package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ICheckTokenBind
{
    Observable<BaseResponse<UserEntity>> checkTokenBind();

}
