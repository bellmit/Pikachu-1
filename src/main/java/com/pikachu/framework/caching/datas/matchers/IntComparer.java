package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum IntComparer implements IComparer<Integer> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Integer compareValue, Integer conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Integer parseConditionValue(Class<Integer> returnType, Object value) {
        return PikachuConverts.toInt(value);
    }
    private final Operator operator;

    private IntComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        IntComparer[] comparers = values();
        for (IntComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
