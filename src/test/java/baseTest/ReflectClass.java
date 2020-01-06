package baseTest;

import Entity.Book;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Slf4j
public class ReflectClass {

	public static void main(String[] args) {
		reflectNewInstance();
		reflectPrivateConstructor();
		reflectPrivateMethod();
	}

	// 创建对象
	public static void reflectNewInstance() {
		try {
			Class<?> classBook = Class.forName("Entity.Book");
			Object objectBook = classBook.newInstance();
			Book book = (Book) objectBook;
			book.setName("Android进阶之光");
			book.setAuthor("刘望舒");
			log.info("reflectNewInstance book = " + book.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 反射私有的构造方法
	public static void reflectPrivateConstructor() {
		try {
			Class<?> classBook = Class.forName("Entity.Book");
			Constructor<?> declaredConstructorBook = classBook.getDeclaredConstructor(String.class,String.class);
			declaredConstructorBook.setAccessible(true);
			Object objectBook = declaredConstructorBook.newInstance("Android开发艺术探索","任玉刚");
			Book book = (Book) objectBook;
			log.info("reflectPrivateConstructor book = " + book.toString());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 反射私有属性
	public static void reflectPrivateField() {
		try {
			Class<?> classBook = Class.forName("Entity.Book");
			Object objectBook = classBook.newInstance();
			Field fieldTag = classBook.getDeclaredField("TAG");
			fieldTag.setAccessible(true);
			String tag = (String) fieldTag.get(objectBook);
			log.info("reflectPrivateField tag = " + tag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// 反射私有方法
	public static void reflectPrivateMethod() {
		try {
			Class<?> classBook = Class.forName("Entity.Book");
			Method methodBook = classBook.getDeclaredMethod("declaredMethod",int.class);
			methodBook.setAccessible(true);
			Object objectBook = classBook.newInstance();
			String string = (String) methodBook.invoke(objectBook,0);

			log.info("reflectPrivateMethod string = " + string);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}