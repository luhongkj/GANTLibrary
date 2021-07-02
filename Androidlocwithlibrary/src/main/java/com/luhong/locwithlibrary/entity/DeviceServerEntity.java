package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class DeviceServerEntity implements Serializable
{
    private int status;//0设备未激活。1，
    private String sn;//设备Sn
    private String unitModel;//设备型号
    private String telAlarmDate;//电话告警过期时间
    private String fenceAlarmDate;//电子围栏过期时间
    private int oweFeeType;// 设备欠费类型 1：流量账户余额大于等于欠费额(确认补缴)；2：流量账户余额小于欠费额(立即补缴)；
    private float oweFee;
    private boolean isTelPass;//电话告警是否过期
    private boolean isFencePass;//电子围栏是否过期

    public DeviceServerEntity()
    {
    }


    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getUnitModel()
    {
        return unitModel;
    }

    public void setUnitModel(String unitModel)
    {
        this.unitModel = unitModel;
    }

    public String getTelAlarmDate()
    {
        return telAlarmDate;
    }

    public void setTelAlarmDate(String telAlarmDate)
    {
        this.telAlarmDate = telAlarmDate;
    }

    public String getFenceAlarmDate()
    {
        return fenceAlarmDate;
    }

    public void setFenceAlarmDate(String fenceAlarmDate)
    {
        this.fenceAlarmDate = fenceAlarmDate;
    }

    public int getOweFeeType()
    {
        return oweFeeType;
    }

    public void setOweFeeType(int oweFeeType)
    {
        this.oweFeeType = oweFeeType;
    }

    public float getOweFee()
    {
        return oweFee;
    }

    public void setOweFee(float oweFee)
    {
        this.oweFee = oweFee;
    }

    public boolean isTelPass()
    {
        return isTelPass;
    }

    public void setTelPass(boolean telPass)
    {
        isTelPass = telPass;
    }

    public boolean isFencePass()
    {
        return isFencePass;
    }

    public void setFencePass(boolean fencePass)
    {
        isFencePass = fencePass;
    }

}
