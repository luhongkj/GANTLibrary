package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface ISettingModel
{
    Observable<BaseResponse<List<SettingEntity>>> getSetting();

    Observable<BaseResponse<Object>> setSetting(int type, boolean isChecked);
}
