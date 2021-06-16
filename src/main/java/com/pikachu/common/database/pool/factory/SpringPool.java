package com.pikachu.common.database.pool.factory;

import com.pikachu.common.database.core.DatabaseType;
import com.pikachu.common.database.pool.core.IPool;

import javax.sql.DataSource;
import java.sql.Connection;

/**
 * @Desc TODO
 * @Date 2021/5/30 18:17
 * @Author AD
 */
public class SpringPool implements IPool {
    
    private final DataSource dataSource;
    public SpringPool(DataSource dataSource){
        this.dataSource = dataSource;
    }
    
    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MYSQL;
    }
    
    @Override
    public Connection getConnection() throws Exception {
        return dataSource.getConnection();
    }
    
    @Override
    public String getStatus() {
        return "null";
    }
    
    @Override
    public void stop() throws Exception {
    
    }
    
}
