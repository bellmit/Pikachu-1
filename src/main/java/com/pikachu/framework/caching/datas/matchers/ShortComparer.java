package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum ShortComparer implements IComparer<Short> {
    EQUALS("=") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Short compareValue, Short conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Short parseConditionValue(Class<Short> returnType, Object value) {
        return PikachuConverts.toShort(value);
    }

    private final String operator;

    private ShortComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        ShortComparer[] comparers = values();
        for (ShortComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
