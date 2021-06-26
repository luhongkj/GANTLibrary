package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 电子围栏半径
 * Created by ITMG on 2020-01-03.
 */
public class FenceRadiusEntity implements Serializable
{
    private String id;
    private String sn;
    private String vin;
    private int status;//":"1",
    private int freeFlowTimes;//":4,
    private float oweFee;
    private int installModel;//":"0",
    private String unitModel;
    private String vehicleId;
    private String fenceAlarmRadius;

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getVin()
    {
        return vin;
    }

    public void setVin(String vin)
    {
        this.vin = vin;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getFreeFlowTimes()
    {
        return freeFlowTimes;
    }

    public void setFreeFlowTimes(int freeFlowTimes)
    {
        this.freeFlowTimes = freeFlowTimes;
    }

    public float getOweFee()
    {
        return oweFee;
    }

    public void setOweFee(float oweFee)
    {
        this.oweFee = oweFee;
    }

    public int getInstallModel()
    {
        return installModel;
    }

    public void setInstallModel(int installModel)
    {
        this.installModel = installModel;
    }

    public String getUnitModel()
    {
        return unitModel;
    }

    public void setUnitModel(String unitModel)
    {
        this.unitModel = unitModel;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public String getFenceAlarmRadius()
    {
        return fenceAlarmRadius;
    }

    public void setFenceAlarmRadius(String fenceAlarmRadius)
    {
        this.fenceAlarmRadius = fenceAlarmRadius;
    }
}
