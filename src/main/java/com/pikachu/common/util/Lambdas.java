package com.pikachu.common.util;

import com.pikachu.common.collection.IFunction;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

/**
 * Lambda工具类
 *
 * @author AD
 * @date 2021/8/25 18:38
 */
public class Lambdas {
    
    public static <T, R> String getMethodName(IFunction<T, R> func) {
        try {
            // 直接调用writeReplace
            Class<? extends IFunction> clazz = func.getClass();
            Method writeReplace = null;
            writeReplace = clazz.getDeclaredMethod("writeReplace");
            writeReplace.setAccessible(true);
            Object o = writeReplace.invoke(func);
            SerializedLambda s = (SerializedLambda) o;
            return s.getImplMethodName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getColumns(IFunction... ks) {
        StringBuilder columns = new StringBuilder();
        for (int i = 0; i < ks.length; i++) {
            if (i > 0) {
                columns.append(",");
            }
            String field = fix(Lambdas.getMethodName(ks[i]));
            columns.append(field);
        }
        return columns.toString();
    }
    
    private static String fix(String methodName) {
        String name = methodName.toUpperCase();
        if (name.startsWith("GET") || name.startsWith("SET")) {
            return name.substring(3);
        } else if (name.startsWith("IS")) {
            return name.substring(2);
        }
        return methodName;
    }
    
}
