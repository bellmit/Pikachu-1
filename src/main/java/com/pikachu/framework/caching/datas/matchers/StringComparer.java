package com.pikachu.framework.caching.datas.matchers;

import java.text.Collator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum StringComparer implements IComparer<String> {
    EQUALS("=") {
        @Override
        public boolean compare(String first, String second) {
            if (first == second) {
                return true;
            } else if (first != null && second != null) {
                return first.equals(second);
            }
            return false;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(String first, String second) {
            return !EQUALS.compare(first, second);
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(String first, String second) {
            if (first == second) {
                return false;
            } else if (first == null && second != null) {
                return false;
            } else if (first != null && second == null) {
                return true;
            } else {
                return LOCAL.compare(first, second) > 0;
            }
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(String first, String second) {
            if (first == second) {
                return true;
            } else if (first == null && second != null) {
                return false;
            } else if (first != null && second == null) {
                return true;
            } else {
                return LOCAL.compare(first, second) >= 0;
            }
        }
    },
    LESS("<") {
        @Override
        public boolean compare(String first, String second) {
            if (first == second) {
                return false;
            } else if (first == null && second != null) {
                return true;
            } else if (first != null && second == null) {
                return false;
            } else {
                return LOCAL.compare(first, second) < 0;
            }
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(String first, String second) {
            if (first == second) {
                return true;
            } else if (first == null && second != null) {
                return true;
            } else if (first != null && second == null) {
                return false;
            } else {
                return LOCAL.compare(first, second) <= 0;
            }
        }
    };

    private static final Collator LOCAL = Collator.getInstance();

    private final String operator;

    private StringComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        StringComparer[] comparers = values();
        for (StringComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
