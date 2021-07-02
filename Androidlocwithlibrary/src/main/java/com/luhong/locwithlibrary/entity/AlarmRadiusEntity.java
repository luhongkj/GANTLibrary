package com.luhong.locwithlibrary.entity;

import java.io.Serializable;

public class AlarmRadiusEntity implements Serializable {

    private String content;
    private boolean isCheck;

    public AlarmRadiusEntity() {
    }

    public AlarmRadiusEntity(String content) {
        this.content = content;
    }

    public AlarmRadiusEntity(String content, boolean isCheck) {
        this.content = content;
        this.isCheck = isCheck;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isCheck() {return isCheck;}

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
