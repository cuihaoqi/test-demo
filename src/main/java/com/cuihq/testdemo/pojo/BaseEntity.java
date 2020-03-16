package com.cuihq.testdemo.pojo;

import com.cuihq.testdemo.annotation.CommentTarget;
import com.cuihq.testdemo.util.SFunction;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;

public class BaseEntity {
	/**
	 * 获取字段的备注
	 * @param column
	 * @return
	 */
	public <T> String getComment(SFunction<T, ?> column){
		String fieldName = resovse(column.getImplMethodName());
		Field f = getField(this, this.getClass(), fieldName);
		CommentTarget commentTarget = f.getAnnotation(CommentTarget.class);
		return commentTarget==null ? "": commentTarget.value();
	}
	private static Field getField(Object obj, Class<?> c, String FieldName) {
		try {
			Field f = c.getDeclaredField(FieldName);
			return f;
		} catch (NoSuchFieldException e) {
			if (c.getSuperclass() == null){
				return null;
			}
			else{
				return getField(obj, c.getSuperclass(), FieldName);
			}
		} catch (Exception ex) {
			return null;
		}
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
