package com.luhong.locwithlibrary.entity;

public class EmergencyInfo
{
    private int isOpenEmergency;

    public int getIsOpenEmergency()
    {
        return isOpenEmergency;
    }

    public void setIsOpenEmergency(int isOpenEmergency)
    {
        this.isOpenEmergency = isOpenEmergency;
    }
    
    public boolean isOpen()
    {
        return isOpenEmergency != 0;
    }
}


