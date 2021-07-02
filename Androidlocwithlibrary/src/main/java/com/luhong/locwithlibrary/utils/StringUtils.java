package com.luhong.locwithlibrary.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;

public class StringUtils {

    /**
     * MD5加密
     */
    public static String md5Hex(String sourceStr) {
        String result = "";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(sourceStr.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            result = buf.toString();//32位的加密
            Logger.error("MD5加密结果=" + buf.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 格式化数字（不够两位数前补0）
     *
     * @param data
     * @return
     */
    public static String formatInt(int data) {
        return String.format("%02d", data);
    }

    /**
     * 格式化保留一位小数
     *
     * @param d
     * @return
     */
    public static String formatSingleData(double d) {
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(d);
    }

    public static String formatDoubleData(double d) {
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }


}
