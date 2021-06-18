package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

public class SportEntity implements Serializable {
    private List<TrackList> trackList;
    private Count count;

    public static class Count implements Serializable {
        private int size;
        private String startTime;
        private String endTime;
        private String createTime;
        private float dayMileage;
        private float weekMileage;
        private float monthMileage;
        private float mileageTotal;
        private int rideTotal;
        private float reductionTotal;
        private float calorieTotal;

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public float getDayMileage() {
            return dayMileage;
        }

        public void setDayMileage(float dayMileage) {
            this.dayMileage = dayMileage;
        }

        public float getWeekMileage() {
            return weekMileage;
        }

        public void setWeekMileage(float weekMileage) {
            this.weekMileage = weekMileage;
        }

        public float getMonthMileage() {
            return monthMileage;
        }

        public void setMonthMileage(float monthMileage) {
            this.monthMileage = monthMileage;
        }

        public float getMileageTotal() {
            return mileageTotal;
        }

        public void setMileageTotal(float mileageTotal) {
            this.mileageTotal = mileageTotal;
        }

        public int getRideTotal() {
            return rideTotal;
        }

        public void setRideTotal(int rideTotal) {
            this.rideTotal = rideTotal;
        }

        public float getReductionTotal() {
            return reductionTotal;
        }

        public void setReductionTotal(float reductionTotal) {
            this.reductionTotal = reductionTotal;
        }

        public float getCalorieTotal() {
            return calorieTotal;
        }

        public void setCalorieTotal(float calorieTotal) {
            this.calorieTotal = calorieTotal;
        }
    }

    public static class TrackList implements Serializable {
        public static final int TYPE_PHONE = 1;//手机码表
        public static final int TYPE_DEVICE = 2;//自行车终端
        public static final int TYPE_E_DEVICE = 3;//电动车
        public static final int TYPE_LIB_DEVICE = 4;//锂电助力车

        private int size;
        private String id;
        private String userId;
        private int type;//
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
        private int model;//":1,
        private String sn;
        private String startTime;//":"2019-12-05 09:10:10",
        private String endTime;//":"2019-12-05 09:10:16"

        public int getSize() {
            return size;
        }

        public void setSize(int size) {
            this.size = size;
        }

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
    }

    public List<TrackList> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<TrackList> trackList) {
        this.trackList = trackList;
    }

    public Count getCount() {
        return count;
    }

    public void setCount(Count count) {
        this.count = count;
    }
}
