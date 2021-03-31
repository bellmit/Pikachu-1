package com.pikachu.common.util;


import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Date 2019-03-04 21:52
 * @Author AD
 */
public final class PikachuArrays {
    
    /**
     * 空 byte[] 数组
     */
    public static final byte[] EMPTY_BYTE = new byte[0];
    /**
     * 空 short[] 数组
     */
    public static final short[] EMPTY_SHORT = new short[0];
    /**
     * 空 int[] 数组
     */
    public static final int[] EMPTY_INT = new int[0];
    /**
     * 空 long[] 数组
     */
    public static final long[] EMPTY_LONG = new long[0];
    /**
     * 空 float[] 数组
     */
    public static final float[] EMPTY_FLOAT = new float[0];
    /**
     * 空 double[] 数组
     */
    public static final double[] EMPTY_DOUBLE = new double[0];
    /**
     * 空 String[] 数组
     */
    public static final String[] EMPTY_STRING = new String[0];
    /**
     * 空 File[] 数组
     */
    public static final File[] EMPTY_FILE = new File[0];
    /**
     * 空 Object[] 数组
     */
    public static final Object[] EMPTY = new Object[0];
    
    private PikachuArrays() {
    }
    
    public static byte[] copy(byte[] data) {
        if (!isEmpty(data)) {
            byte[] copy = new byte[data.length];
            System.arraycopy(data, 0, copy, 0, copy.length);
            return copy;
        }
        return EMPTY_BYTE;
    }
    
    public static boolean isValid(Object[] os1, Object[] os2) {
        if (!isEmpty(os1) && !isEmpty(os2) && os1.length == os2.length) {
            return true;
        }
        return false;
    }
    
    public static <T> boolean isEmpty(Set<T> set) {
        return set == null || set.size() == 0;
    }
    
    public static <T> boolean isEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }
    
    public static <T> boolean isEmpty(T[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(int[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(byte[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(short[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(long[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(float[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(double[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static boolean isEmpty(char[] arrays) {
        return arrays == null || arrays.length == 0;
    }
    
    public static <T> Set<T> toSet(T[] ts) {
        return Stream.of(ts).collect(Collectors.toSet());
    }
    
    public static <T> T[] toArray(List<T> list, Class<T> clazz) {
        if (isEmpty(list)) {
            return null;
        }
        T[] ts = (T[]) Array.newInstance(clazz, list.size());
        return list.toArray(ts);
    }
    
    public static <T> List<T> join(T[] arr1, T[] arr2) {
        if (arr1 == null) {
            return Arrays.asList(arr2);
        }
        if (arr2 == null) {
            return Arrays.asList(arr1);
        }
        List<T> list = new ArrayList<>(arr1.length + arr2.length);
        list.addAll(Arrays.asList(arr1));
        list.addAll(Arrays.asList(arr2));
        return list;
    }
    

}
