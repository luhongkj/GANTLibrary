package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class HomeEntity implements Serializable
{
    private int error;
    private int sum;
    private int overtime;

    public HomeEntity()
    {
    }

    public int getError()
    {
        return error;
    }

    public void setError(int error)
    {
        this.error = error;
    }

    public int getSum()
    {
        return sum;
    }

    public void setSum(int sum)
    {
        this.sum = sum;
    }

    public int getOvertime()
    {
        return overtime;
    }

    public void setOvertime(int overtime)
    {
        this.overtime = overtime;
    }
}
