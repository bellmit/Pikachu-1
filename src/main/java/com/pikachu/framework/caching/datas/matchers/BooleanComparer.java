package com.pikachu.framework.caching.datas.matchers;


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
        public boolean compare(Boolean first, Boolean second) {
            return first.compareTo(second) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Boolean first, Boolean second) {
            return first.compareTo(second) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Boolean first, Boolean second) {
            return first && !second;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Boolean first, Boolean second) {
            boolean a = first;
            boolean b = second;
            return a == b || a && !b;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Boolean first, Boolean second) {
            return !first && second;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Boolean first, Boolean second) {
            boolean a = first;
            boolean b = second;
            return a == b || !a && b;
        }
    };

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
