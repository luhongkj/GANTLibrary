package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * Created by ITMG on 2018/6/12 0012.
 */

public class UrlEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    private int picType;//图片类型
    private String id;
    private String name;
    private String url;
    private String createTime;
    private int isDel;

    public int getPicType() {
        return picType;
    }

    public void setPicType(int picType) {
        this.picType = picType;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }
}
