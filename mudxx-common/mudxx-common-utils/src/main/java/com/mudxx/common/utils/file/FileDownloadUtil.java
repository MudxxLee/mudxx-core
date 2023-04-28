package com.mudxx.common.utils.file;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.mudxx.common.utils.map.MapUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Map;

/**
 * @description 文件下载工具类
 * @author laiwen
 * @date 2019年5月10日 下午4:25:53
 */
@SuppressWarnings("ALL")
public class FileDownloadUtil {
	
	/**
     * @description 将用户提交的表单信息下载成我们指定的文件
     * @param request 请求对象
     * @param response 响应对象
     * @param fileName 指定下载生成的文件名，比如：a.ini
     */
    public static void downloadForm(HttpServletRequest request, HttpServletResponse response, String fileName) {
        //表单提交的数据
        Map<String, Object> queryParams = MapUtil.getQueryParams(request);
        //转换成JSON对象
        JSONObject jsonObject = JSONUtil.parseObj(queryParams);
        /* 遍历JSON，将JSON的值放至缓冲区 */
        StringBuilder sb = new StringBuilder();
        Iterator<?> it = jsonObject.iterator();
        while (it.hasNext()) {
            String key =  (String) it.next();
            String value = (String) jsonObject.get(key);
            sb.append(key).append("=").append(value).append("\n");
        }

        /*  设置文件ContentType类型，这样设置，会自动判断下载文件类型   */
        response.setContentType("application/multipart/form-data");

        /* 设置文件头：最后一个参数是设置下载文件名(假如我们叫a.ini)   */
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);

        try {
            /* 用流将数据写给前端 */
            OutputStream os = response.getOutputStream();
            os.write(sb.toString().getBytes());
            os.flush();
            os.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @description 从服务器上下载文件
     * @param response 响应对象
     * @param filePath 文件在服务器上存储的绝对路径，比如：/home/a.txt
     */
    public static void downloadFile(HttpServletResponse response, String filePath) {
        //获取服务器文件
        File file = null;
        InputStream ins = null;
        try {
            file = new File(filePath);
            ins = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        /* 设置文件ContentType类型，这样设置，会自动判断下载文件类型 */
        response.setContentType("multipart/form-data");

        /* 设置文件头：最后一个参数是设置下载文件名 */
        response.setHeader("Content-Disposition", "attachment;filename=" + file.getName());
        try {
            OutputStream os = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            while((len = ins.read(b)) > 0) {
                os.write(b, 0, len);
            }
            os.flush();
            os.close();
            ins.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    /**
     * @description 根据url路径下载文件到本地
     * @param urlString 文件访问url地址，比如：http://*******:8080/picService/resLogoPic/93694182a2ad4778933fa15aa2fe44c0.jpg
     * @param filename 文件名，比如：93694182a2ad4778933fa15aa2fe44c0.jpg
     * @param savePath 文件存储路径，比如：H:\\
     */
    public static void downloadByUrl(String urlString, String filename, String savePath) {
        try {
            //构造URL
            URL url = new URL(urlString);
            //打开连接
            URLConnection con = url.openConnection();
            //设置请求超时为5s
            con.setConnectTimeout(5 * 1000);
            //输入流
            InputStream is = con.getInputStream();
            //1K的数据缓冲
            byte[] bs = new byte[1024];
            //读取到的数据长度
            int len;
            //输出的文件流
            File sf = new File(savePath);
            if (!sf.exists()) {
                sf.mkdirs();
            }
            OutputStream os = new FileOutputStream(sf.getPath() + "\\" + filename);
            //开始读取
            while ((len = is.read(bs)) != -1) {
                os.write(bs, 0, len);
            }
            //完毕，关闭所有连接
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
