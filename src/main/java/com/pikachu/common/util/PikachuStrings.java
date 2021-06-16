package com.pikachu.common.util;

import java.util.UUID;

/**
 * @Desc TODO
 * @Date 2021/3/31 21:40
 * @Author AD
 */
public class PikachuStrings {
    
    /**
     * 将字符串进行复制，并以分割符分割
     *
     * @param who    需要复制的字符串
     * @param split  分割符
     * @param length 长度
     *
     * @return 如：?,?,?,?,?
     */
    public static String duplicate(String who, String split, int length) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; ++i) {
            if (i > 0) {
                sb.append(split);
            }
            sb.append(who);
        }
        return sb.toString();
    }
    
    /**
     * 判断字符串是否为"","    "
     *
     * @param check 需检查的字符串,默认去除前后空白条
     *
     * @return boolean
     */
    public static boolean isNull(String check) {
        return isNull(check, true);
    }
    
    /**
     * 判断字符串是否不为"","  "
     *
     * @param check 需检查的字符串,默认去除前后空白条
     *
     * @return boolean
     */
    public static boolean isNotNull(String check) {
        
        return !isNull(check, true);
    }
    
    /**
     * 判断字符串是否为null
     *
     * @param check 需要检查的字符串
     * @param trim  是否去除前后空格
     *
     * @return
     */
    public static boolean isNull(String check, boolean trim) {
        if (trim) {
            return check == null || check.trim().length() == 0;
        } else {
            return check == null || check.length() == 0;
        }
    }
    
    public static String firstCharToLowercase(String str) {
        if (isNull(str)) {
            return "";
        }
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
    
    /**
     * 获取唯一序列号
     *
     * @return
     */
    public static String UUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
    
}
