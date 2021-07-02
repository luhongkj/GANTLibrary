package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class FlowAccountEntity implements Serializable {
    private int size;
    private String id;
    private String userId;
    private float money;
    private int status;
    private int isDel;
    private String createTime;
    private String createId;
    private double flowFee;

    //流量月租费：flowFee
    public double getFlowFee() {
        return flowFee;
    }

    public void setFlowFee(double flowFee) {
        this.flowFee = flowFee;
    }

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

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateId() {
        return createId;
    }

    public void setCreateId(String createId) {
        this.createId = createId;
    }
}
