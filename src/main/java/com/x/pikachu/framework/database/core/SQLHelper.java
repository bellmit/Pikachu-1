package com.x.pikachu.framework.database.core;

import com.x.pikachu.common.collection.KeyValue;
import com.x.pikachu.common.collection.Where;
import com.x.pikachu.common.database.core.DatabaseType;
import com.x.pikachu.common.util.PikachuConverts;
import com.x.pikachu.common.util.PikachuStrings;
import com.x.pikachu.framework.caching.methods.MethodInfo;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

/**
 * @Desc SQL工具类
 * @Date 2019-12-05 22:17
 * @Author AD
 */
public final class SQLHelper {
    // ------------------------ 变量定义 ------------------------
    
    // ------------------------ 构造方法 ------------------------
    
    private SQLHelper() {}
    
    // ------------------------ 方法定义 ------------------------
    
    /**
     * 获取返回值类型所对应的sql类型
     *
     * @param method get方法对象
     *
     * @return int-sql对应类型
     */
    public static int getReturnSqlType(Method method) {
        return TypeMapping.getSQLType(method.getReturnType());
    }
    
    /**
     * 获取方法第一个参数类型所对应的sql类型
     *
     * @param method set方法对象
     *
     * @return int-sql对应类型
     */
    public static int getParameterSqlType(Method method) {
        Class[] classes = method.getParameterTypes();
        return classes.length == 1 ? TypeMapping.getSQLType(classes[0]) : Types.NULL;
    }
    
    /**
     * 生成where数组（查找条件）
     *
     * @param columns 字段,映射bean类的属性
     * @param values  字段值
     *
     * @return Where[]
     */
    public static Where[] getWheres(String[] columns, Object[] values) {
        if (isValid(columns, values)) {
            Where[] wheres = new Where[columns.length];
            
            for (int i = 0, L = columns.length; i < L; ++i) {
                wheres[i] = new Where(columns[i], "=", values[i]);
            }
            
            return wheres;
        } else {
            return null;
        }
    }
    
    /**
     * 生成更新信息
     *
     * @param columns 需更新的字段
     * @param values  字段值
     *
     * @return KeyValue[]
     */
    public static KeyValue[] getUpdates(String[] columns, Object[] values) {
        if (isValid(columns, values)) {
            KeyValue[] kvs = new KeyValue[columns.length];
            
            for (int i = 0, L = columns.length; i < L; ++i) {
                kvs[i] = new KeyValue(columns[i], values[i]);
            }
            
            return kvs;
        } else {
            return null;
        }
    }
    
    /**
     * 将bean对象转换成可写入数据库的参数（生成唯一主键,将boolean转为char的Y或N）
     *
     * @param gets    get方法数组
     * @param setsMap set方法映射
     * @param pks     主键集合
     * @param bean    bean对象
     *
     * @return SQLParams
     */
    public static SQLParams getCreateParams(MethodInfo[] gets, Map<String, MethodInfo> setsMap, List<String> pks,
            Object bean) {
        Object[] params = new Object[gets.length];
        int[] sqlTypes = new int[gets.length];
        String pk = pks.size() == 1 ? pks.get(0) : null;
        
        for (int i = 0; i < gets.length; ++i) {
            MethodInfo get = gets[i];
            DatabaseType dbType = get.getDbType();
            int sqlType = get.getSqlType();
            
            try {
                // 获取bean所对应字段的参数
                Object param = get.getMethod().invoke(bean);
                // 获取字段（大写）
                String column = get.getKey();
                // 生成主键（bean类唯一字符串主键,如果未设置,则自动生成）
                if (sqlType == Types.VARCHAR) {
                    // 当bean类有唯一字符串主键时,自动生成32位uuid作为主键
                    if ((param == null || "".equals(param)) && column.equals(pk)) {
                        param = PikachuStrings.UUID();
                        MethodInfo set = setsMap.get(column);
                        // 反射调用设置主键
                        if (set != null) {
                            set.getMethod().invoke(bean, param);
                        }
                    }
                } else {
                    // 主要转换boolean->数据库char的Y或N
                    param = toSQLData(param, dbType, sqlType, get.getMethod().getReturnType());
                }
                params[i] = param;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            sqlTypes[i] = sqlType;
        }
        return new SQLParams("", params, sqlTypes);
    }
    
    /**
     * 将数据库里的值转为对应的Java值
     *
     * @param dbValue column值
     * @param set     set方法信息
     *
     * @return java类型值
     */
    public static Object toJavaData(Object dbValue, MethodInfo set) {
        int sqlType = set.getSqlType();
        switch (sqlType) {
            case Types.TINYINT:
                return PikachuConverts.toByte(dbValue);
            case Types.BIGINT:
                return PikachuConverts.toLong(dbValue);
            case Types.CHAR:
                Class<?> paramType = set.getMethod().getParameterTypes()[0];
                TypeMapping mapper = TypeMapping.getMapper(paramType);
                if (TypeMapping.BOOL != mapper) {
                    return dbValue == null ? null : String.valueOf(dbValue).trim();
                } else {
                    return dbValue != null && "Y".equals(String.valueOf(dbValue));
                }
            case Types.NUMERIC:
                return PikachuConverts.toBigDecimal(dbValue);
            case Types.INTEGER:
                return PikachuConverts.toInt(dbValue);
            case Types.SMALLINT:
                return PikachuConverts.toShort(dbValue);
            case Types.FLOAT:
                return PikachuConverts.toFloat(dbValue);
            case Types.DOUBLE:
                return PikachuConverts.toDouble(dbValue);
            case Types.VARCHAR:
                return dbValue == null ? null : String.valueOf(dbValue).trim();
            case Types.TIMESTAMP:
                Date date = null;
                if (dbValue == null) {
                    return null;
                } else if (dbValue instanceof java.sql.Date) {
                    java.sql.Date sqlDate = (java.sql.Date) dbValue;
                    date = new Date(sqlDate.getTime());
                } else if (dbValue instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) dbValue;
                    date = new Date(timestamp.getTime());
                }
                if (date != null) {
                    Class<?>[] paramTypes = set.getMethod().getParameterTypes();
                    if (paramTypes != null && paramTypes.length > 0) {
                        Class<?> param = paramTypes[0];
                        if (LocalDateTime.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(date);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (LocalDate.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(date).toLocalDate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (LocalTime.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(date).toLocalTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return date;
                }
                return dbValue;
            default:
                return dbValue;
        }
    }
    
    public static SQLParams getWhereParams(Map<String, MethodInfo> getsMap, Where[] wheres) {
        if (wheres != null && wheres.length > 0) {
            StringBuilder sb = new StringBuilder();
            List<Object> params = new ArrayList<>();
            List<Integer> sqlTypeList = new ArrayList<>();
            int wheresLen = wheres.length;
            int index = 0;
            while (true) {
                if (index < wheresLen) {
                    Where where = wheres[index];
                    if (where == null) {
                        return null;
                    }
                    String column = where.getK();
                    if (column != null) {
                        String o = fixOperation(where.getO());
                        if (o == null || o.trim().length() == 0) {
                            return null;
                        }
                        Object v = where.getV();
                        // 用空白符分割
                        String[] keys = column.toUpperCase().split("\\s*,\\s*");
                        if (keys.length > 1) {
                            if (sb.length() == 0) {
                                sb.append("(");
                            } else {
                                sb.append(" AND (");
                            }
                            int whereCount = 0;
                            for (String key : keys) {
                                if (getsMap.containsKey(key)) {
                                    if (whereCount > 0) {
                                        sb.append(" OR ");
                                    }
                                    analyzeWhere(key, o, v, getsMap, sb, params,
                                            sqlTypeList);
                                    ++whereCount;
                                }
                            }
                            if (whereCount == 0) {
                                return null;
                            }
                            sb.append(")");
                        } else {
                            if (sb.length() > 0) {
                                sb.append(" AND ");
                            }
                            if (!analyzeWhere(keys[0], o, v, getsMap, sb, params,
                                    sqlTypeList)) {
                                return null;
                            }
                        }
                        ++index;
                        continue;
                    }
                    return null;
                }
                int[] sqlTypes = new int[sqlTypeList.size()];
                wheresLen = 0;
                for (index = sqlTypeList.size(); wheresLen < index; ++wheresLen) {
                    sqlTypes[wheresLen] = sqlTypeList.get(wheresLen);
                }
                return new SQLParams(" WHERE " + sb.toString(),
                        params.toArray(new Object[0]), sqlTypes);
            }
        } else {
            return new SQLParams("", null, null);
        }
    }
    
    /**
     * 生成排序语句
     *
     * @param gets   bean对象的方法,用来和orders里的key比对是否存在该方法
     * @param orders 排序条件
     *
     * @return 排序语句, 如：order by xxx asc,xxx desc
     */
    public static String getOrderSQL(Map<String, MethodInfo> gets, KeyValue[] orders) {
        if (orders != null && orders.length > 0) {
            StringBuilder sb = new StringBuilder(" ORDER BY ");
            boolean failed = false;
            
            for (KeyValue order : orders) {
                // 获取key,并判断是否有效
                String k = order.getK();
                if (k == null) {
                    return null;
                }
                
                // 获取value,判断是asc(升序)或desc(降序)
                String v = String.valueOf(order.getV());
                if (v != null) {
                    v = v.toUpperCase();
                    if (!"DESC".equals(v) && !"ASC".equals(v)) {
                        return null;
                    }
                } else {
                    v = "ASC";
                }
                
                // 判断该排序字段key的有效性
                MethodInfo method = gets.get(k.toUpperCase());
                if (method == null) return null;
                
                // 生成sql语句
                sb.append(method.getKey());
                sb.append(" ");
                sb.append(v);
                sb.append(",");
                if (!failed) {
                    failed = true;
                }
            }
            return failed ? sb.deleteCharAt(sb.length()).toString() : "";
        } else {
            return "";
        }
    }
    
    /**
     * 生成更新参数（sql语句,参数值,sql类型）
     *
     * @param gets    bean类的get方法
     * @param updates 更新条件
     *
     * @return SQLParams sql参数对象
     */
    public static SQLParams getUpdateParams(Map<String, MethodInfo> gets, KeyValue[] updates) {
        if (updates != null && updates.length != 0) {
            // 创建容器
            StringBuilder sb = new StringBuilder();
            Object[] params = new Object[updates.length];
            int[] sqlTypes = new int[updates.length];
            // 循环生成
            for (int i = 0; i < updates.length; ++i) {
                // 获取更新字段,判断有效性
                KeyValue update = updates[i];
                String k = update.getK();
                if (PikachuStrings.isNull(k)) {return null;}
                
                // 获取方法,判断是否存在此属性
                MethodInfo get = gets.get(k.toUpperCase());
                if (get == null) return null;
                
                // 转换为sql参数
                params[i] = toSQLData(update.getV(), get.getDbType(),
                        get.getSqlType(),
                        get.getMethod().getReturnType());
                // 获取sql类型
                sqlTypes[i] = get.getSqlType();
                
                // 追加update sql
                sb.append(get.getKey());
                sb.append("=?");
                sb.append(",");
            }
            return new SQLParams(sb.deleteCharAt(sb.length() - 1).toString(), params, sqlTypes);
        } else {
            return null;
        }
    }
    
    /**
     * 根据 bean,pks 生成where sql语句
     *
     * @param gets get方法
     * @param pks  主键集合
     * @param bean 改bean类对象
     *
     * @return SQLParams sql参数对象
     *
     * @throws Exception sql参数值根据bean反射机制调用,可能发生反射异常
     */
    public static SQLParams getPrimaryParamsByBean(Map<String, MethodInfo> gets, List<String> pks, Object bean)
            throws Exception {
        // 判断数据有效性
        int pksLength = pks.size();
        if (bean == null || pksLength == 0) return null;
        // 生成容器
        Object[] params = new Object[pksLength];
        int[] sqlTypes = new int[pksLength];
        StringBuilder sb = new StringBuilder(" WHERE ");
        
        for (int i = 0; i < pksLength; ++i) {
            // 判断bean里是否存在该pk字段
            MethodInfo get = gets.get(pks.get(i));
            if (get == null) return null;
            
            // 生成参数和类型
            params[i] = toSQLData(get.getMethod().invoke(bean),
                    get.getDbType(),
                    get.getSqlType(),
                    get.getMethod().getReturnType());
            sqlTypes[i] = get.getSqlType();
            // 追加sql
            if (i == 0) {
                sb.append(get.getKey());
                sb.append("=?");
            } else {
                sb.append(" AND ");
                sb.append(get.getKey());
                sb.append("=?");
            }
        }
        return new SQLParams(sb.toString(), params, sqlTypes);
    }
    
    /**
     * 获取主键参数（转换成所对应的sql类型）,不包含sql语句
     *
     * @param gets     bean里的get方法
     * @param pks      主键字段
     * @param pksValue 主键字段所对应的值
     *
     * @return
     */
    public static SQLParams getPrimaryParams(Map<String, MethodInfo> gets, List<String> pks, Object[] pksValue) {
        // 判断有效性
        int pksLength = pks.size();
        if (pksLength == 0) return null;
        // 生成容器
        Object[] params = new Object[pksLength];
        int[] types = new int[pksLength];
        
        for (int i = 0; i < pksLength; ++i) {
            // 判断是否存在该主键
            MethodInfo get = gets.get(pks.get(i));
            if (get == null) return null;
            // 生成参数
            params[i] = toSQLData(pksValue[i], get.getDbType(), get.getSqlType(),
                    get.getMethod().getReturnType());
            types[i] = get.getSqlType();
        }
        
        return new SQLParams("", params, types);
    }
    
    /**
     * 获取需更新bean的所有参数(更新字段在前,主键字段在后)
     *
     * @param gets   bean的get方法
     * @param pkList 主键字段,不能更新
     * @param bean   bean对象
     *
     * @return SQLParams sql参数对象
     */
    public static SQLParams getUpdateBeanParams(MethodInfo[] gets, List<String> pkList, Object bean) {
        if (pkList != null && pkList.size() != 0) {
            Object[] params = new Object[gets.length];
            int[] types = new int[gets.length];
            int index = 0;
            Map<String, MethodInfo> pks = new HashMap<>();
            // 遍历bean所有属性
            for (int i = 0, L = gets.length; i < L; ++i) {
                MethodInfo get = gets[i];
                // 需更新的字段
                if (pkList.contains(get.getKey())) {
                    pks.put(get.getKey(), get);
                } else {
                    // 填充需更新字段参数
                    try {
                        fillSQLParam(params, types, bean, index, get);
                        ++index;
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            }
            // 填充主键字段
            for (int i = 0, size = pkList.size(); i < size; ++i) {
                MethodInfo get = pks.get(pkList.get(i));
                try {
                    fillSQLParam(params, types, bean, index, get);
                    ++index;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return new SQLParams("", params, types);
        } else {
            return null;
        }
    }
    
    // ------------------------ 私有方法 ------------------------
    
    private static void fillSQLParam(Object[] params, int[] types, Object bean, int index, MethodInfo info)
            throws Exception {
        params[index] = toSQLData(info.getMethod().invoke(bean),
                info.getDbType(), info.getSqlType(),
                info.getMethod().getReturnType());
        types[index] = info.getSqlType();
    }
    
    /**
     * 主要转换boolean的类型，将java的boolean类型转为Y或N
     */
    private static Object toSQLData(Object param, DatabaseType dbType, int sqlType, Class<?> returnType) {
        TypeMapping mapper = TypeMapping.getMapper(returnType);
        if (sqlType == Types.CHAR && TypeMapping.BOOL == mapper) {
            return (Boolean) param ? "Y" : "N";
        } else {
            return param;
        }
    }
    
    private static boolean analyzeWhere(String column, String o, Object value, Map<String, MethodInfo> gets, StringBuilder sb,
            List<Object> params, List<Integer> types) {
        MethodInfo get = gets.get(column);
        if (get == null) return false;
        
        if (value == null || "null".equals(value)) {
            if ("=".equals(o)) {
                sb.append(column);
                sb.append(" IS NULL");
                return true;
            }
            
            if ("<>".equals(o)) {
                sb.append(column);
                sb.append(" IS NOT NULL");
                return true;
            }
        }
        
        int type = get.getSqlType();
        Object param;
        if (type == Types.VARCHAR) {
            if (" LIKE ".equals(o)) {
                // 将column转为大写(upper是数据库里的函数)
                column = "UPPER(" + column + ")";
                param = String.valueOf(value).toUpperCase();
            } else {
                param = String.valueOf(value);
            }
        } else {
            param = toSQLData(value, get.getDbType(), type,
                    get.getMethod().getReturnType());
        }
        
        sb.append(column);
        sb.append(o);
        sb.append("?");
        params.add(param);
        types.add(type);
        return true;
    }
    
    /**
     * 修正操作符
     */
    private static String fixOperation(String operator) {
        if (operator != null && !"".equals(operator.trim())) {
            if (!operator.equals("=") && !operator.equals("<>") && !operator.equals(
                    ">") && !operator.equals("<") &&
                !operator.equals(">=") && !operator.equals("<=")) {
                return operator.trim().toLowerCase().equals("like") ? " LIKE " : null;
            } else {
                return operator;
            }
        } else {
            return null;
        }
    }
    
    private static boolean isValid(Object[] os1, Object[] os2) {
        if (os1 != null && os2 != null && os1.length == os2.length) {
            return true;
        }
        return false;
    }
    
}
