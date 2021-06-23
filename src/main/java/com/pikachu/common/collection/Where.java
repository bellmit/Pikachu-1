package com.pikachu.common.collection;

import java.io.Serializable;
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
    private String o;
    // 条件值（属性值）
    private Object v;
    
    public static Where[] getLikeWhere(String k, Object v) {
        return new Where[]{new Where(k, "like", v)};
    }
    
    public static Where[] getInWhere(String k, Object v) {
        return new Where[]{new Where(k, "in", v)};
    }
    
    public static Where[] getEqualsWhere(String k, Object v) {
        return new Where[]{new Where(k, "=", v)};
    }
    
    public Where() {}
    
    public Where(String k, String o, Object v) {
        this.k = k;
        this.o = o;
        this.v = v;
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
    public void setK(String k) {
        this.k = k;
    }
    
    /**
     * 获取 操作方式（ 包括：>, =, <, <>, >=, <=, like ）
     */
    public String getO() {
        return this.o;
    }
    
    /**
     * 设置 操作方式（ 包括：>, =, <, <>, >=, <=, like ）
     */
    public void setO(String o) {
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
    public String toString() {
        return new StringJoiner(", ", Where.class.getSimpleName() + "[", "]")
                .add("k='" + k + "'")
                .add("o='" + o + "'")
                .add("v=" + v)
                .toString();
    }
    
}
