package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2018/8/15 0015.
 */
public interface ITrackModel
{

    Observable<BaseResponse<DevicePositionEntity>> getTrackInfo(String vehicleId);

    Observable<BaseResponse<TrackInfoRefreshEntity>> getTrackInfoRefresh();
}
