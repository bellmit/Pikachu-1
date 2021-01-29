package com.x.pikachu.common.database.pool.core;

import com.x.pikachu.common.database.core.DatabaseType;

/**
 * @Desc 数据库连接池配置对象
 * @Date 2020/12/2 21:18
 * @Author AD
 */
public abstract class PoolConfig {
    
    // ------------------------ 变量定义 ------------------------
    // 连接池名
    protected final String poolName;
    // 连接池类型
    protected PoolType poolType = getPoolType();
    // 数据库类型
    protected DatabaseType databaseType;
    // 数据库URL
    protected String url;
    // 数据库驱动
    protected String driver;
    // 数据库用户名
    protected String username;
    // 数据库密码
    protected String password;
    
    // ------------------------ 构造方法 ------------------------
    
    /**
     * 数据库连接池配置对象构造方法
     *
     * @param poolName 连接池名
     */
    protected PoolConfig(String poolName) {
        this.poolName = poolName;
    }
    
    // ------------------------ 方法定义 ------------------------
    public abstract PoolType getPoolType();
    
    /**
     * 获取连接池名
     *
     * @return String 连接池名
     */
    public String getPoolName() {
        return this.poolName;
    }
    
    /**
     * 获取数据库类型
     *
     * @return DatabaseType 数据库类型
     */
    public DatabaseType getDatabaseType() {
        return this.databaseType;
    }
    
    /**
     * 获取数据库URL
     *
     * @return String 数据库URL
     */
    public String getUrl() {
        return this.url;
    }
    
    /**
     * 设置数据库URL
     *
     * @param url 数据库URL
     */
    public void setUrl(String url) {
        this.url = url;
        this.databaseType = parseUrl(url);
    }
    
    /**
     * 获取数据库驱动
     *
     * @return String 数据库驱动
     */
    public String getDriver() {
        return this.driver;
    }
    
    /**
     * 设置数据库驱动
     *
     * @param driver 数据库驱动
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }
    
    /**
     * 获取数据库用户名
     *
     * @return String 数据库用户名
     */
    public String getUsername() {
        return this.username;
    }
    
    /**
     * 设置数据库用户名
     *
     * @param username 数据库用户名
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * 获取数据库密码
     *
     * @return String 数据库密码
     */
    public String getPassword() {
        return this.password;
    }
    
    /**
     * 设置数据库密码
     *
     * @param password 数据库密码
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    // ------------------------ 私有方法 ------------------------
    
    /**
     * 通过URL解析数据库类型
     *
     * @param url JDBC连接URL
     *
     * @return
     */
    private DatabaseType parseUrl(String url) {
        if (url != null && url.startsWith("jdbc:")) {
            url = url.substring("jdbc:".length());
            if (url != null && url.length() > 0) {
                String database = url.substring(0, url.indexOf(":"));
                switch (database) {
                    case "mysql":
                        return DatabaseType.MYSQL;
                    case "oracle":
                        return DatabaseType.ORACLE;
                    case "sqlserver":
                        return DatabaseType.SQLSERVER;
                    case "derby":
                        return DatabaseType.DERBY;
                    default:
                        return DatabaseType.OTHERS;
                }
            }
        }
        return DatabaseType.OTHERS;
    }
    
}
