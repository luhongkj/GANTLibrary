package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

/**
 *
 */
public class CarEntity implements Serializable {
    private String nickName;//车架号
    private String sn;//设备sn
    private boolean flag;

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }


}
