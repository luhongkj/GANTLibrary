package com.luhong.locwithlibrary.model;

import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.entity.CodeTableParams;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * 运动数据
 * Created by ITMG on 2018/8/15 0015.
 */
public class SportModel implements ISportModel
{

    @Override
    public Observable<BaseResponse<SportEntity>> getSportList(String queryMonth, int pageSize, int pageNo)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        if (!TextUtils.isEmpty(queryMonth))
            bodyParams.put("yearMonth", queryMonth);
        if (!TextUtils.isEmpty(AppVariable.currentVehicleId))
            bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        bodyParams.put("current", pageNo);
        bodyParams.put("size", pageSize);
        return ApiClient.getInstance().getApiServer().getSportList(bodyParams);
    }

    @Override
    public Observable<BaseResponse<List<SportLocationEntity>>> getSportLocations(String sportId)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", sportId);
        return ApiClient.getInstance().getApiServer().getSportLocations(bodyParams);
    }

    @Override
    public Observable<BaseResponse<SportLocationPageEntity<SportLocationEntity>>> getSportLocationsNew(String sportId)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", sportId);
        return ApiClient.getInstance().getApiServer().getSportLocationsNew(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> updateSport(String sportId, String sportName)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("id", sportId);
        bodyParams.put("name", sportName);
        return ApiClient.getInstance().getApiServer().updateSport(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> uploadSport(CodeTableParams codeTableParams)
    {
        return ApiClient.getInstance().getApiServer().uploadSport(codeTableParams);
    }

}
