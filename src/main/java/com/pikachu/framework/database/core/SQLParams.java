package com.pikachu.framework.database.core;

/**
 * @Desc sql参数信息
 * @Date 2019-12-05 21:57
 * @Author AD
 */
public final class SQLParams {
    // ------------------------ 变量定义 ------------------------
    
    /**
     * sql语句
     */
    private final String sql;
    
    /**
     * 参数
     */
    private final Object[] params;
    
    /**
     * 参数类型
     */
    private final int[] types;
    
    // ------------------------ 构造方法 ------------------------
    
    public SQLParams(String sql, Object[] params, int[] sqlTypes) {
        this.sql = sql;
        this.params = params;
        this.types = sqlTypes;
    }
    
    // ------------------------ 方法定义 ------------------------
    
    /**
     * 获取  sql语句
     */
    public String getSql() {
        return this.sql;
    }
    
    /**
     * 获取  参数
     */
    public Object[] getParams() {
        return this.params;
    }
    
    /**
     * 获取  参数类型
     */
    public int[] getTypes() {
        return this.types;
    }
    
}
