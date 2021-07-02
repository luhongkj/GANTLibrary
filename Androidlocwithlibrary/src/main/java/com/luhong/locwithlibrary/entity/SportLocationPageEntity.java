package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 运动轨迹统计数据
 * Created by ITMG on 2019-12-14.
 */
public class SportLocationPageEntity<T> implements Serializable {
    public static final int TYPE_PHONE = 1;//手机
    public static final int TYPE_DEVICE = 2;//终端
    private String id;//列表ID
    private String userId; //用户ID
    private int type;//车辆类型
    private String vehicleId;//设备id
    private String name;//行程名称
    private String mileage;//里程20.5,
    private String ascent;//上坡15.2,
    private int rideTime;//骑行时间254,
    private float avgSpeed;//均速15.6,
    private float maxSpeed;//极速20.5,
    private float planSpeed;//配速5.6,
    private float reduction;//减排36.4,
    private float calorie;//卡路里253.5,
    private String weather;//天气晴
    private float temperature;//温度20,
    private int model;//":1,手机,2终端
    private String sn;//设备SN
    private String startTime;//":"2019-12-05 09:10:10", 行驶开始时间
    private String endTime;//":"2019-12-05 09:10:16"//行驶结束时间

    private List<T> list;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public String getAscent() {
        return ascent;
    }

    public void setAscent(String ascent) {
        this.ascent = ascent;
    }

    public int getRideTime() {
        return rideTime;
    }

    public void setRideTime(int rideTime) {
        this.rideTime = rideTime;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public float getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(float maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public float getPlanSpeed() {
        return planSpeed;
    }

    public void setPlanSpeed(float planSpeed) {
        this.planSpeed = planSpeed;
    }

    public float getReduction() {
        return reduction;
    }

    public void setReduction(float reduction) {
        this.reduction = reduction;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getModel() {
        return model;
    }

    public void setModel(int model) {
        this.model = model;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
