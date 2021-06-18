package com.luhong.locwithlibrary.entity;


// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":11,//指令ID,必填
// "paramValue":0,//指令参数,必填。0：自动；1：手动
// "vehicleId":1//车辆ID，必填

import com.luhong.locwithlibrary.event.BaseCommandBean;

/**
 * 灯的工作模式
 */
public class LightWordModelParams extends BaseCommandBean
{
    /**
     * @param vehicleId 车辆ID，必填
     */
    public LightWordModelParams(String vehicleId)
    {
        super(vehicleId, 11);
    }

    /**
     * @param paramValue 指令参数,必填。0：自动；1：手动
     */
    public void setParamValue(int paramValue)
    {
        super.setParamValue(paramValue);
    }
}
