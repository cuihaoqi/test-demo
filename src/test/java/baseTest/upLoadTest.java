package baseTest;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

public class upLoadTest {
	@Test
	public void fu01(){
		double floor = Math.max(Math.floor(2.01),3);
//		double floor = Math.floor(2.01);
		System.out.println(floor);
	}

	@Test
	public void fu02(){
		String str = "112.csv";
		String extension = FilenameUtils.getExtension(str);
		System.out.println(extension);
	}
	@Test
	public void fu03(){
		String identifier="9c20cbfbddcca0157989fb44e41be435";
		String s = identifier.replaceAll("[^0-9A-Za-z_-]", "");
		System.out.println(s);
		System.out.println(identifier.equals(s));
	}
}
