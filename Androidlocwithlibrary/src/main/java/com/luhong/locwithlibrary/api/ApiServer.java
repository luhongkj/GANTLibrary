package com.luhong.locwithlibrary.api;

import com.luhong.locwithlibrary.BuildConfig;
import com.luhong.locwithlibrary.entity.AlarmSensitivityParams;
import com.luhong.locwithlibrary.entity.BannerEntity;
import com.luhong.locwithlibrary.entity.BrakeSensitivityParams;
import com.luhong.locwithlibrary.entity.ClaimEntity;
import com.luhong.locwithlibrary.entity.CodeTableParams;
import com.luhong.locwithlibrary.entity.DeviceBrandEntity;
import com.luhong.locwithlibrary.entity.DeviceEntity;
import com.luhong.locwithlibrary.entity.DevicePositionEntity;
import com.luhong.locwithlibrary.entity.DeviceServerEntity;
import com.luhong.locwithlibrary.entity.EmergencyInfo;
import com.luhong.locwithlibrary.entity.FeedbackEntity;
import com.luhong.locwithlibrary.entity.FeedbackRecordEntity;
import com.luhong.locwithlibrary.entity.FenceRadiusEntity;
import com.luhong.locwithlibrary.entity.FlowAccountEntity;
import com.luhong.locwithlibrary.entity.FlowBillEntity;
import com.luhong.locwithlibrary.entity.HistoryInfoEntity;
import com.luhong.locwithlibrary.entity.HomeDataEntity;
import com.luhong.locwithlibrary.entity.InstallModeEntity;
import com.luhong.locwithlibrary.entity.LightBrightnesParams;
import com.luhong.locwithlibrary.entity.LightModelParams;
import com.luhong.locwithlibrary.entity.LightShakeModelParams;
import com.luhong.locwithlibrary.entity.LightWordModelParams;
import com.luhong.locwithlibrary.entity.PayPackageEntity;
import com.luhong.locwithlibrary.entity.PdfEntity;
import com.luhong.locwithlibrary.entity.PhoneAlarmEntity;
import com.luhong.locwithlibrary.entity.RechargeRecordEntity;
import com.luhong.locwithlibrary.entity.RescueBean;
import com.luhong.locwithlibrary.entity.SafeguardEntity;
import com.luhong.locwithlibrary.entity.SafeguardIntroduceEntity;
import com.luhong.locwithlibrary.entity.SettingEntity;
import com.luhong.locwithlibrary.entity.SportEntity;
import com.luhong.locwithlibrary.entity.SportLocationEntity;
import com.luhong.locwithlibrary.entity.SportLocationPageEntity;
import com.luhong.locwithlibrary.entity.TrackInfoRefreshEntity;
import com.luhong.locwithlibrary.entity.UrlEntity;
import com.luhong.locwithlibrary.entity.UserEntity;
import com.luhong.locwithlibrary.entity.VehicleDtoEntity;
import com.luhong.locwithlibrary.entity.VehicleListEntity;
import com.luhong.locwithlibrary.entity.VehicleModelEntity;
import com.luhong.locwithlibrary.net.response.BasePageEntity;
import com.luhong.locwithlibrary.net.response.BaseResponse;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * api接口服务
 * Created by ITMG on 2020-03-05.
 */
public interface ApiServer {
    /**
     * 测试环境
     * buildConfigField "String", "BASE_API_URL", "\"https://apptest.luhongkj.com:8443/\""
     * buildConfigField "String", "BASE_FILE_URL", "\"http://gis.luhongkj.com:8808/files/\""
     * buildConfigField "String", "BASE_UPLOAD_URL", "\"http://gis.luhongkj.com:9900/files/image/upload\""
     * buildConfigField "String", "H5_PAY", "\"http://gis.luhongkj.com:8802""
     * <p>
     * 正式环境
     * /*  buildConfigField "String", "BASE_API_URL", "\"https://app.luhongkj.com:8443/\""
     * buildConfigField "String", "BASE_FILE_URL", "\"https://zxc.luhongkj.com:8808/files/\""
     * buildConfigField "String", "BASE_UPLOAD_URL", "\"http://zxc.luhongkj.com:9900/files/image/upload\""
     * buildConfigField "String", "BASE_WEB_URL", "http://app.luhongkj.com:8802/"
     *
     * @param requestBody
     * @return
     */


    //图片上传
    @POST(BuildConfig.BASE_UPLOAD_URL)
    Observable<BaseResponse<UrlEntity>> uploadFile(@Body RequestBody requestBody);

    // 登录 password, phone
    @POST("login/checkIn")
    Observable<BaseResponse<UserEntity>> login(@Body Map<String, Object> bodyParams);

    //设置新密码   code, password, phone
    @POST("login/reSetPwd")
    Observable<BaseResponse<Object>> updatePassword(@Body Map<String, Object> bodyParams);

    //注册(保存用户信息)
    @POST("appUser/login/save")
    Observable<BaseResponse<UserEntity>> saveUserInfo(@Body UserEntity userEntity);

    //获取用户信息
    @POST("appUser/info")
    Observable<BaseResponse<UserEntity>> getUserInfo(@Body UserEntity userEntity);

    //消息推送ack确认。必填项：flowId流水号
    @POST("home/notifyAck")
    Observable<BaseResponse<Object>> notifyAck(@Body Map<String, Object> bodyParams);

    // 遥控器配对
    @POST("home/notifyAck")
    Call<BaseResponse<String>> notifyAckPairingWorkModle(@Body Map<String, Object> bodyParams);

    //修改用户信息
    @POST("appUser/update")
    Observable<BaseResponse<UserEntity>> updateUserInfo(@Body UserEntity userEntity);

    //获取登录验证码
    @POST("login/loginCode")
    Observable<BaseResponse<Object>> getLoginCode(@Body Map<String, Object> bodyParams);

    //检验验证码
    @POST("login/checkCode")
    Observable<BaseResponse<Object>> checkCode(@Body Map<String, Object> bodyParams);

    //车辆管理不分页
    @POST("myVehicle/queryAll")
    Observable<BaseResponse<List<DeviceEntity>>> getDeviceList(@Body Map<String, Object> bodyParams);

    //车辆管理不分页
    @POST("myVehicle/queryAll")
    Call<BaseResponse<List<DeviceEntity>>> getDeviceListCall(@Body Map<String, Object> bodyParams);

    //添加设备
    @POST("myVehicle/save")
    Observable<BaseResponse<Object>> saveDevice(@Body DeviceEntity deviceEntity);

    //根据设备号激活设备
    @POST("myVehicle/activeBySn")
    Observable<BaseResponse<Object>> activeVehicleBySn(@Body Map<String, Object> bodyParams);

    //设备欠费补缴
    @POST("myVehicle/confirmPay")
    Observable<BaseResponse<Object>> getVehicleConfirmPay(@Body Map<String, Object> bodyParams);

    //解绑用户爱车
    @POST("myVehicle/unbind")
    Observable<BaseResponse<DeviceEntity>> deleteVehicle(@Body DeviceEntity deviceEntity);

    //修改车辆设备
    @POST("myVehicle/update")
    Observable<BaseResponse<DeviceEntity>> updateVehicle(@Body DeviceEntity deviceEntity);

    //根据设备ID获取设备信息，用于判断设备的加装模式,绑定的设备是否欠费
    @POST("myVehicle/addBySn")
    Observable<BaseResponse<InstallModeEntity>> getInstallMode(@Body Map<String, Object> bodyParams);// getVehicleBySns,getInstallModes

    //获取车辆是否购买告警服务，有效期
    @POST("myVehicle/findBySn")
    Observable<BaseResponse<DeviceServerEntity>> getVehicleBySn(@Body Map<String, Object> bodyParams);//myVehicle/addBySn

    //根据车架号获取车辆信息，用于判断用户是否需要手动添加信息
    @POST("myVehicle/findByVin")
    Observable<BaseResponse<VehicleDtoEntity>> getVehicleByVin(@Body Map<String, Object> params);

    // 发布帖子
    @POST("note/save")
    Observable<BaseResponse<Object>> publish(@Body Map<String, Object> bodyParams);

    //添加保单
    @POST("insurance/save")
    Observable<BaseResponse<Object>> saveSafeguard(@Body Map<String, Object> bodyParams);

    //修改保单
    @POST("insurance/update")
    Observable<BaseResponse<Object>> updateSafeguard(@Body Map<String, Object> bodyParams);

    //单个查询保单
    @POST("insurance/findById")
    Observable<BaseResponse<SafeguardEntity>> getSafeguardInfo(@Body Map<String, Object> bodyParams);

    //安全保障我的列表
    @POST("insurance/myList")
    Observable<BaseResponse<BasePageEntity<SafeguardEntity>>> getSafeguardList(@Body Map<String, Object> bodyParams);

    //获取电动自行车保费标准数据介绍
    @POST("common/getElectricInsureRates")
    Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getElectricIntroduce(@Body Map<String, Object> bodyParams);

    //获取自行车保费标准数据介绍
    @POST("common/getBicyleInsureRates")
    Observable<BaseResponse<List<SafeguardIntroduceEntity>>> getBicycleIntroduce(@Body Map<String, Object> bodyParams);

    //安全保障首页列表和保险支付套餐(取第一条)
    @POST("common/getInsureSets")
    Observable<BaseResponse<List<SafeguardEntity>>> getInsureSets(@Body Map<String, Object> bodyParams);


    //单个查询保险理赔
    @POST("claim/findById")
    Observable<BaseResponse<ClaimEntity>> getClaim(@Body Map<String, Object> bodyParams);

    //理赔挂失
    @POST("claim/save")
    Observable<BaseResponse<ClaimEntity>> saveClaim(@Body Map<String, Object> bodyParams);

    //提交理赔信息
    @POST("claim/update")
    Observable<BaseResponse<Object>> updateClaim(@Body Map<String, Object> bodyParams);

    //取消理赔
    @POST("claim/cancel")
    Observable<BaseResponse<Object>> cancelClaim(@Body Map<String, Object> bodyParams);

    //电话告警
    @POST("msg/telList")
    Observable<BaseResponse<BasePageEntity<PhoneAlarmEntity>>> getPhoneAlarm(@Body Map<String, Object> bodyParams);

    //电子围栏短信告警
    @POST("msg/fenceList")
    Observable<BaseResponse<BasePageEntity<PhoneAlarmEntity>>> getSMSAlarm(@Body Map<String, Object> bodyParams);

    //充值记录 rechargeRecord/list
    @POST("flowAccount/queryRecharges")
    Observable<BaseResponse<BasePageEntity<RechargeRecordEntity>>> getRechargeRecord(@Body Map<String, Object> bodyParams);

    //车辆所有告警开关状态列表。必填项:vehicleId
    @POST("set/all")
    Observable<BaseResponse<List<SettingEntity>>> getSetting(@Body Map<String, Object> bodyParams);

    @POST("set/all")
    Call<BaseResponse<List<SettingEntity>>> getSettingCall(@Body Map<String, Object> bodyParams);

    //设置告警开关。必填项:currentVehicleId,value,type
    @POST("set/alarm")
    Observable<BaseResponse<Object>> setSetting(@Body Map<String, Object> bodyParams);

    @POST("set/alarm")
    Call<BaseResponse<Object>> setSettingCall(@Body Map<String, Object> bodyParams);

    //获取电子围栏半径
    @POST("set/fenceValue")
    Observable<BaseResponse<FenceRadiusEntity>> getFenceRadius(@Body Map<String, Object> bodyParams);

    //获取产品说明书PDF链接
    @POST("jieante/pdf/findBySn")
    Observable<BaseResponse<PdfEntity>> getProductDescription(@Body Map<String, Object> bodyParams);

    //修改系统消息为已读。必填项：id
    @POST("msg/sys/read")
    Observable<BaseResponse<Object>> getSystemRead(@Body Map<String, Object> bodyParams);

    //系统消息全部已读
    @POST("msg/sys/readAll")
    Observable<BaseResponse<Object>> getSystemReadAll(@Body Map<String, Object> bodyParams);

    //删除系统消息
    @POST("msg/sys/deleteBatch")
    Observable<BaseResponse<Object>> deleteSystemMessage(@Body List<String> bodyParams);

    /*//设备告警消息。必填项：vehicleIds车辆id集合,current页码,size页容量
    @POST("msg/alarm/listPage")
    Observable<BaseResponse<BasePageEntity<MessageEntity>>> getAlarmMessage(@Body Map<String, Object> bodyParams);*/

    //修改终端设备告警消息为已读。必填项：id
    @POST("msg/alarm/read")
    Observable<BaseResponse<Object>> getAlarmRead(@Body Map<String, Object> bodyParams);

    //告警消息全部已读
    @POST("msg/alarm/readAll")
    Observable<BaseResponse<Object>> getAlarmReadAll(@Body Map<String, Object> bodyParams);

    //删除告警消息
    @POST("msg/alarm/deleteBatch")
    Observable<BaseResponse<Object>> deleteAlarmMessage(@Body List<String> bodyParams);

    //运动记录
    @POST("sport/listTrack")
    Observable<BaseResponse<SportEntity>> getSportList(@Body Map<String, Object> bodyParams);

    //运动轨迹名称修改 id,name
    @POST("sport/update")
    Observable<BaseResponse<Object>> updateSport(@Body Map<String, Object> bodyParams);

    //运动轨迹经纬度  //id
    @POST("sport/locations")
    Observable<BaseResponse<List<SportLocationEntity>>> getSportLocations(@Body Map<String, Object> bodyParams);

    //新轨迹详情,待统计数据 //id
    @POST("sport/trackLocations")
    Observable<BaseResponse<SportLocationPageEntity<SportLocationEntity>>> getSportLocationsNew(@Body Map<String, Object> bodyParams);

    // 实时码表数据上传
    @POST("sport/upload")
    Observable<BaseResponse<Object>> uploadSport(@Body CodeTableParams codeTableParams);//

    //车辆最后位置
    @POST("history/info")
    Observable<BaseResponse<DevicePositionEntity>> getTrackInfo(@Body Map<String, Object> bodyParams);

    //车辆追踪位置刷新(无权限判断)
    @POST("history/vehickeTrack")
    Observable<BaseResponse<TrackInfoRefreshEntity>> getTrackInfoRefresh(@Body Map<String, Object> bodyParams);

    //首页数据
    @POST("home/data")
    Observable<BaseResponse<HomeDataEntity>> getHomeData(@Body Map<String, Object> bodyParams);

    //捷安特获取车辆列表
    @POST("jieante/api/getVehicles")
    Observable<BaseResponse<List<VehicleListEntity>>> getVehicles();

    //下发指令给终端。必填项：msgId(4设防，5解防)，flowId流水号（时间戳），vehicleId车辆id，paramValue半径   发送指令
    @POST("home/command")
    Observable<BaseResponse<Object>> sendCommand(@Body Map<String, Object> bodyParams);


    //返回会的states数组里包含有23是车辆熄火，33是车辆发动
    @POST("history/info")
    Observable<BaseResponse<HistoryInfoEntity>> sendHistoryInfo(@Body Map<String, Object> bodyParams);

    @POST("home/command")
    Call<BaseResponse<Object>> sendCommandCall(@Body Map<String, Object> bodyParams);

    //获取车辆类型
    @POST("common/getVehicleTypes")
    Observable<BaseResponse<List<VehicleModelEntity>>> getVehicleType();

    //获取车辆型号
    @POST("common/getVehicleModels")
    Observable<BaseResponse<String>> getDeviceModels(@Body Map<String, Object> bodyParams);

    //获取车辆品牌
    @POST("common/getVehicleBrands")
    Observable<BaseResponse<List<DeviceBrandEntity>>> getDeviceBrands(@Body Map<String, Object> bodyParams);


    // 城市区域
    //    @POST("common/getAreaSelects")
    //    Observable<BaseResponse<List<CityAreaEntity>>> getAreaSelects(@Body Map<String, Object> bodyParams);


    /*

      //流量套餐
      @POST("common/getFlowSets")
      Observable<BaseResponse<List<PayPackageEntity>>> getFlowSets(@Body Map<String, Object> bodyParams);



      //微信支付订单信息
      @POST("api/weixin/pay")
      Observable<BaseResponse<WeChatPayOrderInfoEntity>> getWeChatOrderInfo(@Body Map<String, Object> bodyParams);

      //微信支付失败
      @POST("api/weixin/cancel")
      Observable<BaseResponse<Object>> getWeChatCancel(@Body Map<String, Object> bodyParams);

      //支付宝支付订单信息
      @POST("api/alipay/pay")
      Observable<BaseResponse<AliPayOrderInfoEntity>> getAliOrderInfo(@Body Map<String, Object> bodyParams);

      //支付宝支付失败
      @POST("api/alipay/cancel")
      Observable<BaseResponse<Object>> getAliCancel(@Body Map<String, Object> bodyParams);





   */
    //banner  findBannerByName
    @POST("common/findBannerByType")
    Observable<BaseResponse<BannerEntity>> getBanner(@Body Map<String, Object> bodyParams);

    //    获取电子围栏告警设置数据
    @POST("common/getFenceAlarmSets")
    Observable<BaseResponse<List<PayPackageEntity>>> getFenceAlarmSets(@Body Map<String, Object> bodyParams);

    //获取电话告警设置数据
    @POST("common/getPhoneAlarmSets")
    Observable<BaseResponse<List<PayPackageEntity>>> getPhoneAlarmSets(@Body Map<String, Object> bodyParams);

    //反馈类型
    @POST("common/getQuestionTypes")
    Observable<BaseResponse<List<FeedbackEntity>>> getFeedbackType(@Body Map<String, Object> bodyParams);

    //获取当前登录用户的问题反馈信息列表
    @POST("question/queryQuestions")
    Observable<BaseResponse<BasePageEntity<FeedbackRecordEntity>>> getQuestions(@Body Map<String, Object> bodyParams);

    //添加问题反馈
    @POST("question/save")
    Observable<BaseResponse<Object>> saveQuestions(@Body Map<String, Object> bodyParams);

    // 当前登陆的紧急联系人
    @POST("appUser/queryContacts")
    Call<BaseResponse<List<RescueBean>>> getContacts(@Body Map<String, Object> bodyParams);

    // 添加紧急联系人
    @POST("appUser/insertContact")
    Call<BaseResponse<String>> insertContact(@Body Map<String, Object> bodyParams);

    // 删除紧急联系人
    @POST("appUser/deleteContact")
    Call<BaseResponse<String>> deleteContact(@Body Map<String, Object> bodyParams);

    // 获取用户紧急救援设置信息
    @POST("appUser/emergencyInfo")
    Call<BaseResponse<EmergencyInfo>> emergencyInfo(@Body Map<String, Object> bodyParams);

    //流量账户余额
    @POST("flowAccount/info")
    Observable<BaseResponse<FlowAccountEntity>> getFlowAccountInfo(@Body Map<String, Object> bodyParams);

    //流水账单
    @POST("flowAccount/queryAccountBills")
    Observable<BaseResponse<BasePageEntity<FlowBillEntity>>> getFlowBill(@Body Map<String, Object> bodyParams);

    // 设置是否开启紧急救援
    @POST("appUser/setEmergency")
    Call<BaseResponse<String>> setEmergency(@Body Map<String, Object> bodyParams);

    // 告警灵敏度
    @POST("home/command")
    Call<BaseResponse<String>> homeCommand(@Body AlarmSensitivityParams bodyParams);

    // 灯的工作模式
    @POST("home/command")
    Call<BaseResponse<String>> lightWorkModel(@Body LightWordModelParams bodyParams);

    // 遥控器配对
    @POST("home/command")
    Call<BaseResponse<String>> pairingWorkModle(@Body Map<String, Object> bodyParams);

    // 灯亮度大小设置
    @POST("home/command")
    Call<BaseResponse<String>> lightBrightness(@Body LightBrightnesParams bodyParams);

    // 刹车灵敏度
    @POST("home/command")
    Call<BaseResponse<String>> brakeSensitivity(@Body BrakeSensitivityParams bodyParams);

    // 亮灯模式
    @POST("home/command")
    Call<BaseResponse<String>> lightModel(@Body LightModelParams bodyParams);

    // 闪灯模式+频率
    @POST("home/command")
    Call<BaseResponse<String>> lightShakeModel(@Body LightShakeModelParams bodyParams);

    //判断是否注册并使用过鹿卫士
    @POST("jieante/api/checkTokenBind")
    Observable<BaseResponse<UserEntity>> checkTokenBind();
}
