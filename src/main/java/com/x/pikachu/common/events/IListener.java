package com.x.pikachu.common.events;

/**
 * @Desc 监听器
 * @Date 2021/3/27 14:20
 * @Author AD
 */
public interface IListener {
    void onEvent(Event event) throws Exception;
}
