package com.pikachu.common.collection;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Desc
 * @Date 2019-12-08 13:09
 * @Author AD
 */
public class KeyValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private String k;

    private Object v;

    public KeyValue() {
    }

    public KeyValue(String key, Object value) {
        this.k = key;
        this.v = value;
    }

    public String getK() {
        return this.k;
    }

    public void setK(String key) {
        this.k = key;
    }

    public Object getV() {
        return this.v;
    }

    public void setV(Object value) {
        this.v = value;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", KeyValue.class.getSimpleName() + "[", "]")
                .add("k='" + k + "'")
                .add("v=" + v)
                .toString();
    }
    
}
