package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

/**
 * "id":"1189235766423269378",
 * "vin":"123456743493",
 * "unitId":"123456743493",
 * "vehicleType":"3",
 * "brandId":"1181824206114013187",
 * "vehicleModel":"轻风2019",
 * "unitModel":"zx1",
 * "createTime":"2019-10-30 01:41:16",
 * "createId":"1",
 * "isDel":0,
 * "installModel":"0"
 */
public class VehicleListEntity implements Serializable {

    /**
     * "vehicleName":"",
     * "vehicleModel":"",
     * "vehicleModelName":"",
     * "vehicleType":"",
     * "vin":"109712105200040",
     * "brand":"",
     * "brandName":
     */

    private String vehicleName;
    private String vehicleType;
    private String vehicleModel;

    private String vin;
    private String brand;

    private String brandName;

    private String vehicleModelName;
    private boolean flag;

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public void setVehicleName(String vehicleName) {
        this.vehicleName = vehicleName;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }


    public String getVehicleModelName() {
        return vehicleModelName;
    }

    public void setVehicleModelName(String vehicleModelName) {
        this.vehicleModelName = vehicleModelName;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setBrandName(String brandName) {
        this.brandName = brand;
    }

    public String getBrandName() {
        return brandName;
    }


}
