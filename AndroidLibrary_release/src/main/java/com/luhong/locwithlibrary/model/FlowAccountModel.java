package com.luhong.locwithlibrary.model;

import com.luhong.locwithlibrary.api.ApiClient;
import com.luhong.locwithlibrary.contract.IFlowAccountModel;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2019/8/15 0015.
 */

public class FlowAccountModel implements IFlowAccountModel
{

    @Override
    public Observable<BaseResponse<FlowAccountEntity>> getFlowAccountInfo()
    {
        Map<String, Object> bodyParams = new HashMap<>();
        return ApiClient.getInstance().getApiServer().getFlowAccountInfo(bodyParams);
    }

    @Override
    public Observable<BaseResponse<BasePageEntity<FlowBillEntity>>> getFlowBill(int pageSize, int pageNo)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("size", pageSize);
        bodyParams.put("current", pageNo);
        return ApiClient.getInstance().getApiServer().getFlowBill(bodyParams);
    }

   /* @Override
    public Observable<BaseResponse<List<PayPackageEntity>>> getPayPackage(int dataType)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        //        bodyParams.put("type", dataType);
        if (dataType == RechargeActivity.TYPE_FLOW)
        {
            return ApiClient.getInstance().getApiServer().getFlowSets(bodyParams);
        } else if (dataType == RechargeActivity.TYPE_PHONE)
        {
            return ApiClient.getInstance().getApiServer().getPhoneAlarmSets(bodyParams);
        } else if (dataType == RechargeActivity.TYPE_FENCE)
        {
            return ApiClient.getInstance().getApiServer().getFenceAlarmSets(bodyParams);
        } else
        {
            //            return ApiClient.getInstance().getApiServer().getFlowSets(bodyParams);
            return null;
        }
    }*/

    @Override
    public Observable<BaseResponse<BasePageEntity<RechargeRecordEntity>>> getRechargeRecord(int pageSize, int pageNo)
    {
        Map<String, Object> bodyParams = new HashMap<>();
        bodyParams.put("size", pageSize);
        bodyParams.put("current", pageNo);
        return ApiClient.getInstance().getApiServer().getRechargeRecord(bodyParams);
    }


}
