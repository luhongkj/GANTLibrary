package com.luhong.locwithlibrary.api;

/**
 * APP全局变量
 */
public class AppVariable {
    public static String currentDeviceId = "";//设备sn:8800000000011-8800000000030
    public static String currentDevicePdfUrl = "";//设备sn:8800000000011-8800000000030
    public static String currentVehicleId = "";//车辆id
    public static double deviceLat = -1;
    public static double deviceLng = -1;
    public static double phoneLat = 0.0;
    public static double phoneLng = 0.0;
    public static int TYPE_PAY_MSG = -1;//支付类型
    public static String GIANT_TOKEN ;//捷安特token
    public static String GIANT_PHONE ;//捷安特登录手机号

    public static boolean GIANT_ISBIN ;//是否注册过鹿卫士设备
}
