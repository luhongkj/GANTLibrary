package com.luhong.locwithlibrary.entity;


// "flowId":"123456",//流水号,每次唯一，必填
// "msgId":17,//指令ID,必填
// "paramValue":0,//指令参数,必填。由十位(1:SOS;2:闪烁模式1；3：闪烁模式2；4:闪灯；5呼吸灯)+个位(0:慢速；1:正常；2:快速)组成数值。
// "vehicleId":1//车辆ID，必填

import com.luhong.locwithlibrary.event.BaseCommandBean;

/**
 * 闪烁模式
 */
public class LightShakeModelParams extends BaseCommandBean
{
    /**
     * @param vehicleId 车辆ID，必填
     */
    public LightShakeModelParams(String vehicleId)
    {
        super(vehicleId, 17);
    }

    /**
     * @param paramValue 由十位(1:SOS;2:闪烁模式1；3：闪烁模式2；4:闪灯；5呼吸灯)+个位(0:慢速；1:正常；2:快速)组成数值。
     */
    public void setParamValue(int paramValue)
    {
        super.setParamValue(paramValue);
    }
}
