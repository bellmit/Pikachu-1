package com.pikachu.common.database.pool.core;

import com.pikachu.common.database.core.DatabaseType;

/**
 * @Desc 数据库连接池配置类
 * @Date 2019-11-08 17:31
 * @Author AD
 */
public class PoolConfig {
    
    // ---------------------- 成员变量 ----------------------
    // 连接池名
    private String poolName;
    // 数据库连接池类型，默认使用Hikari
    private PoolType poolType;
    // 数据库类型
    private DatabaseType databaseType;
    // 数据库URL
    private String url;
    // 数据库用户名
    private String user;
    // 数据库密码
    private String password;
    // 驱动类名
    private String driver;
    // 初始化连接数
    private int initialSize = 0;
    // 最大允许的连接数
    private int maxActive = 20;
    // 最小空闲连接数
    private int minIdle = 1;
    // 连接等待超时时间
    private long maxWait = 60000L;
    // 是否保持连接活动
    private boolean keepActive = false;
    // 连接最小生存时间
    private long minEvictableIdleTimeMillis = 1800000L;
    // 检测需要关闭的空闲连接的间隔时间
    private long timeBetweenEvictionRunsMillis = 60000L;
    // 检测连接是否可用的sql语句
    private String validationQuery = "SELECT 1";
    // 空闲时是否检测连接可用性
    private boolean testWhileIdle = true;
    // 获取连接时是否检测连接可用性
    private boolean testOnBorrow = false;
    // 归还连接时检测连接是否有效
    private boolean testOnReturn = false;
    // 当发现池中的可用实例已经用光时，需要做的动作
    private boolean exhaustedAction = true;
    // 是否缓存preparedStatement(PSCache)。对支持游标的数据库性能提升巨大，如:oracle。mysql下建议关闭
    private boolean poolPreparedStatements = false;
    
    // ---------------------- 构造方法 ----------------------
    
    public PoolConfig() {}
    
    // ---------------------- 成员方法 ----------------------
    
    /**
     * 获取 数据库URL
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * 设置 数据库URL
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
    /**
     * 获取 数据库用户名
     */
    public String getUser() {
        return this.user;
    }
    
    /**
     * 设置 数据库用户名
     */
    public void setUser(String user) {
        this.user = user;
    }
    
    /**
     * 获取 数据库密码
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 设置 数据库密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * 获取 驱动类名
     */
    public String getDriver() {
        return this.driver;
    }
    
    /**
     * 设置 驱动类名
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    /**
     * 获取 连接池类型
     */
    public PoolType getPoolType() {
        return this.poolType;
    }
    
    /**
     * 设置 连接池类型
     */
    public void setPoolType(PoolType type) {
        this.poolType = poolType;
    }
    
    /**
     * 获取 连接池名称
     */
    public String getPoolName() {
        return this.poolName;
    }
    
    /**
     * 设置 连接池名称
     */
    public void setPoolName(String poolName) {
        this.poolName = poolName;
    }
    
    /**
     * 获取 初始化连接数
     */
    public int getInitialSize() {
        return this.initialSize;
    }
    
    /**
     * 设置 初始化连接数
     */
    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }
    
    /**
     * 获取 最大允许的连接数
     */
    public int getMaxActive() {
        return this.maxActive;
    }
    
    /**
     * 设置 最大允许的连接数
     */
    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }
    
    /**
     * 获取 最小空闲连接数
     */
    public int getMinIdle() {
        return this.minIdle;
    }
    
    /**
     * 设置 最小空闲连接数
     */
    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }
    
    /**
     * 获取 连接等待超时时间
     */
    public long getMaxWait() {
        return this.maxWait;
    }
    
    /**
     * 设置 连接等待超时时间
     */
    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }
    
    /**
     * 获取 是否保持连接活动
     */
    public boolean isKeepActive() {
        return this.keepActive;
    }
    
    /**
     * 设置 是否保持连接活动
     */
    public void setKeepActive(boolean keepActive) {
        this.keepActive = keepActive;
    }
    
    /**
     * 获取 连接最小生存时间
     */
    public long getMinEvictableIdleTimeMillis() {
        return this.minEvictableIdleTimeMillis;
    }
    
    /**
     * 设置 连接最小生存时间
     */
    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }
    
    /**
     * 获取 检测需要关闭的空闲连接的间隔时间
     */
    public long getTimeBetweenEvictionRunsMillis() {
        return this.timeBetweenEvictionRunsMillis;
    }
    
    /**
     * 设置 检测需要关闭的空闲连接的间隔时间
     */
    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }
    
    /**
     * 获取 检测连接是否可用的sql语句
     */
    public String getValidationQuery() {
        return this.validationQuery;
    }
    
    /**
     * 设置 检测连接是否可用的sql语句
     */
    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }
    
    /**
     * 获取 空闲时是否检测连接可用性
     */
    public boolean isTestWhileIdle() {
        return this.testWhileIdle;
    }
    
    /**
     * 设置 空闲时是否检测连接可用性
     */
    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
    
    /**
     * 获取 获取连接时是否检测连接可用性
     */
    public boolean isTestOnBorrow() {
        return this.testOnBorrow;
    }
    
    /**
     * 设置 获取连接时是否检测连接可用性
     */
    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }
    
    /**
     * 获取 归还连接时检测连接是否有效
     */
    public boolean isTestOnReturn() {
        return this.testOnReturn;
    }
    
    /**
     * 设置 归还连接时检测连接是否有效
     */
    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }
    
    /**
     * 获取 是否缓存preparedStatement(PSCache)。对支持游标的数据库性能提升巨大，如:oracle。mysql下建议关闭
     */
    public boolean isPoolPreparedStatements() {
        return this.poolPreparedStatements;
    }
    
    /**
     * 设置 是否缓存preparedStatement(PSCache)。对支持游标的数据库性能提升巨大，如:oracle。mysql下建议关闭
     */
    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }
    
    /**
     * 获取 当发现池中的可用实例已经用光时，需要做的动作
     */
    public boolean isExhaustedAction() {
        return this.exhaustedAction;
    }
    
    /**
     * 设置 当发现池中的可用实例已经用光时，需要做的动作
     */
    public void setExhaustedAction(boolean exhaustedAction) {
        this.exhaustedAction = exhaustedAction;
    }
    
    /**
     * 获取数据库类型
     * @return DatabaseType 数据库类型
     */
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }
    
    /**
     * 设置数据库类型
     * @param databaseType 数据库类型
     */
    public void setDatabaseType(DatabaseType databaseType) {
        this.databaseType = databaseType;
    }
    
}
