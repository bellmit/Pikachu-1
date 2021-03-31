package com.x.pikachu.framework.caching.methods;

import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.framework.database.core.SQLHelper;

import java.lang.reflect.Method;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc Bean类所有方法数据，一般是Get、Set
 * @Date 2019-12-05 22:13
 * @Author AD
 */
public final class MethodData {

    // ------------------------ 变量定义 ------------------------
    // bean类型
    private final Class<?> dataClass;

    // get方法信息数组
    private final MethodInfo[] methodsGet;

    // set方法信息数组
    private final MethodInfo[] methodsSet;

    // get方法信息数组map
    private final Map<String, MethodInfo> methodsGetMap = new HashMap<>();

    // set方法信息数组map
    private final Map<String, MethodInfo> methodsSetMap = new HashMap<>();

    // ------------------------ 构造方法 ------------------------

    /**
     * 构造方法
     *
     * @param dataClass 数据类型
     * @param dbType    数据库类型，可为null
     */
    public MethodData(Class<?> dataClass, DatabaseType dbType) {
        this.dataClass = dataClass;
        List<MethodInfo> sets = new ArrayList<>();
        List<MethodInfo> gets = new ArrayList<>();
        /**
         * 获取所有的方法进行遍历，将bean里面的方法封装成MethodInfo对象
         * 同时以大写属性名作为key存入map
         */
        for (Method method : dataClass.getDeclaredMethods()) {
            String name = method.getName();
            String NAME = name.toUpperCase();
            String column;
            String propName;
            int returnSqlType;
            if (NAME.startsWith("GET")) {
                // 映射表的字段名,也是key
                column = NAME.substring(3);
                // 属性名
                propName = name.substring(3);
                // 返回值所对应的数据库表sql类型
                returnSqlType = SQLHelper.getReturnSqlType(method);
                gets.add(new MethodInfo(column, propName, method, dbType, returnSqlType));
            } else if (NAME.startsWith("SET")) {
                // 获取参数类型
                int paramSqlType = SQLHelper.getParameterSqlType(method);
                if (paramSqlType != Types.NULL) {
                    // 字段名
                    column = NAME.substring(3);
                    // 属性名
                    propName = name.substring(3);
                    sets.add(new MethodInfo(column, propName, method, dbType, paramSqlType));
                }
            } else if (NAME.startsWith("IS")) {
                column = NAME.substring(2);
                propName = name.substring(2);
                returnSqlType = SQLHelper.getReturnSqlType(method);
                gets.add(new MethodInfo(column, propName, method, dbType, returnSqlType));
            }
        }

        this.methodsGet = new MethodInfo[gets.size()];

        MethodInfo methodInfo;
        for (int i = 0, L = gets.size(); i < L; ++i) {
            methodInfo = gets.get(i);
            this.methodsGet[i] = methodInfo;
            this.methodsGetMap.put(methodInfo.getKey(), methodInfo);
        }

        this.methodsSet = new MethodInfo[sets.size()];

        for (int i = 0, L = sets.size(); i < L; ++i) {
            methodInfo = sets.get(i);
            this.methodsSet[i] = methodInfo;
            this.methodsSetMap.put(methodInfo.getKey(), methodInfo);
        }

    }

    // ------------------------ 方法定义 ------------------------

    /**
     * 获取 bean类型
     */
    public Class<?> getDataClass() {
        return this.dataClass;
    }

    /**
     * 获取 get方法信息数组
     */
    public MethodInfo[] getMethodsGet() {
        return this.methodsGet;
    }

    /**
     * 获取 set方法信息数组
     */
    public MethodInfo[] getMethodsSet() {
        return this.methodsSet;
    }

    /**
     * 获取 get方法信息数组map
     */
    public Map<String, MethodInfo> getMethodsGetMap() {
        return this.methodsGetMap;
    }

    /**
     * 获取 set方法信息数组map
     */
    public Map<String, MethodInfo> getMethodsSetMap() {
        return this.methodsSetMap;
    }

}
