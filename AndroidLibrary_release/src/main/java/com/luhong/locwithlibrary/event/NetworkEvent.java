package com.luhong.locwithlibrary.event;

/**
 * 网络
 * Created by ITMG on 2019-12-24.
 */
public class NetworkEvent {
    public static final int NETWORK_NONE = -1;//没有网络
    public static final int NETWORK_MOBILE = 0;//移动网络
    public static final int NETWORK_WIFI = 1;//无线网络
    private int netType;

    public NetworkEvent(int netType) {
        this.netType = netType;
    }

    public int getNetType() {
        return netType;
    }

    public void setNetType(int netType) {
        this.netType = netType;
    }
}
