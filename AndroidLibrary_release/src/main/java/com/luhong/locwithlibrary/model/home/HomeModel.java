package com.luhong.locwithlibrary.model.home;


import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public class HomeModel implements IHomeModel {

    @Override
    public Observable<BaseResponse<HomeDataEntity>> getHomeData() {
        Map<String, Object> bodyParams = new HashMap<>();
        return ApiClient.getInstance().getApiServer().getHomeData(bodyParams);
    }

    //    @Override
    //    public Observable<BaseResponse<DevicePositionEntity>> getTrackInfo() {
    //        Map<String, Object> bodyParams = new HashMap<>();
    //        if (!TextUtils.isEmpty(AppVariable.currentVehicleId))
    //            bodyParams.put("vehicleId", AppVariable.currentVehicleId);
    //        return ApiClient.getInstance().getApiServer().getTrackInfo(bodyParams);
    //    }

}
