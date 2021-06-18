package com.luhong.locwithlibrary.net.cache;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by xiaolei on 2018/2/7.
 */

public class Util
{
    /**
     * 对字符串进行MD5编码
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String encryptMD5(String string)
    {
        try
        {
            byte[] hash = MessageDigest.getInstance("MD5").digest(
                    string.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash)
            {
                if ((b & 0xFF) < 0x10)
                {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        return string;
    }
}
