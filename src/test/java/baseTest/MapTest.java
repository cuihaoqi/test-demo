package baseTest;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.sun.xml.internal.fastinfoset.util.ValueArray.MAXIMUM_CAPACITY;

public class MapTest {
	@Test
	public void fu01() {
		Map map = new HashMap();
		for (int i = 0; i < 10000; i++) {
			map.put("test" + i, "崔" + i);
			System.out.println("map长度=====>>>>>:" + map.size());
		}
		System.out.println("=============");
		System.out.println("map总长度=====>>>>>:" + map.size());
		int i = tableSizeFor(9);
		System.out.println(i);

	}


	static final int tableSizeFor(int cap) {
		int n = cap - 1;
		n |= n >>> 1;
		n |= n >>> 2;
		n |= n >>> 4;
		n |= n >>> 8;
		n |= n >>> 16;
		return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
	}


	@Test
	public void fu02(){
		com.cuihq.testdemo.entity.Map<String, String> map = new com.cuihq.testdemo.entity.HashMap<>();
		for (int i = 0; i < 100; i++) {
			map.put("test" + i, "崔" + i);
			System.out.println("map长度=====>>>>>:" + map.size());
		}
		String test2 = map.get("test76");
		System.out.println(test2);

	}
	@Test
	public void fu03(){
		int i = 1;
		String s = String.valueOf(i);
		System.out.println(s);
		Integer ii = null;
		String s1 = String.valueOf(ii);
		System.out.println(s1);
	}
}


























