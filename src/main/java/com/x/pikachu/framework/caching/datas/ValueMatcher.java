package com.x.pikachu.framework.caching.datas;

import com.x.pikachu.common.collection.Where;
import com.x.pikachu.common.util.PikachuConverts;
import com.x.pikachu.common.util.PikachuStrings;
import com.x.pikachu.framework.caching.datas.matchers.Compares;
import com.x.pikachu.framework.caching.datas.matchers.IComparer;
import com.x.pikachu.framework.caching.datas.matchers.LikeComparer;
import com.x.pikachu.framework.caching.methods.MethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了方法、比较器、条件值
 *
 * @Author：AD
 * @Date：2020/1/16 11:26
 */
public class ValueMatcher {
    
    private Method[] methods;
    private IComparer comparer;
    private Object conditionValue;
    
    public ValueMatcher(Method[] methods, IComparer comparer, Object conditionValue) {
        this.methods = methods;
        this.comparer = comparer;
        this.conditionValue = conditionValue;
    }
    
    public boolean match(Object o) throws Exception {
        if (methods.length > 1) {
            for (Method method : methods) {
                if (comparer.compare(method.invoke(o), conditionValue)) {
                    return true;
                }
            }
            return false;
        } else {
            return comparer.compare(methods[0].invoke(o), conditionValue);
        }
    }
    
    /**
     * @param gets
     * @param where 操作符、值只能有一个，字段属性可以有多个
     *
     * @return
     *
     * @throws Exception
     */
    public static ValueMatcher getValueMatcher(Map<String, MethodInfo> gets, Where where) throws Exception {
        // 获取操作符
        String operator = where.getO();
        if (PikachuStrings.isNotNull(operator)) {
            // 获取属性
            String[] props = where.getK().split("\\s*,\\s*");
            Method[] methods;
            if (props.length > 1) {
                List<Method> methodList = new ArrayList<>();
                // 根据属性获取对应的方法信息
                for (String prop : props) {
                    MethodInfo info = gets.get(prop);
                    if (info != null) {
                        methodList.add(info.getMethod());
                    }
                }
                if (methodList.size() == 0) {
                    return null;
                }
                methods = methodList.toArray(new Method[methodList.size()]);
            } else {
                methods = new Method[1];
                MethodInfo firstMethodInfo = gets.get(props[0]);
                if (firstMethodInfo == null) {
                    return null;
                }
                methods[0] = firstMethodInfo.getMethod();
            }
            // 获取where值
            Object value = where.getV();
            // 获取返回值类型
            Class<?> returnType = methods[0].getReturnType();
            IComparer comparer = null;
            if (operator.trim().toLowerCase().equals("like")) {
                if (value != null && value.toString().trim().length() != 0) {
                    // 获取值
                    String strValue = value.toString();
                    // 获取比较器
                    LikeComparer likeComparer = new LikeComparer(strValue);
                    // 返回值匹配器
                    return new ValueMatcher(methods, likeComparer, strValue);
                } else {
                    return null;
                }
            } else {
                operator = operator.trim();
                comparer = Compares.getComparer(returnType, operator);
                // IParser<?, Object> parser = Parsers.getParser(returnType);
                // value = parser.parse(value);
                value = parseValue(returnType, value);
                return new ValueMatcher(methods, comparer, value);
            }
        } else {
            return null;
        }
    }
    
    private static Object parseValue(Class<?> returnType, Object value) {
        int type = ClassCode.getType(returnType);
        switch (type) {
            case ClassCode.BYTE:
                return PikachuConverts.toByte(value);
            case ClassCode.SHORT:
                return PikachuConverts.toShort(value);
            case ClassCode.INT:
                return PikachuConverts.toInt(value);
            case ClassCode.LONG:
                return PikachuConverts.toLong(value);
            case ClassCode.FLOAT:
                return PikachuConverts.toFloat(value);
            case ClassCode.DOUBLE:
                return PikachuConverts.toDouble(value);
            case ClassCode.BOOLEAN:
                return PikachuConverts.toBoolean(value);
            case ClassCode.STRING:
                return String.valueOf(value);
            case ClassCode.DATE:
                try {
                    return PikachuConverts.toDate(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case ClassCode.LOCAL_DATE_TIME:
                try {
                    return PikachuConverts.toLocalDateTime(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            default:
                return value;
            
        }
    }
    
}
