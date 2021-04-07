package com.pikachu.framework.database.reader;

import com.pikachu.common.database.core.IDataReader;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.framework.database.core.SQLHelper;

import java.lang.reflect.Array;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Desc
 * @Date 2019-12-13 20:24
 * @Author AD
 */
public class DaoListReader<T> implements IDataReader {
    
    private final Class<T> dataClass;
    private final Map<String, MethodInfo> sets;
    private List<T> datas;
    
    public DaoListReader(Class<T> dataClass, Map<String, MethodInfo> sets) {
        this.dataClass = dataClass;
        this.sets = sets;
        this.datas = new ArrayList<>();
    }
    
    @Override
    public int read(ResultSet rs) throws Exception {
        ResultSetMetaData columns = rs.getMetaData();
        int count = columns.getColumnCount();
        int rows = 0;
        while (rs.next()) {
            ++rows;
            T data = dataClass.newInstance();
            for (int i = 1; i <= count; ++i) {
                String column = columns.getColumnName(i);
                MethodInfo set = sets.get(column.toUpperCase());
                if (set != null) {
                    Object sqlValue = rs.getObject(column);
                    Object param = SQLHelper.toJavaData(sqlValue, set);
                    set.getMethod().invoke(data, param);
                }
            }
            datas.add(data);
        }
        return rows;
    }
    
    public T[] getDatas() {
        T[] os = (T[]) Array.newInstance(dataClass, datas.size());
        return datas.toArray(os);
    }
    
}
