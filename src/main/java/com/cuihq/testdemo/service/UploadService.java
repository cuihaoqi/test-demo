package com.cuihq.testdemo.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UploadService extends Uploader{

	public UploadService(@Value("${upload.path}") String path){
		super(path);
	}
}