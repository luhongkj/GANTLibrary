package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * {
 *     "value": 1, //0关闭，1开启
 *     "type": 1 //告警通知
 * },
 * {
 *     "value": 1, //0关闭，1开启
 *     "type": 2 //告警声音
 * },
 * {
 *     "value": 0, //0关闭，1开启
 *     "type": 3 //自动设防
 * },
 * {
 *     "value": 1, //0关闭，1开启
 *     "type": 4 //状态灯
 * },
 * {
 *     "value": 1, //0:低,1:中,2:高
 *     "type": 5 //告警灵敏度
 * },
 * {
 *     "value": 1, //0:低,1:中,2:高
 *     "type": 6 //刹车灵敏度
 * },
 * {
 *     "value": 0, //0：自动；1：手动
 *     "type": 7 //警示灯工作模式
 * },
 * {
 *     "value": 50, //0~100整数
 *     "type": 8 //亮度调节
 * },
 * {
 *     "value": 5, //1:SOS;2:闪烁模式1；3：闪烁模式2；4:闪灯；5呼吸灯
 *     "type": 9 //闪烁模式
 * },
 * {
 *     "value": 2, //0：灯常亮,1灯常灭,2闪烁
 *     "type": 10 //闪灯模式
 * },
 * {
 *     "value": 1, //0:慢速；1:正常；2:快速
 *     "type": 11 //闪烁频率
 * }
 * {
 *     "value": 1,//0关闭，1开启
 *     "type": 12//紧急救援开关
 *}
 */
public class SettingEntity implements Serializable
{
    private int size;
    private String vehicleId;
    private int value;//0默认关，1开
    private int type;
    private String name;

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public int getValue()
    {
        return value;
    }

    public void setValue(int value)
    {
        this.value = value;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
