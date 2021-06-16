package com.pikachu.framework.database.core;

import com.pikachu.common.annotations.IColumn;
import com.pikachu.common.annotations.ITable;
import com.pikachu.common.util.PikachuArrays;
import com.pikachu.common.util.PikachuStrings;

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
        if(PikachuStrings.isNull(tableName)){
            tableName = clazz.getSimpleName();
        }
        boolean cache = table.cache();
        boolean history = table.history();
        Field[] fields = clazz.getDeclaredFields();
        List<String> pks = new ArrayList<>();
        for (Field field : fields) {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            if (field.isAnnotationPresent(IColumn.class)) {
                IColumn column = field.getAnnotation(IColumn.class);
                if (column.pk()) {
                    String pk = field.getName();
                    pks.add(pk);
                }
            }
            field.setAccessible(accessible);
        }
        return new TableInfo(tableName, pks.toArray(PikachuArrays.EMPTY_STRING), cache, history);
    }
    
}
