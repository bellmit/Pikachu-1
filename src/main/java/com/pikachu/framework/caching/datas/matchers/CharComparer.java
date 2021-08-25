package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CharComparer implements IComparer<Character> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Character parseConditionValue(Class<Character>returnType,Object value){
        return Character.valueOf(value.toString().charAt(0));
    }
    private final Operator operator;

    private CharComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        CharComparer[] comparers = values();
        for (CharComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
