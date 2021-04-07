package com.pikachu.common.database.reader;

import com.pikachu.common.collection.DataSet;
import com.pikachu.common.database.core.IDataReader;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc 多行数据读取器
 * @Date 2019-11-08 17:33
 * @Author AD
 */
public class RowsReader implements IDataReader {

    private List<DataSet> datas;
    
    /**
     * 读取多行结果
     * @param rs 结果集
     *
     * @return int 行数（0-无数据，1～N有数据，并且有1～N行）
     * @throws Exception
     */
    @Override
    public int read(ResultSet rs) throws Exception {
        int row = 0;
        datas = new ArrayList<>();
        // 获取元数据（列名）
        ResultSetMetaData meta = rs.getMetaData();
        // 判断是否有下一行，移动rs的游标，并自增行数
        for (; rs.next(); ++row) {
            // 创建结果集对象
            DataSet data = new DataSet();
            // 获取列数
            int column = meta.getColumnCount();
            // 获取"键-值"存入结果集
            for (int k = 1; k <= column; ++k) {
                data.add(meta.getColumnName(k), rs.getObject(k));
            }
            // 存入一行数据
            datas.add(data);
        }
        // 返回行数
        return row;
    }
    
    /**
     * 获取多行结果
     * @return
     */
    public DataSet[] getRowsData() {
        return datas.toArray(new DataSet[0]);
    }

}
