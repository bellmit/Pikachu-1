package com.pikachu.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Desc TODO
 * @Date 2021/3/31 19:50
 * @Author AD
 */
@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ITable {
    
    String doc();
    
    boolean cache();
    
    boolean history() default false;
    
    String table() default "";
    
    String version() default "1";
}
