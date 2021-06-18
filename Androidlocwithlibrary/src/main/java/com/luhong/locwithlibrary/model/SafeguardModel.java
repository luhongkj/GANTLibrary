package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;


public class SafeguardModel implements ISafeguardModel {

    @Override
    public Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getElectricIntroduce(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().getElectricIntroduce(bodyParams);
    }

    @Override
    public Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getBicycleIntroduce(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().getBicycleIntroduce(bodyParams);
    }

    @Override
    public Observable<BaseResponse<List<SafeguardEntity>>> getSafeguardHome() {
        Map<String, Object> bodyParams = new HashMap<>();
        return ApiClient.getInstance().getApiServer().getInsureSets(bodyParams);
    }

    @Override
    public Observable<BaseResponse<BasePageEntity<SafeguardEntity>>> getSafeguardMine() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("userId", BaseVariable.userId);
        return ApiClient.getInstance().getApiServer().getSafeguardList(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> saveSafeguard(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().saveSafeguard(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> updateSafeguard(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().updateSafeguard(bodyParams);
    }

    @Override
    public Observable<BaseResponse<SafeguardEntity>> getSafeguardInfo(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().getSafeguardInfo(bodyParams);
    }
}
