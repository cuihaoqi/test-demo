package com.cuihq.testdemo.entity;

import lombok.Data;

@Data
public class UploadResultVO {
	/**
	 * 上传状态 200-上传成功 201-部分上传成功
	 */
	private Integer code;
	/**
	 * 上传文件绝对路径
	 */
	private String fileUrl;
	/**
	 * 上传文件名称
	 */
	private String fileName;
	/**
	 * 文件所属位置
	 */
	private String modulFilePath;
}
