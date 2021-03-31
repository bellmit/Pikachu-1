package com.pikachu.common.database.factory;

import com.pikachu.common.database.core.Database;
import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.pool.core.IPool;

/**
 * @Desc TODO
 * @Date 2019-11-29 22:46
 * @Author AD
 */
public class Oracle extends Database {
    
    public Oracle(IPool pool) {
        super(pool);
    }
    
    @Override
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int start, int rows)
            throws Exception {
        String sql = "SELECT * FROM (SELECT AXT1.*, ROWNUM AX_ROWNUM FROM (" + table + ") AXT1 WHERE ROWNUM<=" + (start + rows) +
                     ") AXT2 WHERE AX_ROWNUM>=" + (start + 1);
        return this.executeReader(reader,sql, args, sqlTypes);
    }
    
}
