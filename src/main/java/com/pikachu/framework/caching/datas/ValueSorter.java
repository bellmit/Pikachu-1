package com.pikachu.framework.caching.datas;

import com.pikachu.common.collection.Operator;
import com.pikachu.common.collection.Sort;
import com.pikachu.framework.caching.datas.matchers.ComparerManager;
import com.pikachu.framework.caching.datas.matchers.IComparer;
import com.pikachu.framework.caching.methods.MethodInfo;
import com.pikachu.framework.database.core.Order;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/14 17:14
 */
public final class ValueSorter {
    
    private final Method method;
    
    private final IComparer asc;
    
    private final IComparer desc;
    
    public ValueSorter(Method get, IComparer asc, IComparer desc) {
        this.method = get;
        this.asc = asc;
        this.desc = desc;
    }
    
    public boolean matchAsc(Object o1, Object o2) {
        if (o1 == o2) {
            return false;
        } else if (o1 == null && o2 != null) {
            return true;
        } else if (o1 != null && o2 == null) {
            return false;
        } else {
            try {
                return asc.compare(method.invoke(o1), method.invoke(o2));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    public boolean matchDesc(Object o1, Object o2) {
        if (o1 == o2) {
            return false;
        } else if (o1 == null && o2 != null) {
            return false;
        } else if (o1 != null && o2 == null) {
            return true;
        } else {
            try {
                return desc.compare(method.invoke(o1), method.invoke(o2));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
    }
    
    /**
     * @param gets  get方法信息，prop=属性名，methodInfo=对应的方法信息
     * @param order key=prop,value=asc|desc
     *
     * @return
     */
    public static ValueSorter getValueSorter(Map<String, MethodInfo> gets, Order order) {
        // 判断有效性
        if (order == null) {
            return null;
        }
        String prop = order.getK().toUpperCase();
        MethodInfo get = gets.get(prop);
        if (get == null) {
            return null;
        }
        // 判断是asc还是desc
        boolean isAsc = order.getV() == Sort.ASC;
        
        // 获取返回值类型，判断出需要使用哪种类型的比较器
        Method method = get.getMethod();
        Class<?> type = method.getReturnType();
        // 根据方法的返回值类型，判断使用哪种类型比较器
        IComparer asc = isAsc ? ComparerManager.getComparer(type, Operator.GREATER) :
                ComparerManager.getComparer(type, Operator.LESS);
        IComparer desc = isAsc ? ComparerManager.getComparer(type, Operator.GREATER) :
                ComparerManager.getComparer(type, Operator.LESS);
        return new ValueSorter(method, asc, desc);
    }
    
}
