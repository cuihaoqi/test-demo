package com.cuihq.testdemo.util;

import com.cuihq.testdemo.annotation.CommentTarget;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;

//函数式接口注解
@FunctionalInterface
public interface SFunction<T,R> extends Serializable {
	/**
	 * 函数接口方法，如User对象，利用user获取到其 id,name
	 * 此种方法可以使用 User::getId
	 * 其余需要传入 user::getId
	 * @param t
	 * @return
	 */
	R apply(T t);


	/**
	 * 获取SerializedLambda，如果传入User::getId 含有内容
	 *	implClass：接口实现类型 User
	 *	implMethodName：调用方法 getId
	 *  implMethodSignature：方法属性返回值类型
	 * @return SerializedLambda
	 * @throws Exception
	 */
	default SerializedLambda getSerializedLambda() throws Exception {
		//writeReplace改了好像会报异常
		Method write = this.getClass().getDeclaredMethod("writeReplace");
		write.setAccessible(true);
		return (SerializedLambda) write.invoke(this);
	}

	/**
	 * 获取接口实现类
	 * @return com.cuihq.testdemo.pojo.User
	 */
	default String getImplClass() {
		try {
			//这里拿到的数据是com/cuihq/。。。 需要将 /转换成. 利用Class.forName才会生效
			String implClass = getSerializedLambda().getImplClass();
			String result = implClass.replace("/", ".");
			return result;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取调用方法
	 * User::getName -> getName();
	 * @return 调用方法名称
	 */
	default String getImplMethodName() {
		try {
			return getSerializedLambda().getImplMethodName();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 获取bean属性,getName->name
	 * 只针对get方法使用
	 * @return bean属性
	 */
	default String getFieldName(){
		String methodName = this.getImplMethodName();
		String valueTmp = StringUtils.removeStartIgnoreCase(methodName, "get");
		String valuePre = valueTmp.substring(0, 1);
		String valueSux = valueTmp.substring(1);
		return valuePre.toLowerCase() + valueSux;
	}

	/**
	 * 获取CommentTarget注解的value(字段中文含义);
	 * 思路：SFunction函数中可以获取，传入类的类型，调用的方法，即可获取到对应的字段属性
	 * @return CommentTarget注解的value;
	 */
	default  String getCommentValue(){
		String fieldName = this.getFieldName();
		String implClass = this.getImplClass();
		Field f = null;
		try {
			f = getField(Class.forName(implClass), fieldName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		CommentTarget commentTarget = null;
		if(f!=null){
			commentTarget = f.getAnnotation(CommentTarget.class);
		}
		return commentTarget==null ? "": commentTarget.value();
	}

	/**
	 * 获取Class类的字段属性Field
	 * @param c class类型
	 * @param FieldName 属性名称
	 * @return Field类
	 */
	default Field getField( Class<?> c, String FieldName) {
		try {
			Field f = c.getDeclaredField(FieldName);
			return f;
		} catch (NoSuchFieldException e) {
			if (c.getSuperclass() == null){
				return null;
			}
			else{
				return getField( c.getSuperclass(), FieldName);
			}
		} catch (Exception ex) {
			return null;
		}
	}
}
