package com.luhong.locwithlibrary.entity;


// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":16,//指令ID,必填
// "paramValue":0,//指令参数,必填。0：灯常亮,1灯常灭,2闪烁
// "vehicleId":1//车辆ID，必填

import com.luhong.locwithlibrary.event.BaseCommandBean;

/**
 * 亮灯模式
 */
public class LightModelParams extends BaseCommandBean {
    /**
     * @param vehicleId 车辆ID，必填
     */
    public LightModelParams(String vehicleId) {
        super(vehicleId, 16);
    }

    /**
     * @param paramValue 0：灯常亮,1灯常灭,2闪烁
     */
    public void setParamValue(int paramValue) {
        super.setParamValue(paramValue);
    }
}
