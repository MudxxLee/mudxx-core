package com.mudxx.common.utils.file;

import com.mudxx.common.utils.date.CalendarUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;

/**
 * @description 文件相关操作辅助类（包括文件的拷贝、删除，获取文件的扩展名、文件名称，保存文件内容打印异常信息）。
 * @author laiwen
 * @date 2018年7月6日 上午11:28:51
 */
@SuppressWarnings("ALL")
public class FileUtil {

	/**
	 * 文件夹分隔符（使用"/"作为文件夹分隔符也可以，因为系统会自动进行转换）
	 */
	private final static String FOLDER_SEPARATOR = "\\";

	/**
	 * 文件后缀名分隔符
	 */
	private final static char EXTENSION_SEPARATOR = '.';

	/**
	 * 换行
	 */
	private final static String NEW_LINE = "\r\n";

	/**
	 * 文件路径（其实文件路径可以没有，直接写到文件里面即可）
	 */
	private final static String FILE_PATH = "E:\\my_log\\my_log.txt";

	/**
	 * 文件（参数可以直接写具体的文件路径）
	 */
	private final static File FILE = new File(FILE_PATH);

	/**
	 * IE浏览器的关键字
	 */
	private final static String[] IEBROWSERSIGNALS = {"MSIE", "Trident", "Edge"};

	//当前日期（变化的，final修饰并不影响）
	//private final static String NOW_DATE = CalendarUtil.getNowDate("yyyy-MM-dd HH:mm:ss");

	/**
	 * @description 获取当前路径，比如打印输出：D:\workspace\project\ideaProject\demo\. 当前目录就是demo目录。
	 * @return 返回当前路径
	 */
	public static String getPresentPath() {
		//D:\workspace\project\ideaProject\demo
		//user.dir指定了当前的路径
		//return System.getProperty("user.dir");
		return new File(".").getAbsolutePath();
	}

	/**
	 * @description 根据文件全路径生成文件或者文件夹（如果是文件，不生成文件内容）
	 * @param fileName 文件全路径（文件夹+文件）
	 * @return 返回生成的文件或者文件夹
	 */
	public static File createFile(String fileName) throws IOException {
		/*File file = new File(fileName);
		//if (file.isDirectory()) {//如果文件是文件夹（必须存在才能判断）
			//file.mkdirs();//创建文件夹
		//} else if(file.isFile()) {//如果文件是文件（必须存在才能判断）
			if (!file.getParentFile().exists()) {//如果父文件（文件夹）不存在
				file.getParentFile().mkdirs();//只会创建文件夹，不会创建文件
			}
			if (!file.exists()) {//如果文件不存在
				file.createNewFile();//创建文件
			}
		//}*/
		File file = new File(fileName);
		//if (fileName.indexOf(".") == -1) {//文件夹
		if (!fileName.contains(".")) {
			//文件夹
			if (!file.exists()) {
				//如果文件夹不存在
				//创建文件夹
				file.mkdirs();
			}
		} else {
			//文件
			if (!file.getParentFile().exists()) {
				//如果父文件（文件夹）不存在
				//只会创建文件夹，不会创建文件
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				//如果文件不存在
				//创建文件
				file.createNewFile();
			}
		}
		return file;
	}

	/**
	 * @description copy文件或者文件夹
	 * @param inputFile 源文件
	 * @param outputFile 目的文件
	 * @param isOverWrite 是否允许覆盖（只针对文件），true表示允许，false表示不允许
	 * @throws IOException IO流异常
	 */
	public static void copy(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (!inputFile.exists()) {
			//如果源文件不存在
			//抛出运行时异常不再向下执行
			throw new RuntimeException(inputFile.getPath() + "源文件不存在！");
		}
		//递归copy文件或者文件夹
		copyByRecursion(inputFile, outputFile, isOverWrite);
	}

	/**
	 * @description 为copy做递归使用
	 * @param inputFile 源文件
	 * @param outputFile 目的文件
	 * @param isOverWrite 是否允许覆盖（只针对文件），true表示允许，false表示不允许
	 * @throws IOException IO流异常
	 */
	public static void copyByRecursion(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (inputFile.isFile()) {
			//如果源文件是个文件
			//copy单个文件
			copySingleFile(inputFile,outputFile,isOverWrite);
		} else {
			//如果源文件是个文件夹
			if (isFileNotDir(outputFile)) {
				//单个文件而非文件夹
				//如果是单个文件而不是文件夹，就直接抛出运行时异常，停止程序继续向下执行
				throw new RuntimeException("确保是文件夹而不是单个文件！");
			}
			if (!outputFile.exists()) {
				//如果目的文件夹不存在
				//创建目的文件夹，最好使用mkdirs，少用mkdir
				outputFile.mkdirs();
			}
			for (File child : inputFile.listFiles()) {
				//循环子文件夹
				copy(child, new File(outputFile.getPath() + FOLDER_SEPARATOR + child.getName()), isOverWrite);
			}
		}
	}

	/**
	 * @description copy单个文件
	 * @param inputFile 源文件
	 * @param outputFile 目的文件
	 * @param isOverWrite 是否允许覆盖（只针对文件），true表示允许，false表示不允许
	 * @throws IOException IO流异常
	 */
	public static void copySingleFile(File inputFile, File outputFile, boolean isOverWrite) throws IOException {
		if (outputFile.exists()) {
			//如果目的文件已经存在
			if (isOverWrite) {
				//允许覆盖
				if (!outputFile.delete()) {
					//如果已存在的目的文件删除不成功
					//抛出运行时异常不再向下执行
					throw new RuntimeException(outputFile.getPath() + "无法覆盖！");
				}
			} else {
				//不允许覆盖
				return;
			}
		}

		//判断是不是单个文件
		//方式一
		/*String fileName = outputFile.getName();//getName()获取文件名（含有后缀），不含路径
		if(!isFileNotDir(fileName)){//文件夹*/
		//方式二
		/*String filePath = outputFile.getPath();//getPath()获取文件全路径
		if(!isFileNotDir(filePath)){//文件夹*/
		//方式三
		if (!isFileNotDir(outputFile)) {
			//文件夹
			//如果是文件夹而不是单个文件，就直接抛出运行时异常，停止程序继续向下执行
			throw new RuntimeException("确保是文件而不是文件夹！");
		}

		//验证文件后缀名称是否一致
		if (!getFileNameExtension(inputFile.getPath())
				.equals(getFileNameExtension(outputFile.getPath()))) {
			//如果文件后缀名称不一致，就直接抛出运行时异常，停止程序继续向下执行
			throw new RuntimeException("后缀名不一致！");
		}

		if (!outputFile.getParentFile().exists()) {
			//如果父文件（文件夹）不存在
			//只会创建文件夹，不会创建文件
			outputFile.getParentFile().mkdirs();
		}

		//new FileInputStream(File file)的好处是如果文件不存在就创建，当然存在的话就使用已存在的！
		InputStream in = new FileInputStream(inputFile);
		OutputStream out = new FileOutputStream(outputFile);
		//缓冲器
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = in.read(buffer)) != -1) {
			//缓冲器一次性读取输入流里面的1024个字节
			//将buffer里面1024个字节的数据写入到输出流里面，写完之后缓冲器存储清空，依次循环
			out.write(buffer, 0, len);
		}
		//就近原则先关闭输出流
		out.close();
		//就近原则后关闭输入流
		in.close();
	}

	/**
	 * @description 删除文件或者文件夹
	 * @param file 待删除的文件或者文件夹
	 * @return boolean true：删除成功，false：删除失败
	 */
	public static boolean delete(File file) {
		return deleteFile(file);
	}

	/**
	 * @description 删除文件或者文件夹，内部递归使用
	 * @param file 待删除的文件
	 * @return boolean true：删除成功，false：删除失败
	 */
	public static boolean deleteFile(File file) {
		if (null == file || !file.exists()) {
			//如果文件为空或者文件根本不存在
			return false;
		}
		if (!file.isDirectory()) {
			//如果文件不是文件夹（目录），即单个文件
			boolean delFlag = file.delete();
			if (!delFlag) {
				//如果删除失败
				return false;
			} else {
				//如果删除成功
				return true;
			}
		}
		return deleteDir(file);
	}

	/**
	 * @description 删除文件夹（目录）
	 * @param dir 文件夹（目录）
	 * @return boolean true：删除成功，false：删除失败
	 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			//如果文件是文件夹（目录）
			String[] children = dir.list();
			for (int i = 0; i < children.length; i++) {
				//递归删除目录中的子目录
				boolean flag = deleteDir(new File(dir, children[i]));
				if (!flag) {
					//如果删除不成功，返回false
					return false;
				}
			}
		}
		//删除单个文件或者空目录
		return dir.delete();
	}

	/**
	 * @description 从文件路径中抽取文件的扩展名，比如aaa.txt的扩展名就是txt
	 * @param path 文件全路径
	 * @return 如果path为null，直接返回null
	 */
	public static String getFileNameExtension(String path) {
		if (path == null) {
			//如果文件路径为null
			return null;
		}

		// "."最后一次出现的位置，其实只有一个
		int extensionIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		if (extensionIndex == -1) {
			//如果文件路径不含有"."
			return null;
		}

		// "\"最后一次出现的位置
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		if (folderIndex > extensionIndex) {
			return null;
		}

		//从索引extensionIndex + 1开始截取直到末尾，即不包括"."
		return path.substring(extensionIndex + 1);
	}

	/**
	 * @description 从文件路径中抽取文件名，比如从"E:\aaa\bbb.txt"路径中抽取的文件名就是bbb.txt
	 * @param path 文件全路径
	 * @return 抽取出来的文件名, 如果path为null，直接返回null
	 */
	public static String getFileName(String path) {
		if (path == null) {
			//如果文件路径为null
			return null;
		}
		int extensionIndex = path.lastIndexOf(EXTENSION_SEPARATOR);
		int folderIndex = path.lastIndexOf(FOLDER_SEPARATOR);
		return extensionIndex == -1 ? "" : path.substring(folderIndex + 1);
	}

	/**
	 * 自我感觉这个方法不是很好。。。
	 * @description 根据文件名称判断文件是文件还是文件夹
	 * @param fileName 文件名称，可以是文件全路径，也可以仅仅只是文件名称而已（包括后缀）
	 * @return true文件，false文件夹
	 */
	public static boolean isFileNotDir(String fileName) {
		//true代表是文件全路径；false代表只是文件名而已（不包含目录）（包括后缀）
		boolean isPath = false;
		if (fileName.contains(FOLDER_SEPARATOR)) {
			//包含"\"
			isPath = true;
		}
		String suf ;
		if (isPath) {
			//文件全路径
			//"\"最后出现的索引位置
			int folderIndex = fileName.lastIndexOf(FOLDER_SEPARATOR);
			//文件全路径最后一层
			suf = fileName.substring(folderIndex + 1);
		} else {
			//仅仅只是文件名称，不包含路径
			suf = fileName;
		}
		if (suf.contains(".")) {
			//含有"."则是文件
			return true;
		}
		return false;
	}

	/**
	 * @description 根据文件判断文件是文件还是文件夹
	 * @param file 文件对象
	 * @return true文件，false文件夹
	 */
	public static boolean isFileNotDir(File file) {
		/*String filePath = file.getPath();//文件全路径
		int folderIndex = filePath.lastIndexOf(FOLDER_SEPARATOR);//"\"最后出现的索引位置
		String fileName = filePath.substring(folderIndex + 1);//文件全路径最后一层*/

		//文件名称而已（包括后缀）
		String fileName = file.getName();
		if (fileName.contains(".")) {
			//含有"."则是文件
			return true;
		}
		return false;
	}

	/**
	 * @description 保存文件（不建议使用，如果得到的是字节数组建议使用byteArrayToString进行转换）
	 * @param content 文件内容（参数类型是字节数组还是字符串根据需求来）
	 * @param file 保存的文件
	 * @throws IOException IO流异常
	 */
	public static void save(byte[] content, File file) throws IOException {
		if (content == null) {
			//抛出运行时异常，中断（终止）程序运行
			throw new RuntimeException("文件内容不能为空！");
		}
		if (file == null) {
			//抛出运行时异常，停止程序继续向下执行
			throw new RuntimeException("保存文件不能为空！");
		}
		InputStream inputStream = new ByteArrayInputStream(content);
		save(inputStream, file);
	}

	/**
	 * @description 字节数组转字符串
	 * @param byteArray
	 * @return
	 */
	public static String byteArrayToString(byte[] byteArray) {
		return new String(byteArray);
	}

	/**
	 * @description 保存文件
	 * @param content 文件内容（参数类型是字节数组还是字符串根据需求来）
	 * @param file 保存的文件
	 * @throws IOException IO流异常
	 */
	public static void save(String content, File file) throws IOException {
		if (content == null) {
			//抛出运行时异常，中断（终止）程序运行
			throw new RuntimeException("文件内容不能为空！");
		}
		if (file == null) {
			//抛出运行时异常，停止程序继续向下执行
			throw new RuntimeException("保存文件不能为空！");
		}
		//字符串转换为字节数组
		byte[] bytes = content.getBytes();
		InputStream inputStream = new ByteArrayInputStream(bytes);
		save(inputStream, file);
	}

	/**
	 * @description 保存文件（说明：文件的字符编码格式必须是utf-8才不会出现中文乱码）
	 * @param inputStream 输入流
	 * @param file 保存到的文件
	 * @throws IOException IO流异常
	 */
	public static void save(InputStream inputStream, File file) throws IOException {
		if (inputStream == null) {
			//抛出运行时异常，中断（终止）程序运行
			throw new RuntimeException("文件内容不能为空！");
		}
		if (file == null) {
			//抛出运行时异常，停止程序继续向下执行
			throw new RuntimeException("保存文件不能为空！");
		}
		if (!file.getParentFile().exists()) {
			//文件夹不存在就创建
			//创建文件夹，最好使用mkdirs，少用mkdir
			file.getParentFile().mkdirs();
		}
		//覆盖原文件内容，如果文件没有就自动创建
		//OutputStream out = new FileOutputStream(file);
		//true表示追加不覆盖原有的内容
		OutputStream out = new FileOutputStream(file,true);
		//1024byte代表1KB
		byte[] buffer = new byte[1024];
		int len = 0;
		//while ((len = inputStream.read(buffer)) != -1) {//个人认为这两种方式相同
		while ((len = inputStream.read(buffer,0,1024)) != -1) {
			out.write(buffer, 0, len);
		}
		out.close();
		inputStream.close();
	}

	/**
	 * @description 获取标准化内容
	 * @param content
	 * @return
	 */
	public static String getContent(String content) {
		return CalendarUtil.getNowDate("yyyy-MM-dd HH:mm:ss") + "：" + content + NEW_LINE;
	}

	/**
	 * @description 保存文件内容（追加不覆盖）
	 * @param content 待保存的文件内容
	 * @throws IOException IO流异常
	 */
	public static void saveContent(String content) throws IOException {
		save(getContent(content), FILE);
	}

	/**
	 * @description 在日志文件中，打印异常堆栈
	 * @param e 异常对象
	 * @return
	 */
	public static String logExceptionStack(Throwable e) {
		StringWriter errorsWriter = new StringWriter();
		e.printStackTrace(new PrintWriter(errorsWriter));
		return errorsWriter.toString();
	}

	/**
	 * @description 打印异常信息到指定文件中
	 * @param e 异常对象
	 * @throws IOException
	 */
	public static void printExceptionInfo(Throwable e) throws IOException {
		save(getContent(logExceptionStack(e)),FILE);
	}

	/**
	 * @description 判断是否是IE浏览器
	 * @param request 请求对象
	 * @return true：IE浏览器；false：其他浏览器
	 */
	private static boolean isIeBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		for (String signal : IEBROWSERSIGNALS) {
			if (userAgent.contains(signal)){
				return true;
			}
		}
		return false;
	}

	/**
	 * @description 处理IE浏览器以及其他浏览器下文件名显示乱码
	 * @param request 请求实体
	 * @param oldFileName 待处理文件名
	 * @return 返回处理过的文件名
	 */
	public static String handleFileName(HttpServletRequest request, String oldFileName) {
		String newFileName = null;
		try {
			if (isIeBrowser(request)) {
				//IE浏览器
				newFileName = URLEncoder.encode(oldFileName, "UTF-8");
				newFileName = StringUtils.replace(newFileName, "+", " ");
			} else {
				//其他浏览器
				newFileName = new String(oldFileName.getBytes("UTF-8"), "ISO-8859-1");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newFileName;
	}

	/**
	 * 说明：MultipartFile转File成功之后，会在项目的根目录的临时文件夹下生成一个临时文件 *.tmp，
	 * 		toFile = new File(file.getOriginalFilename());会在服务器本地生成临时文件（占用内存）只有当服务器关闭之后才会自动清除，
	 * 		为了避免产生大量的临时文件，我们需要在MultipartFile转为file后删除本地缓存文件，调用本类的deleteTempFile方法即可
	 * description: MultipartFile 转 File
	 * @author laiwen
	 * @date 2021-09-28 13:22:05
	 * @param file MultipartFile对象
	 * @return 返回File对象
	 * @throws Exception 异常信息
	 */
	public static File multipartFileToFile(MultipartFile file) throws Exception {
		File toFile = null;
		if (file.equals("") || file.getSize() <= 0) {
			file = null;
		} else {
			InputStream ins = null;
			ins = file.getInputStream();
			toFile = new File(file.getOriginalFilename());
			inputStreamToFile(ins, toFile);
			ins.close();
		}
		return toFile;
	}

	/**
	 * description: 获取流文件
	 * @author laiwen
	 * @date 2021-09-28 13:22:19
	 * @param ins 输入流
	 * @param file File对象
	 */
	private static void inputStreamToFile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * description: 删除本地临时文件
	 * @author laiwen
	 * @date 2021-09-28 13:22:29
	 * @param file File对象
	 */
	public static void deleteTempFile(File file) {
		if (file != null) {
			File del = new File(file.toURI());
			del.delete();
		}
	}

}
