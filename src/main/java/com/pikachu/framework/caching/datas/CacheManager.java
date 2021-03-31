package com.pikachu.framework.caching.datas;

import com.pikachu.common.collection.KeyValue;
import com.pikachu.common.collection.Where;
import com.pikachu.common.database.core.DatabaseType;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/19 15:25
 */
public final class CacheManager {
    
    private static final Map<Class<?>, CacheData<?>> cacheMap = new HashMap<>();
    private static final Object cacheLock = new Object();
    
    private CacheManager() {
    }
    
    /**
     * 创建缓存
     *
     * @param dataClass    缓存类
     * @param pks          主键值
     * @param cacheHistory 是否缓存历史数据
     * @param type         数据库类型
     */
    public static <T> void createCache(Class<T> dataClass, String[] pks, boolean cacheHistory, DatabaseType type) {
        if (!cacheMap.containsKey(dataClass)) {
            synchronized (cacheLock) {
                if (!cacheMap.containsKey(dataClass)) {
                    cacheMap.put(dataClass, new CacheData<>(dataClass, pks, cacheHistory, type));
                }
            }
        }
    }
    
    public static <T> boolean contains(Class<T> dataClass) {
        return dataClass != null && cacheMap.containsKey(dataClass);
    }
    
    /**
     * 清除所有缓存
     */
    public static void clear() {
        synchronized (cacheLock) {
            CacheData[] cacheDatas = cacheMap.values().toArray(new CacheData[0]);
            int L = cacheDatas.length;
            
            for (CacheData data : cacheDatas) {
                data.lock();
                data.clear();
                data.unlock();
            }
            
        }
    }
    
    /**
     * 清除对象缓存
     *
     * @param dataClass 数据类
     */
    public static void clear(Class<?> dataClass) {
        if (dataClass != null) {
            CacheData data = cacheMap.get(dataClass);
            if (data != null) {
                data.lock();
                data.clear();
                data.unlock();
            }
            
        }
    }
    
    public static void clear(CacheData<?> cacheData) {
        if (cacheData != null) {
            cacheData.clear();
        }
        
    }
    
    /**
     * 清除历史缓存
     *
     * @param dataClass bean类
     */
    public static void clearHistory(Class<?> dataClass) {
        if (dataClass != null) {
            CacheData data = cacheMap.get(dataClass);
            if (data != null) {
                data.lock();
                data.clearHistory();
                data.unlock();
            }
            
        }
    }
    
    public static void clearHistory(CacheData<?> cacheData) {
        if (cacheData != null) {
            cacheData.clearHistory();
        }
        
    }
    
    /**
     * 锁住某个类缓存信息，并返回改缓存数据对象
     *
     * @param dataClass bean类
     *
     * @return 该bean类缓存信息
     */
    public static <T> CacheData<T> lock(Class<T> dataClass) {
        if (dataClass == null) {
            return null;
        } else {
            CacheData<T> data = (CacheData<T>) cacheMap.get(dataClass);
            if (data != null) {
                data.lock();
            }
            
            return data;
        }
    }
    
    public static <T> void unlock(CacheData<T> cacheData) {
        if (cacheData != null) {
            cacheData.unlock();
        }
        
    }
    
    public static <T> void put(CacheData<T> cache, T data) throws Exception {
        if (cache != null && data != null) {
            cache.put(data);
        }
    }
    
    public static <T> void putAll(CacheData<T> cache, T[] datas, boolean isCache) throws Exception {
        if (cache != null && datas != null && datas.length > 0) {
            if (isCache || !cache.hasData()) {
                cache.putAll(datas);
            }
        }
    }
    
    public static <T> void remove(CacheData<T> cache, Where[] wheres) throws Exception {
        if (cache != null) {
            cache.remove(wheres);
        }
        
    }
    
    public static <T> T getBean(Class<T> dataClass, Where[] wheres) throws Exception {
        if (dataClass == null) {
            return null;
        } else {
            CacheData cache = cacheMap.get(dataClass);
            return cache == null ? null : (T) cache.getOne(wheres);
        }
    }
    
    public static <T> T getByPrimary(Class<T> dataClass, Object[] pks) {
        if (dataClass != null && pks != null && pks.length != 0) {
            CacheData cache = cacheMap.get(dataClass);
            return cache == null ? null : (T) cache.getByPrimary(pks);
        } else {
            return null;
        }
    }
    
    public static <T> boolean contains(Class<T> dataClass, Where[] wheres) throws Exception {
        if (dataClass == null) {
            return false;
        } else {
            CacheData cache = cacheMap.get(dataClass);
            return cache != null && cache.contains(wheres);
        }
    }
    
    public static <T> int getCount(Class<T> dataClass, Where[] wheres) throws Exception {
        if (dataClass == null) {
            return -1;
        } else {
            CacheData cache = cacheMap.get(dataClass);
            return cache == null ? -1 : cache.getCount(wheres);
        }
    }
    
    public static <T> T[] getList(Class<T> dataClass, Where[] wheres, KeyValue[] orders) throws Exception {
        if (dataClass == null) {
            return null;
        } else {
            CacheData cache = cacheMap.get(dataClass);
            return cache == null ? null : (T[]) cache.getList(wheres, orders);
        }
    }
    
    public static <T> T[] getPage(Class<T> dataClass, int page, int pageSize, Where[] wheres, KeyValue[] orders)
            throws Exception {
        if (dataClass == null) {
            return null;
        } else {
            CacheData cache = cacheMap.get(dataClass);
            return cache == null ? null : (T[]) cache.getPage(page, pageSize, wheres, orders);
        }
    }
    
    public static <T> void update(CacheData<T> cache, KeyValue[] updates, Where[] wheres) throws Exception {
        if (cache != null && updates != null && updates.length != 0) {
            cache.update(updates, wheres);
        }
    }
    
}
