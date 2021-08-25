package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ShortComparer implements IComparer<Short> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue > conditionValue;
        }
    },
    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Short parseConditionValue(Class<Short> returnType, Object value) {
        return PikachuConverts.toShort(value);
    }
    
    private final Operator operator;
    
    private ShortComparer(Operator operator) {
        this.operator = operator;
    }
    
    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();
    
    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }
    
    static {
        ShortComparer[] comparers = values();
        for (ShortComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
