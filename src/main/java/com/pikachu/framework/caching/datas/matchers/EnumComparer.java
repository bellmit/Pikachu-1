package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc
 * @Date 2021/6/13 14:31
 * @Author AD
 */
public enum EnumComparer implements IComparer<Enum> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Enum compareValue, Enum conditionValue) {
            return compareValue == conditionValue;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Enum compareValue, Enum conditionValue) {
            return compareValue != conditionValue;
        }
    };
    
    @Override
    public boolean compare(Enum compareValue, Enum conditionValue) {
        return compareValue == conditionValue;
    }
    
    @Override
    public Enum parseConditionValue(Class<Enum> returnType, Object value) {
        return PikachuConverts.toEnum(returnType, value.toString());
    }
    
    private final Operator operator;
    
    private EnumComparer(Operator operator) {
        this.operator = operator;
    }
    
    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();
    
    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }
    
    static {
        EnumComparer[] comparers = values();
        for (EnumComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
