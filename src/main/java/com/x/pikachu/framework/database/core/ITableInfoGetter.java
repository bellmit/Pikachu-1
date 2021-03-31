package com.x.pikachu.framework.database.core;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/19 15:44
 */
public interface ITableInfoGetter<T> {
    TableInfo getTableInfo(Class<T> clazz);
}
