package com.pikachu.framework.caching.datas.matchers;

public interface IComparer<T> {
    
    boolean compare(T compareValue, T conditionValue);
    
    T parseConditionValue(Class<T> returnType, Object value);
    
}
