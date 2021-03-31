package com.x.pikachu.common.database.core;

/**
 * @Desc 数据库工厂
 * @Date 2020/11/21 00:02
 * @Author AD
 */
public interface IDatabase {
    
    /**
     * 单条执行
     *
     * @param params sql参数
     *
     * @return
     *
     * @throws Exception
     */
    int execute(String sql, Object[] params, int[] sqlTypes) throws Exception;
    
    /**
     * 批量执行
     *
     * @param sqls sql语句
     *
     * @return
     *
     * @throws Exception
     */
    int[] executeBatch(String[] sqls) throws Exception;
    
    /**
     * 使用数据读取器读取数据
     *
     * @param reader
     * @param params
     *
     * @return
     *
     * @throws Exception
     */
    int executeReader(IDataReader reader, String sql, Object[] params, int[] sqlTypes) throws Exception;
    
    /**
     * 使用数据读取器分页读取
     *
     * @param reader   数据读取器
     * @param table    数据库表
     * @param args     SQL参数
     * @param sqlTypes SQL参数类型，参见java.sql.Types
     * @param start    开始行
     * @param rows     总共读取的行数
     *
     * @return
     *
     * @throws Exception
     */
    int executeReader(IDataReader reader, String table, Object[] args, int[] sqlTypes, int start, int rows) throws Exception;
    
    /**
     * 执行SQL语句并返回主键
     *
     * @param params
     * @param rows
     *
     * @return
     *
     * @throws Exception
     */
    Object[] executeReturnGeneratedKeys(String sql, Object[] params, int[] sqlTypes, String[] rows) throws Exception;
    
}
