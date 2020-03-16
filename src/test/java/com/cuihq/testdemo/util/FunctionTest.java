package com.cuihq.testdemo.util;

import com.cuihq.testdemo.pojo.User;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FunctionTest {
	@Test
	public void fu01() throws Exception {
//		User user = new User(1L,"崔昊琦");
		User user = new User(1L,"");
//		check1(user::getId,user::getName);
		JsonUtils.validEmpty(user,User::getId,User::getName);
	}

	@Test
	public void fu02() throws Exception {
		User user1 = new User(1L,"唐僧");
		User user2 = new User(2L,"孙悟空");
		User user3 = new User(3L,"猪八戒");
		User user4 = new User(4L,"沙僧");
		JsonUtils.Object2List(Arrays.asList(user1,user2,user3,user4),User::getId,User::getName);
		JsonUtils.Object2List(Arrays.asList(user1,user2,user3,user4),User::getName);
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