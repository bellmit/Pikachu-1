package com.pikachu.framework.database.reader;

import com.pikachu.common.database.core.IDataReader;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.framework.database.core.SQLHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Desc
 * @Date 2019-12-13 21:28
 * @Author AD
 */
public class DaoPageReader<T> implements IDataReader {
    
    private final Class<T> dataClass;
    
    private final Map<String, MethodInfo> sets;
    
    private final int pageSize;
    
    private final List<T> datas;
    
    public DaoPageReader(Class<T> dataClass, Map<String, MethodInfo> sets, int pageSize) {
        this.dataClass = dataClass;
        this.sets = Collections.unmodifiableMap(sets);
        this.pageSize = Math.max(pageSize, 0);
        this.datas = new ArrayList<>();
    }
    
    @Override
    public int read(ResultSet rs) throws Exception {
        // 设置查找行数
        rs.setFetchSize(pageSize);
        // 获取所有列
        ResultSetMetaData columns = rs.getMetaData();
        // 获取列数
        int count = columns.getColumnCount();
        int rows = 0;
        while (rs.next() && rows < pageSize) {
            ++rows;
            Constructor<T> c = dataClass.getDeclaredConstructor();
            c.setAccessible(true);
            T data = c.newInstance();
            for (int i = 1; i <= count; ++i) {
                String prop = columns.getColumnName(i);
                MethodInfo info = sets.get(prop.toUpperCase());
                if (info != null) {
                    Object sqlValue = rs.getObject(prop);
                    Object param = SQLHelper.toJavaData(sqlValue, info);
                    Method method = info.getMethod();
                    method.invoke(data, param);
                    datas.add(data);
                }
            }
        }
        return rows;
    }
    
    public T[] getDatas() {
        T[] ts = (T[])Array.newInstance(dataClass, datas.size());
        return datas.toArray(ts);
    }
    
}
