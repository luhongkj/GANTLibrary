package com.luhong.locwithlibrary.utils;


import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Des加密算法
 *
 * @author cdx
 */
public class DateSecret {

    private static String keyCode = "abcdefgh";//秘钥可以任意改，只要总长度是8个字节就行
    private static byte[] iv = {1, 2, 3, 4, 5, 6, 7, 8};

    /**
     * 加密
     * @param encryptString
     * @return
     * @throws Exception
     */
    public static String encryptDES(String encryptString)
            throws Exception {
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, zeroIv);
        byte[] encryptedData = cipher.doFinal(encryptString.getBytes("utf-8"));
        return Base64Util.encode(encryptedData);
    }

    /**
     * 解密
     * @param decryptString
     * @return
     * @throws Exception
     */
    public static String decryptDES(String decryptString)  throws Exception {
//        byte[] byteMi = Base64.decode(decryptString);
        byte[] byteMi = Base64Util.decode(decryptString);
        IvParameterSpec zeroIv = new IvParameterSpec(iv);
        SecretKeySpec key = new SecretKeySpec(keyCode.getBytes(), "DES");
        Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, zeroIv);
        byte decryptedData[] = cipher.doFinal(byteMi);
        return new String(decryptedData, "utf-8");
    }

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return String
     */
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        try {
            System.out.println(DateSecret.encryptDES("{\"result\":\"1\",\"msg\":\"操作成功\"}"));
            String ss = DateSecret.decryptDES("kY+V+SIJYvWi4z3wDExqseCfNgG6WZIfEmJkNtXJ1Sq/HZmW0xQscJeV0yHBry9m+OOArtDSE1Y=");
            System.out.println(ss);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
