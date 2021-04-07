package com.pikachu.common.database.factory;

import com.pikachu.common.database.core.Database;
import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.pool.core.IPool;

/**
 * @Desc Derby数据库
 * @Date 2019-11-29 22:46
 * @Author AD
 */
public class Derby extends Database {
    
    public Derby(IPool pool) {
        super(pool);
    }
    
    /**
     * 使用数据读取器分页读取
     *
     * @param reader     数据读取器
     * @param table      数据库表
     * @param args       SQL参数
     * @param sqlTypes   SQL参数类型，参见java.sql.Types
     * @param startIndex 开始行
     * @param rows       总共读取的行数
     *
     * @return
     *
     * @throws Exception
     */
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int startIndex, int rows)
            throws Exception {
        String sql = "SELECT * FROM (SELECT ROW_NUMBER() OVER() AS DAO_ROWNUM, AXT1.* FROM (" + table +
                     ") AXT1) AS AXT2 WHERE DAO_ROWNUM>=" + (startIndex + 1) + " AND DAO_ROWNUM<=" + (startIndex + rows);
        return this.executeReader(reader, sql, args, sqlTypes);
        
    }
    
}
