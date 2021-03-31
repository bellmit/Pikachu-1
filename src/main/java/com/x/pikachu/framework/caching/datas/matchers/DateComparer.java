package com.x.pikachu.framework.caching.datas.matchers;


import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DateComparer implements IComparer<Date> {
    EQUALS("=") {
        @Override
        public boolean compare(Date first, Date second) {
            if (first == second) {
                return true;
            } else if (first != null && second != null) {
                return first.getTime() == second.getTime();
            }
            return false;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Date first, Date second) {
            return !EQUALS.compare(first, second);
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Date first, Date second) {
            if (first == second) {
                return false;
            } else if (first != null && second == null) {
                return true;
            } else if (first == null && second != null) {
                return false;
            } else {
                return first.getTime() > second.getTime();
            }
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Date first, Date second) {
            if (first == second) {
                return true;
            } else if (first != null && second == null) {
                return true;
            } else if (first == null && second != null) {
                return false;
            } else {
                return first.getTime() >= second.getTime();
            }
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Date first, Date second) {
            if (first == second) {
                return false;
            } else if (first != null && second == null) {
                return false;
            } else if (first == null && second != null) {
                return true;
            } else {
                return first.getTime() < second.getTime();
            }
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Date first, Date second) {
            if (first == second) {
                return true;
            } else if (first != null && second == null) {
                return false;
            } else if (first == null && second != null) {
                return true;
            } else {
                return first.getTime() <= second.getTime();
            }
        }
    };


    private final String operator;

    private DateComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        DateComparer[] comparers = values();
        for (DateComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
