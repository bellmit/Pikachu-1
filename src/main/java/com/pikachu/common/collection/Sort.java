package com.pikachu.common.collection;

/**
 * 排序枚举类
 *
 * @author AD
 * @date 2021/8/25 22:36
 */
public enum Sort {
    ASC("ASC"),
    DESC("DESC");
    
    private final String code;
    
    private Sort(String code) {this.code = code;}
    
    public String getCode() {
        return code;
    }
}
