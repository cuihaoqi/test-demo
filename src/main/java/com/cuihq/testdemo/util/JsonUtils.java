package com.cuihq.testdemo.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonUtils {

	/**
	 * 函数接口
	 * 传入返回载体map，泛型对象
	 * @param <T>
	 */
	@FunctionalInterface
	public interface AfterSet<T> extends Serializable {
		void after(Map<String, Object> row, T baseModel);
	}

	/**
	 * 需要继承自BaseEntity接口的非空校验
	 * @throws Exception
	 */
/*	public static <T extends BaseEntity> void validEmpty(T obj, SFunction<T, ?>... functions) throws Exception {
		String msg = Stream.of(functions)
				.filter(x -> isAnyoneEmpty(x.apply((T) obj)))
				.map(fun -> String.format("【%s】不能为空", obj.getComment(fun)))
				.collect(Collectors.joining(","));
		System.out.println(msg);

	}*/

	/**
	 * 不需要继承接口的非空校验
	 * @param obj 需要被校验的对象
	 * @param functions 对象需要被校验的方法
	 * @param <T> 校验对应的泛型
	 * @throws Exception
	 */
	static <T> void validEmpty(Object obj, SFunction<T, ?>... functions){
		String msg = Stream.of(functions)
				.filter(x -> isAnyoneEmpty(x.apply((T) obj)))
				.map(fun -> String.format("【%s】不能为空", fun.getCommentValue()))
				.collect(Collectors.joining(","));
		System.out.println(msg);
	}


	/**
	 * list<T> ->list<Map> 类的按需返回
	 * @param list 数据集合
	 * @param set 自定义的返回内容
	 * @param functions 需要返回的字段
	 */
	static <T> void Object2List(List<T> list,AfterSet<T> set, SFunction<T, ?>... functions){
		ArrayList<Map<String, Object>> result = new ArrayList<>();
		Supplier<Map<String, Object>> mapSupplier = LinkedHashMap::new;
		for(T obj:list){
			Map<String, Object> map = mapSupplier.get();
			for (SFunction fun : functions) {
				String fileName = fun.getFieldName();
				map.put(fileName,fun.apply(obj));
			}
			result.add(map);
			//这里使用返回的map和泛型的obj作为参数，具体方法在传入的时候指定
			if(set!=null){
				set.after(map,obj);
			}
		}
		System.out.println(result);
	}

	/**
	 * Object->Map 类的按需返回
	 * @param obj 需要转换的一个对象
	 * @param set 自定义的返回内容
	 * @param functions 需要返回的字段
	 */
	static <T> void Object2Map(T obj,AfterSet<T> set, SFunction<T, ?>... functions){
		Map<String, Object> result = new LinkedHashMap<>();
		for (SFunction fun : functions) {
			String fileName = fun.getFieldName();
			result.put(fileName,fun.apply(obj));
			if(set!=null){
				set.after(result,obj);
			}
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

	public static <T> void testConsumer(Object obj, Consumer<T>... functions) throws Exception {
		for (Consumer consumer:functions){
			consumer.accept(obj);
			SerializedLambda serializedLambda = consumer.getSerializedLambda();
			System.out.println(serializedLambda);
		}
	}
}
