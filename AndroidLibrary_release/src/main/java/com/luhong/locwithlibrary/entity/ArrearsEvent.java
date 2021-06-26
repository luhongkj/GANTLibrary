package com.luhong.locwithlibrary.entity;

/**
 * 欠费
 */
public class ArrearsEvent {
    public static final int TYPE_ARREARS = 0;//0欠费
    public static final int TYPE_REPAY = 1;//1补缴成功
    private int type;//0欠费，1补缴成功

    public ArrearsEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
