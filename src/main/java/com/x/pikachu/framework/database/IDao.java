package com.x.pikachu.framework.database;

import com.x.pikachu.common.collection.KeyValue;
import com.x.pikachu.common.collection.Where;
import com.x.pikachu.common.events.IListener;

/**
 * @Desc
 * @Date 2019-12-05 21:53
 * @Author AD
 */
public interface IDao<T> {
    String[] getPrimaryKeys();

    void refreshCache();

    void addCacheListener(String type, IListener listener);

    void addCacheListener(String type, IListener listener, int action);

    boolean hasCacheListener(String type);

    void removeCacheListener(String type);

    boolean hasCacheListener(String type, IListener listener);

    void removeAllCacheListeners();

    void removeCacheListener(String type, IListener listener);

    T add(T bean) throws Exception;

    T add(String[] columns, Object[] values) throws Exception;

    T[] addAll(T[] beans) throws Exception;

    T put(T bean) throws Exception;

    T[] putAll(T[] beans) throws Exception;

    int delete(Where[] wheres) throws Exception;

    int delete(String[] columns, Object[] values) throws Exception;

    int edit(T bean) throws Exception;

    T getBean(Where[] wheres) throws Exception;

    T getBean(String[] columns, Object[] values) throws Exception;

    T getByPrimary(Object... pks) throws Exception;

    boolean contains(Where[] wheres) throws Exception;

    boolean contains(String[] columns, Object[] values) throws Exception;

    int getCount(Where[] wheres) throws Exception;

    int getCount(String[] columns, Object[] values) throws Exception;

    T[] getList(Where[] wheres, KeyValue[] orders) throws Exception;

    T[] getList(String[] columns, Object[] values, KeyValue[] orders) throws Exception;

    T[] getPage(int page, int pageSize, Where[] wheres, KeyValue[] orders) throws Exception;

    T[] getPage(int page, int pageSize, String[] columns, Object[] values, KeyValue[] orders) throws Exception;

    int update(KeyValue[] updates, Where[] wheres) throws Exception;

    int update(String[] updateColumns, Object[] updateValues, String[] whereColumns, Object[] whereValues) throws Exception;
}
