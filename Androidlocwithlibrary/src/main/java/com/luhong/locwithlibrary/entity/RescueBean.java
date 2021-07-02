package com.luhong.locwithlibrary.entity;

public class RescueBean
{
    private String contactName;
    private String phone;
    private String userId;
    private Type type;

    public String getContactName()
    {
        return contactName;
    }

    public void setContactName(String contactName)
    {
        this.contactName = contactName;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public void setType(Type type)
    {
        this.type = type;
    }

    public Type getType()
    {
        if (contactName != null && phone != null)
        {
            return Type.DATA;
        }
        return type;
    }

    public static enum Type
    {
        TITLE,
        DATA,
        ADD
    }
}
