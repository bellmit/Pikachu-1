package com.pikachu.common.transaction;

/**
 * @Desc
 * @Date 2020-05-01 22:50
 * @Author AD
 */
public interface IAop {
    
    void before() throws Exception;
    
    void after() throws Exception;
    
    void onException();
    
}
