package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * Created by ITMG on 2019-12-07.
 */
public class SafeguardIntroduceEntity implements Serializable
{

    private String name;
    private float feeRate;
    private float payRate;
    private String orderNum;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public float getFeeRate()
    {
        return feeRate;
    }

    public void setFeeRate(float feeRate)
    {
        this.feeRate = feeRate;
    }

    public float getPayRate()
    {
        return payRate;
    }

    public void setPayRate(float payRate)
    {
        this.payRate = payRate;
    }

    public String getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(String orderNum)
    {
        this.orderNum = orderNum;
    }
}
