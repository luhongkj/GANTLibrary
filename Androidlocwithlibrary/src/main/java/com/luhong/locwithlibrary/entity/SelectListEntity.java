package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class SelectListEntity implements Serializable
{
    private String content;
    private String deviceSn;
    private String vehicleId;
    private String vehicleBrand;

    public SelectListEntity(String content)
    {
        this.content = content;
    }

    public SelectListEntity(String content, String deviceSn, String vehicleId, String vehicleBrand)
    {
        this.content = content;
        this.deviceSn = deviceSn;
        this.vehicleId = vehicleId;
        this.vehicleBrand = vehicleBrand;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getDeviceSn()
    {
        return deviceSn;
    }

    public void setDeviceSn(String deviceSn)
    {
        this.deviceSn = deviceSn;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public String getVehicleBrand()
    {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand)
    {
        this.vehicleBrand = vehicleBrand;
    }
}
