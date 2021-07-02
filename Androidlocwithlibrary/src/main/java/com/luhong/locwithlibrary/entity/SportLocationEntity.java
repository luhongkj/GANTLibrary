package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 运动轨迹经纬度
 * Created by ITMG on 2019-12-14.
 */
public class SportLocationEntity implements Serializable {
    private String elevation;//海拔高度
    private double lat;//纬度
    private double lon;// 经度
    private float speed;//速度
    private String time;//时间

    public SportLocationEntity(double lat, double lon, float speed, String time) {
        this.lat = lat;
        this.lon = lon;
        this.speed = speed;
        this.time = time;
    }

    public String getElevation() {
        return elevation;
    }

    public void setElevation(String elevation) {
        this.elevation = elevation;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "SportLocationEntity{" +
                "elevation='" + elevation + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", speed=" + speed +
                ", time='" + time + '\'' +
                '}';
    }
}
