package com.luhong.locwithlibrary.entity;


// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":11,//指令ID,必填
// "paramValue":0,//指令参数,必填。0：自动；1：手动
// "vehicleId":1//车辆ID，必填

import com.luhong.locwithlibrary.event.BaseCommandBean;

/**
 * 刹车灵敏度
 */
public class BrakeSensitivityParams extends BaseCommandBean
{
    /**
     * @param vehicleId 车辆ID，必填
     */
    public BrakeSensitivityParams(String vehicleId)
    {
        super(vehicleId, 13);
    }

    /**
     * @param paramValue 0:低,1:中,2:高
     */
    public void setParamValue(int paramValue)
    {
        super.setParamValue(paramValue);
    }
}
