package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 反馈类型
 * Created by ITMG on 2020-01-06.
 */
public class FeedbackEntity implements Serializable
{
    private String value;
    private String name;
    private int orderNum;
    private boolean isCheck;

    public FeedbackEntity(String name, boolean isCheck)
    {
        this.name = name;
        this.isCheck = isCheck;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public int getOrderNum()
    {
        return orderNum;
    }

    public void setOrderNum(int orderNum)
    {
        this.orderNum = orderNum;
    }

    public boolean isCheck()
    {
        return isCheck;
    }

    public void setCheck(boolean check)
    {
        isCheck = check;
    }
}
