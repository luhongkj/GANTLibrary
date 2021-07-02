package com.luhong.locwithlibrary.entity;

import android.text.TextUtils;

import java.io.Serializable;

public class FlowBillEntity implements Serializable {
    private int size;
    private String accountId;
    private String orderNo;
    private int sourceType;
    private String rechargeAccount = "0";
    private String rechargeType;
    private String content;
    private String optime;

    private String sn;
    private String month;
    private String costMoney;
    private String feeType;
    private double flowFee;

    //流量月租费：flowFee
    public double getFlowFee() {
        return flowFee;
    }

    public void setFlowFee(double flowFee) {
        this.flowFee = flowFee;
    }


    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public int getSourceType() {
        return sourceType;
    }

    public void setSourceType(int sourceType) {
        this.sourceType = sourceType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRechargeAccount() {
        return rechargeAccount;
    }

    public void setRechargeAccount(String rechargeAccount) {
        if (TextUtils.isEmpty(rechargeAccount)) {
            this.rechargeAccount = "0";
            return;
        }
        this.rechargeAccount = rechargeAccount;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getOptime() {
        return optime;
    }

    public void setOptime(String optime) {
        this.optime = optime;
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getCostMoney() {
        return costMoney;
    }

    public void setCostMoney(String costMoney) {
        this.costMoney = costMoney;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }
}
