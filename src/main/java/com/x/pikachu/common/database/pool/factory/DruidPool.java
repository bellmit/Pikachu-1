package com.x.pikachu.common.database.pool.factory;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.common.database.pool.core.IPool;

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
    private final DruidPoolConfig config;
    // ------------------------ 构造方法 ------------------------
    
    /**
     * Druid数据库连接池构造方法
     *
     * @param config Druid数据库连接池配置
     *
     * @throws Exception
     */
    public DruidPool(DruidPoolConfig config) {
        try {
            this.pool = (DruidDataSource) DruidDataSourceFactory.createDataSource(config.toProperties());
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.config = config;
    }
    // ------------------------ 方法定义 ------------------------
    
    @Override
    public Connection getConnection() throws Exception {
        return pool.getConnection();
    }
    
    @Override
    public DatabaseType getDatabaseType() {
        return config.getDatabaseType();
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
