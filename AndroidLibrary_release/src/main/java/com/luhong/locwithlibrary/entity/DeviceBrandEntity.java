package com.luhong.locwithlibrary.entity;

import com.luhong.locwithlibrary.utils.Pinyin4jUtils;

import java.io.Serializable;
import java.text.Collator;
import java.util.Locale;

public class DeviceBrandEntity implements Comparable<DeviceBrandEntity>, Serializable {
    private String id;
    private String name;
    private int type;
    private String channelId;
    private String pinyin;//全拼
    private String firstLatter;//首字母
    private boolean isCheck;

    public DeviceBrandEntity() {
    }

    public DeviceBrandEntity(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public String getFirstLatter() {
        return firstLatter;
    }

    public void setFirstLatter(String firstLatter) {
        this.firstLatter = firstLatter;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    @Override
    public int compareTo(DeviceBrandEntity entity) {
        String thisPinyin = this.pinyin = Pinyin4jUtils.converterToSpell(this.name);//将汉字转拼音再排序
        String entityPinyin = entity.pinyin = Pinyin4jUtils.converterToSpell(entity.name);
        this.firstLatter = thisPinyin.substring(0, 1).toUpperCase();
        entity.firstLatter = entityPinyin.substring(0, 1).toUpperCase();
        int flags;
        Collator collator = Collator.getInstance(Locale.CHINA);
        if (collator.compare(thisPinyin, entityPinyin) < 0) {
            flags = -1;
        } else if (collator.compare(thisPinyin, entityPinyin) > 0) {
            flags = 1;
        } else {
            flags = 0;
        }
        //        Logger.error(flags + "=flags|| " + this.name + " 的首字母= " + firstLatter + "，&拼音= " + thisPinyin + "，|| " + entity.name + "的拼音= " + entityPinyin);
        return flags;

        //        float firstRate = 0;
        //        float lastRate = 0;
        //        if ((lastRate - firstRate) > 0) {// 根据通过率降序排列，降序修改相减顺序即可
        //            return 1;
        //        } else {
        //            return -1;
        //        }
    }
}
