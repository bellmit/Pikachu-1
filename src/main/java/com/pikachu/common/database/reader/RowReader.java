package com.pikachu.common.database.reader;

import com.pikachu.common.collection.DataSet;
import com.pikachu.common.database.core.IDataReader;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @Desc 第一行数据读取器
 * @Date 2019-11-08 17:33
 * @Author AD
 */
public class RowReader implements IDataReader {

    private DataSet data;

    public RowReader() {
    }
    
    /**
     * 读取一行数据
     * @param rs 结果集
     *
     * @return int 0-没有数据 1-有数据
     * @throws Exception
     */
    @Override
    public int read(ResultSet rs) throws Exception {
        // 设置读取一行
        rs.setFetchSize(1);
        // 没有值
        if (!rs.next()) {
            return 0;
        }
        // 创建结果集对象
        this.data = new DataSet();
        // 获取元数据
        ResultSetMetaData meta = rs.getMetaData();
        // 获取列数
        int count = meta.getColumnCount();
        // 将"键-值"存入结果集
        for (int i = 1; i <= count; ++i) {
            data.add(meta.getColumnName(i), rs.getObject(i));
        }
        return 1;
    }
    
    /**
     * 获取数据集
     * @return
     */
    public DataSet getRowData() {
        return this.data;
    }

}
