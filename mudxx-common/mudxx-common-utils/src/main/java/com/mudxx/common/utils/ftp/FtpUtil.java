package com.mudxx.common.utils.ftp;

import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.id.NumberGenerator;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description ftp上传下载工具类
 * @author laiwen
 * @date 2017年6月6日 下午10:29:50
 */
@SuppressWarnings("ALL")
public class FtpUtil {

	private static final Logger log = LoggerFactory.getLogger(FtpUtil.class);
	
	/** 
	 * @description 向FTP服务器上传文件 
	 * @param host FTP服务器hostname（其实就是FTP服务器的IP地址）（192.168.0.204）
	 * @param port FTP服务器端口 （21）
	 * @param username FTP登录账号 （virtual）
	 * @param password FTP登录密码 （Goodfaith=100m）
	 * @param basePath FTP服务器基础目录（/home/ftp）（一般我们使用nginx的根目录，比如默认的/usr/local/nginx/html）
	 * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01（/adminNew/developerSettleAct/2018-08-23），文件的路径为basePath+filePath（/home/ftp/adminNew/developerSettleAct/2018-08-23）
	 * @param filename 上传到FTP服务器上的文件名 （gaigeming.jpg）
	 * @param input 输入流 （new FileInputStream(new File("d:\\sctp.png"))）
	 * @return 返回true表示上传文件成功，返回false表示上传文件失败
	 */
	public static boolean uploadFile(String host, int port, String username, String password, String basePath, String filePath, String filename, InputStream input) {
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			// 连接FTP服务器，如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.connect(host, port);
			// 登录
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.info("登录失败！");
				return false;
			}
			log.info("登录成功！");
			// 切换到上传目录
			if (!ftp.changeWorkingDirectory(basePath + filePath)) {
				// 如果目录不存在创建目录
				String[] dirs = filePath.split("/");
				String tempPath = basePath;
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) {
						continue;
					}
					tempPath += "/" + dir;
					if (!ftp.changeWorkingDirectory(tempPath)) {
						if (!ftp.makeDirectory(tempPath)) {
							log.info("创建目录失败");
							return false;
						} else {
							ftp.changeWorkingDirectory(tempPath);
						}
					}
				}
			}
			// 设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			// 上传文件
			if (!ftp.storeFile(filename, input)) {
				log.info("上传失败！");
				return false;
			}
			log.info("上传成功！");
			input.close();
			ftp.logout();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return false;
	}

	/** 
	 * @description 从FTP服务器下载文件 
	 * @param host FTP服务器hostname（其实就是FTP服务器的IP地址）
	 * @param port FTP服务器端口号
	 * @param username FTP登录账号 
	 * @param password FTP登录密码 
	 * @param remotePath FTP服务器目录（需要下载的文件所在的目录，比如/home/ftp/adminNew/developerSettleAct/2018-08-23）
	 * @param fileName 要下载的文件名 
	 * @param localPath 下载后保存到本地的路径 
	 * @return 返回true表示下载文件成功，返回false表示下载文件失败
	 */
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath, String fileName, String localPath) {
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			// 登录
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return false;
			}
			// 转移到FTP服务器目录
			ftp.changeWorkingDirectory(remotePath);
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());
					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}
			ftp.logout();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * @description 图片（文件）上传
	 * @param uploadFiles 上传的图片文件对象（可能不止一张） 
	 * @param ftpAddress ftp服务器地址（IP地址）
	 * @param ftpPort ftp服务器端口号
	 * @param ftpUsername ftp服务器登录账号
	 * @param ftpPassword ftp服务器登录密码
	 * @param ftpBasePath 图片上传的基础目录，比如：/home/ftp（一般我们使用nginx的根目录，比如默认的/usr/local/nginx/html）
	 * @param filePath 图片上传的自定义目录，即在基础目录下面创建的指定目录（项目名+模块名），比如： /adminNew/developerSettleAct，根据日期添加子目录比如/2018-08-23
	 * @param imageBaseUrl NGINX服务访问地址，比如：http://192.168.0.204:10000
	 * @return 返回上传图片的结果，包含错误代号以及相应的错误信息
	 */
	public static Map<String, Object> uploadPicture(List<MultipartFile> uploadFiles, String ftpAddress, int ftpPort, String ftpUsername, String ftpPassword, String ftpBasePath, String filePath, String imageBaseUrl) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			String urls = "";
			for (MultipartFile uploadFile : uploadFiles) {
				// 取原始文件名
				String oldName = uploadFile.getOriginalFilename();
				// 生成新文件名
				String newName = NumberGenerator.generatorId() + oldName.substring(oldName.lastIndexOf("."));
				String datePath = "/" + CalendarUtil.dateToString(CalendarUtil.getSystemCurrentDate(), "yyyy-MM-dd");
				boolean result = uploadFile(ftpAddress, ftpPort, ftpUsername, ftpPassword, ftpBasePath, filePath + datePath, newName, uploadFile.getInputStream());
				if (!result) {
					// 如果上传图片失败
					resultMap.put("error", 1);
					resultMap.put("message", "图片上传失败！");
					return resultMap;
				}
				String url = imageBaseUrl + filePath + datePath + "/" + newName;
				if ("".equals(urls)) {
					urls += url;
				} else {
					urls += "," + url;
				}
			}
			resultMap.put("error", 0);
			resultMap.put("url", urls);
			return resultMap;
		} catch (Exception e) {
			resultMap.put("error", 1);
			resultMap.put("message", "图片上传出现异常！");
			return resultMap;
		}
	}

	/**
	 * @description 借助FTP服务器将文件删除（从NGINX服务器里面删除）
	 * @param host FTP服务器hostname（其实就是FTP服务器的IP地址）（192.168.0.204）
	 * @param port FTP服务器端口号（21）
	 * @param username FTP登录账号（登录用户名）（virtual）
	 * @param password FTP登录密码（Goodfaith=100m）
	 * @param basePath FTP服务器基础目录（/home/ftp）（一般我们使用nginx的根目录，比如默认的/usr/local/nginx/html）
	 * @param filePath FTP服务器文件存放路径。例如分日期存放：/2015/01/01（/adminNew/developerSettleAct/2018-08-23），文件的路径为basePath+filePath（/home/ftp/adminNew/developerSettleAct/2018-08-23）
	 * @param fileName 上传到FTP服务器上的文件名 （gaigeming.jpg）
	 * @return 返回true表示删除文件成功，返回false表示删除文件失败
	 */
	public static boolean deleteFileFtp(String host, int port, String username, String password, String basePath, String filePath, String fileName) {
		try {
			int reply;
			// host为FTP服务器的IP地址，port为FTP服务器的登录端口，host为String类型，port为int类型。
			FTPClient ftp = new FTPClient();
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.connect(host, port);
			// username、password分别为FTP服务器的登录用户名和密码
			ftp.login(username, password);
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				log.info("登录失败！");
				return false;
			} else {
				log.info("登录成功！");
				try {
					String ftpPath = basePath + filePath;
					// 转移到FTP服务器目录（其实就是需要被删除的文件所在的目录）
					ftp.changeWorkingDirectory(ftpPath);
					boolean flag = ftp.deleteFile(fileName);
					if (flag) {
						log.info("删除成功！");
						ftp.logout();
						return true;
					} else {
						log.info("删除失败或文件不存在！");
						return false;
					}
				} catch (Exception e) {
					log.info("删除文件失败！请检查系统FTP设置，并确认FTP服务启动");
					return false;
				}
			}
		} catch (Exception e) {
			log.info("删除文件失败！");
			return false;
		}
	}

	public static void main(String[] args) {
		try {
			/*File file = new File("C:\\Users\\Public\\Pictures\\Sample Pictures\\112.jpg");
			FileInputStream in = new FileInputStream(file);
			boolean flag = uploadFile("192.168.0.204", 21, "virtual", "Goodfaith=100m", "/home/ftp", "/adminNew/developerSettleAct/2018-08-23", "gaigeming.jpg", in);
			System.out.println(flag);*/
			boolean flag = deleteFileFtp("192.168.0.204", 21, "virtual", "Goodfaith=100m", "/home/ftp", "/adminNew/developerSettleAct/2018-08-23", "gaigeming.jpg");
			System.out.println(flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
