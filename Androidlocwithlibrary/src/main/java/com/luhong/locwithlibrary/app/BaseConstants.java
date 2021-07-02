package com.luhong.locwithlibrary.app;

/**
 * 全局常量
 * Created by ITMG on 2020-01-13.
 */
public interface BaseConstants {
    String TOKEN = "token";
    String IS_APP_FIRST = "IS_APP_FIRST";//App第一次启动
    String LOGIN_KEY = "loginKey";//登录信息
    String USERNAME_KEY = "USERNAME_KEY";//用户名手机号
    String WEB_TITLE_KEY = "WEB_TITLE_KEY";
    String WEB_URL_KEY = "WEB_URL_KEY";
    String WEB_RIGHT_TEXT_KEY = "WEB_RIGHT_TEXT_KEY";
    String APP_UPDATE = "appUpdate";//版本升级
    String CURRENT_LANGUAGE = "currentLanguage";//当前语言

    int TYPE_NOT_OPEN_GPS = 2;//GPS未打开
    int TYPE_NOT_FINE_LOCATION = 3;//未授予定位权限

}
