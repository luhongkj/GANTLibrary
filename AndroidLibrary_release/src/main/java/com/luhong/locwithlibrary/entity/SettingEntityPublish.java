package com.luhong.locwithlibrary.entity;


import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {
 * "value": 1, //0关闭，1开启
 * "type": 1 //告警通知
 * },
 * {
 * "value": 1, //0关闭，1开启
 * "type": 2 //告警声音
 * },
 * {
 * "value": 0, //0关闭，1开启
 * "type": 3 //自动设防
 * },
 * {
 * "value": 1, //0关闭，1开启
 * "type": 4 //状态灯
 * },
 * {
 * "value": 1, //0:低,1:中,2:高
 * "type": 5 //告警灵敏度
 * },
 * {
 * "value": 1, //0:低,1:中,2:高
 * "type": 6 //刹车灵敏度
 * },
 * {
 * "value": 0, //0：自动；1：手动
 * "type": 7 //警示灯工作模式
 * },
 * {
 * "value": 50, //0~100整数
 * "type": 8 //亮度调节
 * },
 * {
 * "value": 5, //1:SOS;2:闪烁模式1；3：闪烁模式2；4:闪灯；5呼吸灯
 * "type": 9 //闪烁模式
 * },
 * {
 * "value": 2, //0：灯常亮,1灯常灭,2闪烁
 * "type": 10 //闪灯模式
 * },
 * {
 * "value": 1, //0:慢速；1:正常；2:快速
 * "type": 11 //闪烁频率
 * }
 * {
 * "value": 1,//0关闭，1开启
 * "type": 12//紧急救援开关
 * }
 */
public class SettingEntityPublish {
    private List<SettingEntity> entities;
    private Map<Integer, OnValueListener> listeners = new HashMap<>();

    public SettingEntityPublish(@NonNull List<SettingEntity> entities) {
        this.entities = entities;
    }

    // 告警通知
    public void setOnNotify(OnValueListener listener) {
        listeners.put(1, listener);
    }

    // 告警声音
    public void setOnNotifyVoice(OnValueListener listener) {
        listeners.put(2, listener);
    }

    //自动设防
    public void setOnAutoFortify(OnValueListener listener) {
        listeners.put(3, listener);
    }

    //状态灯
    public void setOnStateLight(OnValueListener listener) {
        listeners.put(4, listener);
    }

    //告警灵敏度
    public void setOnAlarmSensitivity(OnValueListener listener) {
        listeners.put(5, listener);
    }

    //刹车灵敏度
    public void setOnBrakeSensitivity(OnValueListener listener) {
        listeners.put(6, listener);
    }

    //警示灯工作模式
    public void setOnWarningLightWorkModel(OnValueListener listener) {
        listeners.put(7, listener);
    }

    //亮度调节
    public void setOnLightRevise(OnValueListener listener) {
        listeners.put(8, listener);
    }

    //闪灯模式
    public void setOnLightModel(OnValueListener listener) {
        listeners.put(10, listener);
    }

    //闪烁模式
    public void setOnShakeModel(OnValueListener listener) {
        listeners.put(9, listener);
    }

    //闪烁频率
    public void setOnShakeRate(OnValueListener listener) {
        listeners.put(11, listener);
    }

    /**
     * "value": 1,//0关闭，1开启
     * "type": 12//紧急救援开关
     */
    public void setOnRescue(OnValueListener listener) {
        listeners.put(12, listener);
    }

    // 开始
    public void start() {
        for (SettingEntity entity : entities) {
            int type = entity.getType();
            int value = entity.getValue();
            OnValueListener listener = listeners.get(type);
            if (listener != null) {
                listener.onValue(value);
            }
        }
    }

    /**
     * 当事件的值获取到了之后
     */
    public static interface OnValueListener {
        public void onValue(int value);
    }
}
