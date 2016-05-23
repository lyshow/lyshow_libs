
package com.libs.my_libs.framework.utils;

import java.security.MessageDigest;

/**
 * MD5工具类
 * 
 * @author chenys
 */
public class MD5Util {

    /**
     * 获取MD5
     * 
     * @param input 字符串
     * @return
     */
    public static String getMd5(String input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes());
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取MD5
     * 
     * @param input 字符串
     * @param encode 编码
     * @return
     */
    public static String getMd5(String input, String encode) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input.getBytes(encode));
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取MD5
     * 
     * @param input
     * @return
     */
    public static String getMd5(byte[] input) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(input);
            return toHexString(md5.digest());
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * byte数组转成十六进制字符串
     * 
     * @param b
     * @return
     * @throws Exception
     */
    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
            sb.append(hexChar[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    private static char[] hexChar = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    private static final String HEX_NUMS_STR = "0123456789ABCDEF";

    /**
     * 将16进制字符串转换成字节数组
     * 
     * @param hex
     * @return
     */
    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] hexChars = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (HEX_NUMS_STR.indexOf(hexChars[pos]) << 4 | HEX_NUMS_STR
                    .indexOf(hexChars[pos + 1]));
        }
        return result;
    }

}
