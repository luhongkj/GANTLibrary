package com.luhong.locwithlibrary.net.response;

import java.io.Serializable;

/**
 * Created by ITMG on 2018-12-15.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private int code;
    private String msg;

    public BaseEntity() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
