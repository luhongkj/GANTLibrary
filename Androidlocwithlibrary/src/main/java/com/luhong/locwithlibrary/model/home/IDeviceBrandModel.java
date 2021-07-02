package com.luhong.locwithlibrary.model.home;


import com.luhong.locwithlibrary.entity.DeviceBrandEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IDeviceBrandModel
{
    Observable<BaseResponse<List<DeviceBrandEntity>>> getVehicleBrands(int vehicleType);

    Observable<BaseResponse<List<VehicleModelEntity>>> getVehicleType();

    //        Observable<BaseResponse<String>> getVehicleModels();
}
