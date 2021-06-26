package com.luhong.locwithlibrary.utils;

import android.content.Context;
import android.text.TextUtils;

import com.luhong.locwithlibrary.api.AppConstants;
import com.luhong.locwithlibrary.api.AppVariable;
import com.luhong.locwithlibrary.app.BaseConstants;
import com.luhong.locwithlibrary.app.BaseVariable;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.UserEntity;

import java.util.List;

public class LoginSuccessUtil {
    /**
     * 登录成功
     * @param context
     * @param resultEntity
     */
    public static void onLoginSuccess(Context context, UserEntity resultEntity ) {
        String lastDeviceId = SPUtils.getString(context, AppConstants.LAST_DEVICE_ID_KEY + resultEntity.getPhone());
        //   SPUtils.put(context, BaseConstants.TOKEN, resultEntity.getToken());
        BaseVariable.userId = resultEntity.getUserId();
        BaseVariable.phone = resultEntity.getPhone();
        List<UserEntity.VehicleList> deviceList = resultEntity.getVehicles();
        if (deviceList != null && deviceList.size() != 0) {
            for (UserEntity.VehicleList device : deviceList) {
                if (device == null) continue;
                if ((!TextUtils.isEmpty(lastDeviceId) && !TextUtils.isEmpty(device.getSn()) && lastDeviceId.equals(device.getSn())) || TextUtils.isEmpty(lastDeviceId)) {//
                    AppVariable.currentDeviceId = device.getSn();
                    SPUtils.put(context, "currentDeviceId", AppVariable.currentDeviceId);
                    AppVariable.currentVehicleId = device.getId();
                    break;
                }
            }
        }
        SPUtils.put(context, BaseConstants.USERNAME_KEY, resultEntity.getPhone());
        boolean isSaveSuccess = SPUtils.putObject(context, BaseConstants.LOGIN_KEY, resultEntity);
        Logger.error("用户信息保存成功？=" + isSaveSuccess);
    }
}

