package com.pikachu.framework.database.core;

import com.pikachu.common.database.core.DatabaseType;
import com.pikachu.common.util.PikachuArrays;
import com.pikachu.common.util.PikachuConverts;
import com.pikachu.common.util.PikachuStrings;
import com.pikachu.framework.caching.methods.MethodData;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.framework.caching.methods.MethodManager;
import com.pikachu.framework.database.reader.DaoBeanReader;
import com.pikachu.framework.database.reader.DaoListReader;
import com.pikachu.framework.database.reader.DaoPageReader;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Desc SQL信息，根据bean类、注解信息创建对应的sql语句
 * @Date 2019-12-05 22:01
 * @Author AD
 */
public class SQLInfo<T> {

    // 数据库表名
    private String tableName;

    // 创建语句
    private String createSQL;

    // 删除语句
    private String deleteSQL;

    // 查找语句
    private String retrieveSQL;

    // 查询条数语句
    private String countSQL;

    // 更新语句
    private String updateSQL;

    // 更新Bean对象语句
    private String updateBeanSQL;

    // 主键
    private String retrievePK;

    // 数据库类型
    private DatabaseType dbType;

    // 所对应的java类
    private Class<T> dataClass;

    // 缓存
    private boolean caching;

    // 缓存历史
    private boolean cachingHistory;

    // 主键数组
    private String[] primaryKeys = PikachuArrays.EMPTY_STRING;

    // 大写PK
    private List<String> PKList = new ArrayList<>();

    // 类里面的所有方法数据
    private MethodData methods;

    /**
     * 构造方法
     *
     * @param dataClass Bean类
     * @param tableInfo 带注解的表信息，如@XData
     * @param dbType    数据库类型
     */
    public SQLInfo(Class<T> dataClass, TableInfo tableInfo, DatabaseType dbType) {

        String tableName;
        // 没有用注解
        if (tableInfo == null) {
            tableName = dataClass.getSimpleName();
            this.caching = false;
            this.cachingHistory = false;
        } else {
            // 用注解
            tableName = tableInfo.getTableName();
            if (PikachuStrings.isNull(tableName)) {
                tableName = dataClass.getSimpleName();
            }
            this.caching = tableInfo.isCaching();
            this.cachingHistory = tableInfo.isCachingHistory();
        }

        // 根据数据库类型修正表名
        if (dbType != DatabaseType.MYSQL) {
            if (dbType == DatabaseType.SQLSERVER) {
                tableName = "[" + tableName + "]";
            } else {
                tableName = "\"" + tableName + "\"";
            }
        }
        // 表名转换为大写
        tableName = tableName.toUpperCase();
        this.tableName = tableName;
        // 数据库类型
        this.dbType = dbType;
        // bean数据类型
        this.dataClass = dataClass;
        // bean里的方法信息
        this.methods = MethodManager.getMethodData(dataClass, dbType);
        // 删除语句
        this.deleteSQL = "DELETE FROM " + tableName;
        // 查找语句
        this.retrieveSQL = "SELECT * FROM " + tableName;
        // 数目语句
        this.countSQL = "SELECT COUNT(*) C FROM " + tableName;
        // 更新语句
        this.updateSQL = "UPDATE " + tableName + " SET ";
        // 所有的Get方法信息
        MethodInfo[] gets = methods.getMethodsGet();
        // 属性容器，即表字段容器
        List<String> columns = new ArrayList<>();
        // 初始化属性容器，用于以下主键和非主键语句的生成
        for (MethodInfo get : gets) {
            // 方法信息的key，即属性，在封装时都转为大写了
            columns.add(get.getKey());
        }
        // 生成插入语句
        this.createSQL = "INSERT INTO " + tableName + " (" + PikachuConverts.toString(
                columns) + ") VALUES (" +
                PikachuStrings.duplicate("?", ",", columns.size()) + ")";
        // 根据有注解的类,生成根据主键更新和查找的语句：Update语句和Select语句
        StringBuffer sb = new StringBuffer();
        // 判断表信息有效性
        if (tableInfo != null && tableInfo.getPrimaryKeys() != null) {
            // 获取主键数组
            String[] pks = tableInfo.getPrimaryKeys();
            // 遍历主键字段
            for (String pk : pks) {
                if (!PikachuStrings.isNull(pk)) {
                    // 转为大写
                    String PK = pk.toUpperCase();
                    PKList.add(PK);
                    // 在属性字段里，将PK的字段移除
                    columns.remove(PK);
                    if (sb.length() == 0) {
                        sb.append(PK);
                        sb.append("=?");
                    } else {
                        sb.append(" AND ");
                        sb.append(PK);
                        sb.append("=?");
                    }
                }
            }
        }
        // 有主键
        if (this.PKList.size() > 0) {
            this.primaryKeys = PKList.toArray(PikachuArrays.EMPTY_STRING);
            String pkSQL = sb.toString();
            // 根据主键Select语句
            this.retrievePK = "SELECT * FROM " + tableName + " WHERE " + pkSQL;
            if (columns.size() > 0) {
                // 根据主键Update语句
                this.updateBeanSQL =
                        "UPDATE " + tableName + " SET " + PikachuConverts.toString(columns,
                                "=?,") + "=? WHERE " + pkSQL;
            }
        } else {
            // 没有主键时，缓存和历史查询缓存都将失效
            this.caching = false;
            this.cachingHistory = false;
        }

    }

    /**
     * 根据给定的(字段名&值)创建Bean对象(反射调用set方法)
     *
     * @param columns 表字段，即Bean对象属性
     * @param values  字段值
     * @return 当前bean类型反射创建的对象
     * @throws Exception 反射机制,可能会有反射异常
     */
    public T createBean(String[] columns, Object[] values) throws Exception {
        if (columns != null && values != null && columns.length == values.length) {
            // 创建对象
            Constructor<T> c = dataClass.getDeclaredConstructor();
            c.setAccessible(true);
            T t = dataClass.newInstance();
            // 获取set方法集合
            Map<String, MethodInfo> setMap = methods.getMethodsSetMap();
            // 循环反射调用set方法赋值
            for (int i = 0, L = columns.length; i < L; ++i) {
                String column = columns[i];
                if (PikachuStrings.isNull(column)) {
                    return null;
                }
                MethodInfo info = setMap.get(column.toUpperCase());
                if (info == null) {
                    return null;
                }
                info.getMethod().invoke(t, values[i]);
            }
            return t;
        }
        return null;
    }

    /**
     * 生成insert语句：insert into table (xxx,xxx) values (?,?)
     *
     * @param t bean对象
     * @return SQLParams
     */
    public SQLParams getCreate(T t) {
        SQLParams params = SQLHelper.getCreateParams(methods.getMethodsGet(),
                methods.getMethodsSetMap(), PKList, t);
        return params == null ? null : new SQLParams(createSQL, params.getParams(),
                params.getTypes());
    }

    /**
     * 生成根据主键查找的select语句:select * from table where pk=?
     *
     * @param pksValue 主键字段值
     * @return SQLParams
     */
    public SQLParams getByPrimary(Object[] pksValue) {
        if (retrievePK != null && pksValue != null && pksValue.length == PKList.size()) {
            SQLParams sql = SQLHelper.getPrimaryParams(methods.getMethodsGetMap(), PKList, pksValue);
            return sql == null ? null : new SQLParams(retrievePK, sql.getParams(), sql.getTypes());
        } else {
            return null;
        }
    }

    /**
     * 生成select语句:select * from table where xxx=?
     *
     * @param wheres 查询条件
     * @return SQLParams
     */
    public SQLParams getRetrieve(Where[] wheres) {
        SQLParams sql = getWhere(wheres);
        return sql == null ? null : new SQLParams(retrieveSQL + sql.getSql(), sql.getParams(),
                sql.getTypes());
    }

    /**
     * 根据wheres、orders条件生成查找语句,并排序<br/>
     * select * from table where xxx=? order by xxx asc,xxx desc
     *
     * @param wheres 查询条件
     * @param orders 排序条件
     * @return SQLParams
     */
    public SQLParams getRetrieve(Where[] wheres, Order[] orders) {
        // 生成where参数和order参数
        SQLParams where = this.getWhere(wheres);
        if (where == null) {
            return null;
        }
        String orderSQL = getOrderSQL(orders);
        if (orderSQL == null) {
            return null;
        }
        String whereSQL = where.getSql();
        return new SQLParams(retrieveSQL + whereSQL + orderSQL, where.getParams(),
                where.getTypes());
    }

    /**
     * 生成查找数量语句:select count(*) c from table where xxx=?
     *
     * @param wheres 查询条件
     * @return SQLParams
     */
    public SQLParams getCount(Where[] wheres) {
        SQLParams where = this.getWhere(wheres);
        return where == null ? null : new SQLParams(
                this.countSQL + where.getSql(), where.getParams(), where.getTypes());
    }

    /**
     * 生成根据主键获取数据的语句:select count(*) c from table where pk=?
     *
     * @param bean bean对象
     * @return SQLParams
     */
    public SQLParams getCountByPrimary(T bean) throws Exception {
        SQLParams pk = SQLHelper.getPrimaryParamsByBean(methods.getMethodsGetMap(), PKList,
                bean);
        return pk == null ? null : new SQLParams(countSQL + pk.getSql(),
                pk.getParams(), pk.getTypes());
    }

    /**
     * 生成update更新参数：update table set(xxx=?,xxx=?) where xxx=? and xxx=? or xxx=?
     *
     * @param updates 更新字段&值
     * @param wheres  更新条件
     * @return SQLParams
     */
    public SQLParams getUpdate(Update[] updates, Where[] wheres) {
        // 判断update参数和where参数的有效性
        SQLParams update = SQLHelper.getUpdateParams(methods.getMethodsGetMap(), updates);
        if (update == null) {
            return null;
        }
        // update table set xxx=?,yyy=? where zzz=?
        // 获取更新语句部分：xxx=?,yyy=?
        String updateSql = update.getSql();
        if (PikachuStrings.isNull(updateSql)) {
            return null;
        }
        SQLParams where = this.getWhere(wheres);
        if (where == null) {
            return null;
        }
        String whereSQL = where.getSql();
        Object[] params = PikachuConverts.concat(update.getParams(), where.getParams());
        int[] types = PikachuConverts.concat(update.getTypes(), where.getTypes());
        // update table set
        return new SQLParams(this.updateSQL + updateSql + whereSQL, params, types);
    }

    /**
     * 生成更新语句：update table set(xxx=?,xxx=?) where pk1=? and pk2=?
     *
     * @param bean bean对象
     * @return SQLParams
     */
    public SQLParams getUpdateBean(T bean) {
        if (PikachuStrings.isNull(updateBeanSQL)) {
            return null;
        }

        SQLParams sql = SQLHelper.getUpdateBeanParams(methods.getMethodsGet(), PKList, bean);
        return sql == null ? null : new SQLParams(this.updateBeanSQL, sql.getParams(),
                sql.getTypes());
    }

    /**
     * 生成删除语句:delete from table where xxx=?
     *
     * @param deletes 删除条件
     * @return SQLParams
     */
    public SQLParams getDelete(Where[] deletes) {
        SQLParams delete = this.getWhere(deletes);
        if (delete == null) return null;

        String whereSQL = delete.getSql();
        return new SQLParams(this.deleteSQL + whereSQL, delete.getParams(),
                delete.getTypes());
    }

    public DatabaseType getType() {
        return this.dbType;
    }

    public Class<T> getDataClass() {
        return this.dataClass;
    }

    public boolean isCaching() {
        return this.caching;
    }

    public boolean isCachingHistory() {
        return this.cachingHistory;
    }

    public String[] getPrimaryKeys() {
        return this.primaryKeys;
    }

    /**
     * 获取对象的主键值
     *
     * @param t 对象
     * @return
     */
    public Object[] getPrimaryKeysValue(T t) {
        // 要求非空
        Objects.requireNonNull(t);
        // 获取主键字段
        String[] PKs = getPrimaryKeys();
        // 判断是否存在主键
        if (PKs != null && PKs.length > 0) {
            // 获取bean对象对应的get方法信息
            Map<String, MethodInfo> methodsGetMap = methods.getMethodsGetMap();
            // 创建主键值数组
            Object[] values = new Object[PKs.length];
            for (int i = 0; i < PKs.length; i++) {
                MethodInfo methodInfo = methodsGetMap.get(PKs[i]);
                try {
                    Object v = methodInfo.getMethod().invoke(t);
                    values[i] = v;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return values;
        }
        return PikachuArrays.EMPTY;
    }

    public String getTableName() {
        return this.tableName;
    }

    public SQLParams getWhere(Where[] wheres) {
        return SQLHelper.getWhereParams(methods.getMethodsGetMap(), wheres);
    }

    public String getOrderSQL(Order[] orders) {
        return SQLHelper.getOrderSQL(methods.getMethodsGetMap(), orders);
    }

    public DaoBeanReader<T> getBeanReader() {
        return new DaoBeanReader<>(dataClass, methods.getMethodsSetMap());
    }

    public DaoListReader<T> getListReader() {
        return new DaoListReader<>(dataClass, methods.getMethodsSetMap());
    }

    public DaoPageReader<T> getPageReader(int pageSize) {
        return new DaoPageReader<>(dataClass, methods.getMethodsSetMap(), pageSize);
    }

}
