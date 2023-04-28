package com.mudxx.common.utils.file;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @description 下载文件
 * @author laiwen
 * @date 2017-04-26 15:55
 */
@SuppressWarnings("ALL")
public class DownloadFile {

	/**
	 * @Desc 下载文件方式一（下载目录无法控制，默认是 C:\Users\zhuzh\Downloads）
	 *       优点是可以控制下载到本地目录的文件名，即downloadName
	 * @param fileName 服务器文件名（文件全路径）
	 * @param downloadName 下载文件名（含有后缀），不含路径
	 * @param response 响应
	 */
	public static void download(String fileName, String downloadName, HttpServletResponse response) {
		try {
			response.setContentType("octets/stream");
			response.addHeader("Content-Type", "text/html; charset=utf-8");
			String downLoadName = new String(downloadName.getBytes("gbk"), "iso8859-1");
			//downLoadName只是文件名（含有后缀），不含路径
			response.addHeader("Content-Disposition", "attachment;filename=" + downLoadName);

			FileInputStream fileInputStream = new FileInputStream(fileName);
			OutputStream out = response.getOutputStream();
			int i ;
			while ((i = fileInputStream.read()) != -1) {
				out.write(i);
			}
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Desc 下载文件方式二（下载目录无法控制，默认是 C:\Users\zhuzh\Downloads）
	 *       优点暂时还没有发现，下载到本地目录的文件名就是待下载的文件名，即文件名称不变
	 * @param filePath 待下载的文件全路径
	 * @param response
	 * @param isOnLine 是否在线 true：在线，false：不在线
	 * @throws Exception
	 */
	public static void download(String filePath, HttpServletResponse response, boolean isOnLine) throws Exception {
		File f = new File(filePath);
		if (!f.exists()) {
			response.sendError(404, "File not found!");
			return;
		}
		BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
		byte[] buf = new byte[1024];
		int len = 0;
		response.reset();
		if (isOnLine) {
			URL u = new URL("file:///" + filePath);
			response.setContentType(u.openConnection().getContentType());
			//getName()获取文件名（含有后缀），不含路径
			response.setHeader("Content-Disposition", "inline; filename=" + f.getName());
		} else {
			response.setContentType("application/x-msdownload");
			//getName()获取文件名（含有后缀），不含路径
			response.setHeader("Content-Disposition", "attachment; filename=" + f.getName());
		}
		OutputStream out = response.getOutputStream();
		while ((len = br.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		br.close();
		out.close();
	}

	/**
	 * @Desc 下载文件方式三（下载目录无法控制，默认是 C:\Users\zhuzh\Downloads）
	 *       优点暂时还没有发现，下载到本地目录的文件名就是待下载的文件名，即文件名称不变
	 * @param response
	 * @param filePath 所要下载的文件路径，从数据库中查询得到，当然也可以直接写文件路径，如：E:\\style_2003_01.xls
	 * @throws Exception
	 */
	public static void download(HttpServletResponse response, String filePath) throws Exception {
		File file = new File(filePath);
		//得到文件名
		String fileName = filePath.substring(filePath.lastIndexOf(File.separator) + 1);

		//方案一：
		//fileName = new String(fileName.getBytes("UTF-8"),"ISO8859-1");//把文件名按UTF-8取出并按ISO8859-1编码，保证弹出窗口中的文件名中文不乱码，中文不要太多，最多支持17个中文，因为header有150个字节限制。
		//response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
		//response.addHeader("Content-Disposition", "attachment;filename="+fileName);//Content-Disposition中指定的类型是文件的扩展名，并且弹出的下载对话框中的文件类型图片是按照文件的扩展名显示的，点保存后，文件以filename的值命名，保存类型以Content中设置的为准。注意：在设置Content-Disposition头字段之前，一定要设置Content-Type头字段。
		//方案二：
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename="+ new String(fileName.getBytes("UTF-8"), "iso8859-1"));

		String len = String.valueOf(file.length());
		//设置内容长度
		response.setHeader("Content-Length", len);
		OutputStream out = response.getOutputStream();
		FileInputStream in = new FileInputStream(file);
		byte[] b = new byte[1024];
		int n;
		while((n=in.read(b))!=-1){
			out.write(b, 0, n);
		}
		in.close();
		out.close();
	}

}
