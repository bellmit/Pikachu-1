package com.pikachu.framework.database.core;


import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Desc
 * @Date 2020-03-08 20:00
 * @Author AD
 */
public class DatabaseConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    // ------------------------ 变量定义 ------------------------
    private String name;

    private String poolType;
    
    private String url;

    private String driver;
    
    private String user;

    private String password;

    private String initialSize;

    private String maxActive;

    private String maxIdle;

    private String minIdle;

    private String maxWait;

    private String minEvictableIdleTimeMillis;

    private String numTestsPerEvictionRun;

    private String testOnBorrow;

    private String testOnReturn;

    private String testWhileIdle;

    private String timeBetweenEvictionRunsMillis;

    private String validateQuery;

    // ------------------------ 构造方法 ------------------------
    public DatabaseConfig() {}
    // ------------------------ 方法定义 ------------------------

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoolType() {
        return poolType;
    }

    public void setPoolType(String poolType) {
        this.poolType = poolType;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    public String getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(String maxIdle) {
        this.maxIdle = maxIdle;
    }

    public String getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(String minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getNumTestsPerEvictionRun() {
        return numTestsPerEvictionRun;
    }

    public void setNumTestsPerEvictionRun(String numTestsPerEvictionRun) {
        this.numTestsPerEvictionRun = numTestsPerEvictionRun;
    }

    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(String testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public String getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(String timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public String getValidateQuery() {
        return validateQuery;
    }

    public void setValidateQuery(String validateQuery) {
        this.validateQuery = validateQuery;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", DatabaseConfig.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("poolType='" + poolType + "'")
                .add("driver='" + driver + "'")
                .add("url='" + url + "'")
                .add("user='" + user + "'")
                .add("password='" + password + "'")
                .add("initialSize='" + initialSize + "'")
                .add("maxActive='" + maxActive + "'")
                .add("maxIdle='" + maxIdle + "'")
                .add("minIdle='" + minIdle + "'")
                .add("maxWait='" + maxWait + "'")
                .add("minEvictableIdleTimeMillis='" + minEvictableIdleTimeMillis + "'")
                .add("numTestsPerEvictionRun='" + numTestsPerEvictionRun + "'")
                .add("testOnBorrow='" + testOnBorrow + "'")
                .add("testOnReturn='" + testOnReturn + "'")
                .add("testWhileIdle='" + testWhileIdle + "'")
                .add("timeBetweenEvictionRunsMillis='" + timeBetweenEvictionRunsMillis + "'")
                .add("validateQuery='" + validateQuery + "'")
                .toString();
    }
    
}
