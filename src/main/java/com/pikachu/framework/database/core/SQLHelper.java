package com.pikachu.framework.database.core;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.collection.Sort;
import com.pikachu.common.database.core.DatabaseType;
import com.pikachu.common.util.PikachuConverts;
import com.pikachu.common.util.PikachuStrings;
import com.pikachu.framework.caching.methods.MethodInfo;
import oracle.sql.TIMESTAMP;

import java.lang.reflect.Method;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.*;

/**
 * @Desc SQL工具类
 * @Date 2019-12-05 22:17
 * @Author AD
 */
public final class SQLHelper {
    // ------------------------ 变量定义 ------------------------
    
    // ------------------------ 构造方法 ------------------------
    
    private SQLHelper() {
    }
    
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
                wheres[i] = new Where(values[i], Operator.EQUALS, columns[i]);
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
    public static Update[] getUpdates(String[] columns, Object[] values) {
        if (isValid(columns, values)) {
            Update[] kvs = new Update[columns.length];
            
            for (int i = 0, L = columns.length; i < L; ++i) {
                kvs[i] = new Update(columns[i], values[i]);
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
                // 获取bean所对应字段的值
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
        // 获取Java值对应的sql类型
        int sqlType = set.getSqlType();
        switch (sqlType) {
            //    byte
            case Types.TINYINT:
                return PikachuConverts.toByte(dbValue);
            //    short
            case Types.SMALLINT:
                return PikachuConverts.toShort(dbValue);
            //    int
            case Types.INTEGER:
                return PikachuConverts.toInt(dbValue);
            //    long
            case Types.BIGINT:
                return PikachuConverts.toLong(dbValue);
            //    float
            case Types.FLOAT:
                return PikachuConverts.toFloat(dbValue);
            //    double
            case Types.DOUBLE:
                return PikachuConverts.toDouble(dbValue);
            //    char（这里使用1个字符（'Y'）代替boolean，因此需要先判断是boolean还是char）
            case Types.CHAR:
                // 获取需要设置的数值类型
                Class<?> paramType = set.getMethod().getParameterTypes()[0];
                // 获取类型映射枚举
                TypeMapping mapper = TypeMapping.getMapper(paramType);
                // 不是boolean类型，转成字符串
                if (TypeMapping.BOOL != mapper) {
                    return dbValue == null ? null : dbValue.toString();
                } else {
                    // 是boolean类型，判断是Y或N（数据库使用Y或N表示boolean类型）
                    return dbValue != null && "Y".equals(dbValue.toString());
                }
            case Types.VARCHAR:
                if (dbValue == null) {
                    return null;
                } else {
                    if (dbValue instanceof String) {
                        // 判断参数类型是否是枚举
                        Class<?> parameterType = set.getMethod().getParameterTypes()[0];
                        if (parameterType.isEnum()) {
                            return Enum.valueOf((Class<? extends Enum>) parameterType, dbValue.toString());
                        } else {
                            return dbValue.toString();
                        }
                    } else {
                        Clob clob = (Clob) dbValue;
                        try {
                            String sub = clob.getSubString(1, (int) clob.length());
                            return sub;
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                    }
                }
                //    bigDecimal
            case Types.DECIMAL:
                return PikachuConverts.toBigDecimal(dbValue);
            case Types.DATE:
                Date date = null;
                if (dbValue instanceof java.sql.Date) {
                    java.sql.Date sqlDate = (java.sql.Date) dbValue;
                    date = new Date(sqlDate.getTime());
                }
                if (dbValue instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) dbValue;
                    date = new Date(timestamp.getTime());
                }
                if (date != null) {
                    // 获取Java Bean类set方法的参数类型数组
                    Class<?>[] paramTypes = set.getMethod().getParameterTypes();
                    // 判断参数是否有效
                    if (paramTypes.length > 0) {
                        // 获取第一个参数
                        Class<?> param = paramTypes[0];
                        // LocalDateTime类型
                        if (LocalDate.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(date).toLocalDate();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return null;
            case Types.TIME:
                Date time = null;
                if (dbValue instanceof java.sql.Time) {
                    java.sql.Time sqlTime = (java.sql.Time) dbValue;
                    time = new Date(sqlTime.getTime());
                }
                if (dbValue instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) dbValue;
                    time = new Date(timestamp.getTime());
                }
                if (time != null) {
                    // 获取Java Bean类set方法的参数类型数组
                    Class<?>[] paramTypes = set.getMethod().getParameterTypes();
                    // 判断参数是否有效
                    if (paramTypes.length > 0) {
                        // 获取第一个参数
                        Class<?> param = paramTypes[0];
                        // LocalDateTime类型
                        if (LocalTime.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(time).toLocalTime();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                return null;
            //    timestamp
            case Types.TIMESTAMP:
                Date dateTime = null;
                // MySQL
                if (dbValue instanceof Timestamp) {
                    Timestamp timestamp = (Timestamp) dbValue;
                    dateTime = new Date(timestamp.getTime());
                }
                // Oracle
                else if (dbValue instanceof TIMESTAMP) {
                    TIMESTAMP timestamp = (TIMESTAMP) dbValue;
                    try {
                        LocalDateTime localDateTime = timestamp.toLocalDateTime();
                        dateTime = PikachuConverts.toDate(localDateTime);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
                if (dateTime != null) {
                    // 获取Java Bean类set方法的参数类型数组
                    Class<?>[] paramTypes = set.getMethod().getParameterTypes();
                    // 判断参数是否有效
                    if (paramTypes.length > 0) {
                        // 获取第一个参数
                        Class<?> param = paramTypes[0];
                        // LocalDateTime类型
                        if (LocalDateTime.class.equals(param)) {
                            try {
                                return PikachuConverts.toLocalDateTime(dateTime);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    return dateTime;
                }
                return dbValue;
            case Types.BLOB:
                // MySQL
                if (dbValue instanceof byte[]) {
                    return (byte[]) dbValue;
                }
                // Oracle
                Blob blob = (Blob) dbValue;
                try {
                    byte[] bytes = blob.getBytes(1, (int) blob.length());
                    return bytes;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            default:
                return dbValue;
        }
    }
    
    /**
     * 将where数组转换成SQL参数（SQL语句、SQL参数、SQL参数类型）
     *
     * @param getsMap where的key所对应Java bean的get方法
     * @param wheres  where查询条件数组
     *
     * @return
     */
    public static SQLParams getWhereParams(Map<String, MethodInfo> getsMap, Where[] wheres) {
        // 判断where条件是否有效
        if (wheres != null && wheres.length > 0) {
            // 创建字符串拼接对象
            StringBuilder sb = new StringBuilder();
            // 参数集合
            List<Object> params = new ArrayList<>();
            // sql数据类型集合
            List<Integer> sqlTypeList = new ArrayList<>();
            // where条件数组长度
            int wheresLen = wheres.length;
            // 当前下标
            int index = 0;
            // 遍历where条件数组
            while (true) {
                if (index < wheresLen) {
                    Where where = wheres[index];
                    if (where == null) {
                        return null;
                    }
                    // 获取字段
                    String column = where.getK();
                    // 判断字段有效性
                    if (column != null) {
                        // 修正操作符号，如果是like返回LIKE，其它直接返回比较符号，如：=,>,<>,>=
                        Operator o = where.getO();
                        if (o == null) {
                            return null;
                        }
                        // 获取值
                        Object v = where.getV();
                        /*
                        - \s 匹配任何空白字符，包括空格、制表符、换页符等等。等价于 [ \f\n\r\t\v]
                        - * 匹配前面的子表达式零次或多次
                         */
                        // 该正则表达式要匹配："   ,   "，将where条件的key如：XXX , YYY进行分割
                        // String[] keys = column.toUpperCase().split("\\s*,\\s*");
                        String[] keys = column.split(",");
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
                                    // 将v（where条件的值分析称SQL语句的值）
                                    analyzeWhere(key, o, v, getsMap, sb, params, sqlTypeList);
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
                            if (!analyzeWhere(keys[0], o, v, getsMap, sb, params, sqlTypeList)) {
                                return null;
                            }
                        }
                        ++index;
                        continue;
                    }
                    return null;
                }
                // 构建sql参数类型数组
                int[] sqlTypes = new int[sqlTypeList.size()];
                for (int i = 0; i < sqlTypes.length; i++) {
                    sqlTypes[i] = sqlTypeList.get(i);
                }
                // 创建SQL参数对象并返回（SQL=" WHERE (key1=? OR key2>?) AND key3<>?"）
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
    public static String getOrderSQL(Map<String, MethodInfo> gets, Order[] orders) {
        if (orders != null && orders.length > 0) {
            StringBuilder sb = new StringBuilder(" ORDER BY ");
            boolean failed = false;
            
            for (Order order : orders) {
                // 获取key,并判断是否有效
                String k = order.getK();
                if (k == null) {
                    return null;
                }
                
                // 获取value,判断是asc(升序)或desc(降序)
                Sort v = order.getV();
                if (v == null) {
                    v = Sort.ASC;
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
            return failed ? sb.deleteCharAt(sb.length() - 1).toString() : "";
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
    public static SQLParams getUpdateParams(Map<String, MethodInfo> gets, Update[] updates) {
        if (updates != null && updates.length != 0) {
            // 创建容器
            StringBuilder sb = new StringBuilder();
            Object[] params = new Object[updates.length];
            int[] sqlTypes = new int[updates.length];
            // 循环生成
            for (int i = 0; i < updates.length; ++i) {
                // 获取更新字段,判断有效性
                Update update = updates[i];
                String k = update.getK();
                if (PikachuStrings.isNull(k)) {
                    return null;
                }
                
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
                // 判断是否是主键字段（主键字段不参与更新）
                if (pkList.contains(get.getKey())) {
                    pks.put(get.getKey(), get);
                } else {
                    // 填充需更新字段参数(非主键字段)
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
    
    public static List<Object> getConditionValuesWithOperatorIsIn(Object value) {
        List<Object> valueList = new ArrayList<>();
        if (value == null) {
            return valueList;
        } else {
            Class<?> c = value.getClass();
            // 判断是否是数组
            if (c.isArray()) {
                Class<?> componentType = c.getComponentType();
                // 基本数据类型数组
                if (componentType.isPrimitive()) {
                    if (char.class.equals(componentType)) {
                        char[] chars = (char[]) value;
                        for (char cs : chars) {
                            valueList.add(cs);
                        }
                    } else if (boolean.class.equals(componentType)) {
                        boolean[] booleans = (boolean[]) value;
                        for (boolean b : booleans) {
                            if (b) {
                                valueList.add('Y');
                            } else {
                                valueList.add('N');
                            }
                        }
                    } else if (byte.class.equals(componentType)) {
                        byte[] bytes = (byte[]) value;
                        for (byte b : bytes) {
                            valueList.add(b);
                        }
                    } else if (short.class.equals(componentType)) {
                        short[] shorts = (short[]) value;
                        for (short s : shorts) {
                            valueList.add(s);
                        }
                    } else if (int.class.equals(componentType)) {
                        int[] ints = (int[]) value;
                        for (int i : ints) {
                            valueList.add(i);
                        }
                    } else if (long.class.equals(componentType)) {
                        long[] longs = (long[]) value;
                        for (long l : longs) {
                            valueList.add(l);
                        }
                    } else if (float.class.equals(componentType)) {
                        float[] floats = (float[]) value;
                        for (float f : floats) {
                            valueList.add(f);
                        }
                    } else if (double.class.equals(componentType)) {
                        double[] doubles = (double[]) value;
                        for (double d : doubles) {
                            valueList.add(d);
                        }
                    }
                } else {
                    // 对象类型数组
                    Object[] objects = (Object[]) value;
                    for (Object object : objects) {
                        valueList.add(object);
                    }
                }
            } else if (List.class.isAssignableFrom(c)) {
                valueList = (List<Object>) value;
            } else if (Set.class.isAssignableFrom(c)) {
                Set<Object> set = (Set<Object>) value;
                for (Object s : set) {
                    valueList.add(s);
                }
            } else if (c.equals(String.class)) {
                String ss = (String) value;
                if (ss != null && !"".equals(ss.trim())) {
                    ss = ss.replace("，", ",");
                    for (String s : ss.split(",")) {
                        valueList.add(s);
                    }
                }
            } else if (Enum.class.isAssignableFrom(c)) {
                Class<Enum> enumClass = (Class<Enum>) c;
                Enum conditionValueEnum = Enum.valueOf(enumClass, value.toString());
                valueList.add(conditionValueEnum);
            }
        }
        valueList.sort(new Comparator<Object>() {
            @Override
            public int compare(Object first, Object second) {
                return first.toString().compareTo(second.toString());
            }
        });
        return valueList;
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
     * 将Java的数据类型转换为数据库类型（主要将Java的boolean类型转换为数据库的char类型，true=Y，false=N）
     *
     * @param param      where参数值
     * @param dbType     数据库类型
     * @param sqlType    数据库字段类型
     * @param returnType Java get方法返回值类型
     *
     * @return
     */
    private static Object toSQLData(Object param, DatabaseType dbType, int sqlType, Class<?> returnType) {
        TypeMapping mapper = TypeMapping.getMapper(returnType);
        if (sqlType == Types.CHAR && TypeMapping.BOOL == mapper) {
            return (Boolean) param ? "Y" : "N";
        } else {
            if (param != null && param.getClass().isEnum()) {
                return param.toString();
            }
            return param;
        }
    }
    
    /**
     * 分析Where条件，将where条件转换为SQL语句的表示方式
     *
     * @param column 列字段
     * @param o      操作符，如：=，>，<，<>等
     * @param value  where条件值
     * @param gets   Java Bean对象属性方法（get方法信息）
     * @param sb     字符串拼接对象
     * @param params 将Java Bean对象值(value)转换为SQL值后的容器
     * @param types  value所对应的sql数据类型
     *
     * @return
     */
    private static boolean analyzeWhere(String column, Operator o, Object value, Map<String, MethodInfo> gets, StringBuilder sb,
            List<Object> params, List<Integer> types) {
        // 获取该字段对应的get方法，没有属性方法，则不能获取对应数据库中的值
        MethodInfo get = gets.get(column);
        // Java Bean类中没有该字段对应的属性方法，则返回空
        if (get == null) {
            return false;
        }
        // 判断值是不是为空,如果值为空，解析称对应的sql语句
        if (value == null || "".equals(value.toString().trim()) || "null".equals(value)) {
            // 值=null，sql语句解析为is null
            if (o == Operator.EQUALS) {
                sb.append(column);
                sb.append(" IS NULL");
                return true;
            }
            // 值!=null，sql语句解析称is not null
            if (o == Operator.NO_EQUALS) {
                sb.append(column);
                sb.append(" IS NOT NULL");
                return true;
            }
        }
        // 获取对应的数据库数据类型
        int type = get.getSqlType();
        if (o == Operator.IN) {
            List<Object> valueList = getConditionValuesWithOperatorIsIn(value);
            if (valueList == null || valueList.size() == 0) {
                return false;
            }
            sb.append(column);
            sb.append(o);
            sb.append("(");
            for (int i = 0, L = valueList.size(); i < L; i++) {
                if (i > 0) {
                    sb.append(",");
                }
                sb.append("?");
                Object o1 = valueList.get(i);
                Object sqlParam = toSQLData(o1, get.getDbType(), type, get.getMethod().getReturnType());
                params.add(sqlParam);
                types.add(type);
            }
            sb.append(")");
            return true;
        } else {
            Object param;
            if (type == Types.VARCHAR) {
                if (o == Operator.LIKE) {
                    // 将column转为大写(upper是数据库里的函数)
                    column = "UPPER(" + column + ")";
                /*
                - 数据库的字段是varchar类型，且是like比较符时，like模糊查询不区分大小写
                - 如果要区分大小写，则使用like binary %xxx%
                 */
                    param = String.valueOf(value).toUpperCase();
                } else {
                    param = String.valueOf(value);
                }
            } else {
                // 将Java的参数转为SQL参数，主要将value（boolean类型）转为数据库表示的char类型（Y或N）
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
        
    }
    
    // /**
    //  * 修正操作符
    //  *
    //  * @param operator 操作符，如：>,<,=,<>,>=,<=,like
    //  *
    //  * @return
    //  */
    // private static String fixOperation(String operator) {
    //     // 判断操作符的有效性
    //     if (operator != null && !"".equals(operator.trim())) {
    //         // 判断操作符是否是like
    //         if (!operator.equals("=") && !operator.equals("<>") && !operator.equals(">") && !operator.equals("<") &&
    //             !operator.equals(">=") && !operator.equals("<=")) {
    //             // 返回大写LIKE
    //             if (operator.trim().toLowerCase().equals("like")) {
    //                 return " LIKE ";
    //             } else if (operator.trim().toLowerCase().equals("in")) {
    //                 return " IN ";
    //             } else {
    //                 return operator;
    //             }
    //         } else {
    //             // 返回比较符号
    //             return operator;
    //         }
    //     } else {
    //         return null;
    //     }
    // }
    
    private static boolean isValid(Object[] os1, Object[] os2) {
        if (os1 != null && os2 != null && os1.length == os2.length) {
            return true;
        }
        return false;
    }
    
}
