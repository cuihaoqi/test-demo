package com.cuihq.testdemo.util;

import com.cuihq.testdemo.annotation.CommentTarget;
import com.cuihq.testdemo.pojo.BaseEntity;
import com.cuihq.testdemo.pojo.User;
import org.apache.commons.lang3.StringUtils;

import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtils {
	/**
	 * 需要继承自BaseEntity接口的非空校验
	 * @param obj
	 * @param functions
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends BaseEntity> void validEmpty(T obj, SFunction<T, ?>... functions) throws Exception {
		String msg = Stream.of(functions)
				.filter(x -> isAnyoneEmpty(x.apply((T) obj)))
				.map(fun -> String.format("【%s】不能为空", obj.getComment(fun)))
				.collect(Collectors.joining(","));
		System.out.println(msg);

	}

	/**
	 * 不需要继承接口的非空校验
	 * @param obj
	 * @param functions
	 * @param <T>
	 * @throws Exception
	 */
	public static <T extends Object> void validEmpty1(Object obj, SFunction<T, ?>... functions) throws Exception {
		String msg = Stream.of(functions)
				.filter(x -> isAnyoneEmpty(x.apply((T) obj)))
				.map(fun -> String.format("【%s】不能为空", getComment(fun)))
				.collect(Collectors.joining(","));
		System.out.println(msg);
	}
	private static <T> String getComment(SFunction<T, ?> column){
		String fieldName = resovse(column.getImplMethodName());
		String implClass = column.getImplClass();
		Field f = null;
		try {
			f = getField(Class.forName(implClass), fieldName);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		CommentTarget commentTarget = f.getAnnotation(CommentTarget.class);
		return commentTarget==null ? "": commentTarget.value();
	}
	private static Field getField( Class<?> c, String FieldName) {
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


	/**
	 * 类的按需返回
	 * @param list
	 * @param functions
	 * @param <T>
	 */
	public static <T extends Object> void Object2List(List<T> list, SFunction<T, ?>... functions){
		ArrayList result = new ArrayList();
		Supplier<Map> mapSupplier = LinkedHashMap::new;
		for(T obj:list){
			Map map = mapSupplier.get();
			for (SFunction fun : functions) {
				System.out.println("传入的实现类：" + fun.getImplClass());
				System.out.println("传入的方法名：" + fun.getImplMethodName());
				String fileName = resovse(fun.getImplMethodName());
				map.put(fileName,fun.apply(obj));
			}
			result.add(map);
		}
		System.out.println(result);
	}






	private static boolean isAnyoneEmpty(Object obj) {
		if (obj == null) {
			return obj == null;
		} else if (obj instanceof Collection<?>) {
			return ((Collection<?>) obj).isEmpty();
		} else if (obj instanceof String) {
			return obj.toString().length() == 0;
		} else if (obj.getClass().isArray()) {
			return ((Object[]) obj).length == 0;
		} else if (obj instanceof Map) {
			return ((Map<?, ?>) obj).isEmpty();
		} else if (obj instanceof StringBuffer) {
			return ((StringBuffer) obj).length() == 0;
		} else if (obj instanceof StringBuilder) {
			return ((StringBuilder) obj).length() == 0;
		}
		return false;
	}

	private static String resovse(String key){
		if(StringUtils.isBlank(key)){
			return key;
		}
		String valueTmp = StringUtils.removeStartIgnoreCase(key, "get");
		String valuePre = valueTmp.substring(0, 1);
		String valueSux = valueTmp.substring(1);
		return valuePre.toLowerCase() + valueSux;
	}
}
