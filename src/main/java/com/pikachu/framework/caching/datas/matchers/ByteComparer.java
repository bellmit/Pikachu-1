package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 15:31
 */
public enum ByteComparer implements IComparer<Byte> {
    EQUALS("=") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS("<>") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(">") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(">=") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS("<") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS("<=") {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    @Override
    public Byte parseConditionValue(Class<Byte> returnType,Object value){
        return PikachuConverts.toByte(value);
    }

    private final String operator;

    private ByteComparer(String operator) {
        this.operator = operator;
    }

    private static final Map<String, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(String operator) {
        return map.get(operator);
    }

    static {
        ByteComparer[] comparers = values();
        for (ByteComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
