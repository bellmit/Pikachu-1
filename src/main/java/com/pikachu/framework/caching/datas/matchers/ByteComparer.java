package com.pikachu.framework.caching.datas.matchers;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.PikachuConverts;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 15:31
 */
public enum ByteComparer implements IComparer<Byte> {
    EQUALS(Operator.EQUALS) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue.compareTo(conditionValue) == 0;
        }
    },
    NO_EQUALS(Operator.NO_EQUALS) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue.compareTo(conditionValue) != 0;
        }
    },
    GREATER(Operator.GREATER) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue > conditionValue;
        }
    },

    GREATER_EQUALS(Operator.GREATER_EQUALS) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue >= conditionValue;
        }
    },
    LESS(Operator.LESS) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue < conditionValue;
        }
    },
    LESS_EQUALS(Operator.LESS_EQUALS) {
        @Override
        public boolean compare(Byte compareValue, Byte conditionValue) {
            return compareValue <= conditionValue;
        }
    };
    @Override
    public Byte parseConditionValue(Class<Byte> returnType,Object value){
        return PikachuConverts.toByte(value);
    }

    private final Operator operator;

    private ByteComparer(Operator operator) {
        this.operator = operator;
    }

    private static final Map<Operator, IComparer> map = new ConcurrentHashMap<>();

    public static IComparer getComparer(Operator operator) {
        return map.get(operator);
    }

    static {
        ByteComparer[] comparers = values();
        for (ByteComparer comparer : comparers) {
            map.put(comparer.operator, comparer);
        }
    }
}
