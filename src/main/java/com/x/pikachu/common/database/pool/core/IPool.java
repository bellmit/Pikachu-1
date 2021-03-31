package com.x.pikachu.common.database.pool.core;

import com.x.pikachu.common.database.core.DatabaseType;

import java.sql.Connection;

/**
 * @Desc 数据库连接池接口
 * @Date 2020/12/4 19:34
 * @Author AD
 */
public interface IPool {
    
    /**
     * 获取数据库类型
     * @return
     */
    DatabaseType getDatabaseType();
    
    /**
     * 获取数据库连接
     *
     * @return
     *
     * @throws Exception
     */
    Connection getConnection() throws Exception;
    
    /**
     * 连接池状态信息
     *
     * @return
     */
    String getStatus();
    
    /**
     * 关闭连接池
     *
     * @throws Exception
     */
    void stop() throws Exception;
    
}
