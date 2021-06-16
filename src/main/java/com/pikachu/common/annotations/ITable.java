package com.pikachu.common.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Desc
 * @Date 2021/3/31 19:50
 * @Author AD
 */
@Inherited
@Documented
@Target(TYPE)
@Retention(RUNTIME)
public @interface ITable {
    
    String doc();
    
    /**
     * - 当bean中没有主键时（使用{@link IColumn}进行主键标注）cache和history都将为false
     * - 要使用cache和history，bean必须有主键
     */
    boolean cache();
    
    /**
     * 标识是否缓存历史查询数据（非主键查询的数据）
     */
    boolean history() default false;
    
    String table() default "";
    
    String version() default "1";
}
