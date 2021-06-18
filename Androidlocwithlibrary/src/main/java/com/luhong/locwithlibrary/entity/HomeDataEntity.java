package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 首页同步数据
 */
public class HomeDataEntity implements Serializable {
    private float accountMoney;//流量账号余额
    private float flowDefaultCost;//月租
    private float firstActiveCost;//首次激活预充值费用
    private List<Vehicle> vehicles;

    public static class Vehicle implements Serializable {
        public static final int TYPE_ACTIVATED = 1;//1已激活
        public static final int TYPE_INACTIVATED = 0;//0未激活
        private String id;
        private String vin;
        private String sn;
        private String nickName;
        private String vehicleType = "";
        private String vehicleTypeCn;
        private String userId;
        private float oweFee;// 小于 0 设备欠钱金额
        private int oweFeeType;// 设备欠费类型 1：流量账户余额大于等于欠费额(确认补缴)；2：流量账户余额小于欠费额(立即补缴)；
        private int unitStatus;//设备激活状态（1激活 0未激活）

        public String getVehicleType() {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType) {
            this.vehicleType = vehicleType;
        }

        public String getVehicleTypeCn() {
            return vehicleTypeCn;
        }

        public void setVehicleTypeCn(String vehicleTypeCn) {
            this.vehicleTypeCn = vehicleTypeCn;
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

        public String getSn() {
            return sn;
        }

        public void setSn(String sn) {
            this.sn = sn;
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

        public float getOweFee() {
            return oweFee;
        }

        public void setOweFee(float oweFee) {
            this.oweFee = oweFee;
        }

        public int getUnitStatus() {
            return unitStatus;
        }

        public void setUnitStatus(int unitStatus) {
            this.unitStatus = unitStatus;
        }

        public int getOweFeeType() {
            return oweFeeType;
        }

        public void setOweFeeType(int oweFeeType) {
            this.oweFeeType = oweFeeType;
        }
    }

    public float getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(float accountMoney) {
        this.accountMoney = accountMoney;
    }

    public float getFlowDefaultCost() {
        return flowDefaultCost;
    }

    public void setFlowDefaultCost(float flowDefaultCost) {
        this.flowDefaultCost = flowDefaultCost;
    }

    public float getFirstActiveCost() {
        return firstActiveCost;
    }

    public void setFirstActiveCost(float firstActiveCost) {
        this.firstActiveCost = firstActiveCost;
    }

    public List<Vehicle> getVehicles() {
        return vehicles;
    }

    public void setVehicles(List<Vehicle> vehicles) {
        this.vehicles = vehicles;
    }
}
