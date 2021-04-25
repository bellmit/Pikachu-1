package com.pikachu.framework.database.core;

import java.math.BigDecimal;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Desc：java-sql类型映射
 * @Author：AD
 * @Date：2020/3/24 12:06
 */
enum TypeMapping {
    BOOL(Types.CHAR, boolean.class, Boolean.class),
    CHAR(Types.CHAR, char.class, Character.class),
    BYTE(Types.TINYINT, byte.class, Byte.class),
    SHORT(Types.SMALLINT, short.class, Short.class),
    INT(Types.INTEGER, int.class, Integer.class),
    LONG(Types.BIGINT, long.class, Long.class),
    FLOAT(Types.FLOAT, float.class, Float.class),
    DOUBLE(Types.DOUBLE, double.class, Double.class),

    BIG_DECIMAL(Types.DECIMAL, BigDecimal.class),
    BYTES(Types.BLOB, byte[].class),
    STRING(Types.VARCHAR, String.class),
    ENUM(Types.VARCHAR, Enum.class),
    
    TIME(Types.TIME,LocalTime.class),
    DATE(Types.DATE,LocalDate.class),

    TIMESTAMP(Types.TIMESTAMP, Date.class, LocalDateTime.class);


    private static final Map<Class<?>, Integer> TYPES = new HashMap<>();
    private static final Map<Class<?>, TypeMapping> MAPPERS = new HashMap<>();
    static {
        for (TypeMapping mapper : values()) {
            Class<?>[] clazzs = mapper.getClasses();
            for (Class<?> clazz : clazzs) {
                TYPES.put(clazz, mapper.getSqlType());
                MAPPERS.put(clazz,mapper);
            }
        }
    }

    public static int getSQLType(Class<?> javaClass) {
        if (TYPES.containsKey(javaClass)) {
            return TYPES.get(javaClass);
        }else{
            if(javaClass.isEnum()){
                return TYPES.get(Enum.class);
            }
            return Types.OTHER;
        }
    }

    public static TypeMapping getMapper(Class<?> javaClass){
        return MAPPERS.get(javaClass);
    }

    private final int sqlType;

    private final Class<?>[] classes;

    TypeMapping(int sqlType, Class<?>... classes) {
        this.sqlType = sqlType;
        this.classes = classes;
    }

    public Class<?>[] getClasses() {
        return classes;
    }

    public int getSqlType() {
        return sqlType;
    }
}
