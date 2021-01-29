package com.x.pikachu.util;

/**
 * @Desc
 * @Date 2020/12/12 00:14
 * @Author AD
 */
public class ArrayHelper {
    
    public static final int[] EMPTY_INT = new int[0];
    
    public static boolean isEmpty(Object[] array) {
        return !isNotEmpty(array);
    }
    
    public static boolean isNotEmpty(Object[] array) {
        return array != null && array.length > 0;
    }
    
    public static boolean isNotEmpty(int[] array) {
        return array != null && array.length > 0;
    }
    
}
