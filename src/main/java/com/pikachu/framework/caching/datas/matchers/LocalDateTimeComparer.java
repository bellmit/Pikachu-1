package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @LocalDateTime：2020/1/14 18:06
 */
public enum LocalDateTimeComparer implements IComparer<LocalDateTime> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue != null) {
                return compareValue.compareTo(conditionValue) == 0;
            }
            return false;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            return !EQUALS.compare(compareValue, conditionValue);
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else {
                return compareValue.compareTo(conditionValue) == 1;
            }
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return true;
            } else if (compareValue == null && conditionValue != null) {
                return false;
            } else {
                return EQUALS.compare(compareValue, conditionValue) || GREATER.compare(compareValue, conditionValue);
            }
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            if (compareValue == conditionValue) {
                return false;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else {
                return compareValue.compareTo(conditionValue) == -1;
            }
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(LocalDateTime compareValue, LocalDateTime conditionValue) {
            if (compareValue == conditionValue) {
                return true;
            } else if (compareValue != null && conditionValue == null) {
                return false;
            } else if (compareValue == null && conditionValue != null) {
                return true;
            } else {
                return EQUALS.compare(compareValue, conditionValue) || LESS.compare(compareValue, conditionValue);
            }
        }
    };
    
    @Override
    public LocalDateTime parseConditionValue(Class<LocalDateTime> returnType, Object value) {
        return PikachuConverts.toLocalDateTime(value);
    }

    private final Operator operator;

    private LocalDateTimeComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        LocalDateTimeComparer[] comparers = values();
        for (LocalDateTimeComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
