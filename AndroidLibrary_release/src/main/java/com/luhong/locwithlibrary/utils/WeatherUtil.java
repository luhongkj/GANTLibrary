package com.luhong.locwithlibrary.utils;


import com.luhong.locwithlibrary.R;

public class WeatherUtil
{

    public static int getWeather(String weather)
    {
        int resId = -1;
        switch (weather)
        {
            case "晴":
                resId = R.mipmap.weather_sunny;
                break;
            case "多云":
                resId = R.mipmap.weather_partly_cloudy;
                break;
            case "阴":
                resId = R.mipmap.weather_cloudy;
                break;
            case "阵雨":
                resId = R.mipmap.weather_shower;
                break;
            case "雷阵雨":
                resId = R.mipmap.weather_thunder_shower;
                break;
            case "小雨":
                resId = R.mipmap.weather_light_rain;
                break;
            case "中雨":
                resId = R.mipmap.weather_light_rain;
                break;
            case "大雨":
                resId = R.mipmap.weather_heavy_rain;
                break;
            case "暴雨":
                resId = R.mipmap.weather_rainstorm;
                break;
            case "大暴雨":
                resId = R.mipmap.weather_rainstorm;
                break;
            case "特大暴雨":
                resId = R.mipmap.weather_rainstorm;
                break;
            case "阵雪":
                resId = R.mipmap.weather_snow_shower;
                break;
            case "小雪":
                resId = R.mipmap.weather_light_snow;
                break;
            case "中雪":
                resId = R.mipmap.weather_light_snow;
                break;
            case "大雪":
                resId = R.mipmap.weather_heavy_snow;
                break;
            case "暴雪":
                resId = R.mipmap.weather_heavy_snow;
                break;
            case "雾":
                resId = R.mipmap.weather_smog;
                break;
            case "冻雨":
                resId = R.mipmap.weather_light_rain;
                break;
            case "沙尘暴":
                resId = R.mipmap.weather_smog;
                break;
            case "小雨-中雨":
                resId = R.mipmap.weather_light_rain;
                break;
            case "中雨-大雨":
                resId = R.mipmap.weather_heavy_rain;
                break;
            case "大雨-暴雨":
                resId = R.mipmap.weather_heavy_rain;
                break;
            case "暴雨-大暴雨":
                resId = R.mipmap.weather_rainstorm;
                break;
            case "大暴雨-特大暴雨":
                resId = R.mipmap.weather_rainstorm;
                break;
            case "小雪-中雪":
                resId = R.mipmap.weather_light_snow;
                break;
            case "中雪-大雪":
                resId = R.mipmap.weather_heavy_snow;
                break;
            case "大雪-暴雪":
                resId = R.mipmap.weather_heavy_snow;
                break;
            case "浮尘":
                resId = R.mipmap.weather_smog;
                break;
            case "扬沙":
                resId = R.mipmap.weather_smog;
                break;
            case "强沙尘暴":
                resId = R.mipmap.weather_smog;
                break;
            case "飑":
                resId = R.mipmap.weather_smog;
                break;
            case "龙卷风":
                resId = R.mipmap.weather_smog;
                break;
            case "弱高吹雪":
                resId = R.mipmap.weather_light_snow;
                break;
            case "轻霾":
                resId = R.mipmap.weather_smog;
                break;
            case "霾":
                resId = R.mipmap.weather_smog;
                break;
            default:
                resId = R.mipmap.weather_partly_cloudy;
                break;
        }

        return resId;
    }
}
