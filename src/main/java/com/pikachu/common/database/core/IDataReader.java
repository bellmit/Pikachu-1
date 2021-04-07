package com.pikachu.common.database.core;

import java.sql.ResultSet;

/**
 * @Desc 数据读取器（策略模式）
 * @Date 2020/11/21 00:03
 * @Author AD
 */
public interface IDataReader {
    
    /**
     * 读取结果集
     *
     * @param rs 结果集
     *
     * @return 结果条数
     *
     * @throws Exception
     */
    int read(ResultSet rs) throws Exception;
    
}
