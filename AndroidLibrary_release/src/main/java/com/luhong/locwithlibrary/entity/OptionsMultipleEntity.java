package com.luhong.locwithlibrary.entity;

import com.zyyoona7.wheel.IWheelEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OptionsMultipleEntity implements IWheelEntity, Serializable {

    //国家
    public static final String LEVEL_COUNTRY = "country";
    //省
    public static final String LEVEL_PROVINCE = "province";
    //市
    public static final String LEVEL_CITY = "city";
    //区
    public static final String LEVEL_DISTRICT = "district";

    private String citycode;
    private String adcode;
    private String name;
    private String center;
    private String level;
    private List<OptionsMultipleEntity> districts;

    public String getCitycode() {
        return citycode;
    }

    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    public String getAdcode() {
        return adcode;
    }

    public void setAdcode(String adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCenter() {
        return center;
    }

    public void setCenter(String center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<OptionsMultipleEntity> getDistricts() {
        return districts == null ? new ArrayList<OptionsMultipleEntity>(1) : districts;
    }

    public void setDistricts(List<OptionsMultipleEntity> districts) {
        this.districts = districts;
    }

    @Override
    public String toString() {
        return "OptionsMultipleEntity{" +
                "citycode='" + citycode + '\'' +
                ", adcode='" + adcode + '\'' +
                ", name='" + name + '\'' +
                ", center='" + center + '\'' +
                ", level='" + level + '\'' +
                ", districts=" + districts +
                '}';
    }

    @Override
    public String getWheelText() {
        return name == null ? "" : name;
    }
}
