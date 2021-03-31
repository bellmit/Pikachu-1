package com.pikachu.framework.caching.datas;

import com.pikachu.common.util.PikachuStrings;
import com.pikachu.framework.caching.datas.matchers.*;
import com.pikachu.common.collection.KeyValue;
import com.pikachu.framework.caching.methods.MethodInfo;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/14 17:14
 */
public final class ValueSorter {
    
    private final Method method;
    private final IComparer asc;
    private final IComparer desc;
    
    public ValueSorter(Method get, IComparer asc, IComparer desc) {
        this.method = get;
        this.asc = asc;
        this.desc = desc;
    }
    
    public boolean matchAsc(Object o1, Object o2) {
        if (o1 == o2) {
            return false;
        } else if (o1 == null && o2 != null) {
            return true;
        } else if (o1 != null && o2 == null) {
            return false;
        } else {
            try {
                return asc.compare(method.invoke(o1), method.invoke(o2));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    public boolean matchDesc(Object o1, Object o2) {
        if (o1 == o2) {
            return false;
        } else if (o1 == null && o2 != null) {
            return false;
        } else if (o1 != null && o2 == null) {
            return true;
        } else {
            try {
                return desc.compare(method.invoke(o1), method.invoke(o2));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    /**
     * @param gets  get方法信息，prop=属性名，methodInfo=对应的方法信息
     * @param order key=prop,value=asc|desc
     *
     * @return
     */
    public static ValueSorter getValueSorter(Map<String, MethodInfo> gets, KeyValue order) {
        // 判断有效性
        if (order == null || PikachuStrings.isNull(order.getK())) {
            return null;
        }
        String prop = order.getK().toUpperCase();
        MethodInfo get = gets.get(prop);
        if (get == null) {
            return null;
        }
        // 判断是asc还是desc
        String v = order.getV() == null ? "" : order.getV().toString();
        // 默认asc（升序）
        boolean isAsc = PikachuStrings.isNull(v) || !"DESC".equals(v);
        
        // 获取返回值类型，判断出需要使用哪种类型的比较器
        Method method = get.getMethod();
        Class<?> type = method.getReturnType();
        IComparer asc = null;
        IComparer desc = null;
        
        // 根据方法的返回值类型，判断使用哪种类型比较器
        int code = ClassCode.getType(type);
        switch (code) {
            case ClassCode.BYTE:
                asc = isAsc ? ByteComparer.LESS : ByteComparer.GREATER;
                desc = isAsc ? ByteComparer.GREATER : ByteComparer.LESS;
                break;
            case ClassCode.SHORT:
                asc = isAsc ? ShortComparer.LESS : ShortComparer.GREATER;
                desc = isAsc ? ShortComparer.GREATER : ShortComparer.LESS;
                break;
            case ClassCode.INT:
                asc = isAsc ? IntComparer.LESS : IntComparer.GREATER;
                desc = isAsc ? IntComparer.GREATER : IntComparer.LESS;
                break;
            case ClassCode.LONG:
                asc = isAsc ? LongComparer.LESS : LongComparer.GREATER;
                desc = isAsc ? LongComparer.GREATER : LongComparer.LESS;
                break;
            case ClassCode.FLOAT:
                asc = isAsc ? FloatComparer.LESS : FloatComparer.GREATER;
                desc = isAsc ? FloatComparer.GREATER : FloatComparer.LESS;
                break;
            case ClassCode.DOUBLE:
                asc = isAsc ? DoubleComparer.LESS : DoubleComparer.GREATER;
                desc = isAsc ? DoubleComparer.GREATER : DoubleComparer.LESS;
                break;
            case ClassCode.BOOLEAN:
                asc = isAsc ? BooleanComparer.LESS : BooleanComparer.GREATER;
                desc = isAsc ? BooleanComparer.GREATER : BooleanComparer.LESS;
                break;
            case ClassCode.CHAR:
                asc = isAsc ? CharComparer.LESS : CharComparer.GREATER;
                desc = isAsc ? CharComparer.GREATER : CharComparer.LESS;
                break;
            case ClassCode.STRING:
                asc = isAsc ? StringComparer.LESS : StringComparer.GREATER;
                desc = isAsc ? StringComparer.GREATER : StringComparer.LESS;
                break;
            case ClassCode.DATE:
                asc = isAsc ? DateComparer.LESS : DateComparer.GREATER;
                desc = isAsc ? DateComparer.GREATER : DateComparer.LESS;
                break;
            case ClassCode.LOCAL_DATE_TIME:
                asc = isAsc ? LocalDateTimeComparer.LESS : LocalDateTimeComparer.GREATER;
                desc = isAsc ? LocalDateTimeComparer.GREATER : LocalDateTimeComparer.LESS;
                break;
            default:
                desc = isAsc ? StringComparer.GREATER : StringComparer.LESS;
                asc = isAsc ? StringComparer.LESS : StringComparer.GREATER;
                break;
        }
        return new ValueSorter(method, asc, desc);
    }
    
}
