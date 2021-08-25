package com.pikachu.common.collection;

/**
 * 数据库操作符枚举
 *
 * @author AD
 * @date 2021/8/25 21:11
 */
public enum Operator {
    IN(" IN "),
    LIKE(" LIKE "),
    EQUALS("="),
    NO_EQUALS("<>"),
    GREATER(">"),
    GREATER_EQUALS(">="),
    LESS("<"),
    LESS_EQUALS("<=");
    
    private final String key;
    
    private Operator(String key) {this.key = key;}
    
    public String getKey() {
        return key;
    }
}
