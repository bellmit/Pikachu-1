package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DateComparer implements IComparer<Date> {
    EQUALS("=") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue != null) {
                return compareValue.getTime() == conditionValue.getTime();
            }
            return false;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            return !EQUALS.compare(compareValue, conditionValue);
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else {
                return compareValue.getTime() > conditionValue.getTime();
            }
        }
    },
    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else {
                return compareValue.getTime() >= conditionValue.getTime();
            }
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else {
                return compareValue.getTime() < conditionValue.getTime();
            }
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else {
                return compareValue.getTime() <= conditionValue.getTime();
            }
        }
    };
    
    @Override
    public Date parseConditionValue(Class<Date> returnType, Object value) {
        return PikachuConverts.toDate(value);
    }
    
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
