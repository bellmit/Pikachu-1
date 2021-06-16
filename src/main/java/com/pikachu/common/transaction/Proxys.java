package com.pikachu.common.transaction;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Desc 获取某个类的代理类
 * @Date 2021/6/2 20:16
 * @Author AD
 */
public final class Proxys {
    
    private static final Set<String> OBJECT_METHODS;
    
    static {
        OBJECT_METHODS = Arrays.stream(Object.class.getDeclaredMethods())
                .map(m -> m.getName())
                .collect(Collectors.toSet());
    }
    
    /**
     * 获取cglib动态代理
     *
     * @param clazz 被代理类（必须有无参构造函数）
     * @param aop   切面
     */
    public static <T> T getProxy( Class<T> clazz, IAop aop) {
        Enhancer enhancer = new Enhancer();
        
        enhancer.setSuperclass(clazz);
        enhancer.setCallback(new MethodInterceptor() {
            /**
             * @param o 代理类对象,只能被methodProxy反射调用
             * @param method 被代理类方法对象(不要用此对象进行反射调用)
             * @param args 被代理类参数
             * @param methodProxy 代理类方法（不能调用invoke，会递归发生栈溢出。需调用invokeSuper）
             * @return
             * @throws Throwable
             */
            @Override
            public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
                try {
                    // 如果是Object里的方法，则直接调用，不进行横切
                    if (OBJECT_METHODS.contains(method.getName())) {
                        Constructor<T> c = clazz.getDeclaredConstructor();
                        c.setAccessible(true);
                        T t = c.newInstance();
                        Method target = clazz.getMethod(method.getName());
                        return target.invoke(t, args);
                    } else {
                        if (aop != null) {
                            aop.before();
                            Object result = methodProxy.invokeSuper(o, args);
                            aop.after();
                            return result;
                        } else {
                            return methodProxy.invokeSuper(o, args);
                        }
                    }
                }catch (Throwable e){
                    aop.onException();
                    throw  e;
                }
            }
        });
        return (T) enhancer.create();
    }
    
}
