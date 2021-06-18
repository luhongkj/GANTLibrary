package com.luhong.locwithlibrary.model.home;


import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * 设备相关
 * Created by ITMG on 2020-01-21.
 */
public interface IDeviceManageModel {
    Observable<BaseResponse<List<DeviceEntity>>> getDeviceList();

    Observable<BaseResponse<DeviceEntity>> updateVehicle(DeviceEntity deviceEntity);

    Observable<BaseResponse<DeviceEntity>> deleteVehicle(DeviceEntity deviceEntity);

    Observable<BaseResponse<Object>> addVehicle(DeviceEntity deviceEntity);

    Observable<BaseResponse<InstallModeEntity>> getInstallMode(String deviceSn);

    Observable<BaseResponse<DeviceServerEntity>> getVehicleBySn(String deviceSn);

    Observable<BaseResponse<VehicleDtoEntity>> getVehicleByVin(String vehicleVin);

    Observable<BaseResponse<Object>> activeVehicleBySn(String activateSn);

    Observable<BaseResponse<Object>> getVehicleConfirmPay(String deviceSn, float oweFee);

    Observable<BaseResponse<PdfEntity>> productDescription(String sn);
}
