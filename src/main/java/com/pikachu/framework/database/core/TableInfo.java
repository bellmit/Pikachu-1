package com.pikachu.framework.database.core;

/**
 * @Desc 映射到数据库表的bean信息
 * @Date 2019-12-05 21:54
 * @Author AD
 */
public final class TableInfo {
    
    // ------------------------ 变量定义 ------------------------
    
    /**
     * 表名
     */
    private final String tableName;
    
    /**
     * 主键
     */
    private final String[] primaryKeys;
    
    /**
     * 是否缓存
     */
    private final boolean caching;
    
    /**
     * 是否缓存历史记录
     */
    private final boolean cachingHistory;
    
    // ------------------------ 构造方法 ------------------------
    
    public TableInfo(String tableName, String[] primaryKeys, boolean caching, boolean cachingHistory) {
        this.tableName = tableName;
        this.primaryKeys = primaryKeys;
        this.caching = caching;
        this.cachingHistory = cachingHistory;
    }
    // ------------------------ 方法定义 ------------------------
    
    /**
     * 获取  表名
     */
    public String getTableName() {
        return this.tableName;
    }
    
    /**
     * 获取  主键
     */
    public String[] getPrimaryKeys() {
        return this.primaryKeys;
    }
    
    /**
     * 获取  是否缓存
     */
    public boolean isCaching() {
        return this.caching;
    }
    
    /**
     * 获取  是否缓存历史记录
     */
    public boolean isCachingHistory() {
        return this.cachingHistory;
    }
    
}
