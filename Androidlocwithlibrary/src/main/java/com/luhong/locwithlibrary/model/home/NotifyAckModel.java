package com.luhong.locwithlibrary.model.home;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.model.INotifyAckModel;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public class NotifyAckModel implements INotifyAckModel {


    @Override
    public Observable<BaseResponse<Object>> notifyAck(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().notifyAck(bodyParams);
    }

}
