package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class RechargeRecordEntity implements Serializable
{
    private int size;
    private String flowId;
    private int type;
    private String orderNo;
    private String status;
    private String rechargeTime;
    private String typeShow;//"微信支付",
    private float account;
    private float realPrice;

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getFlowId()
    {
        return flowId;
    }

    public void setFlowId(String flowId)
    {
        this.flowId = flowId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getRechargeTime()
    {
        return rechargeTime;
    }

    public void setRechargeTime(String rechargeTime)
    {
        this.rechargeTime = rechargeTime;
    }

    public String getTypeShow()
    {
        return typeShow;
    }

    public void setTypeShow(String typeShow)
    {
        this.typeShow = typeShow;
    }

    public float getAccount()
    {
        return account;
    }

    public void setAccount(float account)
    {
        this.account = account;
    }

    public float getRealPrice()
    {
        return realPrice;
    }

    public void setRealPrice(float realPrice)
    {
        this.realPrice = realPrice;
    }
}
