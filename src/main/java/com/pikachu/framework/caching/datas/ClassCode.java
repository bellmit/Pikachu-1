package com.pikachu.framework.caching.datas;


import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ClassCode {

    $BYTE(ClassCode.BYTE, byte.class, Byte.class),
    $SHORT(ClassCode.SHORT, short.class, Short.class),
    $INT(ClassCode.INT, int.class, Integer.class),
    $LONG(ClassCode.LONG, long.class, Long.class),
    $FLOAT(ClassCode.FLOAT, float.class, Float.class),
    $DOUBLE(ClassCode.DOUBLE, double.class, Double.class),
    $BOOLEAN(ClassCode.BOOLEAN, boolean.class, Boolean.class),
    $CHAR(ClassCode.CHAR, char.class, Character.class),
    $STRING(ClassCode.STRING, String.class),
    $DATE(ClassCode.DATE, Date.class),
    $LOCAL_DATE_TIME(ClassCode.LOCAL_DATE_TIME, LocalDateTime.class);

    // 不能使用$BYTE.getCode()方法获取
    public static final int BYTE = 1;

    public static final int SHORT = 2;

    public static final int INT = 3;

    public static final int LONG = 4;

    public static final int FLOAT = 5;

    public static final int DOUBLE = 6;

    public static final int BOOLEAN = 7;

    public static final int CHAR = 8;

    public static final int STRING = 9;

    public static final int DATE = 10;

    public static final int LOCAL_DATE_TIME = 11;

    private final int code;

    private final Class<?>[] clazz;

    private static Map<Class<?>, Integer> codeMap;

    private static final Object LOCK = new Object();

    private static volatile boolean inited = false;

    public static int getType(Class<?> clazz) {
        if (!inited) {
            synchronized (LOCK) {
                if (!inited) {
                    init();
                }
            }
        }
        return codeMap.get(clazz);
    }

    private static void init() {
        codeMap = new ConcurrentHashMap<>();
        ClassCode[] values = values();
        Arrays.stream(values).forEach(value -> {
            int code = value.getCode();
            Class<?>[] clazzs = value.getClazz();
            for (Class<?> clazz : clazzs) {
                codeMap.put(clazz, code);
            }
        });
        inited = true;
    }

    private ClassCode(int code, Class<?>... clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public int getCode() {
        return code;
    }

    public Class<?>[] getClazz() {
        return clazz;
    }
}
