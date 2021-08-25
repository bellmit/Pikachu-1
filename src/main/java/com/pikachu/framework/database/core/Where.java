package com.pikachu.framework.database.core;

import com.pikachu.common.collection.IFunction;
import com.pikachu.common.collection.Operator;
import com.pikachu.common.util.Lambdas;

import java.io.Serializable;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * @Desc
 * @Date 2019-11-29 23:16
 * @Author AD
 */
public class Where implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // 条件名称（属性名称）
    private String k;
    
    // "操作方式（ 包括：>, =, <, <>, >=, <=, like, in ）
    private Operator o;
    
    // 条件值（属性值）
    private Object v;
    
    public static Where[] getLike(Object v, IFunction... ks) {
        return get(v, Operator.LIKE, ks);
    }
    
    public static Where[] getIn(Object v, IFunction... ks) {
        return get(v, Operator.IN, ks);
    }
    
    public static Where[] getEquals(Object v, IFunction... ks) {
        return get(v, Operator.EQUALS, ks);
    }
    
    public static Where[] getNoEquals(Object v, IFunction... ks) {
        return get(v, Operator.NO_EQUALS, ks);
    }
    
    public static Where[] getGreater(Object v, IFunction... ks) {
        return get(v, Operator.GREATER, ks);
    }
    
    public static Where[] getLess(Object v, IFunction... ks) {
        return get(v, Operator.LESS, ks);
    }
    
    public static Where[] getGreaterEquals(Object v, IFunction... ks) {
        return get(v, Operator.GREATER_EQUALS, ks);
    }
    
    public static Where[] getLessEquals(Object v, IFunction... ks) {
        return get(v, Operator.LESS_EQUALS, ks);
    }
    
    public Where() {}
    
    public Where(Object v, Operator o, IFunction... ks) {
        this.k = Lambdas.getColumns(ks);
        this.o = o;
        this.v = v;
    }
    
     Where(Object v, Operator o, String k) {
        this.k = k;
        this.o = o;
        this.v = v;
    }
    
    private static Where[] get(Object v, Operator o, IFunction... ks) {
        String columns = Lambdas.getColumns(ks);
        return new Where[]{new Where(v, o, columns)};
    }
    
    /**
     * 获取 条件名称（属性名称）
     */
    public String getK() {
        return this.k;
    }
    
    /**
     * 设置 条件名称（属性名称）
     */
    public void setK(IFunction... ks) {
        this.k = Lambdas.getColumns(ks);
    }
    
    /**
     * 获取 操作方式（ 包括：>, =, <, <>, >=, <=, like ）
     */
    public Operator getO() {
        return this.o;
    }
    
    /**
     * 设置 操作方式（ 包括：>, =, <, <>, >=, <=, like ）
     */
    public void setO(Operator o) {
        this.o = o;
    }
    
    /**
     * 获取 条件值（属性值）
     */
    public Object getV() {
        return this.v;
    }
    
    /**
     * 设置 条件值（属性值）
     */
    public void setV(Object v) {
        this.v = v;
    }
    
   
    
   
    
    @Override
    public int hashCode() {
        return Objects.hash(k, o, v);
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Where.class.getSimpleName() + "[", "]")
                .add("k='" + k + "'")
                .add("o='" + o + "'")
                .add("v=" + v)
                .toString();
    }
    
}
