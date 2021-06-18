package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

public class UserEntity implements Serializable {
    private static final long serialVersionUID = -1011L;

    /**
     * valid : true
     * bind : true
     * token : RTgwNEU1Qjg5OUM2RTlCREY4NDQ1QUEyRTMwOEY1QzAyNDgyN0UxNzZFQTcyQjNFNTcxNDA2NEQyNzk5NjA5MjdGNEVDODBBNTFGNDJGRUQxRjUxQTFBMENFRjNDN0VFRERFRTYzMTlEQTVDMzgzNkYwRTExNUVGMENBNTMyN0Y4QTI1QjJDOEJCQzAwOTVDQzRCOTY3Mjc3QTMyMjQ4NQGIANTGIANT
     * phone : 13182653333
     * userId : 31
     * vehicles : [{"id":"10000000000617","vin":"146312104500465","brandName":"GIANT 捷安特","vehicleModel":"GLORY ADVANCED 0","unitModelCn":"L1","unitType":"6","originalSn":"867964116000212","encryptSn":"702726E9FA9BED8F7B915100DACE8DA6","sn":"964116000212","unitId":"72","nickName":"我的爱车","userId":"31","oweFee":"0.0","unitStatus":1,"freeInsuredAmount":"0.0","oweFeeType":1,"telAlarmIsOpen":"1","telAlarmDeadline":"2025-06-15","fenceAlarmIsOpen":"1","fenceAlarmDeadline":"2021-12-15","telAlarmDate":"2025-06-15","fenceAlarmDate":"2021-12-15"}]
     * jieanteId : 20896334
     */

    private boolean valid;
    private boolean bind;
    private String token;
    private String phone;
    private String userId;
    private String jieanteId;
    private List<VehicleList> vehicles;

    public boolean getValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean getBind() {
        return bind;
    }

    public void setBind(boolean bind) {
        this.bind = bind;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getJieanteId() {
        return jieanteId;
    }

    public void setJieanteId(String jieanteId) {
        this.jieanteId = jieanteId;
    }

    public List<VehicleList> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<VehicleList> vehicles) {
        this.vehicles = vehicles;
    }

    public static class VehicleList {
        /**
         * id : 10000000000617
         * vin : 146312104500465
         * brandName : GIANT 捷安特
         * vehicleModel : GLORY ADVANCED 0
         * unitModelCn : L1
         * unitType : 6
         * originalSn : 867964116000212
         * encryptSn : 702726E9FA9BED8F7B915100DACE8DA6
         * sn : 964116000212
         * unitId : 72
         * nickName : 我的爱车
         * userId : 31
         * oweFee : 0.0
         * unitStatus : 1
         * freeInsuredAmount : 0.0
         * oweFeeType : 1
         * telAlarmIsOpen : 1
         * telAlarmDeadline : 2025-06-15
         * fenceAlarmIsOpen : 1
         * fenceAlarmDeadline : 2021-12-15
         * telAlarmDate : 2025-06-15
         * fenceAlarmDate : 2021-12-15
         */

        private String id;
        private String vin;
        private String brandName;
        private String vehicleModel;
        private String unitModelCn;
        private String unitType;
        private String originalSn;
        private String encryptSn;
        private String sn;
        private String unitId;
        private String nickName;
        private String userId;
        private String oweFee;
        private int unitStatus;
        private String freeInsuredAmount;
        private int oweFeeType;
        private String telAlarmIsOpen;
        private String telAlarmDeadline;
        private String fenceAlarmIsOpen;
        private String fenceAlarmDeadline;
        private String telAlarmDate;
        private String fenceAlarmDate;


        public VehicleList(String id, String vin, String sn, String nickName, String userId, String brandName) {
            this.id = id;
            this.vin = vin;
            this.sn = sn;
            this.nickName = nickName;
            this.userId = userId;
            this.brandName = brandName;
        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getVin() {
            return vin;
        }

        public void setVin(String vin) {
            this.vin = vin;
        }

        public String getBrandName() {
            return brandName;
        }

        public void setBrandName(String brandName) {
            this.brandName = brandName;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public String getUnitModelCn() {
            return unitModelCn;
        }

        public void setUnitModelCn(String unitModelCn) {
            this.unitModelCn = unitModelCn;
        }

        public String getUnitType() {
            return unitType;
        }

        public void setUnitType(String unitType) {
            this.unitType = unitType;
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

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
        }

        public String getUnitId() {
            return unitId;
        }

        public void setUnitId(String unitId) {
            this.unitId = unitId;
        }

        public String getNickName() {
            return nickName;
        }

        public void setNickName(String nickName) {
            this.nickName = nickName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getOweFee() {
            return oweFee;
        }

        public void setOweFee(String oweFee) {
            this.oweFee = oweFee;
        }

        public int getUnitStatus() {
            return unitStatus;
        }

        public void setUnitStatus(int unitStatus) {
            this.unitStatus = unitStatus;
        }

        public String getFreeInsuredAmount() {
            return freeInsuredAmount;
        }

        public void setFreeInsuredAmount(String freeInsuredAmount) {
            this.freeInsuredAmount = freeInsuredAmount;
        }

        public int getOweFeeType() {
            return oweFeeType;
        }

        public void setOweFeeType(int oweFeeType) {
            this.oweFeeType = oweFeeType;
        }

        public String getTelAlarmIsOpen() {
            return telAlarmIsOpen;
        }

        public void setTelAlarmIsOpen(String telAlarmIsOpen) {
            this.telAlarmIsOpen = telAlarmIsOpen;
        }

        public String getTelAlarmDeadline() {
            return telAlarmDeadline;
        }

        public void setTelAlarmDeadline(String telAlarmDeadline) {
            this.telAlarmDeadline = telAlarmDeadline;
        }

        public String getFenceAlarmIsOpen() {
            return fenceAlarmIsOpen;
        }

        public void setFenceAlarmIsOpen(String fenceAlarmIsOpen) {
            this.fenceAlarmIsOpen = fenceAlarmIsOpen;
        }

        public String getFenceAlarmDeadline() {
            return fenceAlarmDeadline;
        }

        public void setFenceAlarmDeadline(String fenceAlarmDeadline) {
            this.fenceAlarmDeadline = fenceAlarmDeadline;
        }

        public String getTelAlarmDate() {
            return telAlarmDate;
        }

        public void setTelAlarmDate(String telAlarmDate) {
            this.telAlarmDate = telAlarmDate;
        }

        public String getFenceAlarmDate() {
            return fenceAlarmDate;
        }

        public void setFenceAlarmDate(String fenceAlarmDate) {
            this.fenceAlarmDate = fenceAlarmDate;
        }
    }
}
