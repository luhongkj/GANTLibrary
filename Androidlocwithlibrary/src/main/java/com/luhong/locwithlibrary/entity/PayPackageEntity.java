package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 * 套餐
 * Created by ITMG on 2020-01-20.
 */
public class PayPackageEntity implements Serializable {
    //    通用
    private boolean isCheck;//是否选中
    private String id;//套餐id
    private float realPrice;//实际支付价格
    //    流量
    private float account;//显示支付金额
    private String remark;//备注说明
    //电子围栏/电话告警
    private int type;
    private String serverLength;
    private float originalPrice;//显示支付金额
    private String quaUnit;//单位

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getAccount() {
        return account;
    }

    public void setAccount(float account) {
        this.account = account;
    }

    public float getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(float realPrice) {
        this.realPrice = realPrice;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getServerLength() {
        return serverLength;
    }

    public void setServerLength(String serverLength) {
        this.serverLength = serverLength;
    }

    public float getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(float originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getQuaUnit() {
        return quaUnit;
    }

    public void setQuaUnit(String quaUnit) {
        this.quaUnit = quaUnit;
    }
}
