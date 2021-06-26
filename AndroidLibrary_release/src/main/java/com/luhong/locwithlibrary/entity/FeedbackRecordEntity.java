package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class FeedbackRecordEntity implements Serializable
{
    private int size;//":10,
    private String id;//":"13",
    private String userId;//":"56034331239",
    private int type;//":"3",
    private String createTime;//":"2020-01-06 19:36:29",
    private String createId;//":"56034331239",
    private int isReply;//":0,
    private String reply;
    private String replyTime;
    private String phone;//":"18320888505",
    private String typeShow;//":"定位相关",
    private String isReplyShow;//":"未回复"
    private String content;
    private boolean isOpen;

    public boolean isOpen()
    {
        return isOpen;
    }

    public void setOpen(boolean isOpen)
    {
        this.isOpen = isOpen;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getUserId()
    {
        return userId;
    }

    public void setUserId(String userId)
    {
        this.userId = userId;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public String getCreateTime()
    {
        return createTime;
    }

    public void setCreateTime(String createTime)
    {
        this.createTime = createTime;
    }

    public String getReplyTime()
    {
        return replyTime;
    }

    public void setReplyTime(String replyTime)
    {
        this.replyTime = replyTime;
    }

    public String getCreateId()
    {
        return createId;
    }

    public void setCreateId(String createId)
    {
        this.createId = createId;
    }

    public int getIsReply()
    {
        return isReply;
    }

    public void setIsReply(int isReply)
    {
        isOpen = isReply == 1;
        this.isReply = isReply;
    }

    public String getPhone()
    {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getReply()
    {
        return reply;
    }

    public void setReply(String reply)
    {
        this.reply = reply;
    }

    public String getTypeShow()
    {
        return typeShow;
    }

    public void setTypeShow(String typeShow)
    {
        this.typeShow = typeShow;
    }

    public String getIsReplyShow()
    {
        return isReplyShow;
    }

    public void setIsReplyShow(String isReplyShow)
    {
        this.isReplyShow = isReplyShow;
    }
}
