package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

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
public class DeviceEntity implements Serializable {
    private int createId;
    private String userId;
    private String updateId;
    private String id;
    private int isDel;
    private String nickName;//爱车名词
    private String sn;//设备sn
    private String originalSn;//设备sn(全)
    private String encryptSn;//设备sn(加密)
    private String unitModel;
    private String unitType = ""; //设备类型
    private String unitQrCode;
    private String updateTime;
    private String vehicleBrand;
    private String vehicleModel;//车辆型号

    /**
     * oweFee  欠费金额
     * oweFeeType 欠费类型（1：确认补缴，2、立即补缴（充钱去））
     */
    private float oweFee;//欠费金额
    private String oweFeeType;//欠费类型（1：确认补缴，2、立即补缴（充钱去））

    public void setOweFeeType(String oweFeeType) {
        this.oweFeeType = oweFeeType;
    }

    public String getOweFeeType() {
        return oweFeeType;
    }

    public void setOweFee(float oweFee) {
        this.oweFee = oweFee;
    }

    public float getOweFee() {
        return oweFee;
    }


    // 自行车 "1"
    // 电动车 "2"
    // 锂电助力车"3"
    private String vehicleType;//车辆类型
    private String vehicleVin;
    private String vehicleId;
    private String brandId;//品牌
    private String createTime;
    private String installModel;//0前装，1后装
    private String vin;//车架号
    private String brandName;//品牌名词
    private String vehicleTypeCn;//车辆类型
    private String vehicleModelCn;//车辆型号
    private String unitModelCn;//设备型号
    /**
     * fenceAlarmIsOpen:电子围栏服务是否开通：1是 0否
     * fenceAlarmDeadline:电子围栏服务到期时间
     * telAlarmIsOpen:电话告警服务是否开通：1是 0否
     * telAlarmDeadline:电话告警服务到期时间
     */
    private String fenceAlarmIsOpen = "";
    private String fenceAlarmDeadline;
    private String telAlarmIsOpen = "";
    private String telAlarmDeadline;

    public void setFenceAlarmIsOpen(String fenceAlarmIsOpen) {
        this.fenceAlarmIsOpen = fenceAlarmIsOpen;
    }

    public String getFenceAlarmIsOpen() {
        return fenceAlarmIsOpen;
    }

    public void setFenceAlarmDeadline(String fenceAlarmDeadline) {
        this.fenceAlarmDeadline = fenceAlarmDeadline;
    }

    public String getFenceAlarmDeadline() {
        return fenceAlarmDeadline;
    }

    public void setTelAlarmIsOpen(String telAlarmIsOpen) {
        this.telAlarmIsOpen = telAlarmIsOpen;
    }

    public String getTelAlarmIsOpen() {
        return telAlarmIsOpen;
    }

    public void setTelAlarmDeadline(String telAlarmDeadline) {
        this.telAlarmDeadline = telAlarmDeadline;
    }

    public String getTelAlarmDeadline() {
        return telAlarmDeadline;
    }

    public DeviceEntity() {
    }

    public DeviceEntity(String deviceName) {
        this.nickName = deviceName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getOriginalSn() {
        return originalSn;
    }

    public void setOriginalSn(String originalSn) {
        this.originalSn = originalSn;
    }

    public String getEncryptSn() {
        return encryptSn;
    }

    public void setEncryptSn(String encryptSn) {
        this.encryptSn = encryptSn;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getVehicleTypeCn() {
        return vehicleTypeCn;
    }

    public void setVehicleTypeCn(String vehicleTypeCn) {
        this.vehicleTypeCn = vehicleTypeCn;
    }

    public String getUnitModelCn() {
        return unitModelCn;
    }

    public void setUnitModelCn(String unitModelCn) {
        this.unitModelCn = unitModelCn;
    }

    public String getUnitModel() {
        return unitModel;
    }

    public void setUnitModel(String unitModel) {
        this.unitModel = unitModel;
    }

    public String getUnitQrCode() {
        return unitQrCode;
    }

    public void setUnitQrCode(String unitQrCode) {
        this.unitQrCode = unitQrCode;
    }

    public String getUpdateId() {
        return updateId;
    }

    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleVin() {
        return vehicleVin;
    }

    public void setVehicleVin(String vehicleVin) {
        this.vehicleVin = vehicleVin;
    }

    public int getCreateId() {
        return createId;
    }

    public void setCreateId(int createId) {
        this.createId = createId;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getInstallModel() {
        return installModel;
    }

    public void setInstallModel(String installModel) {
        this.installModel = installModel;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getVehicleModelCn() {
        return vehicleModelCn;
    }

    public void setVehicleModelCn(String vehicleModelCn) {
        this.vehicleModelCn = vehicleModelCn;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}
