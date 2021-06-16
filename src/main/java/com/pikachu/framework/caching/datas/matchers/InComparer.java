package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;
import com.pikachu.framework.database.core.SQLHelper;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Desc in操作符比较器
 * @Date 2021/6/11 00:43
 * @Author AD
 */
public class InComparer implements IComparer<Object> {
    
    private final List<Object> valueList;
    
    public InComparer(Object value) throws Exception {
        // 获取in操作符里的所有值，second的类型可以是list、set、array、"a,b,c"
        this.valueList = SQLHelper.getConditionValuesWithOperatorIsIn(value);
        if (valueList == null || valueList.size() == 0) {
            throw new Exception("the value can't be null with operator is in,ane the value class is " + value.getClass());
        }
    }
    
    @Override
    public Object parseConditionValue(Class<Object> returnType, Object value) {
        return SQLHelper.getConditionValuesWithOperatorIsIn(value);
    }
    
    @Override
    public boolean compare(Object compareValue, Object conditionValue) {
        Class<?> clazz = compareValue.getClass();
        if (clazz.equals(String.class)) {
            for (Object o : valueList) {
                if (compareValue.toString().equals(o.toString())) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Byte.class)) {
            byte f = (byte) compareValue;
            for (Object o : valueList) {
                byte s = Byte.parseByte(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Short.class)) {
            short f = (short) compareValue;
            for (Object o : valueList) {
                short s = Short.parseShort(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Integer.class)) {
            int f = (int) compareValue;
            for (Object o : valueList) {
                int s = Integer.parseInt(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Long.class)) {
            long f = (long) compareValue;
            for (Object o : valueList) {
                long s = Long.parseLong(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Float.class)) {
            float f = (float) compareValue;
            for (Object o : valueList) {
                float s = Float.parseFloat(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Double.class)) {
            double f = (double) compareValue;
            for (Object o : valueList) {
                double s = Double.parseDouble(o.toString());
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(Character.class)) {
            char f = (char) compareValue;
            for (Object o : valueList) {
                char s = o.toString().charAt(0);
                if (f == s) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (clazz.equals(BigDecimal.class)) {
            BigDecimal f = (BigDecimal) compareValue;
            for (Object o : valueList) {
                BigDecimal s = PikachuConverts.toBigDecimal(o);
                if (f.equals(s)) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        } else if (Enum.class.isAssignableFrom(clazz)) {
            Class<Enum> enumClass = (Class<Enum>) clazz;
            for (Object conditionValueObject : valueList) {
                Enum conditionValueEnum = Enum.valueOf(enumClass, conditionValueObject.toString());
                if (compareValue == conditionValueEnum) {
                    return true;
                } else {
                    continue;
                }
            }
            return false;
        }
        return false;
    }
    
}
