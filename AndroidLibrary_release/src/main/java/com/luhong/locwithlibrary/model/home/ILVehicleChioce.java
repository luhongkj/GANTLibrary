package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public interface ILVehicleChioce {

    Observable<BaseResponse<List<VehicleListEntity>>> getVehicle();


}
