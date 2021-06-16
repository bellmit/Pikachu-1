package com.pikachu.framework.database;

import com.pikachu.common.events.IListener;
import com.pikachu.common.util.PikachuStrings;
import com.pikachu.framework.caching.datas.CacheData;
import com.pikachu.framework.caching.datas.CacheManager;
import com.pikachu.framework.database.core.SQLHelper;
import com.pikachu.framework.database.core.SQLInfo;
import com.pikachu.common.collection.KeyValue;
import com.pikachu.common.collection.Where;

/**
 * @Desc 缓存dao，能使用缓存的，bean肯定有主键
 * @Author AD
 * @Date 2020/1/19 17:31
 */
public class CacheDao<T> implements IDao<T> {
    
    private Class<T> dataClass;
    private SQLInfo<T> sqlInfo;
    private Dao<T> dao;
    
    CacheDao(String name, SQLInfo<T> sqlInfo) throws Exception {
        this.dataClass = sqlInfo.getDataClass();
        this.sqlInfo = sqlInfo;
        // 非缓存的dao，缓存里没有数据时，直接访问数据库获取
        this.dao = PikachuStrings.isNotNull(name) ? new Dao<>(name, sqlInfo) : null;
        CacheManager.createCache(dataClass, sqlInfo.getPrimaryKeys(),
                sqlInfo.isCachingHistory(), sqlInfo.getType());
        CacheData<T> cacheData = CacheManager.lock(this.dataClass);
        
        try {
            // 从数据库中获取所有数据存入缓存，bean肯定有主键
            CacheManager.putAll(cacheData, dao.getList(null, null), false);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    // CacheDao(IProtocol protocol, SQLInfo<T> sqlInfo) throws Exception {
    //     this.dataClass = sqlInfo.getDataClass();
    //     this.sqlInfo = sqlInfo;
    //     this.dao = protocol != null ? new Dao<>(protocol, sqlInfo) : null;
    //     CacheManager.createCache(dataClass, sqlInfo.getPrimaryKeys(),
    //             sqlInfo.isCachingHistory(), sqlInfo.getType());
    //     CacheData<T> cacheData = CacheManager.lock(this.dataClass);
    //
    //     try {
    //         CacheManager.putAll(cacheData, dao.getList(null, null), false);
    //     } finally {
    //         CacheManager.unlock(cacheData);
    //     }
    // }
    
    @Override
    public String[] getPrimaryKeys() {
        return sqlInfo.getPrimaryKeys();
    }
    
    @Override
    public Object[] getPrimaryKeysValue(T t) {
        return sqlInfo.getPrimaryKeysValue(t);
    }
    
    @Override
    public String getTableName() {
        return sqlInfo.getTableName();
    }
    
    @Override
    public void refreshCache() {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        try {
            CacheManager.clear(cacheData);
            CacheManager.putAll(cacheData, this.dao.getList(null, null), false);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    @Override
    public void addCacheListener(String type, IListener listener) {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        try {
            cacheData.addListener(type, listener);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    @Override
    public void addCacheListener(String type, IListener listener, int cacheEventAction) {
        CacheData<T> cache = CacheManager.lock(dataClass);
        
        try {
            cache.addListener(type, listener, cacheEventAction);
        } finally {
            CacheManager.unlock(cache);
        }
        
    }
    
    @Override
    public boolean hasCacheListener(String type) {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        boolean contain;
        try {
            contain = cacheData.hasListener(type);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return contain;
    }
    
    @Override
    public boolean hasCacheListener(String type, IListener listener) {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        boolean contain;
        try {
            contain = cacheData.hasListener(type, listener);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return contain;
    }
    
    @Override
    public void removeCacheListener(String type) {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        try {
            cacheData.removeListener(type);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    @Override
    public void removeAllCacheListeners() {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        try {
            cacheData.removeAllListeners();
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    @Override
    public void removeCacheListener(String type, IListener listener) {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        try {
            cacheData.removeListener(type, listener);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
    }
    
    @Override
    public T add(T bean) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        T t;
        try {
            t = dao.add(bean);
            CacheManager.put(cacheData, t);
        } finally {
            CacheManager.unlock(cacheData);
        }
        return t;
    }
    
    @Override
    public T[] addAll(T[] beans) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        T[] results;
        try {
            results = dao.addAll(beans);
            CacheManager.putAll(cacheData, results, true);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return results;
    }
    
    @Override
    public T put(T bean) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        T t;
        try {
            // 先存入数据库
            t = dao.put(bean);
            // 再存入缓存
            CacheManager.put(cacheData, t);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return t;
    }
    
    @Override
    public T[] putAll(T[] beans) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(dataClass);
        
        T[] results;
        try {
            // 先存入数据库
            results = dao.putAll(beans);
            // 再存入缓存
            CacheManager.putAll(cacheData, results, true);
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return results;
    }
    
    @Override
    public int delete(Where[] wheres) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(this.dataClass);
        
        int count = -1;
        try {
            count = this.dao.delete(wheres);
            if (count != -1) {
                CacheManager.remove(cacheData, wheres);
            }
            
        } finally {
            CacheManager.unlock(cacheData);
        }
        return count;
    }
    
    @Override
    public int edit(T bean) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(this.dataClass);
        
        int count = -1;
        try {
            count = this.dao.edit(bean);
            if (count != -1) {
                CacheManager.put(cacheData, bean);
            }
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return count;
    }
    
    @Override
    public T getBean(Where[] wheres) throws Exception {
        return CacheManager.getBean(this.dataClass, wheres);
    }
    
    @Override
    public T getByPrimary(Object... pks) throws Exception {
        return CacheManager.getByPrimary(this.dataClass, pks);
    }
    
    @Override
    public boolean contains(Where[] wheres) throws Exception {
        return CacheManager.contains(this.dataClass, wheres);
    }
    
    @Override
    public int getCount(Where[] wheres) throws Exception {
        return CacheManager.getCount(this.dataClass, wheres);
    }
    
    @Override
    public T[] getList(Where[] wheres, KeyValue[] orders) throws Exception {
        return CacheManager.getList(this.dataClass, wheres, orders);
    }
    
    @Override
    public T[] getPage(int page, int pageSize, Where[] wheres, KeyValue[] orders) throws Exception {
        return CacheManager.getPage(this.dataClass, page, pageSize, wheres, orders);
    }
    
    @Override
    public int update(KeyValue[] updates, Where[] wheres) throws Exception {
        CacheData<T> cacheData = CacheManager.lock(this.dataClass);
        
        int count = -1;
        try {
            count = this.dao.update(updates, wheres);
            if (count != -1) {
                CacheManager.update(cacheData, updates, wheres);
            }
            
        } finally {
            CacheManager.unlock(cacheData);
        }
        
        return count;
    }
    
    @Override
    public T add(String[] columns, Object[] values) throws Exception {
        T data = this.sqlInfo.createBean(columns, values);
        return data == null ? null : this.add(data);
    }
    
    public int delete(String[] columns, Object[] values) throws Exception {
        return this.delete(SQLHelper.getWheres(columns, values));
    }
    
    @Override
    public T getBean(String[] columns, Object[] values) throws Exception {
        return this.getBean(SQLHelper.getWheres(columns, values));
    }
    
    @Override
    public int getCount(String[] columns, Object[] values) throws Exception {
        return this.getCount(SQLHelper.getWheres(columns, values));
    }
    
    @Override
    public boolean contains(String[] columns, Object[] values) throws Exception {
        return CacheManager.contains(this.dataClass, SQLHelper.getWheres(columns, values));
    }
    
    @Override
    public T[] getList(String[] columns, Object[] values, KeyValue[] orders) throws Exception {
        return this.getList(SQLHelper.getWheres(columns, values), orders);
    }
    
    @Override
    public T[] getPage(int page, int pageSize, String[] columns, Object[] values, KeyValue[] orders) throws Exception {
        return this.getPage(page, pageSize, SQLHelper.getWheres(columns, values), orders);
    }
    
    @Override
    public int update(String[] updateColumns, Object[] updateValues, String[] whereColumns, Object[] whereValues)
            throws Exception {
        return this.update(SQLHelper.getUpdates(updateColumns, updateValues),
                SQLHelper.getWheres(whereColumns, whereValues));
    }
    
}
