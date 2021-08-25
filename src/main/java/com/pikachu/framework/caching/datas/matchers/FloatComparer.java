package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum FloatComparer implements IComparer<Float> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Float compareValue, Float conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    @Override
    public Float parseConditionValue(Class<Float> returnType, Object value) {
        return PikachuConverts.toFloat(value.toString());
    }
    private final Operator operator;

    private FloatComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        FloatComparer[] comparers = values();
        for (FloatComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
