package com.x.pikachu.common.database.pool;

import com.x.pikachu.common.database.pool.core.IPool;
import com.x.pikachu.common.database.pool.core.PoolConfig;
import com.x.pikachu.common.database.pool.core.PoolType;
import com.x.pikachu.common.database.pool.factory.DruidPool;
import com.x.pikachu.common.database.pool.factory.DruidPoolConfig;
import com.x.pikachu.common.database.pool.factory.HikariCpPool;
import com.x.pikachu.common.database.pool.factory.HikariPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc 连接池管理者
 * @Date 2020/12/2 21:18
 * @Author AD
 */
public final class PoolManager {
    
    private static final Map<String, IPool> POOLS = new HashMap<>();
    
    public static synchronized IPool start(PoolConfig config) {
        if(!POOLS.containsKey(config.getPoolName())){
            PoolType poolType = config.getPoolType();
            if (poolType == PoolType.DRUID) {
                DruidPoolConfig druidConfig = (DruidPoolConfig) config;
                IPool druidPool = new DruidPool(druidConfig);
                POOLS.put(config.getPoolName(), druidPool);
            }else{
                HikariPoolConfig hikariConfig = (HikariPoolConfig) config;
                IPool hikariPool = new HikariCpPool(hikariConfig);
                POOLS.put(config.getPoolName(), hikariPool);
            }
        }
        return POOLS.get(config.getPoolName());
    }
    
    public static synchronized void stop(String name) throws Exception {
        IPool pool = POOLS.remove(name);
        if (pool != null) {
            pool.stop();
        }
    }
    
    public static synchronized void stopAll() {
        for (IPool pool : POOLS.values()) {
            try {
                pool.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        POOLS.clear();
    }
    
    public IPool getPool(String name) {
        return POOLS.get(name);
    }
    
}
