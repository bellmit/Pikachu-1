package com.x.pikachu.common.database.pool.factory;

import com.x.pikachu.common.database.pool.core.PoolConfig;
import com.x.pikachu.common.database.pool.core.PoolType;

/**
 * @Desc Hikari数据库连接池配置
 * @Date 2020/12/4 20:58
 * @Author AD
 */
public class HikariPoolConfig extends PoolConfig {
    
    // ------------------------ 变量定义 ------------------------
   
    // 自动提交从池中返回的连接
    private boolean autoCommit = true;
    // 等待来自池的连接的最大毫秒数(如果小于250毫秒，则被重置回30秒)
    private long connectionTimeout = 30000;
    // 连接允许在池中闲置的最长时间
    private long idleTimeout = 600000;
    // 池中连接最长生命周期(如果不等于0且小于30秒则会被重置回30分钟)
    private long maxLifetime = 1800000;
    // 如果您的驱动程序支持JDBC4，强烈建议您不要设置此属性
    private String connectionTestQuery;
    // 池中维护的最小空闲连接数(minIdle<0或者minIdle>maxPoolSize,则被重置为maxPoolSize)
    private int minimumIdle = 1;
    // 池中最大连接数，包括闲置和使用中的连接
    private int maximumPoolSize = 20;
    // 连接将被测试活动的最大时间量(如果小于250毫秒，则会被重置回5秒)
    private int validationTimeout = 5000;
    // ------------------------ 构造方法 ------------------------
    
    /**
     * Hikari数据库连接池配置构造方法
     * @param poolName 连接池名
     */
    public HikariPoolConfig(String poolName) {
        super(poolName);
    }
    // ------------------------ 方法定义 ------------------------
    @Override
    public PoolType getPoolType() {
        return PoolType.HIKARI;
    }
    
    public boolean isAutoCommit() {
        return autoCommit;
    }
    
    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
    
    public long getConnectionTimeout() {
        return connectionTimeout;
    }
    
    public void setConnectionTimeout(long connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
    
    public long getIdleTimeout() {
        return idleTimeout;
    }
    
    public void setIdleTimeout(long idleTimeout) {
        this.idleTimeout = idleTimeout;
    }
    
    public long getMaxLifetime() {
        return maxLifetime;
    }
    
    public void setMaxLifetime(long maxLifetime) {
        this.maxLifetime = maxLifetime;
    }
    
    public String getConnectionTestQuery() {
        return connectionTestQuery;
    }
    
    public void setConnectionTestQuery(String connectionTestQuery) {
        this.connectionTestQuery = connectionTestQuery;
    }
    
    public int getMinimumIdle() {
        return minimumIdle;
    }
    
    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }
    
    public int getMaximumPoolSize() {
        return maximumPoolSize;
    }
    
    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }
    
    public int getValidationTimeout() {
        return validationTimeout;
    }
    
    public void setValidationTimeout(int validationTimeout) {
        this.validationTimeout = validationTimeout;
    }
    
}
