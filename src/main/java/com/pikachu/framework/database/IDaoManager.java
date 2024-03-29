package com.pikachu.framework.database;

import com.pikachu.common.database.core.IDatabase;
import com.pikachu.framework.database.core.ITableInfoGetter;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/19 15:46
 */
public interface IDaoManager {
    <T> IDao<T> getDao(Class<T> clazz);

    <T> IDao<T> getDao(Class<T> clazz, ITableInfoGetter<T> tableInfoGetter);

    IDatabase getDatabaseAccess();
}
