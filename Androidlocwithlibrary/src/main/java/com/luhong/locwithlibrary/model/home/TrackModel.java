package com.luhong.locwithlibrary.model.home;

import android.text.TextUtils;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.model.home.ITrackModel;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2019-8-15.
 */
public class TrackModel implements ITrackModel {

    @Override
    public Observable<BaseResponse<DevicePositionEntity>> getTrackInfo(String vehicleId) {
        Map<String, Object> bodyParams = new HashMap<>();
        if (!TextUtils.isEmpty(vehicleId))
            bodyParams.put("vehicleId", vehicleId);
        return ApiClient.getInstance().getApiServer().getTrackInfo(bodyParams);
    }

    @Override
    public Observable<BaseResponse<TrackInfoRefreshEntity>> getTrackInfoRefresh() {
        Map<String, Object> bodyParams = new HashMap<>();
        if (!TextUtils.isEmpty(AppVariable.currentDeviceId))
            bodyParams.put("sn", AppVariable.currentDeviceId);
        return ApiClient.getInstance().getApiServer().getTrackInfoRefresh(bodyParams);
    }

}
