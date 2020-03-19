package com.cuihq.testdemo.util;

import com.cuihq.testdemo.pojo.User;
import org.junit.Test;

import java.util.Arrays;

public class FunctionTest {
/*	@Test
	public void fu01() throws Exception {
		User user = new User(1L,"");
		JsonUtils.validEmpty(user,User::getId,User::getName);
	}*/

	/*************************************************重要**************************************************************/
	/**
	 * 测试非空校验方法
	 */
	@Test
	public void validEmptyTest() {
		User user = new User(1L,"");
		JsonUtils.validEmpty(user,User::getId,User::getName);
	}

	/**
	 * 一群对象的按需返回
	 */
	@Test
	public void Object2ListTest(){
		User user1 = new User(1L,"唐僧");
		User user2 = new User(2L,"孙悟空");
		User user3 = new User(3L,"猪八戒");
		User user4 = new User(4L,"沙僧");
		JsonUtils.Object2List(
				Arrays.asList(user1,user2,user3,user4)
				,(m, entity) -> {
					m.put("test","110");
				}
				,User::getId
				,User::getName
		);
//		JsonUtils.Object2List(Arrays.asList(user1,user2,user3,user4),User::getName);
	}

	/**
	 * 一个对象的按需返回
	 */
	@Test
	public void Object2MapTest(){
		User user1 = new User(1L,"唐僧");
		JsonUtils.Object2Map(
				user1
				,(m, entity) -> {
					m.put("test","110");
				}
				,User::getName);
	}

	/*************************************************不太重要***********************************************************/

	@Test
	public void fu04(){
		User user = new User(1L,"");
		check1(user::getId,user::getName);
	}

	@Test
	public void notImportant() throws Exception {
		User user1 = new User(1L,"唐僧");
		Consumer<User> consumer1 = (Consumer<User>) user -> System.out.println(user.getId());
		JsonUtils.testConsumer(user1,consumer1);
	}

	public static void check1(Function... functions) {
		for (Function fun : functions) {
			System.out.println("传入的实现类：" + fun.getImplClass());
			System.out.println("传入的方法名：" + fun.getImplMethodName());
			System.out.println("传入的参数值：" + fun.get());
			System.out.println("传入数据类型：" + fun.get().getClass());
		}
	}


}