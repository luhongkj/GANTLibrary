package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.DeviceBrandEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */

public class DeviceBrandModel implements IDeviceBrandModel
{

    @Override
    public Observable<BaseResponse<List<VehicleModelEntity>>> getVehicleType()
    {
        return ApiClient.getInstance().getApiServer().getVehicleType();
    }
    //
    //    @Override
    //    public Observable<BaseResponse<String>> getVehicleModels() {
    //        return ApiClient.getInstance().getApiServer().getDeviceModels();
    //    }

    @Override
    public Observable<BaseResponse<List<DeviceBrandEntity>>> getVehicleBrands(int vehicleType)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("type", vehicleType);
        return ApiClient.getInstance().getApiServer().getDeviceBrands(bodyParams);
    }
}
