package com.pikachu.framework.database.reader;

import com.pikachu.common.database.core.IDataReader;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.framework.database.core.SQLHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Map;

/**
 * @Desc 读取数据库某一条记录
 * @Date 2019-12-13 19:38
 * @Author AD
 */
public class DaoBeanReader<T> implements IDataReader {
    
    private final Class<T> dataClass;
    
    private final Map<String, MethodInfo> sets;
    
    private T data;
    
    public DaoBeanReader(Class<T> dataClass, Map<String, MethodInfo> sets) {
        this.dataClass = dataClass;
        this.sets = sets;
    }
    
    @Override
    public int read(ResultSet rs) throws Exception {
        // 取一行数据
        rs.setFetchSize(1);
        if (rs.next()) {
            // 获取columns
            ResultSetMetaData columns = rs.getMetaData();
            int count = columns.getColumnCount();
            Constructor<T> c = dataClass.getDeclaredConstructor();
            c.setAccessible(true);
            this.data = dataClass.newInstance();
            for (int i = 1; i <= count; ++i) {
                // 获取字段名
                String prop = columns.getColumnName(i);
                // 转为大写
                prop = prop.toUpperCase();
                // 根据属性获取set方法信息
                MethodInfo set = sets.get(prop);
                if (set != null) {
                    // 获取set方法
                    Method method = set.getMethod();
                    // 获取数据库里的值
                    Object param = rs.getObject(i);
                    // 转为Java值
                    Object javaData = SQLHelper.toJavaData(param, set);
                    // 调用set方法
                    method.invoke(data, javaData);
                }
            }
        }
        return data == null ? 0 : 1;
    }
    
    public T getData() {
        return this.data;
    }
    
}
