package com.cuihq.testdemo.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

@Service
public class App
{
//	public static void main( String[] args )
	public void data2Word()
	{
		System.out.println( "Hello World!" );
		Map<String, Object> dataMap = new HashMap<String, Object>();
		dataMap.put("name",                     "北京经开股份有限公司<w:br/>"+
				"  1<w:br/>"+
				"    2<w:br/>");
		dataMap.put("age", 25);

		List<Map<String, Object>> works = new ArrayList<Map<String,Object>>();
		List<Object> images = new ArrayList<Object>();
		images.add(getImageBase("C:\\Users\\cuihq\\Desktop\\1.jpg"));
		images.add(getImageBase("C:\\Users\\cuihq\\Desktop\\2.jpg"));
		for(int i=0;i<3;i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("time", "2018-10-21 09:09:2"+i);
			map.put("address", "测试地址"+i);
			map.put("images", images);
			works.add(map);
		}

		dataMap.put("image", getImageBase("C:\\Users\\cuihq\\Desktop\\3.jpg"));
		dataMap.put("workLists", works);
		try {
			WordUtils.exportMillCertificateWord(null, null, dataMap, "test4", "test4.ftl");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@SuppressWarnings("deprecation")
	public static String getImageBase(String src) {
		if(src==null||src==""){
			return "";
		}
		File file = new File(src);
		if(!file.exists()) {
			return "";
		}
		InputStream in = null;
		byte[] data = null;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		try {
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);
	}



}
