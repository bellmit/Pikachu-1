package com.pikachu.common.events;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @Desc：
 * @Author：AD
 * @Date：2020/1/13 11:14
 */
final class ListenerData {
    /**
     * 监听器优先级
     */
    private final int priority;

    /**
     * 监听器对象
     */
    private IListener listener;

    /**
     * 监听器类
     */
    private final Class<?> listenerClass;

    /**
     * 监听器参数
     */
    private final Object[] params;

    ListenerData(IListener listener) {
        this(0, listener, null);
    }

    ListenerData(int priority, IListener listener, Object[] params) {
        this.priority = 0;
        this.listener = listener;
        this.listenerClass = listener == null ? null : listener.getClass();
        this.params = params;
    }

    ListenerData(Class<?> listenerClass) {
        this(0, listenerClass, null);
    }

    ListenerData(int priority, Class<?> listenerClass, Object[] params) {
        this.priority = 0;
        this.listenerClass = listenerClass;
        this.params = params;
        // 这里不对listener赋值
    }

    public int getPriority() {
        return priority;
    }

    public IListener getListener() {
        return listener;
    }

    public void setListener(IListener listener) {
        this.listener = listener;
    }

    public Class<?> getListenerClass() {
        return listenerClass;
    }

    public Object[] getParams() {
        return params;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", ListenerData.class.getSimpleName() + "[", "]")
                .add("priority=" + priority)
                .add("listener=" + listener)
                .add("listenerClass=" + listenerClass)
                .add("params=" + Arrays.toString(params))
                .toString();
    }
    
}
