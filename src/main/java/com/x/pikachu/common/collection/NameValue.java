package com.x.pikachu.common.collection;

import java.io.Serializable;
import java.util.StringJoiner;

/**
 * @Desc
 * @Date 2019-11-08 12:08
 * @Author AD
 */
public final class NameValue implements Serializable {
    private static final long serialVersionUID = 1L;

    // ---------------------- 成员变量 ----------------------

    private String name;

    private String value;

    // ---------------------- 构造方法 ----------------------

    public NameValue(String name, String value) {
        this.name = name;
        this.value = value;
    }

    // ---------------------- 成员方法 ----------------------

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", NameValue.class.getSimpleName() + "[", "]")
                .add("name='" + name + "'")
                .add("value='" + value + "'")
                .toString();
    }
    
}
