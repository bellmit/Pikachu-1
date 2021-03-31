package com.pikachu.common.database.reader;

import com.pikachu.common.collection.DataSet;
import com.pikachu.common.database.core.IDataReader;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

/**
 * @Desc
 * @Date 2019-11-08 17:33
 * @Author AD
 */
public class RowsReader implements IDataReader {

    private List<DataSet> datas;

    @Override
    public int read(ResultSet rs) throws Exception {
        int row = 0;
        datas = new ArrayList<>();
        ResultSetMetaData meta = rs.getMetaData();
        for (; rs.next(); ++row) {
            DataSet data = new DataSet();
            int column = meta.getColumnCount();
            for (int k = 1; k <= column; ++k) {
                data.add(meta.getColumnName(k), rs.getObject(k));
            }
            datas.add(data);

        }
        return row;
    }

    public DataSet[] getRowsData() {
        return datas.toArray(new DataSet[0]);
    }

}
