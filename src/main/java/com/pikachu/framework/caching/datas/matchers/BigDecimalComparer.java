package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum BigDecimalComparer implements IComparer<BigDecimal> {
    EQUALS("=") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) > 0;
        }
    },
    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) >= 0;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) < 0;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(BigDecimal compareValue, BigDecimal conditionValue) {
            return compareValue.compareTo(conditionValue) <= 0;
        }
    };
    
    @Override
    public BigDecimal parseConditionValue(Class<BigDecimal> returnType,Object value){
        return PikachuConverts.toBigDecimal(value);
    }
    private final String operator;
    
    private BigDecimalComparer(String operator) {
        this.operator = operator;
    }
    
    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();
    
    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }
    
    static {
        BigDecimalComparer[] comparers = values();
        for (BigDecimalComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
