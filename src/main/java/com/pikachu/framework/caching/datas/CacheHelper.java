package com.pikachu.framework.caching.datas;

import com.pikachu.common.util.PikachuConverts;
import com.pikachu.framework.caching.datas.matchers.ComparerManager;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.common.collection.KeyValue;
import com.pikachu.common.collection.Where;
import com.pikachu.common.util.PikachuArrays;
import com.pikachu.framework.database.core.SQLHelper;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/15 16:44
 */
public final class CacheHelper {
    
    private CacheHelper() {
    }
    
    public static <T> T[] getPageData(Class<T> clazz, T[] src, int page, int length) {
        if (!PikachuArrays.isEmpty(src)) {
            if (page <= 0) {
                page = 1;
            }
            if (length <= 0) {
                length = 1;
            }
            int srcPos = (page - 1) * length;
            if (srcPos < src.length) {
                if (srcPos + length > src.length) {
                    length = src.length - srcPos;
                }
                if (length < 1) {
                    return (T[]) Array.newInstance(clazz, 0);
                } else {
                    T[] result = (T[]) Array.newInstance(clazz, length);
                    System.arraycopy(src, srcPos, result, 0, length);
                    return result;
                }
            } else {
                return (T[]) Array.newInstance(clazz, 0);
            }
        } else {
            return src;
        }
    }
    
    public static void upperCaseWhereKeys(Where[] wheres) {
        Arrays.stream(wheres).forEach(where -> {
            where.setK(where.getK().toUpperCase());
        });
    }
    
    /**
     * 获取结果：key=value,key1=value1
     *
     * @param ws
     *
     * @return
     */
    public static String getWhereString(Where[] ws) {
        if (!PikachuArrays.isEmpty(ws)) {
            Where[] wheres = new Where[ws.length];
            System.arraycopy(ws, 0, wheres, 0, ws.length);
            Arrays.sort(wheres, new Comparator<Where>() {
                public int compare(Where w1, Where w2) {
                    if (w1 != null && w2 != null) {
                        String key1 = w1.getK();
                        String key2 = w2.getK();
                        if (key1 == null && key2 == null) {
                            return 0;
                        } else if (key1 == null && key2 != null) {
                            return -1;
                        } else {
                            return key1 != null && key2 == null ? 1 : key1.compareToIgnoreCase(
                                    key2);
                        }
                    } else {
                        return 0;
                    }
                }
            });
            StringBuilder sb = new StringBuilder();
            for (Where where : wheres) {
                if (where == null) {
                    return null;
                }
                sb.append(where.getK());
                sb.append(where.getO().toUpperCase());
                Object value = where.getV();
                if (value != null) {
                    if (value instanceof Date) {
                        sb.append(((Date) value).getTime());
                    } else if (value instanceof LocalDateTime) {
                        try {
                            Date date = PikachuConverts.toDate(value);
                            sb.append(date.getTime());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        sb.append(value);
                    }
                }
                sb.append(",");
            }
            return sb.deleteCharAt(sb.length() - 1).toString();
        } else {
            return null;
        }
    }
    
    /**
     * 1、先对where数组进行排序
     * 2、生成字符串："list$whereKey:Operator:WhereValue$orderKey:orderValue"
     * 3、生成大写字符串的hash code
     *
     * @param action
     * @param wheres
     * @param orders
     *
     * @return
     */
    public static int getHistoryCacheKey(String action, Where[] wheres, KeyValue[] orders) {
        if (action != null && !"".equals(action.trim())) {
            StringBuilder sb = new StringBuilder(action);
            Object value;
            if (!PikachuArrays.isEmpty(wheres)) {
                Where[] copyWheres = new Where[wheres.length];
                System.arraycopy(wheres, 0, copyWheres, 0, wheres.length);
                Arrays.sort(copyWheres, new Comparator<Where>() {
                    public int compare(Where where1, Where where2) {
                        if (where1 != null && where2 != null) {
                            String k1 = where1.getK();
                            String k2 = where2.getK();
                            if (k1 == null && k2 == null) {
                                return 0;
                            } else if (k1 == null && k2 != null) {
                                return -1;
                            } else {
                                return k1 != null && k2 == null ? 1 : k1.compareToIgnoreCase(
                                        k2);
                            }
                        } else {
                            return 0;
                        }
                    }
                });
                
                for (Where where : copyWheres) {
                    if (where != null) {
                        sb.append("$");
                        sb.append(where.getK());
                        sb.append(where.getO());
                        value = where.getV();
                        // 操作符不是in
                        if (!"in".equalsIgnoreCase(where.getO())) {
                            if (value != null) {
                                if (value instanceof Date) {
                                    sb.append(((Date) value).getTime());
                                } else if(value instanceof LocalDateTime){
                                    sb.append(((LocalDateTime)value).toString());
                                }else {
                                    sb.append(value);
                                }
                            }
                        } else {
                            List<Object> valueList = SQLHelper.getConditionValuesWithOperatorIsIn(value);
                            for (Object o : valueList) {
                                sb.append(o);
                            }
                        }
                        
                    }
                }
            }
            
            if (!PikachuArrays.isEmpty(orders)) {
                for (int i = 0, L = orders.length; i < L; ++i) {
                    KeyValue kv = orders[i];
                    if (kv != null) {
                        String key = kv.getK();
                        if (key != null && key.length() != 0) {
                            sb.append("$");
                            sb.append(key);
                            sb.append(":");
                            value = kv.getV();
                            if (value != null && "DESC".equals(value.toString().toUpperCase())) {
                                sb.append("DESC");
                            } else {
                                sb.append("ASC");
                            }
                        }
                    }
                }
            }
            String result = sb.toString().toUpperCase();
            return result.hashCode();
        } else {
            return 0;
        }
    }
    
    /**
     * 获取(o1|o2|o3).toString().hashCode()
     *
     * @param os 对象数组
     *
     * @return 字符串表示的哈希值
     */
    public static String getPrimaryValueAsString(Object[] os) {
        if (os != null && os.length != 0) {
            StringBuilder sb = new StringBuilder();
            for (Object o : os) {
                sb.append(o);
                sb.append("|");
            }
            return String.valueOf(sb.toString().hashCode());
        } else {
            return null;
        }
    }
    
    /**
     * 将主键值转为大写
     *
     * @param pks
     *
     * @return
     */
    public static String[] upperCasePrimaryKeys(String[] pks) {
        String[] keys = new String[pks.length];
        
        for (int i = 0, L = pks.length; i < L; ++i) {
            keys[i] = pks[i].toUpperCase();
        }
        
        return keys;
    }
    
    /**
     * (pkValue1|pkValue2|pkValue3).toString().hasCode();
     *
     * @param pks    被设置为主键的属性名
     * @param wheres key=value的where数组，操作符必须是"="
     *
     * @return 字符串的哈希值
     */
    public static String getPrimaryValueByWheres(String[] pks, Where[] wheres) {
        if (PikachuArrays.isValid(pks, wheres)) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String pk : pks) {
            boolean find = false;
            for (Where where : wheres) {
                if (pk.equals(where.getK())) {
                    if (!"=".equals(where.getO())) {
                        return null;
                    }
                    sb.append(where.getV());
                    sb.append("|");
                    find = true;
                    break;
                }
            }
            
            if (!find) {
                return null;
            }
        }
        
        return String.valueOf(sb.toString().hashCode());
    }
    
    /**
     * 根据属性名进行反射调用获取主键值：(pkValue1|pkValue2|pkValue3).toString().hasCode();
     *
     * @param gets 属性方法map
     * @param pks  主键属性名数组
     * @param o    主键属性所在对象
     *
     * @return
     *
     * @throws Exception
     */
    public static String getPrimaryValueByKeys(Map<String, MethodInfo> gets, String[] pks, Object o) throws Exception {
        if (o == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            for (String pk : pks) {
                MethodInfo get = gets.get(pk);
                Object pkValue = get.getMethod().invoke(o);
                // 拼接值
                sb.append(pkValue);
                sb.append("|");
            }
            
            return String.valueOf(sb.toString().hashCode());
        }
    }
    
    /**
     * 根据get方法的返回值类型、where条件的操作符获取匹配器
     *
     * @param gets   get方法信息map
     * @param wheres 从where里获取操作符、值，再通过key获取gets里的方法信息对象
     */
    public static ValueMatcher[] getWhereMatchers(Map<String, MethodInfo> gets, Where[] wheres) throws Exception {
        ValueMatcher[] matchers = new ValueMatcher[wheres.length];
        
        for (int i = 0, L = wheres.length; i < L; ++i) {
            ValueMatcher matcher = ValueMatcher.getValueMatcher(gets, wheres[i]);
            if (matcher == null) {
                throw new Exception("Where condition error: " + getWhereString(wheres));
            }
            matchers[i] = matcher;
        }
        
        return matchers;
    }
    
    public static boolean matchCondition(ValueMatcher[] matchers, Object o) throws Exception {
        
        for (ValueMatcher matcher : matchers) {
            // 有一个条件没有匹配上，即返回false，valueMatcher对于where数组的匹配，where数组是and条件
            if (!matcher.match(o)) {
                return false;
            }
        }
        
        return true;
    }
    
    public static void sortArray(Map<String, MethodInfo> getMethodInfos, KeyValue[] kvs, Object[] os) {
        if (kvs != null && kvs.length != 0 && os != null && os.length >= 2) {
            final ValueSorter[] sorters = new ValueSorter[kvs.length];
            
            for (int i = 0, L = kvs.length; i < L; ++i) {
                sorters[i] = ValueSorter.getValueSorter(getMethodInfos, kvs[i]);
            }
            
            Arrays.sort(os, new Comparator<Object>() {
                public int compare(Object o1, Object o2) {
                    
                    for (int i = 0, L = sorters.length; i < L; ++i) {
                        ValueSorter sorter = sorters[i];
                        if (sorter != null) {
                            if (sorter.matchAsc(o1, o2)) {
                                return -1;
                            }
                            if (sorter.matchDesc(o1, o2)) {
                                return 1;
                            }
                        }
                    }
                    return 0;
                }
            });
        }
    }
    
    public static ValueUpdater[] getUpdaters(Map<String, MethodInfo> setMethods, KeyValue[] updates) throws Exception {
        ValueUpdater[] updaters = new ValueUpdater[updates.length];
        
        for (int i = 0, L = updates.length; i < L; ++i) {
            KeyValue update = updates[i];
            String prop = update.getK().toUpperCase();
            Method setMethod = setMethods.get(prop).getMethod();
            // 将update的值转为set方法的参数类型
            Object o = fixValueType(setMethod, update.getV());
            ValueUpdater updater = new ValueUpdater(prop, setMethod, o);
            updaters[i] = updater;
        }
        
        return updaters;
    }
    
    private static Object fixValueType(Method setMethod, Object converted) {
        Class[] paramTypes = setMethod.getParameterTypes();
        if (PikachuArrays.isEmpty(paramTypes)) {
            return converted;
        } else {
            Class<?> paramType = paramTypes[0];
            return ComparerManager.parseValue(paramType, converted);
            // int type = ClassCode.getType(paramType);
            // switch (type) {
            //     case ClassCode.BYTE:
            //         return PikachuConverts.toByte(converted);
            //     case ClassCode.SHORT:
            //         return PikachuConverts.toShort(converted);
            //     case ClassCode.INT:
            //         return PikachuConverts.toInt(converted);
            //     case ClassCode.LONG:
            //         return PikachuConverts.toLong(converted);
            //     case ClassCode.FLOAT:
            //         return PikachuConverts.toFloat(converted);
            //     case ClassCode.DOUBLE:
            //         return PikachuConverts.toDouble(converted);
            //     case ClassCode.BOOLEAN:
            //         return PikachuConverts.toBoolean(converted);
            //     case ClassCode.STRING:
            //         return String.valueOf(converted);
            //     case ClassCode.DATE:
            //         try {
            //             return PikachuConverts.toDate(converted);
            //         } catch (Exception e) {
            //             e.printStackTrace();
            //         }
            //     case ClassCode.LOCAL_DATE_TIME:
            //         try {
            //             return PikachuConverts.toLocalDateTime(converted);
            //         } catch (Exception e) {
            //             e.printStackTrace();
            //         }
            //     default:
            //         return converted;
            //
            // }
        }
    }
    
}
