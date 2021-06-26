package com.luhong.locwithlibrary.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期时间
 * Created by ITMG on 2020-01-14.
 */
public class DateUtils {

    /**
     * 格式化秒
     *
     * @param second 秒
     * @return
     */
    public static String formatSecond(long second) {
        String hh = second / 3600 > 9 ? second / 3600 + "" : "0" + second / 3600;
        String mm = (second % 3600) / 60 > 9 ? (second % 3600) / 60 + "" : "0" + (second % 3600) / 60;
        String ss = (second % 3600) % 60 > 9 ? (second % 3600) % 60 + "" : "0" + (second % 3600) % 60;
        return hh + ":" + mm + ":" + ss + "";
    }

    public static String formatHHmmSecond(long second) {
        String hh = second / 3600 > 9 ? second / 3600 + "" : "0" + second / 3600;
        String mm = (second % 3600) / 60 > 9 ? (second % 3600) / 60 + "" : "0" + (second % 3600) / 60;
        return hh + ":" + mm;
    }

    /**
     * 计算字符串时间大小
     *
     * @param startTime 开始时间字符串
     * @param stopTime  结束时间
     * @return
     * @throws Exception
     */
    public static long dateCalculate(String startTime, String stopTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startDate = sdf.parse(startTime);
            Date stopDate = sdf.parse(stopTime);
            return (stopDate.getTime() - startDate.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1l;
    }

    /**
     * 格式化当前日期时间
     *
     * @return
     */
    public static String formatCurrentDateTime(String formatStr) {
        if (isEmpty(formatStr)) {
            formatStr = "yyyy-MM-dd HH:mm:ss";
        }
        SimpleDateFormat format = new SimpleDateFormat(formatStr);
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public static String formatCurrentDateTime() {
        return formatCurrentDateTime("");
    }

    public static String formatCurrentMillisecond() {//文件名
        return formatCurrentDateTime("yyyyMMddHHmmssSSS");
    }

    /**
     * @param strFormat 格式化格式
     * @param strData   要格式化的日期
     * @return
     * @throws Exception
     */
    public static String formatConversionTime(String strFormat, String strData) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strData);
            String formatTime = new SimpleDateFormat(strFormat).format(date);
            return formatTime;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 轨迹时间格式化
     *
     * @param strDate
     * @return
     */
    public static String formatSportDateTime(String strDate) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(strDate);
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断是否为空
     *
     * @param content
     * @return
     */
    public static boolean isEmpty(String content) {
        return TextUtils.isEmpty(content);
    }


    /*
     * 将时间转换为时间戳
     */
    @SuppressLint("SimpleDateFormat")
    public static long dateToStamp(String s) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        return ts;
    }
}
