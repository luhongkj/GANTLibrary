package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.BannerEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IServerModel {
    Observable<BaseResponse<BannerEntity>> getBanner();

    Observable<BaseResponse<BasePageEntity<PhoneAlarmEntity>>> getPhoneAlarm(int dataType, Map<String, Object> bodyParams);

    Observable<BaseResponse<FenceRadiusEntity>> getFenceRadius();

    Observable<BaseResponse<PdfEntity>> productDescription();

}
