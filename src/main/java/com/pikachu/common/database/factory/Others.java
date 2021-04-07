package com.pikachu.common.database.factory;

import com.pikachu.common.database.core.Database;
import com.pikachu.common.database.core.IDataReader;
import com.pikachu.common.database.pool.core.IPool;

import java.sql.ResultSet;

/**
 * @Desc TODO
 * @Date 2019-11-29 22:46
 * @Author AD
 */
public class Others extends Database {
    
    public Others(IPool pool) {
        super(pool);
    }
    
    public int executeReader(IDataReader reader, String sql, Object[] args, int[] sqlTypes, int startIndex, int rows) throws Exception {
        return executeReader(new OtherReader(reader, startIndex), sql, args, sqlTypes);
    }
    
    private static class OtherReader implements IDataReader {
        
        private IDataReader reader;
        
        private int startIndex;
        
        OtherReader(IDataReader reader, int startIndex) {
            this.reader = reader;
            this.startIndex = startIndex;
        }
        
        @Override
        public int read(ResultSet resultSet) throws Exception {
            resultSet.absolute(this.startIndex + 1);
            return reader.read(resultSet);
        }
        
    }
    
}
