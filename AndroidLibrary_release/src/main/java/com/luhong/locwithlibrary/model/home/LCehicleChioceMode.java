package com.luhong.locwithlibrary.model.home;


import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public class LCehicleChioceMode implements ILVehicleChioce {

    @Override
    public Observable<BaseResponse<List<VehicleListEntity>>> getVehicle() {
        return ApiClient.getInstance().getApiServer().getVehicles();
    }

}
