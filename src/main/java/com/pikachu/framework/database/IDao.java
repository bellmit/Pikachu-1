package com.pikachu.framework.database;

import com.pikachu.common.events.IListener;
import com.pikachu.framework.database.core.Order;
import com.pikachu.framework.database.core.Update;
import com.pikachu.framework.database.core.Where;

/**
 * @Desc
 * @Date 2019-12-05 21:53
 * @Author AD
 */
public interface IDao<T> {

    String[] getPrimaryKeys();
    
    Object[] getPrimaryKeysValue(T t);
    
    String getTableName();

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

    /**
     * 获取列表，当wheres为空时表示获取全部
     *
     * @param wheres
     * @param orders
     * @return
     * @throws Exception
     */
    T[] getList(Where[] wheres, Order[] orders) throws Exception;

    T[] getList(String[] columns, Object[] values, Order[] orders) throws Exception;

    T[] getPage(int page, int pageSize, Where[] wheres, Order[] orders) throws Exception;

    T[] getPage(int page, int pageSize, String[] columns, Object[] values, Order[] orders) throws Exception;

    int update(Update[] updates, Where[] wheres) throws Exception;

    int update(String[] updateColumns, Object[] updateValues, String[] whereColumns, Object[] whereValues) throws Exception;

}
