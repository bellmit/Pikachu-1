package com.x.pikachu.framework.caching.datas.matchers;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @LocalDateTime：2020/1/14 18:06
 */
public enum LocalDateTimeComparer implements IComparer<LocalDateTime> {
    EQUALS("=") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            if (first == second) {
                return true;
            } else if (first != null && second != null) {
                return first.compareTo(second) == 0;
            }
            return false;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            return !EQUALS.compare(first, second);
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            if (first == second) {
                return false;
            } else if (first != null && second == null) {
                return true;
            } else if (first == null && second != null) {
                return false;
            } else {
                return first.compareTo(second) == 1;
            }
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            if (first == second) {
                return true;
            } else if (first != null && second == null) {
                return true;
            } else if (first == null && second != null) {
                return false;
            } else {
                return EQUALS.compare(first, second) || GREATER.compare(first, second);
            }
        }
    },
    LESS("<") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            if (first == second) {
                return false;
            } else if (first != null && second == null) {
                return false;
            } else if (first == null && second != null) {
                return true;
            } else {
                return first.compareTo(second)==-1;
            }
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(LocalDateTime first, LocalDateTime second) {
            if (first == second) {
                return true;
            } else if (first != null && second == null) {
                return false;
            } else if (first == null && second != null) {
                return true;
            } else {
                return EQUALS.compare(first, second) || LESS.compare(first, second);
            }
        }
    };

    private final String operator;

    private LocalDateTimeComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        LocalDateTimeComparer[] comparers = values();
        for (LocalDateTimeComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
