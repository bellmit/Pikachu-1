package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.framework.caching.datas.ClassCode;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/16 12:17
 */
public final class ComparerManager {
    
    public static IComparer getComparer(Class<?> clazz, String operator) {
        int type = ClassCode.getType(clazz);
        switch (type) {
            case ClassCode.BYTE:
                return ByteComparer.getComparer(operator);
            case ClassCode.SHORT:
                return ShortComparer.getComparer(operator);
            case ClassCode.INT:
                return IntComparer.getComparer(operator);
            case ClassCode.LONG:
                return LongComparer.getComparer(operator);
            case ClassCode.FLOAT:
                return FloatComparer.getComparer(operator);
            case ClassCode.DOUBLE:
                return DoubleComparer.getComparer(operator);
            case ClassCode.BOOLEAN:
                return BooleanComparer.getComparer(operator);
            case ClassCode.CHAR:
                return CharComparer.getComparer(operator);
            case ClassCode.STRING:
                return StringComparer.getComparer(operator);
            case ClassCode.DATE:
                return DateComparer.getComparer(operator);
            case ClassCode.LOCAL_DATE_TIME:
                return LocalDateTimeComparer.getComparer(operator);
            case ClassCode.BIG_DECIMAL:
                return BigDecimalComparer.getComparer(operator);
            case ClassCode.ENUM:
                return EnumComparer.getComparer(operator);
            default:
                return StringComparer.getComparer(operator);
        }
    }
    
    public static Object parseValue(Class<?> clazz, Object converted) {
        IComparer comparer = getComparer(clazz, "=");
        if (comparer == null) {
            return converted;
        }
        return comparer.parseConditionValue(clazz, converted);
    }
    
}
