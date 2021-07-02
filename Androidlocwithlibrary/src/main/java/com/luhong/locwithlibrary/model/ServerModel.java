package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.contract.IServerModel;
import com.luhong.locwithlibrary.entity.BannerEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */

public class ServerModel implements IServerModel {

    @Override
    public Observable<BaseResponse<BannerEntity>> getBanner() {
        Map<String, Object> bodyParams = new HashMap<>();
        //        bodyParams.put("name", "用户APP服务banner");//参数固定
        bodyParams.put("type", "1");
        return ApiClient.getInstance().getApiServer().getBanner(bodyParams);
    }

    @Override
    public Observable<BaseResponse<BasePageEntity<PhoneAlarmEntity>>> getPhoneAlarm(int dataType, Map<String, Object> bodyParams) {
        if (dataType == 0) {
            return ApiClient.getInstance().getApiServer().getPhoneAlarm(bodyParams);
        } else {
            return ApiClient.getInstance().getApiServer().getSMSAlarm(bodyParams);
        }
    }

    @Override
    public Observable<BaseResponse<FenceRadiusEntity>> getFenceRadius() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", "" + AppVariable.currentVehicleId);
        return ApiClient.getInstance().getApiServer().getFenceRadius(bodyParams);
    }


    @Override
    public Observable<BaseResponse<PdfEntity>> productDescription() {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("sn", "" + AppVariable.currentDeviceId);
        return ApiClient.getInstance().getApiServer().getProductDescription(bodyParams);
    }

}
