package com.pikachu.framework.database.reader;

import com.pikachu.common.database.core.IDataReader;

import java.sql.ResultSet;

/**
 * @Desc
 * @Date 2019-12-13 20:05
 * @Author AD
 */
public class DaoCountReader implements IDataReader {
    
    private int count = -1;
    
    @Override
    public int read(ResultSet rs) throws Exception {
        if (rs.next()) {
            this.count = rs.getInt(1);
            return 1;
        }
        return 0;
    }
    
    public int getCount() {
        return this.count;
    }
    
}
