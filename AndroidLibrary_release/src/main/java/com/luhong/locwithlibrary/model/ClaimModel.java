package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.model.home.IClaimModel;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;


public class ClaimModel implements IClaimModel
{

    @Override
    public Observable<BaseResponse<ClaimEntity>> getClaim(String claimId)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", claimId);
        return ApiClient.getInstance().getApiServer().getClaim(bodyParams);
    }

    @Override
    public Observable<BaseResponse<ClaimEntity>> saveClaim(String insuranceId)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("insuranceId", insuranceId);
        return ApiClient.getInstance().getApiServer().saveClaim(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> updateClaim(Map<String, Object> bodyParams)
    {
        return ApiClient.getInstance().getApiServer().updateClaim(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> cancelClaim(String claimId)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", claimId);
        return ApiClient.getInstance().getApiServer().cancelClaim(bodyParams);
    }
}
