package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IClaimModel
{
    Observable<BaseResponse<ClaimEntity>> getClaim(String claimId);

    Observable<BaseResponse<ClaimEntity>> saveClaim(String insuranceId);

    Observable<BaseResponse<Object>> updateClaim(Map<String, Object> bodyParams);

    Observable<BaseResponse<Object>> cancelClaim(String claimId);
}
