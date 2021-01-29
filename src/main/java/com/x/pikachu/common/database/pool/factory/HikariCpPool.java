package com.x.pikachu.common.database.pool.factory;

import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.common.database.pool.core.IPool;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.pool.HikariPool;

import java.sql.Connection;

/**
 * @Desc
 * @Date 2020/12/5 13:23
 * @Author AD
 */
public class HikariCpPool implements IPool {
    
    private final HikariPool pool;
    
    private final HikariPoolConfig config;
    
    public HikariCpPool(HikariPoolConfig config) {
        this.config = config;
        HikariConfig hc = new HikariConfig();
        hc.setPoolName(config.getPoolName());
        hc.setJdbcUrl(config.getUrl());
        hc.setDriverClassName(config.getDriver());
        hc.setUsername(config.getUsername());
        hc.setPassword(config.getPassword());
        hc.setMaximumPoolSize(config.getMaximumPoolSize());
        hc.setMinimumIdle(config.getMinimumIdle());
        hc.setConnectionTimeout(config.getConnectionTimeout());
        hc.setConnectionTestQuery(config.getConnectionTestQuery());
        hc.setIdleTimeout(config.getIdleTimeout());
        hc.setAutoCommit(config.isAutoCommit());
        hc.setMaxLifetime(config.getMaxLifetime());
        hc.setValidationTimeout(config.getValidationTimeout());
        this.pool = new HikariPool(hc);
    }
    
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
        int active = pool.getActiveConnections();
        int idle = pool.getIdleConnections();
        int total = pool.getTotalConnections();
        int await = pool.getThreadsAwaitingConnection();
        StringBuilder sb = new StringBuilder();
        sb.append(pool.toString()).append("[").append("\n");
        sb.append("\t").append("activeConnections:").append(active).append("\n");
        sb.append("\t").append("idleConnections:").append(idle).append("\n");
        sb.append("\t").append("totalConnections:").append(total).append("\n");
        sb.append("\t").append("threadsAwaitingConnection:").append(await).append("\n");
        sb.append("]");
        return sb.toString();
    }
    
    @Override
    public void stop() throws Exception {
        pool.shutdown();
    }
    
}
