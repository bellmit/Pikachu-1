package com.x.pikachu.common.database.reader;

import com.x.pikachu.common.collection.DataSet;
import com.x.pikachu.common.database.core.IDataReader;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * @Desc
 * @Date 2019-11-08 17:33
 * @Author AD
 */
public class RowReader implements IDataReader {

    private DataSet data;

    public RowReader() {
    }

    @Override
    public int read(ResultSet rs) throws Exception {
        rs.setFetchSize(1);
        if (!rs.next()) {
            return 0;
        }
        this.data = new DataSet();
        ResultSetMetaData meta = rs.getMetaData();
        int count = meta.getColumnCount();
        for (int i = 1; i <= count; ++i) {
            data.add(meta.getColumnName(i), rs.getObject(i));
        }
        return 1;
    }

    public DataSet getRowData() {
        return this.data;
    }

}
