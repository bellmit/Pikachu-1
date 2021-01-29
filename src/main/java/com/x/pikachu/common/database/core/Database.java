package com.x.pikachu.common.database.core;

import com.x.pikachu.common.database.pool.core.IPool;
import com.x.pikachu.util.ArrayHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @Desc 数据库对象
 * @Date 2020/12/2 21:35
 * @Author AD
 */
public abstract class Database implements IDatabase {
    
    // ------------------------ 变量定义 ------------------------
    private final IPool pool;
    // ------------------------ 构造方法 ------------------------
    
    public Database(IPool pool) {
        this.pool = pool;
    }
    // ------------------------ 方法定义 ------------------------
    
    @Override
    public int execute(SqlParams params) throws Exception {
        try (Connection conn = pool.getConnection();
             PreparedStatement ps = conn.prepareStatement(params.getSql())) {
            fillPrepareStatement(ps, params);
            return ps.executeUpdate();
        }
    }
    
    @Override
    public int[] executeBatch(String[] sqls) throws Exception {
        if (ArrayHelper.isEmpty(sqls)) {
            return ArrayHelper.EMPTY_INT;
        }
        try (Connection conn = pool.getConnection()) {
            conn.setAutoCommit(false);
            try (Statement stat = conn.createStatement()) {
                for (String sql : sqls) {
                    stat.addBatch(sql);
                }
                int[] ints = stat.executeBatch();
                conn.setAutoCommit(true);
                return ints;
            }
        }
    }
    
    @Override
    public int executeReader(IDataReader reader, SqlParams params) throws Exception {
        return 0;
    }
    
    @Override
    public int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int start, int rows)
            throws Exception {
        return 0;
    }
    
    @Override
    public Object[] executeReturnGeneratedKeys(SqlParams params, String[] rows) throws Exception {
        
        return new Object[0];
    }
    
    // ------------------------ 私有方法 ------------------------
    private void fillPrepareStatement(PreparedStatement ps, SqlParams params) throws SQLException {
        Object[] arg = params.getParams();
        int[] sqlTypes = params.getTypes();
        if (ps != null) {
            if (ArrayHelper.isNotEmpty(arg)) {
                if (ArrayHelper.isNotEmpty(sqlTypes)) {
                    int min = Math.min(arg.length, sqlTypes.length);
                    for (int i = 0; i < min; ++i) {
                        ps.setObject(i + 1, arg[i], sqlTypes[i]);
                    }
                } else {
                    for (int i = 0; i < arg.length; i++) {
                        ps.setObject(i + 1, arg[i]);
                    }
                }
            }
        }
        
    }
    
}
