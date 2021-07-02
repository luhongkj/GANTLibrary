package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.entity.HistoryInfoEntity;
import com.luhong.locwithlibrary.model.IHistoryInfoModel;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public class HistoryModel implements IHistoryInfoModel {
    @Override
    public Observable<BaseResponse<HistoryInfoEntity>> getHistoryInfo(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().sendHistoryInfo(bodyParams);
    }
}
