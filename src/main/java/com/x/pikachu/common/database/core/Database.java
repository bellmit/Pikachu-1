package com.x.pikachu.common.database.core;

import com.x.pikachu.common.database.pool.core.IPool;
import com.x.pikachu.util.ArrayHelper;

import java.sql.*;

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
    
    /**
     * 单条执行
     *
     * @param params sql参数
     *
     * @return 执行所影响的行数
     *
     * @throws Exception
     */
    @Override
    public int execute(SqlParams params) throws Exception {
        // 获取连接
        try (Connection conn = pool.getConnection();
             // 获取预编译对象
             PreparedStatement ps = conn.prepareStatement(params.getSql())) {
            // 填充sql参数
            fillPrepareStatement(ps, params);
            // 返回执行结果所影响的行数
            return ps.executeUpdate();
        }
    }
    
    /**
     * 批量执行
     *
     * @param sqls sql语句
     *
     * @return
     *
     * @throws Exception
     */
    @Override
    public int[] executeBatch(String[] sqls) throws Exception {
        // 判断sql语句是否为空
        if (ArrayHelper.isEmpty(sqls)) {
            // 返回空数组
            return ArrayHelper.EMPTY_INT;
        }
        // 获取连接对象
        try (Connection conn = pool.getConnection()) {
            // 设置不自动提交
            conn.setAutoCommit(false);
            // 获取sql编译对象
            try (Statement stat = conn.createStatement()) {
                // 循环添加sql
                for (String sql : sqls) {
                    stat.addBatch(sql);
                }
                // 批量执行
                int[] ints = stat.executeBatch();
                // 设置为自动提交
                conn.setAutoCommit(true);
                // 返回每条sql语句执行所影响的行数结果
                return ints;
            }
        }
    }
    
    /**
     * 通过数据读取者读取ResultSet结果集
     *
     * @param reader 数据读取对象
     * @param params sql参数
     *
     * @return int 结果条数
     *
     * @throws Exception
     */
    @Override
    public int executeReader(IDataReader reader, SqlParams params) throws Exception {
        // 打印sql语句
        System.out.println(">>> " + params.getSql());
        // 获取连接
        try (Connection conn = pool.getConnection();
             // 获取sql预编译对象，并指定光标只能向前移动，并发类型为只读
             PreparedStatement ps = conn.prepareStatement(params.getSql(), ResultSet.TYPE_FORWARD_ONLY,
                     ResultSet.CONCUR_READ_ONLY)) {
            // 填充参数
            fillPrepareStatement(ps, params);
            // 执行查询
            ResultSet rs = ps.executeQuery();
            // 读取结果
            return reader.read(rs);
        }
    }
    
    @Override
    public Object[] executeReturnGeneratedKeys(SqlParams params, String[] rows) throws Exception {
        // 打印sql语句
        System.out.println(">>> " + params.getSql());
        // 获取连接对象
        try (Connection conn = pool.getConnection();
             // 获取一个可生成key的sql预编译对象
             PreparedStatement ps = conn.prepareStatement(params.getSql(), Statement.RETURN_GENERATED_KEYS)) {
            // 填充sql参数
            fillPrepareStatement(ps, params);
            // 定义结果数组
            Object[] result = null;
            // 执行更新并判断结果是否有效
            if (ps.executeUpdate() > 0) {
                // 获取生成的key结果集
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    // 判断是否还有key
                    if (rs.next()) {
                        if (ArrayHelper.isEmpty(rows)) {
                            int count = rs.getMetaData().getColumnCount();
                            result = new Object[count];
                            for (int i = 0; i < count; ++i) {
                                result[i] = rs.getObject(i);
                            }
                        } else {
                            result = new Object[rows.length];
                            for (int i = 0, L = rows.length; i < L; ++i) {
                                result[i] = rs.getObject(i);
                            }
                        }
                    }
                }
                conn.commit();
                
            }
            return result;
        }
    }
    
    // ------------------------ 私有方法 ------------------------
    private void fillPrepareStatement(PreparedStatement ps, SqlParams params) throws SQLException {
        // 获取参数
        Object[] arg = params.getParams();
        // 获取参数类型
        int[] sqlTypes = params.getTypes();
        // 判断预编译对象的有效性
        if (ps != null) {
            // 判断判断参数有效性
            if (ArrayHelper.isNotEmpty(arg)) {
                // 判断参数类型有效性
                if (ArrayHelper.isNotEmpty(sqlTypes)) {
                    // 取参数和参数类型之间的最小值
                    int min = Math.min(arg.length, sqlTypes.length);
                    // 以最小值为限填充参数
                    for (int i = 0; i < min; ++i) {
                        // 给预编鱼对象设置参数
                        ps.setObject(i + 1, arg[i], sqlTypes[i]);
                    }
                } else {
                    // 如果参数类型为空，则自动填充预编译对象参数
                    for (int i = 0; i < arg.length; i++) {
                        ps.setObject(i + 1, arg[i]);
                    }
                }
            }
        }
        
    }
    
}
