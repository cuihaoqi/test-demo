package com.cuihq.testdemo.entity;

import lombok.Data;

@Data
public class FileInfoPo {
	/**
	 * 当前是第几个分片
	 */
	private Integer chunkNumber;
	/**
	 * 分块大小(byte)
	 */
	private Integer chunkSize;
	/**
	 * 文件总大小(byte)
	 */
	private Integer totalSize;
	/**
	 * 通过文件内容计算出的md5加密信息
	 */
	private String identifier;
	/**
	 * 文件名称
	 */
	private String filename;
	/**
	 * 文件位置
	 */
	private String modulFilePath;
}
