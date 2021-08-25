package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum LongComparer implements IComparer<Long> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Long compareValue, Long conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Long parseConditionValue(Class<Long> returnType, Object value) {
        return PikachuConverts.toLong(value);
    }

    private final Operator operator;

    private LongComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        LongComparer[] comparers = values();
        for (LongComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
