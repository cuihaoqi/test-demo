package com.cuihq.testdemo.entity;

import lombok.Data;

public enum UploaderStatusEnum {
	DONE(200,"已完成"),
	PAR(201,"部分完成"),
	FILE_VALID_PASS(300,"文件校验通过"),
	FILEVALID_EMPTY(301,"空文件"),
	FILEVALID_NON_UPLOADER_REQUEST(302,"没有文件分片"),
	FILEVALID_CURRSIZE_GT_TOTALSIZE(303,"当前分片数大于总分片数"),
	FILEVALID_INPUTFILE_GT_MAXSIZE(304,"上传文件超过文件最大限制"),
	FILEVALID_SIZE_ERR1(305,"不是最后一个文件 分块大小 != 文件大小"),
	FILEVALID_SIZE_ERR2(306,"最后一个文件, 文件大小!=文件总大小取余+当前分块大小"),
	FILEVALID_SIZE_ERR3(307,"总共就一个文件,文件大小!=总大小"),
	ERROR(500,"不明错误"),
	;



	private Integer id;
	private String name;

	UploaderStatusEnum(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}
}
