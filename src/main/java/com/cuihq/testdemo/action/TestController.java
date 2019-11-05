package com.cuihq.testdemo.action;

import com.alibaba.fastjson.JSON;
import com.cuihq.testdemo.entity.UploadResultVO;
import com.cuihq.testdemo.service.UploadService;
import com.cuihq.testdemo.service.Uploader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@RestController
@CrossOrigin
@RequestMapping("/test")
public class TestController{

	@Autowired
	private UploadService uploadService;

	@RequestMapping(path = "/upload", method = RequestMethod.POST)
	public void upload(HttpServletRequest request, HttpServletResponse response){
		try{
			uploadService.post(request, new Uploader.UploadListener() {
				@Override
				public void callback(Integer status, String absolutePath, String fileName, String modulFilePath) {
					this.success(response,status,absolutePath,fileName,modulFilePath);
				}

				private void success(HttpServletResponse response, Integer status,String absolutePath,String fileName,String modulFilePath) {
					PrintWriter writer = null;
					try {
						response.setHeader("content-type", "text/html;charset=UTF-8");
						writer = response.getWriter();
						UploadResultVO resultVO = new UploadResultVO();
						resultVO.setCode(status);
						resultVO.setFileUrl(absolutePath);
						resultVO.setModulFilePath(modulFilePath);
						resultVO.setFileName(fileName);
						String result = JSON.toJSONString(resultVO);
						writer.write(result);
					} catch(Exception e){} finally{
						if( writer != null ){
							writer.close();
						}
					}
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	@PostMapping("/f1")
	public String st(){
		return "1";
	}
}