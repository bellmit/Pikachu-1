package com.pikachu.framework.database.core;

import com.pikachu.common.collection.IFunction;
import com.pikachu.common.util.Lambdas;

/**
 * @author AD
 * @date 2021/8/25 23:30
 */
public class Update {
    
    private final String k;
    
    private final Object v;
    
    public static Update get(IFunction k, Object v) {
        String column = Lambdas.getColumns(k);
        return new Update(column, v);
    }
    
     Update(String k, Object v) {
        this.k = k;
        this.v = v;
    }
    
    public String getK() {
        return k;
    }
    
    public Object getV() {
        return v;
    }
    
}
