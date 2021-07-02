package com.luhong.locwithlibrary.contract;

import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;

import io.reactivex.Observable;

/**
 * Created by ITMG on 2020-01-21.
 */
public interface IFlowAccountModel
{
    Observable<BaseResponse<FlowAccountEntity>> getFlowAccountInfo();

    Observable<BaseResponse<BasePageEntity<FlowBillEntity>>> getFlowBill(int pageSize, int pageNo);

   // Observable<BaseResponse<List<PayPackageEntity>>> getPayPackage(int dataType);

    Observable<BaseResponse<BasePageEntity<RechargeRecordEntity>>> getRechargeRecord(int pageSize, int pageNo);
}
