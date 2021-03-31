package com.x.pikachu.common.annotations;

/**
 * @Desc TODO
 * @Date 2021/3/31 19:51
 * @Author AD
 */
public @interface IColumn {
    
    String doc();
    
    boolean pk() default false;
    
    String column() default "";
}
