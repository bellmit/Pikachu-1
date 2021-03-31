package com.x.pikachu.framework.caching.datas;



import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/15 11:18
 */
class HistoryCache<T> {

    private static final int CORE_COUNT = 100;

    private static final int MAX_COUNT = 300;

    private final Object lock = new Object();

    private final Map<Integer, HistoryData<T>> map = new ConcurrentHashMap<>();

    HistoryCache() {}

    int size() {
        return map.size();
    }

    void clear() {
        map.clear();
    }

    T[] get(int key) {
        HistoryData<T> history = map.get(key);
        return history == null ? null : history.getDatas();
    }

    void put(int key, T[] datas) {
        if (!map.containsKey(key)) {
            HistoryData<T> history = new HistoryData<>(key, datas);
            synchronized (lock) {
                map.put(key, history);
            }
            if (map.size() > MAX_COUNT) {
                clearUp();
            }
        }
    }

    private synchronized void clearUp() {
        if (map.size() >= MAX_COUNT) {
            HistoryData[] datas;
            synchronized (lock) {
                datas = (HistoryData[]) Array.newInstance(HistoryData.class,
                                                          map.size());
                datas = map.values().toArray(datas);
            }
            Arrays.sort(datas, new Comparator<HistoryData>() {
                @Override
                public int compare(HistoryData o1, HistoryData o2) {
                    long time1 = o1.getAccessTime();
                    long time2 = o2.getAccessTime();
                    if (time1 == time2) {
                        return 0;
                    }
                    // 访问时间早的在前面，升序排列
                    return time1 < time2 ? -1 : 1;
                }
            });
            int clearLen = datas.length - CORE_COUNT;
            List<Integer> keys = new ArrayList<>();
            for (int i = 0; i < clearLen; ++i) {
                HistoryData data = datas[i];
                keys.add(data.getKey());
            }
            // 主要是为了加锁才将key提取到List
            synchronized (lock) {
                Iterator<Integer> it = keys.iterator();
                while (it.hasNext()) {
                    Integer key = it.next();
                    map.remove(key);
                }
            }

        }
    }
}
