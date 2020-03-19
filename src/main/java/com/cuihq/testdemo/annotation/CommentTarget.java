package com.cuihq.testdemo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author cuihq
 * 用于解释字段含义
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentTarget {
	/**
	 * 字段值（驼峰命名方式，该值可无）
	 */
	String value() default "";
}
