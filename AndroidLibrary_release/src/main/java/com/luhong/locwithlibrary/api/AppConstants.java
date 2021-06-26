package com.luhong.locwithlibrary.api;

/**
 * app常量
 */
public interface AppConstants {
    //    result_code
    int RESULT_CODE = 10000;//通用
    int RESULT_CODE_ADD = 10001;//增
    int RESULT_CODE_UPDATE = 10002;//改
    int RESULT_CODE_QUERY = 10003;//查
    int RESULT_CODE_DELETE = 10004;//删
    //request_code
    int REQUEST_CODE = 10010;//通用
    int REQUEST_CODE_ADD = 10011;//增
    int REQUEST_CODE_UPDATE = 10012;//改
    int REQUEST_CODE_QUERY = 10013;//查
    int REQUEST_CODE_DELETE = 10014;//删
    int REQUEST_CODE_OPEN_GPS = 10015;//打开gps
    int REQUEST_CODE_NAVIGATE = 10016;//导航

    String dataTypeKey = "dataTypeKey";//数据类型
    String dataKey = "dataKey";//数据
    String vehicleIdKey = "vehicleIdKey";//车辆id
    String userUrlKey = "userUrlKey";//用户头像
    String nickNameKey = "nickNameKey";//用户昵称
    String positionKey = "positionKey";//位置
    String safeguardVehicleKey = "safeguardVehicleKey";//投保选择车辆
    String deviceLatKey = "deviceLatKey";
    String deviceLngKey = "deviceLngKey";
    String phoneLatKey = "phoneLatKey";
    String phoneLngKey = "phoneLngKey";
    String MAIN_SERVER_KEY = "MAIN_SERVER_KEY";//主界面-服务
    String MAIN_SPORT_KEY = "MAIN_SPORT_KEY";//主界面-运动
    String MAIN_FRIEND_KEY = "MAIN_FRIEND_KEY";//主界面-骑友圈
    String FRIEND_PROMPT_KEY = "friendPromptKey";//骑友圈 弹窗提示
    String PUSH_MSG_KEY = "msgData";

    //    推送消息类型msgId：4设防，5解防,1001终端主动告警上报,告警类型:65 震动报警,245 设备低电报警,129 GPS通讯故障
    //   支付成功
    int MSG_ARREARS = 110001;//流量欠费补缴支付成功
    int MSG_FLOW = 110002; //流量套餐充值
    int MSG_PHONE = 110003; //电话告警服务充值
    int MSG_FENCE = 110004; //电子围栏告警服务充值
    int MSG_SAFEGUARD = 110005; //保险购买
    int MSG_ACTIVE = 110006;//激活预充值
    int MSG_CODE_MSGID = 1;//激活预充值
    //0;指令发送成功 1;指令发送失败 2;参数错误 3;非法指令 4;终端离线 5;网关异常 6;指令超时
    String MSG_CODE_0 = "0";//指令发送成功
    String MSG_CODE_1 = "1";//指令发送失败
    String MSG_CODE_2 = "2";//参数错误
    String MSG_CODE_3 = "3";//非法指令
    String MSG_CODE_4 = "4";//终端离线
    String MSG_CODE_5 = "5";//网关异常
    String MSG_CODE_6 = "6";//指令超时
    /**
     * 4     车载定位器
     * 5     前灯
     * 6     尾灯
     * 7     头盔
     * 4?      D2
     */
    String UNITTYPE_4 = "4";//车载定位器
    String UNITTYPE_5 = "5";
    String UNITTYPE_6 = "6";
    String UNITTYPE_7 = "7";

    String UNITMODELCN_S1 = "S1";//车载定位器

    // 自行车 "1"
    // 电动车 "2"
    // 锂电助力车"3"
    String DEVICETYPE_1 = "1";
    String DEVICETYPE_2 = "2";
    String DEVICETYPE_3 = "3";

    //    终端
    int MSG_FORTIFICATION = 4;//设防
    int MSG_UNFORTIFICATION = 5;//解防
    int MSG_AUTO_FORTIFICATION = 6;//自动设防
    int MSG_EFENCE_FORTIFICATION = 7;//电子围栏，半径（米）
    int MSG_LIGHT = 8;//状态灯
    int DEVICE_SN_LENGTH = 32;//设备sn长度

    int MSG_ELECTROCAR_OFF = 71;//熄火
    int MSG_ELECTROCAR_OPEN = 70;//启动
    int MSG_BLLE = 73;//响铃
    int MSG_PAIRIING = 72;//配对

    //常量key
    String SERVER_ONLINE_NO = "4008877061";//客服电话 4009994902/4008877801
    String DEVICE_MANAGE_KEY = "DEVICE_MANAGE_KEY";//车辆管理
    String LAST_DEVICE_ID_KEY = "LAST_DEVICE_ID_KEY";//当前使用的车辆
    String LAST_DEVICE_POSITION_KEY = "LAST_DEVICE_POSITION_KEY";//车辆最后位置信息
    String SETTING_KEY = "SETTING_KEY"; //开关设置
    String CURRENT_CITY = "currentCity";//城市
}
