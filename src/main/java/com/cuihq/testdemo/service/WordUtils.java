package com.cuihq.testdemo.service;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;


import freemarker.template.Configuration;
import freemarker.template.Template;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WordUtils {
	//配置信息,代码本身写的还是很可读的,就不过多注解了
	private static Configuration configuration = null;
	//这里注意的是利用WordUtils的类加载器动态获得模板文件的位置
	private static final String templateFolder = WordUtils.class.getClassLoader().getResource("").getPath();
	//private static final String templateFolder = "H:/我的项目/lm/lm/web/src/main/webapp/WEB-INF/templates";
	static {
//		configuration = new Configuration();
		configuration = new Configuration(Configuration.VERSION_2_3_23);
		configuration.setDefaultEncoding("utf-8");
		try {
			configuration.setDirectoryForTemplateLoading(new File(templateFolder));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private WordUtils() {
		throw new AssertionError();
	}

	public static void exportMillCertificateWord(HttpServletRequest request, HttpServletResponse response, Map map, String title, String ftlFile) throws IOException {
		Template freemarkerTemplate = configuration.getTemplate(ftlFile);
		File file = null;
		InputStream fin = null;
		//ServletOutputStream out = null;
		FileOutputStream out = null;
		try {
			// 调用工具类的createDoc方法生成Word文档
			file = createDoc(map,freemarkerTemplate);
			fin = new FileInputStream(file);
			String fileName = title + ".doc";
			/*response.setCharacterEncoding("utf-8");
			response.setContentType("application/msword");
			// 设置浏览器以下载的方式处理该文件名
			String fileName = title + ".doc";
			response.setHeader("Content-Disposition", "attachment;filename="
					.concat(String.valueOf(URLEncoder.encode(fileName, "UTF-8"))));

			out = response.getOutputStream(); */
			File file2 = new File("C:\\Users\\cuihq\\Desktop\\"+fileName);
			if (!file2.exists()) {
				file2.createNewFile();
			}
			out = new FileOutputStream(file2);
			byte[] buffer = new byte[512];  // 缓冲区
			int bytesToRead = -1;
			// 通过循环将读入的Word文件的内容输出到浏览器中
			while((bytesToRead = fin.read(buffer)) != -1) {
				out.write(buffer, 0, bytesToRead);
			}
		} finally {
			if(fin != null) fin.close();
			if(out != null) out.close();
			if(file != null) file.delete(); // 删除临时文件
		}
	}

	private static File createDoc(Map<?, ?> dataMap, Template template) {
		String name =  "sellPlan.doc";
		File f = new File(name);
		Template t = template;
		try {
			// 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
			Writer w = new OutputStreamWriter(new FileOutputStream(f), "utf-8");
			t.process(dataMap, w);
			w.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		return f;
	}
}
