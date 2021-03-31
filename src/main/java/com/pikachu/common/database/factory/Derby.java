package com.pikachu.common.database.factory;

import com.pikachu.common.database.core.Database;
import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.pool.core.IPool;

/**
 * @Desc TODO
 * @Date 2019-11-29 22:46
 * @Author AD
 */
public class Derby extends Database {
    
    public Derby(IPool pool) {
        super(pool);
    }
    
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int startIndex, int rows) throws Exception {
        String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER() AS DAO_ROWNUM, AXT1.* FROM (" + table +
                      ") AXT1) AS AXT2 WHERE DAO_ROWNUM>=" + (startIndex + 1) + " AND DAO_ROWNUM<=" + (startIndex + rows);
        return this.executeReader(reader, sql, args, sqlTypes);
        
    }
    
}
