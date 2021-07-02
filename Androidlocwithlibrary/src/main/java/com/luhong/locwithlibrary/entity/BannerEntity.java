package com.luhong.locwithlibrary.entity;

import java.io.Serializable;
import java.util.List;

public class BannerEntity implements Serializable
{
    private String name;
    private String remark;
    private List<String> fileUrls;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public List<String> getFileUrls()
    {
        return fileUrls;
    }

    public void setFileUrls(List<String> fileUrls)
    {
        this.fileUrls = fileUrls;
    }
}
