package com.pikachu.framework.database;

import com.pikachu.common.database.core.IDatabase;
import com.pikachu.common.database.pool.core.PoolType;
import com.pikachu.common.util.PikachuConverts;
import com.pikachu.framework.caching.datas.CacheManager;
import com.pikachu.framework.database.core.*;
import com.pikachu.common.database.DatabaseAccess;
import com.pikachu.common.database.core.DatabaseType;
import com.pikachu.common.database.pool.PoolManager;
import com.pikachu.common.database.pool.core.IPool;
import com.pikachu.common.database.pool.core.PoolConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/19 17:56
 */
public class DaoManager implements IDaoManager {

    private final Map<Class<?>, IDao<?>> daosMap = new ConcurrentHashMap<>();

    private final Object daosLock = new Object();

    private final String name;

    // private IProtocol protocol;

    private boolean isStopped = false;

    private DatabaseType databaseType;

    public DaoManager(DatabaseConfig config) throws Exception {
        this.name = config.getName();
        this.databaseType = DatabaseType.MYSQL;
        PoolConfig defaultConfig = new PoolConfig();
        defaultConfig.setPoolName(name);
        defaultConfig.setDatabaseType(this.databaseType);
        defaultConfig.setPoolType(PoolType.getPoolType(config.getPoolType()));
        defaultConfig.setUrl(config.getUrl());
        defaultConfig.setDriver(config.getDriver());
        defaultConfig.setUser(config.getUser());
        defaultConfig.setPassword(config.getPassword());
        defaultConfig.setInitialSize(PikachuConverts.toInt(config.getInitialSize(), defaultConfig.getInitialSize()));
        defaultConfig.setMaxActive(PikachuConverts.toInt(config.getMaxActive(), defaultConfig.getMaxActive()));
        defaultConfig.setMaxWait(PikachuConverts.toLong(config.getMaxWait(), defaultConfig.getMaxWait()));
        defaultConfig.setMinEvictableIdleTimeMillis(PikachuConverts.toLong(config.getMinEvictableIdleTimeMillis(),
                defaultConfig.getMinEvictableIdleTimeMillis()));
        defaultConfig.setMinIdle(PikachuConverts.toInt(config.getMinIdle(), defaultConfig.getMinIdle()));
        defaultConfig.setTestOnBorrow(PikachuConverts.toBoolean(config.getTestOnBorrow(), defaultConfig.isTestOnBorrow()));
        defaultConfig.setTestOnReturn(PikachuConverts.toBoolean(config.getTestOnReturn(), defaultConfig.isTestOnReturn()));
        defaultConfig.setTestWhileIdle(PikachuConverts.toBoolean(config.getTestWhileIdle(), defaultConfig.isTestWhileIdle()));
        defaultConfig.setTimeBetweenEvictionRunsMillis(
                PikachuConverts.toLong(config.getTimeBetweenEvictionRunsMillis(),
                        defaultConfig.getTimeBetweenEvictionRunsMillis()));
        defaultConfig.setValidationQuery(config.getValidateQuery());

        try {
            IPool pool = PoolManager.start(defaultConfig);
        } catch (Exception e) {
            // Logs.get(name).error(Locals.text("framework.db.start.err", name));
            e.printStackTrace();
            throw e;
        }
    }

    // public void setProtocol(IProtocol protocol) {
    //     this.protocol = protocol;
    // }

    public IDatabase getDatabaseAccess() {
        try {
            return new DatabaseAccess(this.name);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> IDao<T> getDao(Class<T> clazz) {
        return this.getDao(clazz, null);
    }

    public <T> IDao<T> getDao(Class<T> clazz, ITableInfoGetter<T> getter) {
        if (this.isStopped) {
            return null;
        } else {
            IDao<T> dao = (IDao<T>) daosMap.get(clazz);
            if (dao == null) {
                synchronized (this.daosLock) {
                    dao = (IDao<T>) daosMap.get(clazz);
                    if (dao != null) {
                        return dao;
                    }

                    SQLInfo<T> info = this.getDaoMethods(clazz, getter);

                    try {
                        if (info.isCaching()) {
                            // if (protocol == null) {
                            //     dao = new CacheDao<>(this.name, info);
                            // } else {
                            //     dao = new CacheDao<>(this.protocol, info);
                            // }
                            dao = new CacheDao<>(this.name, info);
                        } else {
                            // if (protocol == null) {
                            //     dao = new Dao<>(this.name, info);
                            // } else {
                            //     dao = new Dao<>(this.protocol, info);
                            // }
                            dao = new Dao<>(this.name, info);
                        }

                        this.daosMap.put(clazz, dao);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return dao;
        }
    }

    private <T> SQLInfo<T> getDaoMethods(Class<T> clazz, ITableInfoGetter<T> getter) {
        // if (getter == null) {
        //     getter = new ITableInfoGetter<T>() {
        //
        //         public TableInfo getTableInfo(Class<T> clazz) {
        //             if(DaoManager.this.protocol != null){
        //                 DataConfig cfg = DaoManager.this.protocol.getDataConfig(clazz);
        //                 return cfg == null ? null : new TableInfo(cfg.getTable(), cfg.getPks(),
        //                         cfg.isCache(), cfg.isHistory());
        //             }
        //             return null;
        //         }
        //     };
        // }

        if (getter == null) {
            getter = new PikachuTableInfoGetter<>();
        }

        TableInfo tableInfo = getter.getTableInfo(clazz);
        if (tableInfo == null) {
            getter = new PikachuTableInfoGetter<>();
            tableInfo = getter.getTableInfo(clazz);
        }
        return new SQLInfo<>(clazz, tableInfo, this.databaseType);
    }

    public synchronized void stop() {
        if (!this.isStopped) {
            this.isStopped = true;

            try {
                PoolManager.stop(this.name);
            } catch (Exception e) {
                e.printStackTrace();
            }
            CacheManager.clear();
        }
    }

}
