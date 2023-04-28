package com.mudxx.tool.wx.official;


import com.mudxx.common.utils.equals.EqualsUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * description: 微信公众号文件上传永久文件（主要是图片）工具类
 * @author laiwen
 * @date 2020-01-13 20:20:06
 */
@SuppressWarnings("ALL")
public class WxPermUploadUtil {

    /**
     * 文件上传接口地址
     */
    private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * 温馨提示：上传永久素材
     * description: 模拟form表单的形式，上传文件，以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
     * @param filePath 文件在服务器保存路径，比如：https://dmszzl.mynatapp.cc/ddwashcar/file/video/laiya_demo.mp4
     * @param accessToken 调用接口凭证
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param title 视频标题，type为video时需要，其他类型设null
     * @param introduction 视频描述，type为video时需要，其他类型设null
     * @return 返回接口的响应信息
     * @author laiwen
     * @date 2020-01-13 16:10:27
     */
    public static String uploadFile(String filePath, String accessToken, String type, String title, String introduction) {
        String result;
        HttpURLConnection downloadCon;
        InputStream inputStream;
        try {
            URL urlFile = new URL(filePath);
            downloadCon = (HttpURLConnection) urlFile.openConnection();
            inputStream = downloadCon.getInputStream();

            // 请求地址，微信接口地址，比如：https://api.weixin.qq.com/cgi-bin/material/add_material?access_token=ACCESS_TOKEN&type=video
            String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
            URL urlObj = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlObj.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            String boundary = "-----------------------------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = conn.getOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            String regex = ".*/([^.]+)";
            output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", filePath.replaceAll(regex, "$1")).getBytes());
            output.write("Content-Type: video/mp4 \r\n\r\n".getBytes());
            int bytes;
            byte[] bufferOut = new byte[1024];
            while ((bytes = inputStream.read(bufferOut)) != -1) {
                output.write(bufferOut, 0, bytes);
            }
            inputStream.close();

            output.write(("--" + boundary + "\r\n").getBytes());
            output.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
            output.write(String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}", title, introduction).getBytes());
            output.write(("\r\n--" + boundary + "--\r\n\r\n").getBytes());
            output.flush();
            output.close();
            inputStream.close();
            InputStream resp = conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            while ((bytes = resp.read(bufferOut)) > -1) {
                sb.append(new String(bufferOut, 0, bytes, "utf-8"));
            }
            resp.close();
            result = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return result;
    }

    /**
     * description: 上传永久素材(支持所有文件类型)
     * @param filePath 地文件存储路径，比如："H:\\images\\fwys.jpg"
     * @param accessToken 调用接口凭证
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @param title type为video时需要，其他类型设null
     * @param introduction type为video时需要，其他类型设null
     * @return 返回值类如：{"media_id":MEDIA_ID,"url":URL}
     * @author laiwen
     * @date 2020-01-13 16:36:14
     */
    public static String uploadPermanentMaterial(String filePath, String accessToken, String type, String title, String introduction) {
        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        String result = null;
        try {
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                throw new Exception("文件不存在！");
            }

            URL uploadUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) uploadUrl.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(30000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Cache-Control", "no-cache");
            String boundary = "-----------------------------" + System.currentTimeMillis();
            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            OutputStream output = conn.getOutputStream();
            output.write(("--" + boundary + "\r\n").getBytes());
            output.write(String.format("Content-Disposition: form-data; name=\"media\"; filename=\"%s\"\r\n", file.getName()).getBytes());
            output.write("Content-Type: video/mp4 \r\n\r\n".getBytes());

            byte[] data = new byte[1024];
            int len;
            FileInputStream input = new FileInputStream(file);
            while ((len = input.read(data)) > -1) {
                output.write(data, 0, len);
            }

            // 对类型为video的素材进行特殊处理
            if (EqualsUtil.isEquals("video", type)) {
                output.write(("--" + boundary + "\r\n").getBytes());
                output.write("Content-Disposition: form-data; name=\"description\";\r\n\r\n".getBytes());
                output.write(String.format("{\"title\":\"%s\", \"introduction\":\"%s\"}", title, introduction).getBytes());
            }

            output.write(("\r\n--" + boundary + "--\r\n\r\n").getBytes());
            output.flush();
            output.close();
            input.close();

            InputStream resp = conn.getInputStream();
            StringBuilder sb = new StringBuilder();
            while ((len = resp.read(data)) > -1) {
                sb.append(new String(data, 0, len, "utf-8"));
            }
            resp.close();
            result = sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
