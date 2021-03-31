package com.pikachu.framework.database;

import com.pikachu.common.events.IListener;
import com.pikachu.framework.database.core.SQLHelper;
import com.pikachu.framework.database.core.SQLInfo;
import com.pikachu.framework.database.core.SQLParams;
import com.pikachu.framework.database.reader.DaoBeanReader;
import com.pikachu.framework.database.reader.DaoCountReader;
import com.pikachu.framework.database.reader.DaoListReader;
import com.pikachu.framework.database.reader.DaoPageReader;
import com.pikachu.common.collection.KeyValue;
import com.pikachu.common.collection.Where;
import com.pikachu.common.database.DatabaseAccess;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/19 15:59
 */
public class Dao<T> extends DatabaseAccess implements IDao<T> {

    // private IProtocol protocol;

    private SQLInfo<T> sqlInfo;

    Dao(String name, SQLInfo<T> sqlInfo) throws Exception {
        super(name);
        this.sqlInfo = sqlInfo;
    }

    // Dao(IProtocol protocol, SQLInfo<T> sqlInfo) throws Exception {
    //     super(protocol.getName());
    //     this.protocol = protocol;
    //     this.sqlInfo = sqlInfo;
    // }

    @Override
    public String[] getPrimaryKeys() {
        return sqlInfo.getPrimaryKeys();
    }

    @Override
    public T add(T bean) throws Exception {
        SQLParams param = sqlInfo.getCreate(bean);
        if (param == null) return null;

        try {
            return execute(param.getSql(), param.getParams(),
                           param.getTypes()) > 0 ? bean : null;
        } catch (Exception e) {
            // logErr("framework.db.add.err");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public T[] addAll(T[] beans) throws Exception {
        if (beans == null) {
            return (T[]) Array.newInstance(sqlInfo.getDataClass(), 0);
        } else {
            List<T> results = new ArrayList<>();

            for (T bean : beans) {
                SQLParams param = sqlInfo.getCreate(bean);
                if (param != null) {
                    try {
                        if (execute(param.getSql(), param.getParams(),
                                    param.getTypes()) > 0) {
                            results.add(bean);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            T[] result = (T[]) Array.newInstance(sqlInfo.getDataClass(), results.size());
            results.toArray(result);
            return result;
        }
    }

    @Override
    public T put(T bean) throws Exception {
        SQLParams param = sqlInfo.getCountByPrimary(bean);
        if (param == null) return null;

        DaoCountReader reader = new DaoCountReader();
        executeReader(reader, param.getSql(), param.getParams(), param.getTypes());
        if (reader.getCount() > 0) {
            return this.edit(bean) > 0 ? bean : null;
        } else {
            return this.add(bean);
        }
    }

    @Override
    public T[] putAll(T[] beans) throws Exception {
        if (beans == null) {
            return beans;
        } else {
            List<T> results = new ArrayList<>();
            for (T bean : beans) {
                try {
                    T t = this.put(bean);
                    if (t != null) {
                        results.add(t);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            T[] result = (T[]) Array.newInstance(sqlInfo.getDataClass(), results.size());
            results.toArray(result);
            return result;
        }
    }

    @Override
    public int delete(Where[] wheres) throws Exception {
        SQLParams param = sqlInfo.getDelete(wheres);
        if (param == null) {
            return -1;
        } else {
            try {
                return execute(param.getSql(), param.getParams(), param.getTypes());
            } catch (Exception e) {
                // logErr("framework.db.delete.err");
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public int edit(T bean) throws Exception {
        SQLParams param = sqlInfo.getUpdateBean(bean);
        if (param == null) {
            return -1;
        } else {
            try {
                return execute(param.getSql(), param.getParams(), param.getTypes());
            } catch (Exception e) {
                // logErr("framework.db.edit.err");
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public T getBean(Where[] wheres) throws Exception {
        return getBeanBySQLParams(sqlInfo.getRetrieve(wheres));
    }

    @Override
    public T getByPrimary(Object... pks) throws Exception {
        return getBeanBySQLParams(sqlInfo.getByPrimary(pks));
    }

    @Override
    public boolean contains(Where[] wheres) throws Exception {
        return this.getCount(wheres) > 0;
    }

    @Override
    public int getCount(Where[] wheres) throws Exception {
        SQLParams param = sqlInfo.getCount(wheres);
        if (param == null) {
            return -1;
        } else {
            try {
                DaoCountReader reader = new DaoCountReader();
                executeReader(reader, param.getSql(), param.getParams(), param.getTypes());
                return reader.getCount();
            } catch (Exception e) {
                // logErr("framework.db.count.err");
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public T[] getList(Where[] wheres, KeyValue[] orders) throws Exception {
        SQLParams param = sqlInfo.getRetrieve(wheres, orders);
        if (param == null) return null;
        try {
            DaoListReader<T> reader = sqlInfo.getListReader();
            executeReader(reader, param.getSql(), param.getParams(), param.getTypes());
            return reader.getDatas();
        } catch (Exception e) {
            // logErr("framework.db.list.err");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public T[] getPage(int page, int pageSize, Where[] wheres, KeyValue[] orders) throws Exception {
        SQLParams param = sqlInfo.getRetrieve(wheres, orders);
        if (param == null) return null;
        if (page <= 0) {
            page = 1;
        }

        if (pageSize <= 0) {
            pageSize = 1;
        }

        int totalLength = (page - 1) * pageSize;

        try {
            DaoPageReader<T> reader = sqlInfo.getPageReader(pageSize);
            executeReader(reader, param.getSql(), param.getParams(),
                          param.getTypes(), totalLength,
                          pageSize);
            return reader.getDatas();
        } catch (Exception e) {
            // logErr("framework.db.page.err");
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public int update(KeyValue[] updates, Where[] wheres) throws Exception {
        SQLParams param = sqlInfo.getUpdate(updates, wheres);
        if (param == null) {
            return -1;
        } else {
            try {
                return execute(param.getSql(), param.getParams(), param.getTypes());
            } catch (Exception e) {
                // logErr("framework.db.update.err");
                e.printStackTrace();
                throw e;
            }
        }
    }

    @Override
    public T add(String[] columns, Object[] values) throws Exception {
        T data = sqlInfo.createBean(columns, values);
        return data == null ? null : this.add(data);
    }

    @Override
    public int delete(String[] columns, Object[] values) throws Exception {
        return this.delete(SQLHelper.getWheres(columns, values));
    }

    @Override
    public T getBean(String[] columns, Object[] values) throws Exception {
        return this.getBean(SQLHelper.getWheres(columns, values));
    }

    @Override
    public boolean contains(String[] columns, Object[] values) throws Exception {
        return this.getCount(SQLHelper.getWheres(columns, values)) > 0;
    }

    @Override
    public int getCount(String[] columns, Object[] values) throws Exception {
        return this.getCount(SQLHelper.getWheres(columns, values));
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


    @Override
    public void refreshCache() {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public void addCacheListener(String type, IListener listener) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public void addCacheListener(String type, IListener listener, int action) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public boolean hasCacheListener(String type) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public boolean hasCacheListener(String type, IListener listener) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public void removeCacheListener(String type) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public void removeAllCacheListeners() {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }

    @Override
    public void removeCacheListener(String type, IListener listener) {
        throw new IllegalArgumentException("Not caching data class: " + this.getClass().getName());
    }


    private T getBeanBySQLParams(SQLParams params) throws Exception {
        if (params == null) return null;
        try {
            DaoBeanReader<T> reader = sqlInfo.getBeanReader();
            return executeReader(reader, params.getSql(), params.getParams(),
                                       params.getTypes()) > 0 ? reader.getData() : null;
        } catch (Exception e) {
            // logErr("framework.db.bean.err");
            e.printStackTrace();
            throw e;
        }
    }
    
    // private void logErr(String key) {
    //     Logger logger;
    //     if (protocol == null) {
    //         logger = Logs.get(this.getClass());
    //     } else {
    //         logger = protocol.getLogger();
    //     }
    //     if (logger != null) {
    //         logger.error(Locals.text(key, sqlInfo.getDataClass()));
    //     }
    //
    // }

}
