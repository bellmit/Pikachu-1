package com.x.pikachu.common.collection;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/3/20 14:09
 */
public class ShortValue implements Serializable {
    private static final long serialVersionUID = 1L;

    private short k;

    private Object v;

    public ShortValue(short k, Object v) {
        this.k = k;
        this.v = v;
    }

    public short getK() {
        return k;
    }

    public void setK(short k) {
        this.k = k;
    }

    public Object getV() {
        return v;
    }

    public void setV(Object v) {
        this.v = v;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ShortValue.class.getSimpleName() + "[", "]")
                .add("k=" + k)
                .add("v=" + v)
                .toString();
    }
    
}
