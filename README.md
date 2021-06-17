# GANTLibrary
为捷安特开发的功能模块

里面包含安卓、iOS的Library。

# iOS接入说明：
1、libraay接入
把CloudRiding.framework库和CRResource.bundle资源文件拉到工程上

添加支付回调
添加 URL Type: 在targets->info->URL Types 注意URL Schame中一定要填：CloudRidingLibraryURL
回调URL获取

2、定位功能使用
要在iOS 9及以上版本使用后台定位功能, 需要保证"Background Modes"中的"Location updates"处于选中状态 先到GARGETS - signing & Capabilities 查看有没有Background Modes，如果有把Location updates勾上，没有就添加Background Modes模块，再把Location updates勾上

3、权限申请
定位权限
NSLocationAlwaysAndWhenInUseUsageDescription 
NSLocationAlwaysUsageDescription
NSLocationWhenInUseUsageDescription
麦克风权限
NSMicrophoneUsageDescription
相册权限
NSPhotoLibraryAddUsageDescription
NSPhotoLibraryUsageDescription
摄像机权限
NSCameraUsageDescription 

支持所有http请求
需要在info,plist添加
<key>NSAppTransportSecurity</key>
<dict>
<key>NSAllowsArbitraryLoads</key>
<true/>	
</dict>

依赖第三方库下载
使用cocoapod下载
pod 'AFNetworking'
pod 'FMDB'
pod 'MJExtension'
pod 'AMapSearch-NO-IDFA'
pod 'AMapNavi-NO-IDFA'
pod 'YYKit'
pod 'MJRefresh'
pod 'SDWebImage'

开始使用
1注册高德地图
AppDelegate 引入#import <CloudRiding/CRServices.h>
didFinishLaunchingWithOptions方法初始化高德地图[[CRServices sharedInstancel] setApiKey:@"填写高德地图的Apikey"];

2 打开library
引入头文件#import <CloudRiding/CRServices.h>和#import <CloudRiding/CRMainViewController.h>

在打开library方法上
传输token
[CRServices sharedInstancel].token = @"鼎联后台token";
传输手机号码
[CRServices sharedInstancel].phoneNumber = @"对应用户手机号码";
打开library
[self.navigationController pushViewController:[CRMainViewController new] animated:YES];

常见问题
7.1编译报错armv7可以在Excluded Architecutrues 添加armv7
7.2 ld: 1906 duplicate symbols for architecture arm64 原因文件冲突，可联系鹿卫士开发人员


# 安卓接入说明：
1、鹿卫士library butterknife注解
在工程根目录下/build.gradleclasspath 'com.jakewharton:butterknife-gradle-plugin:9.0.0'//注解框架
classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'//注解library
生成R2文件
task clean(type: Delete) {
    delete rootProject.buildDir
}
编译类文件，解压缩文件，删除文件
2、Android鹿卫士library第三方依赖
implementation 'androidx.appcompat:appcompat:1.0.2'
implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
testImplementation 'junit:junit:4.12'
androidTestImplementation 'androidx.test.ext:junit:1.1.0'
implementation 'androidx.cardview:cardview:1.0.0'
implementation 'androidx.recyclerview:recyclerview:1.2.0-alpha03'
androidTestImplementation 'androidx.test.espresso:espresso-core:3.1.1'
//     网络请求依赖
implementation 'com.squareup.retrofit2:retrofit:2.7.1'
implementation 'com.squareup.retrofit2:adapter-rxjava2:2.7.1'
implementation 'com.squareup.retrofit2:converter-gson:2.7.1'
implementation 'com.squareup.okhttp3:logging-interceptor:4.3.1'
implementation 'com.jakewharton.rxbinding2:rxbinding:2.2.0'
implementation 'com.qianwen:okhttp-utils:3.8.0'
implementation files('libs/Msc.jar')
implementation 'com.jakewharton:butterknife:10.2.1'
annotationProcessor 'com.jakewharton:butterknife-compiler:10.2.1'
implementation 'com.github.bumptech.glide:glide:4.9.0'
annotationProcessor 'com.github.bumptech.glide:compiler:4.9.0'
//图片框架
implementation 'com.github.githubZYQ:easypermission:v1.0.0'
//权限
implementation 'com.github.zyyoona7:pickerview:1.0.9'
//三级联动
implementation 'top.zibin:Luban:1.1.8'
//加密
implementation 'org.greenrobot:eventbus:3.1.1'
//观察者
implementation files('libs/pinyin4j-2.5.0.jar')
//中文转换拼音
implementation 'com.wang.avi:library:2.1.3'
//扫描
implementation files('libs/zxing.jar')
//PDF文件
implementation 'com.github.barteksc:android-pdf-viewer:3.2.0-beta.1'
implementation "android.arch.lifecycle:extensions:1.1.1"//自定义弹窗
/*//3D地图so及jar
implementation 'com.amap.api:3dmap:latest.integration'*/
//定位功能
implementation 'com.amap.api:location:latest.integration'
//搜索功能
implementation 'com.amap.api:search:latest.integration'
//导航
implementation 'com.amap.api:navi-3dmap:latest.integration' 
3、Intern跳入鹿卫士library
捷安特APP进入鹿卫士library前需判断当前账号是否有绑定的车辆
Intent intent = new Intent(MainActivity.this, LHomeActivity.class);
intent.putExtra("token","捷安特APP请求数据token");
intent.putExtra("phone","捷安特APP登录手机号");
startActivity(intent);
4、鹿卫士library使用的权限
<!—访问网络状态权限-->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<!—声效，闪光灯-->
<uses-permission android:name="android.permission.VIBRATE" />
sd读写权限
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
相机权限
<uses-permission android:name="android.permission.CAMERA" />
网络请求权限
<uses-permission android:name="android.permission.INTERNET" />
定位权限
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
拨打电话权限
<uses-permission android:name="android.permission.READ_PHONE_STATE" />
读取手机联系人权限
<uses-permission android:name="android.permission.READ_CONTACTS" />
<!--屏幕常亮-->
<uses-permission android:name="android.permission.WAKE_LOCK" />
<uses-permission android:name="android.permission.DEVICE_POWER" />
