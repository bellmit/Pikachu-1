package com.x.pikachu.framework.caching.datas.matchers;

public interface IComparer<T> {
    boolean compare(T first, T second);
}
