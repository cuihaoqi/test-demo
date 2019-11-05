package com.cuihq.testdemo.service;

import com.cuihq.testdemo.entity.UploaderStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;

//https://github.com/simple-uploader/Uploader/blob/develop/samples/Node.js/uploader-node.js
/**
 * 断点续传
 *
 * @author cuihq
 *
 */
@Slf4j
public class Uploader {
	/**
	 * 临时文件夹
	 */
	private String temporaryFolder;
	/**
	 * 最大文件大小
	 */
	@Value("${upload.maxFileSize}")
	private Integer maxFileSize;

	public Uploader(String temporaryFolder) {
		this.temporaryFolder = temporaryFolder;
		File file = new File(temporaryFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	/**
	 * @title 上传文件主方法
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return
	 * */
	public void post(HttpServletRequest req, UploadListener listener) throws IllegalStateException, IOException {

		int chunkNumber = this.getParamInt(req, "chunkNumber", 0);
		int chunkSize = this.getParamInt(req, "chunkSize", 0);
		int totalSize = this.getParamInt(req, "totalSize", 0);
		String identifier = this.getParamString(req, "identifier", "");
		String filename = this.getParamString(req, "filename", "");
		String modulFilePath = this.getParamString(req, "modulFilePath", "");

		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(req.getSession().getServletContext());
		//请求中是否包含文件
		if (multipartResolver.isMultipart(req)) {
			// 将request变成多部分request
			MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) req;
			// 获取multiRequest 中所有的文件名
			Iterator<String> iter = multiRequest.getFileNames();
			while (iter.hasNext()) {
				String name = iter.next().toString();
				// if (!this.fileParameterName.equals(name)) {
				// continue;
				// }
				MultipartFile file = multiRequest.getFile(name);

				if (file != null && file.getSize() > 0) {
					String original_filename = file.getOriginalFilename();
					// String original_filename =
					// files[this.fileParameterName]['originalFilename'];

					//对文件进行校验
					Integer validation = validateRequest(chunkNumber, chunkSize, totalSize, identifier, filename,
							(int) file.getSize());

					//返回值为valid是 没有校验住,可以对文件进行保存
					if (UploaderStatusEnum.FILE_VALID_PASS.getId().equals(validation)) {
						//获取分片文件绝对路径名称
						String chunkFilename = getChunkFilename(chunkNumber, identifier);

						File f = new File(chunkFilename);
						if (!f.exists()) {
							file.transferTo(f);
						}

						int currentTestChunk = 1;
						int numberOfChunks = (int) Math.max(Math.floor(totalSize / (chunkSize * 1.0)), 1);

						this.mergeChunk(currentTestChunk, chunkNumber, numberOfChunks,
								chunkFilename, original_filename, identifier, listener,modulFilePath);

					} else {
//						listener.callback(validation, filename, original_filename, identifier,"file");
						listener.callback(validation, filename, original_filename,modulFilePath);
					}
				} else {
//					listener.callback("invalid_uploader_request", null, null, null, null);
					listener.callback(UploaderStatusEnum.FILEVALID_EMPTY.getId(), null, null,modulFilePath);
				}
			}
		}
	}
	
	/**
	 * @title 将md5串中的非数字英文元素去除(保证存储在服务器中的文件是以 数字英文组成)
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return 
	 * */
	public String cleanIdentifier(String identifier) {
		return identifier.replaceAll("[^0-9A-Za-z_-]", "");
	}
	/**
	 * @title 获取分片文件名称(分片文件名称使用 前台传来的md5进行唯一标识)
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return 
	 * */
	public String getChunkFilename(int chunkNumber, String identifier) {
		identifier = cleanIdentifier(identifier);
		return new File(temporaryFolder, "uploader-" + identifier + '.' + chunkNumber).getAbsolutePath();
	}

	/**
	 * @title 校验传来的参数
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return
	 * */
	public Integer validateRequest(int chunkNumber, int chunkSize, int totalSize, String identifier, String filename,
								  Integer fileSize) {
		identifier = cleanIdentifier(identifier);

		if (chunkNumber == 0 || chunkSize == 0 || totalSize == 0 || identifier.length() == 0
				|| filename.length() == 0) {
			//没有文件分片
			return UploaderStatusEnum.FILEVALID_NON_UPLOADER_REQUEST.getId();
		}
		//获取总分片数
		int numberOfChunks = (int) Math.max(Math.floor(totalSize / (chunkSize * 1.0)), 1);
		if (chunkNumber > numberOfChunks) {
			//当前分片数大于总分片数
			return UploaderStatusEnum.FILEVALID_CURRSIZE_GT_TOTALSIZE.getId();
		}

		if (this.maxFileSize != null && totalSize > this.maxFileSize) {
			//上传文件超过文件最大限制
			return UploaderStatusEnum.FILEVALID_INPUTFILE_GT_MAXSIZE.getId();
		}

		if (fileSize != null) {
			if (chunkNumber < numberOfChunks && fileSize != chunkSize) {
				//不是最后一个文件 分块大小 != 文件大小
				return UploaderStatusEnum.FILEVALID_SIZE_ERR1.getId();
			}
			if (numberOfChunks > 1 && chunkNumber == numberOfChunks
					&& fileSize != ((totalSize % chunkSize) + chunkSize)) {
				//最后一个文件, 文件大小=文件总大小取余+当前分块大小
				return UploaderStatusEnum.FILEVALID_SIZE_ERR2.getId();
			}
			if (numberOfChunks == 1 && fileSize != totalSize) {
				//总共就一个文件,文件大小=总大小
				return UploaderStatusEnum.FILEVALID_SIZE_ERR3.getId();
			}
		}
		//没有校验住
		return UploaderStatusEnum.FILE_VALID_PASS.getId();
	}



/*	public void get(HttpServletRequest req, UploadListener listener) {
		int chunkNumber = this.getParamInt(req, "chunkNumber", 0);
		int chunkSize = this.getParamInt(req, "chunkSize", 0);
		int totalSize = this.getParamInt(req, "totalSize", 0);
		String identifier = this.getParamString(req, "identifier", "");
		String filename = this.getParamString(req, "filename", "");
		if (validateRequest(chunkNumber, chunkSize, totalSize, identifier, filename, null).equals("valid")) {
			String chunkFilename = getChunkFilename(chunkNumber, identifier);
			if (new File(chunkFilename).exists()) {
				listener.callback("found", chunkFilename, filename, identifier, null);
			} else {
				listener.callback("not_found", null, null, null, null);
			}

		} else {
			listener.callback("not_found", null, null, null, null);
		}
	}*/





	/**
	 * @title 合并分块文件(如果前台没有传完,则直接返回 code:201表示部分分块文件传输成功
	 * 					  如果currentTestChunk = numberOfChunks,则表示分块文件上传成功,将分块文件进行合并)
	 * @param currentTestChunk  服务器上传到第几块标志位
	 * @param chunkNumber       前台传来当前上传到第几块
	 * @param numberOfChunks    总块数
	 * @param filename          文件绝对路径
	 * @param original_filename 源文件名称
	 * @param identifier        md5加密串
	 * @param listener          监听
	 * @return
	 */
	@SuppressWarnings("")
	private int mergeChunk(int currentTestChunk, int chunkNumber, int numberOfChunks, String filename,
						   String original_filename, String identifier, UploadListener listener,String modulFilePath) {
		String cfile = getChunkFilename(currentTestChunk, identifier);
		if (new File(cfile).exists()) {
			currentTestChunk++;
			if (currentTestChunk >= chunkNumber) {
				if (chunkNumber == numberOfChunks) {
					try {
						// 文件合并
						log.info("模块:{}.分片文件开始合并",modulFilePath);
						UploadOptions options = new UploadOptions();
						File f = new File(this.temporaryFolder,
								identifier + "." + FilenameUtils.getExtension(original_filename));
						options.listener = new UploadDoneListener() {
							@Override
							public void onError(Exception err) {
//								listener.callback("invalid_uploader_request", f.getAbsolutePath(), original_filename, identifier, fileType);
								listener.callback(UploaderStatusEnum.ERROR.getId(), f.getAbsolutePath(), original_filename,modulFilePath);
								clean(identifier, null);
							}

							@Override
							public void onDone() {
//								listener.callback("done", f.getAbsolutePath(), original_filename, identifier, fileType);
								listener.callback(UploaderStatusEnum.DONE.getId(), f.getAbsolutePath(), original_filename,modulFilePath);
								clean(identifier, null);
							}
						};
						//将分片文件合并
						this.write(identifier, new FileOutputStream(f), options);
					} catch (FileNotFoundException e) {
						e.printStackTrace();
//						listener.callback("invalid_uploader_request", filename, original_filename, identifier, fileType);
						listener.callback(UploaderStatusEnum.ERROR.getId(), filename, original_filename,modulFilePath);
					} catch (IOException e) {
						e.printStackTrace();
//						listener.callback("invalid_uploader_request", filename, original_filename, identifier, fileType);
						listener.callback(UploaderStatusEnum.ERROR.getId(), filename, original_filename,modulFilePath);
					}
				} else {
//					listener.callback("partly_done", filename, original_filename, identifier, fileType);
					listener.callback(UploaderStatusEnum.PAR.getId(), filename, original_filename,modulFilePath);
				}
			} else {
				return mergeChunk(currentTestChunk, chunkNumber, numberOfChunks, filename, original_filename,
						identifier, listener,modulFilePath);
			}
		} else {
//			listener.callback("partly_done", filename, original_filename, identifier, fileType);
			listener.callback(UploaderStatusEnum.PAR.getId(), filename, original_filename,modulFilePath);
		}
		return currentTestChunk;
	}

	/**
	 * @title 将分片文件删除
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return
	 * */
	public void clean(String identifier, UploadOptions options) {
		if (options == null) {
			options = new UploadOptions();
		}
		pipeChunkRm(1, identifier, options);
	}
	private void pipeChunkRm(int number, String identifier, UploadOptions options) {

		String chunkFilename = getChunkFilename(number, identifier);
		File file = new File(chunkFilename);
		if (file.exists()) {
			try {
				file.delete();
			} catch (Exception e) {
				if (options.listener != null) {
					options.listener.onError(e);
				}
			}
			pipeChunkRm(number + 1, identifier, options);

		} else {
			if (options.listener != null){
				options.listener.onDone();
			}
		}
	}

	/**
	 * @title 将分片文件合并
	 * @author cuihq
	 * @date 2019/11/5
	 * @param
	 * @return
	 * */
	public void write(String identifier, OutputStream writableStream, UploadOptions options) throws IOException {
		if (options == null) {
			options = new UploadOptions();
		}
		if (options.end == null) {
			options.end = true;
		}
		pipeChunk(1, identifier, options, writableStream);
	}
	private void pipeChunk(int number, String identifier, UploadOptions options, OutputStream writableStream)
			throws IOException {
		String chunkFilename = getChunkFilename(number, identifier);
		if (new File(chunkFilename).exists()) {
			FileInputStream inputStream = new FileInputStream(new File(chunkFilename));
			int maxlen = 1024;
			int len = 0;
			try {
				byte[] buff = new byte[maxlen];
				while ((len = inputStream.read(buff, 0, maxlen)) > 0) {
					writableStream.write(buff, 0, len);
				}
			} finally {
				inputStream.close();
			}
			pipeChunk(number + 1, identifier, options, writableStream);
		} else {
			if (options.end){
				writableStream.close();
			}
			if (options.listener != null){
				options.listener.onDone();
			}
		}
	}



	private int getParamInt(HttpServletRequest req, String key, int def) {
		String value = req.getParameter(key);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
		}
		return def;
	}

	private String getParamString(HttpServletRequest req, String key, String def) {
		String value = req.getParameter(key);
		try {
			return value == null ? def : value;
		} catch (Exception e) {
		}
		return def;
	}

	public static interface UploadListener {
//		public void callback(String status, String filename, String original_filename, String identifier,
//							 String fileType);
//		public void callback(Integer status, String absolutePath, String fileName);
		public void callback(Integer status, String absolutePath, String fileName,String modulFilePath);
	}

	public static interface UploadDoneListener {
		public void onDone();

		public void onError(Exception err);
	}

	public static class UploadOptions {
		public Boolean end;
		public UploadDoneListener listener;
	}
}
