package com.x.pikachu.framework.caching.datas;

import com.x.pikachu.common.events.Event;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 14:34
 */
public class CacheEvent<T> extends Event {
    public static final String DATA_CHANGED = "dataChanged";

    public static final int ACTION_ADDED = 1;

    public static final int ACTION_REMOVED = 2;

    public static final int ACTION_UPDATED = 3;

    private final int action;

    private final T[] datas;

    public CacheEvent(String type, int action, T[] datas) {
        super(type);
        this.action = action;
        this.datas = datas;
    }

    public int getAction() {
        return this.action;
    }

    public T[] getDatas() {
        return this.datas;
    }
}
