package com.pikachu.framework.database.core;

import com.pikachu.common.collection.IFunction;
import com.pikachu.common.collection.Sort;
import com.pikachu.common.util.Lambdas;

import java.util.StringJoiner;

/**
 * @Desc
 * @Date 2019-12-08 13:09
 * @Author AD
 */
public class Order {
    
    private final String k;
    
    private final Sort v;
    
    private Order(String k, Sort v) {
        this.k = k;
        this.v = v;
    }
    
    public static Order[] asc(IFunction... ks) {
        return get(Sort.ASC, ks);
    }
    
    public static Order[] desc(IFunction... ks) {
        return get(Sort.DESC, ks);
    }
    
    private static Order[] get(Sort sort, IFunction... ks) {
        Order[] orders = new Order[ks.length];
        for (int i = 0; i < ks.length; i++) {
            String column = Lambdas.getColumns(ks[i]);
            Order order = new Order(column, sort);
            orders[i] = order;
        }
        return orders;
    }
    
    public String getK() {
        return this.k;
    }
    
    public Sort getV() {
        return this.v;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Order.class.getSimpleName() + "[", "]")
                .add("k='" + k + "'")
                .add("v=" + v)
                .toString();
    }
    
}
