package com.pikachu.framework.caching.datas.matchers;

import java.text.Collator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum StringComparer implements IComparer<String> {
    EQUALS("=") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue != null) {
                return compareValue.equals(conditionValue);
            }
            return false;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            return !EQUALS.compare(compareValue, conditionValue);
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else {
                return LOCAL.compare(compareValue, conditionValue) > 0;
            }
        }
    },
    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else {
                return LOCAL.compare(compareValue, conditionValue) >= 0;
            }
        }
    },
    LESS("<") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else {
                return LOCAL.compare(compareValue, conditionValue) < 0;
            }
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(String compareValue, String conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else {
                return LOCAL.compare(compareValue, conditionValue) <= 0;
            }
        }
    };
    
    @Override
    public String parseConditionValue(Class<String> returnType, Object value) {
        return value.toString();
    }
    
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
