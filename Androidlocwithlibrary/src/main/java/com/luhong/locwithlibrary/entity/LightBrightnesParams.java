package com.luhong.locwithlibrary.entity;


// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":11,//指令ID,必填
// "paramValue":0,//指令参数,必填。0：自动；1：手动
// "vehicleId":1//车辆ID，必填

import com.luhong.locwithlibrary.event.BaseCommandBean;

/**
 * 灯亮度大小设置
 */
public class LightBrightnesParams extends BaseCommandBean
{
    /**
     * @param vehicleId 车辆ID，必填
     */
    public LightBrightnesParams(String vehicleId)
    {
        super(vehicleId, 12);
    }

    /**
     * @param paramValue 指令参数,必填。值为0-100
     */
    public void setParamValue(int paramValue)
    {
        super.setParamValue(paramValue);
    }
}
