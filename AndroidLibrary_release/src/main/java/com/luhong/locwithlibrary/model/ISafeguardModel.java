package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ISafeguardModel {
    Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getElectricIntroduce(Map<String, Object> bodyParams);

    Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getBicycleIntroduce(Map<String, Object> bodyParams);

    Observable<BaseResponse<List<SafeguardEntity>>> getSafeguardHome();

    Observable<BaseResponse<BasePageEntity<SafeguardEntity>>> getSafeguardMine();

    Observable<BaseResponse<Object>> saveSafeguard(Map<String, Object> bodyParams);

    Observable<BaseResponse<Object>> updateSafeguard(Map<String, Object> bodyParams);

    Observable<BaseResponse<SafeguardEntity>> getSafeguardInfo(Map<String, Object> bodyParams);
}
