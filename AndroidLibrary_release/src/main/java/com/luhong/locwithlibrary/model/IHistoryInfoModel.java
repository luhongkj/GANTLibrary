package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.entity.HistoryInfoEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IHistoryInfoModel {
    Observable<BaseResponse<HistoryInfoEntity>> getHistoryInfo(Map<String, Object> bodyParams);
}
