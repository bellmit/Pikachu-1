package com.x.pikachu.common.database.pool.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.common.database.pool.core.IPool;
import com.x.pikachu.common.database.pool.core.PoolConfig;

import java.sql.Connection;

/**
 * @Desc Druid数据库连接池
 * @Date 2020/12/4 19:46
 * @Author AD
 */
public final class DruidPool implements IPool {
    
    // ------------------------ 变量定义 ------------------------
    /**
     * 数据库连接池
     */
    private DruidDataSource pool;
    /**
     * 连接池配置对象
     */
    private final PoolConfig config;
    // ------------------------ 构造方法 ------------------------
    
    /**
     * Druid数据库连接池构造方法
     *
     * @param config Druid数据库连接池配置
     *
     * @throws Exception
     */
    public DruidPool(PoolConfig config) {
        this.config = config;
        try {
            DruidDataSource druid = new DruidDataSource();
            druid.setUrl(config.getUrl());
            druid.setDriverClassName(config.getDriver());
            druid.setUsername(config.getUser());
            druid.setPassword(config.getPassword());
            druid.setInitialSize(config.getInitialSize());
            druid.setMaxActive(config.getMaxActive());
            druid.setMinIdle(config.getMinIdle());
            druid.setMaxActive(config.getMaxActive());
            druid.setKeepAlive(config.isKeepActive());
            druid.setMinEvictableIdleTimeMillis(config.getMinEvictableIdleTimeMillis());
            druid.setTimeBetweenConnectErrorMillis(config.getTimeBetweenEvictionRunsMillis());
            druid.setValidationQuery(config.getValidationQuery());
            druid.setTestWhileIdle(config.isTestWhileIdle());
            druid.setTestOnBorrow(config.isTestOnBorrow());
            druid.setTestOnReturn(config.isTestOnReturn());
            druid.setPoolPreparedStatements(config.isPoolPreparedStatements());
            this.pool = druid;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
    // ------------------------ 方法定义 ------------------------
    
    @Override
    public DatabaseType getDatabaseType() {
        return config.getDatabaseType();
    }
    
    @Override
    public Connection getConnection() throws Exception {
        return pool.getConnection();
    }
    
    @Override
    public String getStatus() {
        return pool.toString();
    }
    
    @Override
    public void stop() throws Exception {
        pool.close();
    }
    
}
