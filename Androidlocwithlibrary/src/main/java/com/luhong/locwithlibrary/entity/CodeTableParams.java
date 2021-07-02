package com.luhong.locwithlibrary.entity;


import com.luhong.locwithlibrary.utils.Logger;
import com.luhong.locwithlibrary.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ITMG on 2019-12-05.
 */
public class CodeTableParams implements Serializable
{
    private String id;
    private String gpsTime;
    private String name;
    private String sn;
    private String ascent;//上坡
    private float avgSpeed;
    private String calorie;
    private String endTime;
    private String vehicleId;
    private int insertTotal;
    private String maxSpeed;
    private String mileage;
    private int model = 1;
    private int type;
    private String order;
    private String planSpeed;
    private String reduction;
    private int rideTime;
    private String sidx;
    private String startTime;
    private String userId;
    private List<LocationList> list;

    public static class LocationList implements Serializable
    {
        private String userId;
        private double lat;
        private double lon;
        private float speed;
        private double elevation;//海拔
        private String time;

        public LocationList()
        {
        }

        public LocationList(double lat, double lon, float speed, double elevation, String time)
        {
            this.lat = lat;
            this.lon = lon;
            this.speed = speed;
            this.elevation = elevation;
            this.time = time;
        }

        public String getUserId()
        {
            return userId;
        }

        public void setUserId(String userId)
        {
            this.userId = userId;
        }

        public double getElevation()
        {
            return elevation;
        }

        public void setElevation(double elevation)
        {
            this.elevation = elevation;
        }

        public double getLat()
        {
            return lat;
        }

        public void setLat(double lat)
        {
            this.lat = lat;
        }

        public double getLon()
        {
            return lon;
        }

        public void setLon(double lon)
        {
            this.lon = lon;
        }

        public float getSpeed()
        {
            return speed;
        }

        public void setSpeed(float speed)
        {
            this.speed = speed;
        }

        public String getTime()
        {
            return time;
        }

        public void setTime(String time)
        {
            this.time = time;
        }
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getAscent()
    {
        return ascent;
    }

    public void setAscent(String ascent)
    {
        this.ascent = ascent;
    }

    public float getAvgSpeed()
    {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed)
    {
        this.avgSpeed = avgSpeed;
    }

    public String getCalorie()
    {
        return calorie;
    }

    public void setCalorie(String calorie)
    {
        this.calorie = calorie;
    }

    public String getEndTime()
    {
        return endTime;
    }

    public void setEndTime(String endTime)
    {
        this.endTime = endTime;
    }

    public String getGpsTime()
    {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime)
    {
        this.gpsTime = gpsTime;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getSn()
    {
        return sn;
    }

    public void setSn(String sn)
    {
        this.sn = sn;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getInsertTotal()
    {
        return insertTotal;
    }

    public void setInsertTotal(int insertTotal)
    {
        this.insertTotal = insertTotal;
    }

    public String getMaxSpeed()
    {
        return maxSpeed;
    }

    public void setMaxSpeed(String maxSpeed)
    {
        this.maxSpeed = maxSpeed;
    }

    public String getMileage()
    {
        return mileage;
    }

    public void setMileage(String mileage)
    {
        this.mileage = mileage;
    }

    public int getModel()
    {
        return model;
    }

    public void setModel(int model)
    {
        this.model = model;
    }

    public String getOrder()
    {
        return order;
    }

    public void setOrder(String order)
    {
        this.order = order;
    }

    public String getPlanSpeed()
    {
        return planSpeed;
    }

    public void setPlanSpeed(String planSpeed)
    {
        this.planSpeed = planSpeed;
    }

    public String getReduction()
    {
        return reduction;
    }

    public void setReduction(String reduction)
    {
        this.reduction = reduction;
    }

    public int getRideTime()
    {
        return rideTime;
    }

    public void setRideTime(int rideTime)
    {
        this.rideTime = rideTime;
    }

    public String getSidx()
    {
        return sidx;
    }

    public void setSidx(String sidx)
    {
        this.sidx = sidx;
    }

    public String getStartTime()
    {
        return startTime;
    }

    public void setStartTime(String startTime)
    {
        this.startTime = startTime;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public List<LocationList> getList()
    {
        return list;
    }

    public void setList(List<LocationList> list)
    {
        this.list = list;
    }

    /**
     * 计算卡路里
     * 如果用户没有填写自己的体重信息和性别信息，那么体重因子和性别因子就不参与计算，也就是用如下公式：卡路里=骑行1小时消耗的卡路里(基于骑行均速(km/h)取值)*骑行时间（h）
     * 卡路里=骑行1小时消耗的卡路里(基于骑行均速(km/h)取值)*骑行时间（h）*体重因子*性别因子
     * <p>
     * 15以下/200
     * 15-18/300
     * 19-22/400
     * 23-26/500
     * 27-30/600
     * 31-35/700
     * 36-40/800
     * 41-45/900
     * 45以上/1000
     * ============
     * 36以下/0.94
     * 36-40/ 0.95
     * 41-45/ 0.96
     * 46-50/ 0.97
     * 51-55/0.98
     * 56-60/0.99
     * 61-65/1
     * 66-70/1.01
     * 71-75/1.02
     * 76-80/1.03
     * 81-85/1.04
     * 86-90/1.05
     * 91-95/1.06
     * 96-100/1.07
     * 100以上/1.08
     *
     * @param avgSpeed 均速 (km/h)
     * @param rideTime 骑行时间(h)
     * @param sex      性别
     * @param weight   体重(kg)
     * @return
     */
    public String calculateCalorie(float avgSpeed, float rideTime, int sex, float weight)
    {
        try
        {
            int speed = 0;
            if (avgSpeed < 15)
            {
                speed = 200;
            } else if (avgSpeed < 18)
            {
                speed = 300;
            } else if (avgSpeed < 22)
            {
                speed = 500;
            } else if (avgSpeed < 30)
            {
                speed = 600;
            } else if (avgSpeed < 35)
            {
                speed = 700;
            } else if (avgSpeed < 40)
            {
                speed = 800;
            } else if (avgSpeed < 45)
            {
                speed = 900;
            } else
            {
                speed = 1000;
            }
            if (sex == 2 || weight == 0)
            {//身高或体重未填写
                return StringUtils.formatSingleData(speed * rideTime);
            } else
            {
                float sexFactor = 1f;//男1.03，女1
                if (sex == 1)
                {//男
                    sexFactor = 1.03f;
                }
                float weightFactor;
                if (weight < 36)
                {
                    weightFactor = 0.94f;
                } else if (weight < 40)
                {
                    weightFactor = 0.95f;
                } else if (weight < 45)
                {
                    weightFactor = 0.96f;
                } else if (weight < 50)
                {
                    weightFactor = 0.97f;
                } else if (weight < 55)
                {
                    weightFactor = 0.98f;
                } else if (weight < 60)
                {
                    weightFactor = 0.99f;
                } else if (weight < 65)
                {
                    weightFactor = 1f;
                } else if (weight < 70)
                {
                    weightFactor = 1.01f;
                } else if (weight < 75)
                {
                    weightFactor = 1.02f;
                } else if (weight < 80)
                {
                    weightFactor = 1.03f;
                } else if (weight < 85)
                {
                    weightFactor = 1.04f;
                } else if (weight < 90)
                {
                    weightFactor = 1.05f;
                } else if (weight < 95)
                {
                    weightFactor = 1.06f;
                } else if (weight < 100)
                {
                    weightFactor = 1.07f;
                } else
                {
                    weightFactor = 1.08f;
                }
                return StringUtils.formatSingleData(speed * rideTime * sexFactor * weightFactor);
            }
        } catch (Exception e)
        {
            Logger.error("卡路里计算出错=" + e);
        }
        return "";
    }
}
