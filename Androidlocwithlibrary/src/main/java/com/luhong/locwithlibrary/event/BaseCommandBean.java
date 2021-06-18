package com.luhong.locwithlibrary.event;

import java.util.HashMap;
import java.util.UUID;

// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":11,//指令ID,必填
// "paramValue":0,//指令参数,必填
// "vehicleId":1//车辆ID，必填

/**
 * 命令基类
 */
public class BaseCommandBean extends HashMap<String, Object> {
    /**
     * @param vehicleId 车辆ID，必填
     * @param msgId     命令
     */
    public BaseCommandBean(String vehicleId, int msgId) {
        this.put("flowId", UUID.randomUUID().toString());
        this.put("msgId", msgId);
        this.put("vehicleId", vehicleId);
    }

    /**
     * @param paramValue 指令参数
     */
    public void setParamValue(int paramValue) {
        this.put("paramValue", paramValue);
    }
}
