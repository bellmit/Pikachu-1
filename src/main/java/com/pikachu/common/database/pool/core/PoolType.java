package com.pikachu.common.database.pool.core;

/**
 * @Desc 数据库连接池类型枚举
 * @Date 2020/12/4 20:06
 * @Author AD
 */
public enum PoolType {
    HIKARI,
    DRUID;
    
    public static PoolType getPoolType(String poolType) {
        for (PoolType type : PoolType.values()) {
            if (type.toString().equalsIgnoreCase(poolType)) {
                return type;
            }
        }
        return null;
    }
}
