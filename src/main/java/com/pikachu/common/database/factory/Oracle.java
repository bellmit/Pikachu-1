package com.pikachu.common.database.factory;

import com.pikachu.common.database.core.Database;
import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.pool.core.IPool;
import oracle.jdbc.oracore.OracleTypeBLOB;
import oracle.sql.BLOB;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

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
    protected void setParam(Connection conn, PreparedStatement ps, int index, Object arg, int sqlType) throws SQLException {
        if (Types.BLOB == sqlType) {
            ByteArrayInputStream in = new ByteArrayInputStream((byte[])arg);
            ps.setBlob(index, in);
        } else {
            super.setParam(conn, ps, index, arg, sqlType);
        }

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
     * @return
     * @throws Exception
     */
    @Override
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int startIndex, int rows)
            throws Exception {
        String sql =
                "SELECT * FROM (SELECT AXT1.*, ROWNUM AX_ROWNUM FROM (" + table + ") AXT1 WHERE ROWNUM<=" + (startIndex + rows) +
                        ") AXT2 WHERE AX_ROWNUM>=" + (startIndex + 1);
        return this.executeReader(reader, sql, args, sqlTypes);
    }

}
