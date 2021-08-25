package com.pikachu.framework.caching.datas;

import com.pikachu.common.collection.Operator;
import com.pikachu.framework.database.core.Where;
import com.pikachu.common.util.PikachuStrings;
import com.pikachu.framework.caching.datas.matchers.ComparerManager;
import com.pikachu.framework.caching.datas.matchers.IComparer;
import com.pikachu.framework.caching.datas.matchers.InComparer;
import com.pikachu.framework.caching.datas.matchers.LikeComparer;
import com.pikachu.framework.caching.methods.MethodInfo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 封装了方法、比较器、条件值
 *
 * @Author：AD
 * @Date：2020/1/16 11:26
 */
public class ValueMatcher {
    
    /**
     * 方法数组，对应where条件里的key，key=column1,column2,…
     */
    private Method[] methods;
    /**
     * 比较器，对应where里的operator，如：String比较器，int比较器，boolean比较器，char比较器等，内部实现>、<、=、<>、like等的比较
     */
    private IComparer comparer;
    /**
     * 参与匹配的值，对应where条件里的value
     */
    private Object conditionValue;
    
    /**
     * where条件值匹配器
     *
     * @param methods        Java bean类方法，一般是get方法，可以使用反射机制调用获取值
     * @param comparer       比较器，如：=、>、<、<>、like比较器
     * @param conditionValue where条件值(where里面的v)，用于和反射调用get方法的结果值进行对比
     */
    public ValueMatcher(Method[] methods, IComparer comparer, Object conditionValue) {
        this.methods = methods;
        this.comparer = comparer;
        this.conditionValue = conditionValue;
    }
    
    public boolean match(Object o) throws Exception {
        if (methods.length > 1) {
            for (Method method : methods) {
                if (comparer.compare(method.invoke(o), conditionValue)) {
                    return true;
                }
            }
            return false;
        } else {
            return comparer.compare(methods[0].invoke(o), conditionValue);
        }
    }
    
    /**
     * 根据where条件获取值匹配器，一个where对应一个matcher
     *
     * @param gets
     * @param where 操作符、值只能有一个，字段属性可以有多个
     *
     * @return
     *
     * @throws Exception
     */
    public static ValueMatcher getValueMatcher(Map<String, MethodInfo> gets, Where where) throws Exception {
        // 获取操作符
        Operator operator = where.getO();
        // 判断操作符是否有效
        if (operator!=null) {
            // 获取属性
            String[] props = where.getK().split("\\s*,\\s*");
            Method[] methods;
            /*
             - where的key有多个字段，如：xxx,yyy,zzz = vvv
             - 根据where的key获取get方法
             */
            if (props.length > 1) {
                List<Method> methodList = new ArrayList<>();
                // 根据属性获取对应的方法信息
                for (String prop : props) {
                    MethodInfo info = gets.get(prop);
                    if (info != null) {
                        methodList.add(info.getMethod());
                    }
                }
                if (methodList.size() == 0) {
                    return null;
                }
                methods = methodList.toArray(new Method[methodList.size()]);
            } else {
                methods = new Method[1];
                MethodInfo methodInfo = gets.get(props[0]);
                if (methodInfo == null) {
                    return null;
                }
                methods[0] = methodInfo.getMethod();
            }
            // 获取where值
            Object conditionValue = where.getV();
            // 获取返回值类型（where的key可以有多个，但值只有一个，因为where的value只有一个类型）
            Class<?> returnType = methods[0].getReturnType();
            IComparer comparer = null;
            if (operator==Operator.LIKE) {
                String strValue = "%%";
                if (conditionValue != null && conditionValue.toString().trim().length() != 0) {
                    // 获取值
                    strValue = conditionValue.toString();
                }
                // 获取比较器
                LikeComparer likeComparer = new LikeComparer(strValue);
                // 返回值匹配器
                return new ValueMatcher(methods, likeComparer, strValue);
            } else if (operator==Operator.IN) {
                return new ValueMatcher(methods, new InComparer(conditionValue), conditionValue);
            } else {
                // 非like和in比较符的，conditionValue(Where条件的v)只能是单个值，不能以数组或集合的形式传入
                comparer = ComparerManager.getComparer(returnType, operator);
                // IParser<?, Object> parser = Parsers.getParser(returnType);
                // value = parser.parse(value);
                // conditionValue = parseValue(returnType, conditionValue);
                conditionValue = comparer.parseConditionValue(returnType, conditionValue);
                return new ValueMatcher(methods, comparer, conditionValue);
            }
        } else {
            return null;
        }
    }
    
}
