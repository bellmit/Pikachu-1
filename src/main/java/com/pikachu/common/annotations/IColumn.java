package com.pikachu.common.annotations;

import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @Desc TODO
 * @Date 2021/3/31 19:51
 * @Author AD
 */
@Inherited
@Documented
@Target(FIELD)
@Retention(RUNTIME)
public @interface IColumn {

    String doc();

    boolean pk() default false;

    String column() default "";

}
