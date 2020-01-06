package com.cuihq.testdemo.action;

import com.cuihq.testdemo.service.App;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/data2Word")
public class data2Word {
	@Autowired
	App app;
	@PostMapping("word")
	public void fu01(){
		app.data2Word();
	}
}
