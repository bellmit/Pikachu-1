package com.pikachu.framework.caching.datas;

/**
 * @Desc
 * @Author AD
 * @Date：2020/1/14 16:32
 */
class HistoryData<T> {

    private final int key;

    private final T[] datas;

    private long accessTime;

    HistoryData(int key, T[] datas) {
        this.key = key;
        this.datas = datas;
        this.accessTime = System.currentTimeMillis();
    }

    public int getKey() {
        return key;
    }

    public T[] getDatas() {
        this.accessTime = System.currentTimeMillis();
        return datas;
    }

    public long getAccessTime() {
        return accessTime;
    }
}
