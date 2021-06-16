package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum FloatComparer implements IComparer<Float> {
    EQUALS("=") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    @Override
    public Float parseConditionValue(Class<Float> returnType, Object value) {
        return PikachuConverts.toFloat(value.toString());
    }
    private final String operator;

    private FloatComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        FloatComparer[] comparers = values();
        for (FloatComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
