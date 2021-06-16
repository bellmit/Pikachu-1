package com.pikachu.framework.caching.datas.matchers;


import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 14:53
 */
public enum BooleanComparer implements IComparer<Boolean> {
    EQUALS("=") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            return compareValue && !conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            boolean a = compareValue;
            boolean b = conditionValue;
            return a == b || a && !b;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            return !compareValue && conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Boolean compareValue, Boolean conditionValue) {
            boolean a = compareValue;
            boolean b = conditionValue;
            return a == b || !a && b;
        }
    };
    
    @Override
    public Boolean parseConditionValue(Class<Boolean> returnType,Object value){
        return PikachuConverts.toBoolean(value);
    }

    private final String operator;

    private BooleanComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        BooleanComparer[] comparers = values();
        for (BooleanComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
