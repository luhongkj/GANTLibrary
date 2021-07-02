package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class HistoryInfoEntity implements Serializable {


    /**
     * code : 1
     * msg : 请求成功
     * data : {"imei":"964110603222","onLine":"2","gateway":"47.115.9.17:8500","gnss":"6","course":"323","speed":"5.0","gpsTime":"2021-04-02 19:06:36","vehicleId":"109","activeTime":"2021-04-02 19:06:39","charge":"0","battery":"88","states":"33,248,72,70,22,21,76,503,67","sport":"0","lat":"22.767629","network":"14","locState":"1","lon":"113.816215","sleep":"0","safy":"22","gpsmile":"4.1","trackTime":5}
     */

    /**
     * imei : 964110603222
     * onLine : 2
     * gateway : 47.115.9.17:8500
     * gnss : 6
     * course : 323
     * speed : 5.0
     * gpsTime : 2021-04-02 19:06:36
     * vehicleId : 109
     * activeTime : 2021-04-02 19:06:39
     * charge : 0
     * battery : 88
     * states : 33,248,72,70,22,21,76,503,67
     * sport : 0
     * lat : 22.767629
     * network : 14
     * locState : 1
     * lon : 113.816215
     * sleep : 0
     * safy : 22
     * gpsmile : 4.1
     * trackTime : 5
     */

    private String imei;
    private String onLine;
    private String gateway;
    private String gnss;
    private String course;
    private String speed;
    private String gpsTime;
    private String vehicleId;
    private String activeTime;
    private String charge;
    private String battery;
    private String states="";
    private String sport;
    private String lat;
    private String network;
    private String locState;
    private String lon;
    private String sleep;
    private String safy;
    private String gpsmile;
    private int trackTime;

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getOnLine() {
        return onLine;
    }

    public void setOnLine(String onLine) {
        this.onLine = onLine;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getGnss() {
        return gnss;
    }

    public void setGnss(String gnss) {
        this.gnss = gnss;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getGpsTime() {
        return gpsTime;
    }

    public void setGpsTime(String gpsTime) {
        this.gpsTime = gpsTime;
    }

    public String getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(String vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(String activeTime) {
        this.activeTime = activeTime;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getLocState() {
        return locState;
    }

    public void setLocState(String locState) {
        this.locState = locState;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getSleep() {
        return sleep;
    }

    public void setSleep(String sleep) {
        this.sleep = sleep;
    }

    public String getSafy() {
        return safy;
    }

    public void setSafy(String safy) {
        this.safy = safy;
    }

    public String getGpsmile() {
        return gpsmile;
    }

    public void setGpsmile(String gpsmile) {
        this.gpsmile = gpsmile;
    }

    public int getTrackTime() {
        return trackTime;
    }

    public void setTrackTime(int trackTime) {
        this.trackTime = trackTime;
    }
}
