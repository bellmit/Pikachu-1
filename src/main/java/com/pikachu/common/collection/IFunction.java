package com.pikachu.common.collection;

import java.io.Serializable;
import java.util.function.Function;

/**
 * 自定义function接口
 *
 * @author AD
 * @date 2021/8/25 21:17
 */
@FunctionalInterface
public interface IFunction<T, R> extends Function<T, R>, Serializable {
}
