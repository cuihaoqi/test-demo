package com.cuihq.testdemo.util;


import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;

@FunctionalInterface
public interface Consumer<T> extends Serializable {
	void accept(T t);
	default SerializedLambda getSerializedLambda() throws Exception {
		//writeReplace改了好像会报异常
		Method write = this.getClass().getDeclaredMethod("writeReplace");
		write.setAccessible(true);
		return (SerializedLambda) write.invoke(this);
	}
}
