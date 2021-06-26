package com.luhong.locwithlibrary.entity;

import com.zyyoona7.wheel.IWheelEntity;

import java.io.Serializable;

/**
 * 单选
 */
public class OptionsSingleEntity implements IWheelEntity, Serializable {
    private String id;
    private String name;

    public OptionsSingleEntity(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getWheelText() {
        return name == null ? "" : name;
    }
}
