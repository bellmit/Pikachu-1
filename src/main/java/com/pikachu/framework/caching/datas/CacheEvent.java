package com.pikachu.framework.caching.datas;

import com.pikachu.common.events.Event;

/**
 * @Desc： 缓存事件
 * @Author：AD
 * @Date：2020/1/13 14:34
 */
public class CacheEvent<T> extends Event {

    /**
     * 改变
     */
    public static final String DATA_CHANGED = "dataChanged";

    /**
     * 增加
     */
    public static final int ACTION_ADDED = 1;

    /**
     * 移除
     */
    public static final int ACTION_REMOVED = 2;

    /**
     * 更新
     */
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
