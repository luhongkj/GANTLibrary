package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * "id":"1187262616203186178",
 * "brandId":"1181824206114013185",
 * "vin":"DA000000000005",
 * "vehicleType":"1",
 * "vehicleTypeCn":"自行车",
 * "vehicleModelCn":"测试版"
 */
public class VehicleDtoEntity implements Serializable
{
    private String id;
    private String brandId;
    private String vin;
    private String brandName;
    private String vehicleTypeCn;
    private String vehicleModel;//vehicleModelCn
    private String vehicleType;
    private boolean isCheck;


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getBrandId()
    {
        return brandId;
    }

    public void setBrandId(String brandId)
    {
        this.brandId = brandId;
    }

    public String getVin()
    {
        return vin;
    }

    public void setVin(String vin)
    {
        this.vin = vin;
    }

    public String getBrandName()
    {
        return brandName;
    }

    public void setBrandName(String brandName)
    {
        this.brandName = brandName;
    }

    public String getVehicleTypeCn()
    {
        return vehicleTypeCn;
    }

    public void setVehicleTypeCn(String vehicleTypeCn)
    {
        this.vehicleTypeCn = vehicleTypeCn;
    }

    public String getVehicleModel()
    {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel)
    {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType()
    {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType)
    {
        this.vehicleType = vehicleType;
    }

    public boolean isCheck()
    {
        return isCheck;
    }

    public void setCheck(boolean check)
    {
        isCheck = check;
    }
}
