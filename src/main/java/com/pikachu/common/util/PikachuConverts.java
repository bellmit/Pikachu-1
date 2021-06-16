package com.pikachu.common.util;

import java.math.BigDecimal;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Desc TODO
 * @Date 2021/3/31 21:39
 * @Author AD
 */
public class PikachuConverts {
    
    /**
     * true，y，yes，1字符串值为true，其它为false
     *
     * @param value 需转换的值
     *
     * @return
     */
    public static boolean toBoolean(Object value) {
        return toBoolean(value, false);
    }
    
    /**
     * true，y，yes，1字符串值为true，其它为false
     *
     * @param value        需转换的值
     * @param defaultValue 默认值
     *
     * @return
     */
    public static boolean toBoolean(Object value, boolean defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        String t = value.toString().toUpperCase();
        return "TRUE".equals(t) || "Y".equals(t) || "YES".equals(t) || ("1".equals(t)) || defaultValue;
    }
    
    /**
     * 将对象转为10进制int数值，转换出错时默认返回0
     *
     * @param value 转换对象
     *
     * @return
     */
    public static int toInt(Object value) {
        return toInt(value, 0, 10);
    }
    
    /**
     * 将对象转为10进制int数值，转换出错时默认返回defaultValue
     *
     * @param value        转换对象
     * @param defaultValue 默认值
     *
     * @return
     */
    public static int toInt(Object value, int defaultValue) {
        return toInt(value, defaultValue, 10);
    }
    
    /**
     * 将对象转为指定进制int数值，转换出错时默认返回defaultValue
     *
     * @param value        转换对象
     * @param defaultValue 默认值
     * @param radix        进制
     *
     * @return
     */
    public static int toInt(Object value, int defaultValue, int radix) {
        // 判断有效性
        if (value == null) {
            // 为空返回默认值
            return defaultValue;
        }
        // 非int类型
        if (!value.getClass().equals(Integer.TYPE) && !(value instanceof Integer)) {
            // 非double类型
            if (!value.getClass().equals(Double.TYPE) && !(value instanceof Double)) {
                // 转为字符串
                String s = value.toString();
                // 判断有效性
                if (PikachuStrings.isNull(s)) {
                    // 返回默认值
                    return defaultValue;
                } else {
                    try {
                        // 解析成对应进制的int值
                        return Integer.parseInt(s, radix);
                    } catch (Exception e) {
                        // 异常返回默认值
                        return defaultValue;
                    }
                }
            } else {
                return ((Double) value).intValue();
            }
        } else {
            return (Integer) value;
        }
        
    }
    
    public static byte toByte(Object value) {
        return toByte(value, (byte) 0, 10);
    }
    
    public static byte toByte(Object value, byte defaultValue) {
        return toByte(value, defaultValue, 10);
    }
    
    public static byte toByte(Object value, byte defaultValue, int radix) {
        if (value == null) {
            return defaultValue;
        }
        if (!(value instanceof Byte) && !(value instanceof Integer)) {
            String s = value.toString();
            if (PikachuStrings.isNull(s)) {
                return defaultValue;
            } else {
                try {
                    byte b = (byte) Integer.parseInt(s, radix);
                    return b;
                } catch (Exception e) {
                    return defaultValue;
                }
            }
        } else {
            return Byte.valueOf(value.toString());
        }
        
    }
    
    public static short toShort(Object value) {
        return toShort(value, (short) 0, 10);
    }
    
    public static short toShort(Object value, short defaultValue) {
        return toShort(value, defaultValue, 10);
    }
    
    public static short toShort(Object value, short defaultValue, int radix) {
        if (value == null) {
            return defaultValue;
        }
        if (!value.getClass().equals(Short.TYPE) && !(value instanceof Short)) {
            if (!value.getClass().equals(Integer.TYPE) && !(value instanceof Integer)) {
                if (!value.getClass().equals(Double.TYPE) && !(value instanceof Double)) {
                    String s = value.toString();
                    if (PikachuStrings.isNull(s)) {
                        return defaultValue;
                    } else {
                        try {
                            return Short.valueOf(s, radix);
                        } catch (Exception e) {
                            return defaultValue;
                        }
                    }
                } else {
                    return ((Double) value).shortValue();
                }
            } else {
                return ((Integer) value).shortValue();
            }
        } else {
            return (Short) value;
        }
    }
    
    public static long toLong(Object value) {
        return toLong(value, 0L);
    }
    
    public static long toLong(Object value, long defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (!value.getClass().equals(Long.TYPE) && !(value instanceof Long)) {
            String s = value.toString();
            if (PikachuStrings.isNull(s)) {
                return defaultValue;
            } else {
                try {
                    return Double.valueOf(s).longValue();
                } catch (Exception var5) {
                    return defaultValue;
                }
            }
        } else {
            return (Long) value;
        }
    }
    
    public static float toFloat(Object value) {
        return toFloat(value, 0.0F);
    }
    
    public static float toFloat(Object value, float defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (!value.getClass().equals(Float.TYPE) && !(value instanceof Float)) {
            if (value instanceof Double) {
                return ((Double) value).floatValue();
            } else {
                String s = value.toString();
                if (PikachuStrings.isNull(s)) {
                    return defaultValue;
                } else {
                    try {
                        return Double.valueOf(s).floatValue();
                    } catch (Exception var4) {
                        return defaultValue;
                    }
                }
            }
        } else {
            return (Float) value;
        }
    }
    
    public static double toDouble(Object value) {
        return toDouble(value, 0.0D);
    }
    
    public static double toDouble(Object value, double defaultValue) {
        if (value == null) {
            return defaultValue;
        }
        if (!value.getClass().equals(Double.TYPE) && !(value instanceof Double)) {
            String s = value.toString();
            if (PikachuStrings.isNull(s)) {
                return defaultValue;
            } else {
                try {
                    return Double.valueOf(s);
                } catch (Exception var5) {
                    return defaultValue;
                }
            }
        } else {
            return (Double) value;
        }
    }
    
    public static BigDecimal toBigDecimal(Object value) {
        return toBigDecimal(value, (BigDecimal) null);
    }
    
    public static BigDecimal toBigDecimal(Object value, BigDecimal defaultValue) {
        return value == null ? defaultValue : new BigDecimal(value.toString());
    }
    
    public static Enum toEnum(Class<?> clazz, String enumString) {
        Class<Enum> enumClass = (Class<Enum>) clazz;
        return Enum.valueOf(enumClass, enumString);
    }
    
    public static Date toDate(Object o) {
        if (o == null) {
            return new Date();
        }
        if (o instanceof Date) {
            return (Date) o;
        } else if (o instanceof LocalDateTime) {
            LocalDateTime localDateTime = (LocalDateTime) o;
            Date date = new Date();
            /*
                - LocalDateTime使用正数，如：1100=1100
                - Date使用1900做为基准，1910:1910-1900=10，1100=1100-1900=-800
             */
            date.setYear(localDateTime.getYear() - 1900);
            date.setMonth(localDateTime.getMonthValue() - 1);
            date.setDate(localDateTime.getDayOfMonth());
            date.setHours(localDateTime.getHour());
            date.setMinutes(localDateTime.getMinute());
            date.setSeconds(localDateTime.getSecond());
            return date;
        } else if (!(o instanceof Long) && !(o instanceof Integer)) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
                ParsePosition position = new ParsePosition(0);
                Date date = sdf.parse(o.toString(), position);
                return date;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new Date((Long) o);
    }
    
    public static LocalDateTime toLocalDateTime(Object o) {
        if (o == null) {
            return LocalDateTime.now();
        }
        if (o instanceof LocalDateTime) {
            return (LocalDateTime) o;
        } else if (o instanceof Date) {
            Date date = (Date) o;
            if (date == null) {
                return null;
            }
            // 获取当前时区
            ZoneId zoneId = ZoneId.systemDefault();
            // 先转成LocalDateTime对象
            LocalDateTime local = date.toInstant().atZone(zoneId).toLocalDateTime();
            /*
             * 进行修正,不然会有时间错误。如：
             * date=1700-3-2 1:2:3.234 => localDateTime=1700-03-02T01:07:46
             * date=1100-3-2 1:2:3.234 => localDateTime=1100-03-09T01:07:46.234
             */
            if (date.getYear() != local.getYear()) {
                // Date的年是以1900为基准的，如：1910年为10，1700年为-200
                local = local.withYear(date.getYear() + 1900);
            }
            if (date.getMonth() + 1 != local.getMonthValue()) {
                local = local.withMonth(date.getMonth() + 1);
            }
            if (date.getDate() != local.getDayOfMonth()) {
                local = local.withDayOfMonth(date.getDate());
            }
            if (date.getHours() != local.getHour()) {
                local = local.withHour(date.getHours());
            }
            if (date.getMinutes() != local.getMinute()) {
                local = local.withMinute(date.getMinutes());
            }
            if (date.getSeconds() != local.getSecond()) {
                local = local.withSecond(date.getSeconds());
            }
            return local;
        } else if (!(o instanceof Long) && !(o instanceof Integer)) {
            try {
                TemporalAccessor parse = DateTimeFormatter.ISO_DATE_TIME.parse(o.toString());
                return LocalDateTime.from(parse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return toLocalDateTime(new Date((Long) o));
    }
    
    /**
     * 将两个数组进行复制整合
     *
     * @param first
     * @param second
     * @param <T>
     *
     * @return
     */
    public static <T> T[] concat(T[] first, T[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        } else {
            T[] ts = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, ts, first.length, second.length);
            return ts;
        }
        
    }
    
    public static int[] concat(int[] first, int[] second) {
        if (first == null) {
            return second;
        }
        if (second == null) {
            return first;
        } else {
            int[] ts = Arrays.copyOf(first, first.length + second.length);
            System.arraycopy(second, 0, ts, first.length, second.length);
            return ts;
        }
    }
    
    public static <T> T[] concatAll(T[] first, T[]... ts) {
        if (ts == null) {
            return first;
        }
        int firstLen = first == null ? 0 : first.length;
        int allLen = 0;
        allLen += firstLen;
        for (T[] t : ts) {
            if (t != null) {
                if (first == null) {
                    first = t;
                }
                allLen += t.length;
            }
        }
        if (allLen == 0) return null;
        T[] all = Arrays.copyOf(first, allLen);
        int L = first.length;
        for (T[] t : ts) {
            if (t != null && t.length > 0) {
                System.arraycopy(t, 0, all, L, t.length);
                L += t.length;
            }
            
        }
        return all;
    }
    
    public static <T> String toString(List<T> list) {
        return toString(list, ",");
    }
    
    public static <T> String toString(List<T> list, String split) {
        if (!PikachuArrays.isEmpty(list)) {
            StringBuilder sb = new StringBuilder();
            if (list != null) {
                for (int i = 0, L = list.size(); i < L; ++i) {
                    if (i > 0) {
                        sb.append(split);
                    }
                    sb.append(list.get(i));
                }
            }
            return sb.toString();
        } else {
            return "";
        }
    }
    
}
