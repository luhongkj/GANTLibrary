package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public class DeviceManageModel implements IDeviceManageModel {

    @Override
    public Observable<BaseResponse<List<DeviceEntity>>> getDeviceList() {
        Map<String, Object> bodyParams = new HashMap<>();
        return ApiClient.getInstance().getApiServer().getDeviceList(bodyParams);
    }

    @Override
    public Observable<BaseResponse<DeviceEntity>> updateVehicle(DeviceEntity deviceEntity) {
        return ApiClient.getInstance().getApiServer().updateVehicle(deviceEntity);
    }

    @Override
    public Observable<BaseResponse<DeviceEntity>> deleteVehicle(DeviceEntity deviceEntity) {
        return ApiClient.getInstance().getApiServer().deleteVehicle(deviceEntity);
    }

    @Override
    public Observable<BaseResponse<Object>> addVehicle(DeviceEntity deviceEntity) {
        return ApiClient.getInstance().getApiServer().saveDevice(deviceEntity);
    }

    @Override
    public Observable<BaseResponse<InstallModeEntity>> getInstallMode(String deviceSn) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", deviceSn);
        return ApiClient.getInstance().getApiServer().getInstallMode(bodyParams);
    }

    @Override
    public Observable<BaseResponse<DeviceServerEntity>> getVehicleBySn(String deviceSn) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", deviceSn);
        return ApiClient.getInstance().getApiServer().getVehicleBySn(bodyParams);
    }

    @Override
    public Observable<BaseResponse<VehicleDtoEntity>> getVehicleByVin(String vehicleVin) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vin", vehicleVin);
        return ApiClient.getInstance().getApiServer().getVehicleByVin(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> activeVehicleBySn(String activateSn) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", activateSn);//需要激活的设备sn
        return ApiClient.getInstance().getApiServer().activeVehicleBySn(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> getVehicleConfirmPay(String deviceSn, float oweFee) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", deviceSn);
        bodyParams.put("oweFee", oweFee);//oweFee(欠费金额，例如：-5)
        return ApiClient.getInstance().getApiServer().getVehicleConfirmPay(bodyParams);
    }

    @Override
    public Observable<BaseResponse<PdfEntity>> productDescription(String sn) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", sn);
        return ApiClient.getInstance().getApiServer().getProductDescription(bodyParams);
    }
}
