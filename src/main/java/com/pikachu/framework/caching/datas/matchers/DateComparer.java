package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public enum DateComparer implements IComparer<Date> {
    EQUALS(Operator.EQUALS) {
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
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Date compareValue, Date conditionValue) {
            return !EQUALS.compare(compareValue, conditionValue);
        }
    },
    GREATER(Operator.GREATER) {
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
    GREATER_EQUALS(Operator.GREATER_EQUALS) {
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
    LESS(Operator.LESS) {
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
    LESS_EQUALS(Operator.LESS_EQUALS) {
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
    
    private final Operator operator;
    
    private DateComparer(Operator operator) {
        this.operator = operator;
    }
    
    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();
    
    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }
    
    static {
        DateComparer[] comparers = values();
        for (DateComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
