package com.x.pikachu.common.database.pool.factory;

import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.common.database.pool.core.IPool;
import com.x.pikachu.common.database.pool.core.PoolConfig;
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
    private final PoolConfig config;
    
    public HikariCpPool(PoolConfig config) {
        this.config = config;
        HikariConfig hc = new HikariConfig();
        // 连接池名
        hc.setPoolName(config.getPoolName());
        // 数据库连接路径
        hc.setJdbcUrl(config.getUrl());
        // 驱动
        hc.setDriverClassName(config.getDriver());
        // 用户名
        hc.setUsername(config.getUser());
        // 密码
        hc.setPassword(config.getPassword());
        // 池中最大连接数，包括闲置和使用中的连接
        hc.setMaximumPoolSize(config.getMaxActive());
        // 池中维护的最小空闲连接数(minIdle<0或者minIdle>maxPoolSize,则被重置为maxPoolSize)
        hc.setMinimumIdle(config.getMinIdle());
        // 等待来自池的连接的最大毫秒数(如果小于250毫秒，则被重置回30秒)
        hc.setConnectionTimeout(config.getMaxWait());
        // 连接允许在池中闲置的最长时间
        hc.setIdleTimeout(config.getTimeBetweenEvictionRunsMillis());
        // 池中连接最长生命周期(如果不等于0且小于30秒则会被重置回30分钟)
        hc.setMaxLifetime(config.getMinEvictableIdleTimeMillis());
        // 如果您的驱动程序支持JDBC4，强烈建议您不要设置此属性
        hc.setConnectionTestQuery(config.getValidationQuery());
        
        this.pool = new HikariPool(hc);
    }
    
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
