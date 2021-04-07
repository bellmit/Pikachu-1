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
            // 反射创建bean对象
            Constructor<T> c = dataClass.getDeclaredConstructor();
            c.setAccessible(true);
            T data = c.newInstance();
            for (int i = 1; i <= count; ++i) {
                // 逐列获取数据库字段名
                String prop = columns.getColumnName(i);
                // 获取字段名对应的Java bean对象的set属性
                MethodInfo info = sets.get(prop.toUpperCase());
                if (info != null) {
                    // 获取该行该列的数据库值
                    Object sqlValue = rs.getObject(prop);
                    // 将数据库值转为Java值
                    Object param = SQLHelper.toJavaData(sqlValue, info);
                    // 调用set方法注入值
                    Method method = info.getMethod();
                    method.invoke(data, param);
                }
            }
            // 一行读取完毕，存入集合
            datas.add(data);
        }
        // 返回读取行数
        return rows;
    }
    
    public T[] getDatas() {
        T[] ts = (T[])Array.newInstance(dataClass, datas.size());
        return datas.toArray(ts);
    }
    
}
