package com.pikachu.common.database;

import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.core.IDatabase;
import com.pikachu.common.database.factory.*;
import com.pikachu.common.database.core.DatabaseType;
import com.pikachu.common.database.pool.PoolManager;
import com.pikachu.common.database.pool.core.IPool;

/**
 * @Desc
 * @Date 2019-11-08 17:26
 * @Author AD
 */
public class DatabaseAccess implements IDatabase {
    
    private final IDatabase database;
    
    public DatabaseAccess(String poolName) throws Exception {
        IPool pool = PoolManager.getPool(poolName);
        if (pool == null) {
            throw new Exception("连接池名称无效:" + poolName);
        } else {
            DatabaseType type = pool.getDatabaseType();
            switch (type) {
                case MYSQL:
                    database = new MySQL(pool);
                    break;
                case DERBY:
                    database = new Derby(pool);
                    break;
                case ORACLE:
                    database = new Oracle(pool);
                    break;
                case SQLSERVER:
                    database = new SqlServer(pool);
                    break;
                default:
                    database = new Others(pool);
            }
            
        }
    }
    
    @Override
    public int execute(String sql, Object[] params, int[] sqlTypes) throws Exception {
        return database.execute(sql,params,sqlTypes);
    }
    
    @Override
    public int[] executeBatch(String[] sqls) throws Exception {
        return database.executeBatch(sqls);
    }
    
    @Override
    public int executeReader(IDataReader reader, String sql, Object[] params, int[] sqlTypes) throws Exception {
        return database.executeReader(reader, sql, params, sqlTypes);
    }
    
    @Override
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int start, int rows)
            throws Exception {
        return database.executeReader(reader, table, args, sqlTypes, start, rows);
    }
    
    @Override
    public Object[] executeReturnGeneratedKeys(String sql, Object[] params, int[] sqlTypes, String[] rows) throws Exception {
        return database.executeReturnGeneratedKeys(sql,params,sqlTypes,rows);
    }
    
}
