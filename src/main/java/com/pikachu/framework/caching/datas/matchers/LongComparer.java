package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum LongComparer implements IComparer<Long> {
    EQUALS("=") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Long parseConditionValue(Class<Long> returnType, Object value) {
        return PikachuConverts.toLong(value);
    }

    private final String operator;

    private LongComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        LongComparer[] comparers = values();
        for (LongComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
