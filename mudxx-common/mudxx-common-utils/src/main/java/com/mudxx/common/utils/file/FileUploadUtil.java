package com.mudxx.common.utils.file;

import com.alibaba.fastjson.JSONObject;
import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.ftp.FtpUtil;
import com.mudxx.common.utils.string.StringUtil;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @description 文件上传工具类
 * @author laiwen
 * @date 2018年7月27日 下午8:37:31
 */
@SuppressWarnings("ALL")
public class FileUploadUtil {

	private static final Logger log = LoggerFactory.getLogger(FileUploadUtil.class);

	/**
	 * @description 上传文件（比如图片、PDF文件等等）
	 * @param file 文件对象
	 * @param filePath 文件目录
	 * @return 返回文件全路径
	 * @throws Exception 异常信息
	 */
	public static String uploadFile(MultipartFile file, String filePath) throws Exception {
		String originalFilename = file.getOriginalFilename();
		String fileFullPath = "";
		if(originalFilename.length() > 0) {
			//保证确实有上传文件存在
			if(createDir(filePath)) {
				//文件夹存在或者创建成功
				String newFilename = UUID.randomUUID() + originalFilename
						.substring(originalFilename.lastIndexOf("."));
				fileFullPath = filePath + newFilename;
				File newFile = new File(fileFullPath);
				file.transferTo(newFile);
			}
		}
		return fileFullPath;
	}

	/**
	 * @description 读取绝对路径下的文件（比如图片、PDF文件等等）
	 * @param response 响应对象
	 * @param fileFullPath 文件全路径
	 */
	public static void readFile(HttpServletResponse response, String fileFullPath) {
		log.info("【文件地址】：" + fileFullPath);
		read(response, fileFullPath);
	}

	/**
	 * 说明：如果文件不存在则返回false，如果文件的类型不是文件类型（有可能是文件夹即文件目录）则返回false，
	 * 		如果文件删除不成功则返回false，否则返回true。
	 * @description 删除文件
	 * @param fileFullPath 文件全路径
	 * @return 返回删除文件（不是文件夹）状态
	 */
	public static boolean deleteFile(String fileFullPath) {
		File file = new File(fileFullPath);
		return file.exists() && file.isFile() && file.delete();
	}
	
	/**
	 * 说明：该方法是临时方法，使用场景是：使用tomcat服务器而不是maven的tomcat插件，但是没有项目名的情况
	 * @description 获取tomcat服务器webapps所在的绝对路径
	 * @param request 请求对象
	 * @return 返回tomcat服务器webapps所在的绝对路径
	 */
	public static String getServerPathTemp(HttpServletRequest request) {
		//根据项目名写死
		String projectName = "/projectName";
		String classPath = FileUploadUtil.class.getClassLoader().getResource("").getPath();
		String serverPath = classPath.replace(projectName + "/WEB-INF/classes/", "");
		//有利于debug观察
		return serverPath;
	}
	
	/**
	 * 提醒：不支持maven的tomcat插件启动方式
	 * @description 获取tomcat服务器webapps所在的绝对路径（有缺陷，考虑不周全）
	 * @param request 请求对象
	 * @return 返回tomcat服务器webapps所在的绝对路径
	 */
	public static String getServerPath_(HttpServletRequest request) {
		String projectName = request.getContextPath();
		if (EmptyUtil.isEmpty(projectName)) {
			projectName = "/ROOT";
		}
		log.info("【项目名：{}】", projectName);
		
		String classPath = FileUploadUtil.class.getClassLoader().getResource("").getPath();
		log.info("【类路径：{}】", classPath);
		
		//【方式一】
		String serverPath_1 = classPath.replace(projectName + "/WEB-INF/classes/", "");
		log.info("【tomcat服务器webapps所在的绝对路径：{}】", serverPath_1);
		
		//【方式二】
		String serverPath_2 = classPath.substring(0, classPath.indexOf(projectName));
		log.info("【tomcat服务器webapps所在的绝对路径：{}】", serverPath_2);
		
		return serverPath_2;
	}
	
	/**
	 * 提醒：不支持maven的tomcat插件启动方式
	 * @description 获取tomcat服务器webapps所在的绝对路径（无缺陷，考虑很周全）
	 * @param request 请求对象
	 * @return 返回tomcat服务器webapps所在的绝对路径
	 */
	public static String getServerPath(HttpServletRequest request) {
		String classPath = FileUploadUtil.class.getClassLoader().getResource("").getPath();
		log.info("【类路径：{}】", classPath);
		
		Integer lastIndex = StringUtil.getLastIndex(classPath, "/", 4);
		
		String serverPath = classPath.substring(0, lastIndex);
		log.info("【tomcat服务器webapps所在的绝对路径：{}】", serverPath);
		
		return serverPath;
	}

	/**
	 * @description 上传图片并获取图片访问url地址（上传到tomcat服务器）
	 * @param request 请求对象
	 * @param fileName 页面请求file标签name属性值
	 * @param imageUploadPath 图片上传路径
	 * @param imageServerUrlPrefix 图片访问url地址前缀
	 * @return 如果没有上传图片那么返回空字符串，否则返回图片访问url地址
	 * @throws Exception 异常
	 */
	public static String getImageUrl(HttpServletRequest request, 
			String fileName, String imageUploadPath, String imageServerUrlPrefix) throws Exception {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile multipartFile = multipartRequest.getFile(fileName);
		String imageUrl = "";
		if (multipartFile.getSize() > 0) {
			//如果没有上传图片或者说没有修改图片，那么文件的大小就是0，否则确实有上传图片的话文件的大小就必然大于0
			String imagePath = getServerPath(request) + imageUploadPath;
			log.info("【上传图片绝对路径（文件所在目录）：{}】", imagePath);
			
			String fileFullPath = uploadFile(multipartFile, imagePath);
			log.info("【上传图片绝对全路径包括图片名以及后缀：{}】", fileFullPath);
			
			if (EmptyUtil.isNotEmpty(fileFullPath)) {
				//保证确实有上传文件存在
				String uploadImage = fileFullPath.substring(fileFullPath.indexOf(imageUploadPath));
				log.info("【上传图片相对全路径包括图片名以及后缀：{}】", uploadImage);
				
				imageUrl = imageServerUrlPrefix + uploadImage;
				log.info("【图片访问url地址：{}】", imageUrl);
			}
		}
		return imageUrl;
	}
	
	/**
	 * @description MultipartFile 转 File
	 * @param multipartFile
	 * @return
	 */
	public static File multipartFileToFile(MultipartFile multipartFile) {
		CommonsMultipartFile cf = (CommonsMultipartFile) multipartFile;
		DiskFileItem fi = (DiskFileItem) cf.getFileItem();
		//会在项目的根目录的临时文件夹下生成一个临时文件 *.tmp
		File file = fi.getStoreLocation();
		return file;
	}
	
	/**
	 * 提醒：该方法同样适用于视频文件的上传！
	 * @description 上传图片到nginx图片服务器并获取上传图片的服务访问url地址
	 * @param request 请求对象
	 * @param projectName 项目名称
	 * @param modelName 模块名称
	 * @param pictureName 表单file标签的name属性值
	 * @param paramValue ftp服务信息
	 * @return 返回图片url相关信息
	 */
	public static String getPictureUrl(HttpServletRequest request, String projectName, String modelName, String pictureName, String paramValue) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile>  multipartFiles = multipartRequest.getFiles(pictureName);
		if (EmptyUtil.isEmpty(multipartFiles)) {
			//如果集合元素个数为0个，即没有进行图片上传（或者修改页面图片没有进行修改）
			return null;
		}
		String originalFilename = multipartFiles.get(0).getOriginalFilename();
		String pictureUrl = null;
		if (EmptyUtil.isNotEmpty(originalFilename)) {
			//如果有上传图片
			JSONObject json = JSONObject.parseObject(paramValue);
			//比如：192.168.0.204
			String ftpAddress = json.getString("host");
			//比如：21
			Integer ftpPort = json.getInteger("port");
			//比如：virtual
			String ftpUserName = json.getString("userName");
			//比如：Sound=340
			String ftpPassword = json.getString("password");
			//比如：/home/ftp
			String basePath = json.getString("basePath");
			//比如：/adminNew/developerSettleAct，根据日期添加子目录比如/2018-08-23
			String filePath = "/" + projectName + "/" + modelName;
			//比如：http://192.168.0.204:10000
			String httpPath = json.getString("httpPath");
			Map<String, Object> resultMap = FtpUtil.uploadPicture(multipartFiles, ftpAddress, ftpPort, ftpUserName, ftpPassword, basePath, filePath, httpPath);
			if (Objects.equals(1, resultMap.get("error"))) {
				log.info("【getPictureUrl上传图片出错：{}】", resultMap.get("message"));
			} else {
				pictureUrl = resultMap.get("url").toString();
			}
		}
		return pictureUrl;
	}
	
	/**
	 * @description 获取上传文件对象集合
	 * @param request 请求对象
	 * @param fileName 表单file标签的name属性值
	 * @return 返回上传文件对象集合
	 */
	public static List<MultipartFile> getMultipartFiles(HttpServletRequest request, String fileName) {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		List<MultipartFile> multipartFiles = multipartRequest.getFiles(fileName);
		if (EmptyUtil.isNotEmpty(multipartFiles)) {
			MultipartFile multipartFile = multipartFiles.get(0);
			if (EmptyUtil.isNotEmpty(multipartFile)) {
				//获取上传的文件名
				String originalFilename = multipartFile.getOriginalFilename();
				//文件名为""即空字符串，则表示没有上传文件
				if (EmptyUtil.isEmpty(originalFilename)) {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
		return multipartFiles;
	}

	/**
	 * @description 获取表单中所有的上传文件标签的name的值对应的文件集合
	 * @param request 请求对象
	 * @return 返回map集合，key指的是上传文件标签的name的值，value指的是文件集合
	 */
	public static Map<String, List<MultipartFile>> getMultipartFiles(HttpServletRequest request) {
		Map<String, List<MultipartFile>> map = new HashMap<>();
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		//获取提交表单中所有的file标签的name属性的值
		Iterator<String> names = multipartRequest.getFileNames();
		while (names.hasNext()) {
			String name = names.next();
			List<MultipartFile> multipartFiles = getMultipartFiles(request, name);
			map.put(name, multipartFiles);
		}
		return map;
	}

	/**
	 * @description 借助FTP服务器将文件删除（从NGINX服务器里面删除）
	 * @param nginxParams NGINX服务器配置参数
	 * @param fileUrl 文件访问url地址
	 * @param projectName 项目名称
	 * @param modelName 模块名称
	 * @return 返回true表示删除文件成功，返回false表示删除文件失败
	 */
	public static boolean deleteFileFromNginxByFtp(String nginxParams, String fileUrl, String projectName, String modelName) {
		JSONObject json = JSONObject.parseObject(nginxParams);
		//比如：192.168.0.204
		String ftpAddress = json.getString("host");
		//比如：21
		Integer ftpPort = json.getInteger("port");
		//比如：virtual
		String ftpUserName = json.getString("userName");
		//比如：Sound=340
		String ftpPassword = json.getString("password");
		//比如：/home/ftp
		String basePath = json.getString("basePath");
		Integer lastIndexTwo = StringUtil.getLastIndex(fileUrl, "/", 2);
		Integer lastIndexOne = StringUtil.getLastIndex(fileUrl, "/", 1);
		//比如：/2018-08-23
		String dateName = fileUrl.substring(lastIndexTwo, lastIndexOne);
		//比如：/education/videoInfo/2018-08-23
		String filePath = "/" + projectName + "/" + modelName + dateName;
		//比如：gaigeming.jpg
		String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
		return FtpUtil.deleteFileFtp(ftpAddress, ftpPort, ftpUserName, ftpPassword, basePath, filePath, fileName);
	}
	
	/**
	 * 注意：mkdirs是创建多级文件夹（建议使用），mkdir是创建单级文件夹（不建议使用），createNewFile是创建空文件。
	 * 说明：如果文件目录存在则返回true，如果文件目录不存在就创建多级目录，
	 * 		如果多级目录创建成功则返回true，创建失败返回false。
	 * @description 如果文件目录不存在就创建，有就不再创建
	 * @param fileDir 文件目录
	 * @return 返回创建文件目录状态
	 */
	private static boolean createDir(String fileDir) {
		File dir = new File(fileDir);
		return dir.exists() || dir.mkdirs();
	}

	/**
	 * @description 读取文件
	 * @param response 响应对象
	 * @param fileFullPath 文件全路径
	 */
	private static void read(HttpServletResponse response, String fileFullPath) {
		FileInputStream input = null;
		OutputStream output = null;
		if(StringUtils.isNotBlank(fileFullPath)) {
			//文件全路径不能为null或者""
			File file = new File(fileFullPath);
			if(file.exists()) {
				//文件首先要确保存在
				try {
					input = new FileInputStream(fileFullPath);
					output = response.getOutputStream();
					int count;
					byte[] buffer = new byte[1024 * 8];
					while ((count = input.read(buffer)) != -1) {
						output.write(buffer, 0, count);
						output.flush();
					}
				} catch (IOException e) {
					log.error("【读取失败】：", e);
				}finally {
					try {
						if (output != null) {
							output.close();
						}
						if (input != null) {
							input.close();
						}
					} catch (Exception e) {
						log.error("【文件错误】：", e);
					}
				}
			}
		}
	}

}
