package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class PhoneAlarmEntity implements Serializable {
    private int size;
    private String callid;//c02276c2-131c-11ea-864a-5254006d9082
    private String mobile;
    private String tempId;
    private int result;
    private String acceptTime;
    private String callFrom;
    private String startCalltime;
    private String endCalltime;
    private String fee;//"1",
    private String nationcode;
    private String createTime;
    private String ext;//":"cc4ddf2d92e646b28ead1e80b8cf3b58",
    private String sn;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getCallid() {
        return callid;
    }

    public void setCallid(String callid) {
        this.callid = callid;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTempId() {
        return tempId;
    }

    public void setTempId(String tempId) {
        this.tempId = tempId;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(String acceptTime) {
        this.acceptTime = acceptTime;
    }

    public String getCallFrom() {
        return callFrom;
    }

    public void setCallFrom(String callFrom) {
        this.callFrom = callFrom;
    }

    public String getStartCalltime() {
        return startCalltime;
    }

    public void setStartCalltime(String startCalltime) {
        this.startCalltime = startCalltime;
    }

    public String getEndCalltime() {
        return endCalltime;
    }

    public void setEndCalltime(String endCalltime) {
        this.endCalltime = endCalltime;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getNationcode() {
        return nationcode;
    }

    public void setNationcode(String nationcode) {
        this.nationcode = nationcode;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }
}
