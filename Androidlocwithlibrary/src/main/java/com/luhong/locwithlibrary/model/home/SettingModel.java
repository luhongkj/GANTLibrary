package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public class SettingModel implements ISettingModel
{

    @Override
    public Observable<BaseResponse<List<SettingEntity>>> getSetting()
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        return ApiClient.getInstance().getApiServer().getSetting(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> setSetting(int type, boolean isChecked)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("vehicleId", AppVariable.currentVehicleId);
        bodyParams.put("type", type);
        bodyParams.put("value", isChecked ? 1 : 0);
        return ApiClient.getInstance().getApiServer().setSetting(bodyParams);
    }
}
