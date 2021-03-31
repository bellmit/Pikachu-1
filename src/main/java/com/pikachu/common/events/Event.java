package com.pikachu.common.events;

import java.util.Arrays;
import java.util.StringJoiner;

/**
 * @Desc：监听器事件
 * @Author：AD
 * @Date：2020/1/13 10:48
 */
public class Event {
    
    // ------------------------ 变量定义 ------------------------
    /**
     * 事件类型
     */
    private String type;
    /**
     * 是否停止监听的标识
     */
    private boolean stopped;
    /**
     * 事件异常信息
     */
    private Throwable exception;
    /**
     * 监听器参数
     */
    private Object[] params;
    
    // ------------------------ 构造方法 ------------------------
    
    protected Event() {
    }
    
    public Event(String type) {
        this.type = type;
    }
    
    // ------------------------ 方法定义 ------------------------
    
    /**
     * 获取 事件类型
     *
     * @return
     */
    public String getType() {
        return type;
    }
    
    /**
     * 设置 事件类型
     *
     * @param type
     */
    public void setType(String type) {
        this.type = type;
    }
    
    /**
     * 事件是否已停止监听
     *
     * @return
     */
    public boolean isStopped() {
        return stopped;
    }
    
    /**
     * 设置 事件停止监听
     *
     * @param stopped
     */
    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
    
    /**
     * 获取 异常信息
     *
     * @return
     */
    public Throwable getException() {
        return exception;
    }
    
    /**
     * 设置 异常信息
     *
     * @param exception
     */
    public void setException(Throwable exception) {
        this.exception = exception;
    }
    
    /**
     * 获取 监听器参数
     *
     * @return
     */
    public Object[] getParams() {
        return params;
    }
    
    /**
     * 设置 监听器参数
     *
     * @param params
     */
    public void setParams(Object[] params) {
        this.params = params;
    }
    
    @Override
    public String toString() {
        return new StringJoiner(", ", Event.class.getSimpleName() + "[", "]")
                .add("type='" + type + "'")
                .add("stopped=" + stopped)
                .add("exception=" + exception)
                .add("params=" + Arrays.toString(params))
                .toString();
    }
    
}
