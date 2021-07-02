package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class DevicePositionEntity implements Serializable
{
    public static final String TYPE_FORTIFICATION = "22";//设防状态
    public static final String TYPE_UNFORTIFICATION = "32";//解防状态
    private String safy;//车辆设防状态(设防22，撤防32)
    private String activeTime;//终端上报时间
    private String imei;//设备号
    private String course;//方向角
    private String gpsmile;
    private String gateway;//后台上线网关
    private double lat;//纬度
    private double lon;//经度
    private String speed;//不用管
    private String gpsTime;//终端gps时间
    private String states;//车辆设防/撤防状态(车辆设防22，车辆撤防32) 33,247,72,70,32,21,76,503
    private int locState;//是否定位：1定位 ,0未定位

    private String vehicleId;//车辆主键
    private String charge;//充电状态：0充电;1未充电
    private String sport;//运行状态:1运动,0静止
    private int gnss;//卫星个数
    private int battery;//电量百分比
    private int network;//信号强度
    private int trackTime;//导航追车刷新间隔（秒）

    public String getSafy()
    {
        return safy;
    }

    public void setSafy(String safy)
    {
        this.safy = safy;
    }

    public String getActiveTime()
    {
        return activeTime;
    }

    public void setActiveTime(String activeTime)
    {
        this.activeTime = activeTime;
    }

    public String getImei()
    {
        return imei;
    }

    public void setImei(String imei)
    {
        this.imei = imei;
    }

    public String getCourse()
    {
        return course;
    }

    public void setCourse(String course)
    {
        this.course = course;
    }

    public String getGpsmile()
    {
        return gpsmile;
    }

    public void setGpsmile(String gpsmile)
    {
        this.gpsmile = gpsmile;
    }

    public String getGateway()
    {
        return gateway;
    }

    public void setGateway(String gateway)
    {
        this.gateway = gateway;
    }

    public double getLat()
    {
        return lat;
    }

    public void setLat(double lat)
    {
        this.lat = lat;
    }

    public String getSpeed()
    {
        return speed;
    }

    public void setSpeed(String speed)
    {
        this.speed = speed;
    }

    public String getGpsTime()
    {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime)
    {
        this.gpsTime = gpsTime;
    }

    public double getLon()
    {
        return lon;
    }

    public void setLon(double lon)
    {
        this.lon = lon;
    }

    public String getStates()
    {
        return states;
    }

    public void setStates(String states)
    {
        this.states = states;
    }

    public int getLocState()
    {
        return locState;
    }

    public void setLocState(int locState)
    {
        this.locState = locState;
    }

    public int getNetwork()
    {
        return network;
    }

    public void setNetwork(int network)
    {
        this.network = network;
    }

    public String getVehicleId()
    {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId)
    {
        this.vehicleId = vehicleId;
    }

    public String getCharge()
    {
        return charge;
    }

    public void setCharge(String charge)
    {
        this.charge = charge;
    }

    public String getSport()
    {
        return sport;
    }

    public void setSport(String sport)
    {
        this.sport = sport;
    }

    public int getGnss()
    {
        return gnss;
    }

    public void setGnss(int gnss)
    {
        this.gnss = gnss;
    }

    public int getBattery()
    {
        return battery;
    }

    public void setBattery(int battery)
    {
        this.battery = battery;
    }

    public int getTrackTime()
    {
        return trackTime;
    }

    public void setTrackTime(int trackTime)
    {
        this.trackTime = trackTime;
    }
}
