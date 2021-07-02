package com.luhong.locwithlibrary.entity;

import java.io.File;
import java.io.Serializable;

/**
 * 图片上传
 * Created by ITMG on 2020-01-05.
 */
public class PicEntity implements Serializable
{
    public static final int TYPE_INIT = 2;//初始化图片
    public static final int TYPE_LOCAL = 0;//本地图片
    public static final int TYPE_NET = 1;//网络图片
    private int dataType;//数据类型
    private int type;//图片类型
    private String picUrl;
    private File picFile;

    public PicEntity()
    {
    }

    public PicEntity(File picFile)
    {
        this.picFile = picFile;
        this.type = TYPE_LOCAL;
    }

    public PicEntity(String picUrl, int type)
    {
        this.picUrl = picUrl;
        this.type = type;
    }

    public int getDataType()
    {
        return dataType;
    }

    public void setDataType(int dataType)
    {
        this.dataType = dataType;
    }

    public String getPicUrl()
    {
        return picUrl;
    }

    public void setPicUrl(String picUrl)
    {
        this.picUrl = picUrl;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public File getPicFile()
    {
        return picFile;
    }

    public void setPicFile(File picFile)
    {
        this.picFile = picFile;
    }
}
