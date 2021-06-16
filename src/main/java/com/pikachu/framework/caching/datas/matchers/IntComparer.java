package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum IntComparer implements IComparer<Integer> {
    EQUALS("=") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(">=") {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Integer parseConditionValue(Class<Integer> returnType, Object value) {
        return PikachuConverts.toInt(value);
    }
    private final String operator;

    private IntComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        IntComparer[] comparers = values();
        for (IntComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
