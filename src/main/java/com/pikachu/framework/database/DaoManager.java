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
import com.x.pikachu.framework.database.core.*;

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
    
    private DatabaseType type;
    
    public DaoManager(String name, DatabaseConfig config) throws Exception {
        this.name = name;
        PoolConfig cfg = new PoolConfig();
        cfg.setPoolName(name);
        cfg.setPoolType(PoolType.getPoolType(config.getPoolType()));
        cfg.setUrl(config.getUrl());
        cfg.setDriver(config.getDriver());
        cfg.setUser(config.getUser());
        cfg.setPassword(config.getPassword());
        cfg.setInitialSize(PikachuConverts.toInt(config.getInitialSize(), cfg.getInitialSize()));
        cfg.setMaxActive(PikachuConverts.toInt(config.getMaxActive(), cfg.getMaxActive()));
        cfg.setMaxWait(PikachuConverts.toLong(config.getMaxWait(), cfg.getMaxWait()));
        cfg.setMinEvictableIdleTimeMillis(PikachuConverts.toLong(config.getMinEvictableIdleTimeMillis(),
                cfg.getMinEvictableIdleTimeMillis()));
        cfg.setMinIdle(PikachuConverts.toInt(config.getMinIdle(), cfg.getMinIdle()));
        cfg.setTestOnBorrow(PikachuConverts.toBoolean(config.getTestOnBorrow(), cfg.isTestOnBorrow()));
        cfg.setTestOnReturn(PikachuConverts.toBoolean(config.getTestOnReturn(), cfg.isTestOnReturn()));
        cfg.setTestWhileIdle(PikachuConverts.toBoolean(config.getTestWhileIdle(), cfg.isTestWhileIdle()));
        cfg.setTimeBetweenEvictionRunsMillis(
                PikachuConverts.toLong(config.getTimeBetweenEvictionRunsMillis(),
                        cfg.getTimeBetweenEvictionRunsMillis()));
        cfg.setValidationQuery(config.getValidateQuery());
        
        try {
            IPool pool = PoolManager.start(cfg);
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
        
        TableInfo tableInfo = getter.getTableInfo(clazz);
        if (tableInfo == null) {
            getter = new PikachuTableInfoGetter<>();
            tableInfo = getter.getTableInfo(clazz);
        }
        return new SQLInfo<>(clazz, tableInfo, this.type);
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
