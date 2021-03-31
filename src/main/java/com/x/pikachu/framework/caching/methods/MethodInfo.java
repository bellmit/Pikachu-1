package com.x.pikachu.framework.caching.methods;

import com.x.pikachu.common.database.core.DatabaseType;

import java.lang.reflect.Method;

/**
 * 单个方法信息，如：getXXX或setXXX(T t)
 *
 * @Date 2019-12-05 22:05
 * @Author AD
 */
public final class MethodInfo {

    // ------------------------ 变量定义 ------------------------
    // 属性大写，即映射表的字段名（去掉get或set后的属性名大写）
    private final String key;

    // 属性名(setXXX,去掉get或set之后的名字)
    private final String propName;

    // 方法对象
    private final Method method;

    // 数据库类型
    private final DatabaseType dbType;

    // sql类型（get返回值的类型，或set设置值的类型）
    private final int sqlType;

    // ------------------------ 构造方法 ------------------------

    public MethodInfo(String key, String propName, Method method, DatabaseType dbType, int sqlType) {
        this.key = key;
        this.propName = propName;
        this.method = method;
        this.dbType = dbType;
        this.sqlType = sqlType;
    }
    // ------------------------ 方法定义 ------------------------

    /**
     * 获取
     */
    public String getKey() {
        return this.key;
    }

    /**
     * 获取 属性名
     */
    public String getPropName() {
        return this.propName;
    }

    /**
     * 获取 方法对象
     */
    public Method getMethod() {
        return this.method;
    }

    /**
     * 获取 数据库类型
     */
    public DatabaseType getDbType() {
        return this.dbType;
    }

    /**
     * 获取 sql类型
     */
    public int getSqlType() {
        return this.sqlType;
    }

}
