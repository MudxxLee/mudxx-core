package com.mudxx.tool.aliyun.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.exception.ExceptionUtil;
import com.mudxx.common.utils.id.NumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * @description 阿里云OSS客户端工具类
 * @author laiwen
 * @date 2018年12月15日 下午3:01:21
 */
@SuppressWarnings("ALL")
public class OssClientUtil {

	private static final Logger log = LoggerFactory.getLogger(OssClientUtil.class);
	
	/**
	 * @description 创建存储空间
	 * @param endpoint 服务终端
	 * @param accessKeyId 许可证ID
	 * @param accessKeySecret 许可证秘钥
	 * @param bucketName 存储空间名
	 */
	public static String createBucket(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		// 1、创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		// 2、创建存储空间
		ossClient.createBucket(bucketName);

		// 3、关闭OSSClient
		ossClient.shutdown();
		
		// 4、返回存储空间名称
		return bucketName;
	}
	
	/**
     * @description 上传文件到阿里云OSS
     * @param multipartFile 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址
     */
	public static String uploadFile(MultipartFile multipartFile, String endpoint, String accessKeyId, 
			String accessKeySecret, String bucketName, String projectName, String modelName) {
		String url = "";
		try {
			// 确保上传文件不为空
			if (EmptyUtil.isNotEmpty(multipartFile)) {
				url = uploadFile(multipartFile, null, endpoint, accessKeyId, accessKeySecret, bucketName, projectName, modelName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("uploadFile异常信息：{}", ExceptionUtil.logExceptionStack(e));
		}
		return url;
	}
	
	/**
     * @description 上传多个文件到阿里云OSS
     * @param multipartFiles 上传文件对象集合
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址（多个以,分隔）
     */
	public static String uploadFiles(List<MultipartFile> multipartFiles, String endpoint, String accessKeyId, 
			String accessKeySecret, String bucketName, String projectName, String modelName) {
		StringBuilder builder = new StringBuilder();
		// 确保上传文件不为空
		if (EmptyUtil.isNotEmpty(multipartFiles)) {
			for (int i = 0; i < multipartFiles.size(); i++) {
				String url = uploadFile(multipartFiles.get(i), endpoint, accessKeyId, accessKeySecret, bucketName, projectName, modelName);
				// 最后一个
				if (Objects.equals(i, multipartFiles.size() - 1)) {
					builder.append(url);
				} else {
					builder.append(url).append(",");
				}
			}
		}
		return builder.toString();
	}
	
	/**
     * @description 上传文件到阿里云OSS
     * @param file 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址
     */
	public static String uploadFile(File file, String endpoint, String accessKeyId, 
			String accessKeySecret, String bucketName, String projectName, String modelName) {
		String url = "";
		try {
			//确保上传文件不为空
			if (EmptyUtil.isNotEmpty(file)) {
				url = uploadFile(null, file, endpoint, accessKeyId, accessKeySecret, bucketName, projectName, modelName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error("uploadFile异常信息：{}", ExceptionUtil.logExceptionStack(e));
		}
		return url;
	}
	
    /**
     * @description 上传文件到阿里云OSS
     * @param multipartFile 上传文件对象
     * @param file 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址
     * @throws Exception 异常信息
     */
	private static String uploadFile(MultipartFile multipartFile, File file, String endpoint, String accessKeyId, 
			String accessKeySecret, String bucketName, String projectName, String modelName) throws Exception {
		
		// 1、创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
		
		// 2、判断Bucket是否存在，如果已存在就不再创建，否则创建
		if (ossClient.doesBucketExist(bucketName)) {
            System.out.println("您已经创建Bucket：" + bucketName + "。");
        } else {
            System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
            ossClient.createBucket(bucketName);
        }
		
		// 3、获取上传的文件名（比如：选择题.mp4）
		String fileName = "";
		if (EmptyUtil.isNotEmpty(multipartFile)) {
			fileName = multipartFile.getOriginalFilename();
		}
		if (EmptyUtil.isNotEmpty(file)) {
			fileName = file.getName();
		}
		
		// 4、生成新的文件名（确保唯一，比如：154494713425369550795.mp4）
		fileName = NumberGenerator.createId() + fileName.substring(fileName.lastIndexOf("."));
		
		// 5、生成文件存储目录（比如：education/videoInfo/20181215/）
		String dir = projectName + "/" + modelName + "/" + CalendarUtil.getNowDate("yyyyMMdd") + "/";
		
		// 6、文件最终上传地址（比如：education/videoInfo/20181215/154494713425369550795.mp4）（相对于存储空间的地址）
		String filePath = dir + fileName;
		
		// 7、上传文件
		if (EmptyUtil.isNotEmpty(multipartFile)) {
			ossClient.putObject(bucketName, filePath, new ByteArrayInputStream(multipartFile.getBytes()));
		}
		if (EmptyUtil.isNotEmpty(file)) {
			ossClient.putObject(bucketName, filePath, file);
		}
		
		// 8、关闭OSSClient实例
		ossClient.shutdown();
		
		// 9、获取上传文件访问url地址
		Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
		URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);
		
		// 10、返回上传文件访问url地址
		return url.toString();
	}
	
	/**
	 * @description 删除OSS中存储的文件
	 * @param endpoint 服务终端
	 * @param accessKeyId 许可证ID
	 * @param accessKeySecret 许可证秘钥
	 * @param bucketName 存储空间名
	 * @param filePath 文件地址（相对于存储空间的地址）
	 */
	public static void deleteFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String filePath) {
		// 1、创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		// 2、删除文件
		ossClient.deleteObject(bucketName, filePath);

		// 3、关闭OSSClient
		ossClient.shutdown();
	}
	
	/**
	 * @description 列举文件
	 * @param endpoint 服务终端
	 * @param accessKeyId 许可证ID
	 * @param accessKeySecret 许可证秘钥
	 * @param bucketName 存储空间名
	 * @return 返回列举的文件信息（包括相对于存储空间的地址以及文件大小B）
	 */
	public static List<Map<String, Object>> listFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName) {
		// 1、创建OSSClient实例
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		// 2、获取所有文件的描述信息
		ObjectListing objectListing = ossClient.listObjects(bucketName);
		List<OSSObjectSummary> objectSummaries = objectListing.getObjectSummaries();
		
		// 3、循环遍历打印输出文件的描述信息
		List<Map<String, Object>> fileList = new ArrayList<>();
		for (OSSObjectSummary objectSummary : objectSummaries) {
		    System.out.println("名称：" + objectSummary.getKey() + "，大小：" + objectSummary.getSize() + "B");
		    Map<String, Object> file = new HashMap<>();
		    file.put("filePath", objectSummary.getKey());
		    file.put("fileSize", objectSummary.getSize());
		    fileList.add(file);
		}

		// 4、关闭OSSClient
		ossClient.shutdown();
		
		// 5、返回列举的文件信息（包括相对于存储空间的地址以及文件大小B）
		return fileList;
	}
	
	/**
	 * @description 下载文件
	 * @param endpoint 服务终端
	 * @param accessKeyId 许可证ID
	 * @param accessKeySecret 许可证秘钥
	 * @param bucketName 存储空间名
	 * @param filePath 文件地址
	 */
	public static void downloadFile(String endpoint, String accessKeyId, String accessKeySecret, String bucketName, String filePath) {
		// 1、创建OSSClient实例。
		OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

		// 2、调用ossClient.getObject返回一个OSSObject实例，该实例包含文件内容及文件元信息。
		OSSObject ossObject = ossClient.getObject(bucketName, filePath);
		
		// 3、调用ossObject.getObjectContent获取文件输入流，可读取此输入流获取其内容。
		try {
			InputStream content = ossObject.getObjectContent();
			if (content != null) {
			    BufferedReader reader = new BufferedReader(new InputStreamReader(content));
			    while (true) {
			        String line = reader.readLine();
			        if (line == null) {
						break;
					}
			        System.out.println("\n" + line);
			    }
				// 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
			    content.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
			log.error("downloadFile异常信息：{}", ExceptionUtil.logExceptionStack(e));
		}

		// 4、关闭OSSClient。
		ossClient.shutdown();
	}

}
