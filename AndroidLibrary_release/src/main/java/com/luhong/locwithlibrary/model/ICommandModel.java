package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ICommandModel
{
    Observable<BaseResponse<Object>> sendCommand(Map<String, Object> bodyParams);
}
