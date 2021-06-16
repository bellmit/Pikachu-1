package com.pikachu.framework.caching.datas.matchers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum CharComparer implements IComparer<Character> {
    EQUALS("=") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Character compareValue, Character conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    
    @Override
    public Character parseConditionValue(Class<Character>returnType,Object value){
        return Character.valueOf(value.toString().charAt(0));
    }
    private final String operator;

    private CharComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        CharComparer[] comparers = values();
        for (CharComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
