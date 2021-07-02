package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.FeedbackContract;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2019/11/15 0015.
 */
public class FeedbackModel implements FeedbackContract.IModel {

    @Override
    public Observable<BaseResponse<List<FeedbackEntity>>> getFeedbackType() {
        Map<String, Object> bodyParams = new HashMap<>();
        return ApiClient.getInstance().getApiServer().getFeedbackType(bodyParams);
    }

    @Override
    public Observable<BaseResponse<BasePageEntity<FeedbackRecordEntity>>> getQuestions(int pageSize, int pageNo) {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("size", pageSize);
        bodyParams.put("current", pageNo);
        return ApiClient.getInstance().getApiServer().getQuestions(bodyParams);
    }

    @Override
    public Observable<BaseResponse<Object>> saveQuestions(Map<String, Object> bodyParams) {
        return ApiClient.getInstance().getApiServer().saveQuestions(bodyParams);
    }
}
