package com.x.pikachu.framework.database.core;

import com.x.pikachu.common.annotations.IColumn;
import com.x.pikachu.common.annotations.ITable;
import com.x.pikachu.common.util.PikachuArrays;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Date 2020-03-22 23:51
 * @Author AD
 */
public class PikachuTableInfoGetter<T> implements ITableInfoGetter<T> {
    
    @Override
    public TableInfo getTableInfo(Class<T> clazz) {
        if (!clazz.isAnnotationPresent(ITable.class)) {
            return null;
        }
        ITable table = clazz.getAnnotation(ITable.class);
        String tableName = table.table();
        boolean cache = table.cache();
        boolean history = table.history();
        Field[] fields = clazz.getDeclaredFields();
        List<String> pks = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(IColumn.class)) {
                IColumn column = field.getAnnotation(IColumn.class);
                if (column.pk()) {
                    String pk = field.getName();
                    pks.add(pk);
                }
            }
        }
        return new TableInfo(tableName, pks.toArray(PikachuArrays.EMPTY_STRING), cache, history);
    }
    
}
