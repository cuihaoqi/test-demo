package com.cuihq.testdemo.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommentTarget {
	/**
	 * 字段值（驼峰命名方式，该值可无）
	 */
	String value() default "";
}
