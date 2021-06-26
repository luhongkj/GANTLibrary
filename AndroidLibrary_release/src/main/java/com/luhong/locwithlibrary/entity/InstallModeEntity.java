package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

public class InstallModeEntity implements Serializable
{
    private int status;//0设备未激活。1，
    private int installModel;//0前装
    private String sn;//设备Sn
    private String originalSn;//15位设备Sn
    private String unitModel;//设备型号
    private String telAlarmDate;//电话告警过期时间
    private String fenceAlarmDate;//电子围栏过期时间
    private int oweFeeType;// 设备欠费类型 1：流量账户余额大于等于欠费额(确认补缴)；2：流量账户余额小于欠费额(立即补缴)；
    private float oweFee;
    private boolean isTelPass;//电话告警是否过期
    private boolean isFencePass;//电子围栏是否过期
    private VehicleDto vehicleDto;
    private List<BeforeBandVehicle> beforeBandVehicles;

    public InstallModeEntity()
    {
    }

    public static class BeforeBandVehicle implements Serializable
    {
        private String id;
        private String vin;
        private String brandId;
        private String brandName;
        private String sn;
        private String nickName;
        private String unitId;
        private String unitModelCn;
        private int unitStatus;
        private String userId;
        private String vehicleModel;
        private String vehicleType;
        private String vehicleTypeCn="";
        private boolean isCheck;

        public BeforeBandVehicle()
        {
        }

        public BeforeBandVehicle(String vin, boolean isCheck)
        {
            this.vin = vin;
            this.isCheck = isCheck;
        }

        public BeforeBandVehicle(String beforeVin)
        {
            this.vin = beforeVin;
        }

        public String getId()
        {
            return id;
        }

        public void setId(String id)
        {
            this.id = id;
        }

        public String getVin()
        {
            return vin;
        }

        public void setVin(String vin)
        {
            this.vin = vin;
        }

        public String getBrandId()
        {
            return brandId;
        }

        public void setBrandId(String brandId)
        {
            this.brandId = brandId;
        }

        public String getBrandName()
        {
            return brandName;
        }

        public void setBrandName(String brandName)
        {
            this.brandName = brandName;
        }

        public String getSn()
        {
            return sn;
        }

        public void setSn(String sn)
        {
            this.sn = sn;
        }

        public String getNickName()
        {
            return nickName;
        }

        public void setNickName(String nickName)
        {
            this.nickName = nickName;
        }

        public String getUnitId()
        {
            return unitId;
        }

        public void setUnitId(String unitId)
        {
            this.unitId = unitId;
        }

        public String getUnitModelCn()
        {
            return unitModelCn;
        }

        public void setUnitModelCn(String unitModelCn)
        {
            this.unitModelCn = unitModelCn;
        }

        public int getUnitStatus()
        {
            return unitStatus;
        }

        public void setUnitStatus(int unitStatus)
        {
            this.unitStatus = unitStatus;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
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

        public String getVehicleTypeCn()
        {
            return vehicleTypeCn;
        }

        public void setVehicleTypeCn(String vehicleTypeCn)
        {
            this.vehicleTypeCn = vehicleTypeCn;
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

    public static class VehicleDto implements Serializable
    {
        private String id;
        private String brandId;
        private String vin;
        private String brandName;
        private String vehicleTypeCn;
        private String vehicleModelCn;
        private String vehicleType;
        private String vehicleModel;

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

        public String getVehicleModelCn()
        {
            return vehicleModelCn;
        }

        public void setVehicleModelCn(String vehicleModelCn)
        {
            this.vehicleModelCn = vehicleModelCn;
        }

        public String getVehicleType()
        {
            return vehicleType;
        }

        public void setVehicleType(String vehicleType)
        {
            this.vehicleType = vehicleType;
        }
    }

    public VehicleDto getVehicleDto()
    {
        return vehicleDto;
    }

    public void setVehicleDto(VehicleDto vehicleDto)
    {
        this.vehicleDto = vehicleDto;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public int getInstallModel()
    {
        return installModel;
    }

    public void setInstallModel(int installModel)
    {
        this.installModel = installModel;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getOriginalSn()
    {
        return originalSn;
    }

    public void setOriginalSn(String originalSn)
    {
        this.originalSn = originalSn;
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

    public List<BeforeBandVehicle> getBeforeBandVehicles()
    {
        return beforeBandVehicles;
    }

    public void setBeforeBandVehicles(List<BeforeBandVehicle> beforeBandVehicles)
    {
        this.beforeBandVehicles = beforeBandVehicles;
    }
}
