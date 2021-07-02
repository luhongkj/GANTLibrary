package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class VehicleModelEntity implements Serializable
{
    private int dataKey;
    private int type;
    private String dataValue;

    public int getDataKey()
    {
        return dataKey;
    }

    public void setDataKey(int dataKey)
    {
        this.dataKey = dataKey;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getDataValue()
    {
        return dataValue;
    }

    public void setDataValue(String dataValue)
    {
        this.dataValue = dataValue;
    }
}
