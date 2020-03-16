package com.cuihq.testdemo.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

//函数式接口注解
@FunctionalInterface
public interface Function<T,R> extends Serializable {
	//这里只接收无参方法
	Object get();


	//这个方法返回的SerializedLambda是重点
	default SerializedLambda getSerializedLambda() throws Exception {
		//writeReplace改了好像会报异常
		Method write = this.getClass().getDeclaredMethod("writeReplace");
		write.setAccessible(true);
		return (SerializedLambda) write.invoke(this);
	}

	default String getImplClass() {
		try {
			SerializedLambda serializedLambda = getSerializedLambda();
			return getSerializedLambda().getImplClass();
		} catch (Exception e) {
			return null;
		}
	}

	default String getImplMethodName() {
		try {
			return getSerializedLambda().getImplMethodName();
		} catch (Exception e) {
			return null;
		}
	}
}
