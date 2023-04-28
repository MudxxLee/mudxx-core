package com.mudxx.tool.wx.official;

import com.alibaba.fastjson.JSONObject;
import com.mudxx.common.utils.equals.EqualsUtil;
import com.mudxx.common.utils.json.JsonTool;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.protocol.Protocol;
import org.apache.commons.httpclient.protocol.SSLProtocolSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * description: 微信公众号文件上传临时文件（主要是图片）工具类
 * @author laiwen
 * @date 2020-01-12 16:56:20
 */
@SuppressWarnings("ALL")
public class WxTempUploadUtil {

    private static final Logger log = LoggerFactory.getLogger(WxTempUploadUtil.class);

    /**
     * 文件上传接口地址
     */
    private static final String UPLOAD_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";

    /**
     * description: 上传本地文件到微信服务器并获取mediaId
     * @param filePath 本地文件存储路径，比如："H:\\images\\fwys.jpg"
     * @param accessToken 调用接口凭证
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @return 返回mediaId
     * @throws Exception 异常信息
     * @author laiwen
     * @date 2020-01-12 16:58:12
     */
    public static String upload(String filePath, String accessToken, String type) throws Exception {
        // 1、校验文件是否存在
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            throw new Exception("文件不存在！");
        }

        // 2、封装请求的url地址
        String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
        URL urlObj = new URL(url);

        // 3、获取连接
        HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

        // 4、设置连接属性
        con.setRequestMethod("POST");
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);

        // 5、设置请求头信息
        con.setRequestProperty("Connection", "Keep-Alive");
        con.setRequestProperty("Charset", "UTF-8");

        // 6、设置边界
        String boundary = "----------" + System.currentTimeMillis();
        con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

        // 7、拼接请求头信息
        StringBuilder builder = new StringBuilder();
        builder.append("--");
        builder.append(boundary);
        builder.append("\r\n");
        builder.append("Content-Disposition: form-data;name=\"file\";filename=\"");
        builder.append(file.getName()).append("\"\r\n");
        builder.append("Content-Type:application/octet-stream\r\n\r\n");
        byte[] head = builder.toString().getBytes("utf-8");

        // 8、获得输出流
        OutputStream out = new DataOutputStream(con.getOutputStream());

        // 9、输出表头
        out.write(head);

        // 10、文件正文部分
        // 把文件以流文件的方式推入到url中
        DataInputStream in = new DataInputStream(new FileInputStream(file));
        int bytes;
        byte[] bufferOut = new byte[1024];
        while ((bytes = in.read(bufferOut)) != -1) {
            out.write(bufferOut, 0, bytes);
        }
        in.close();

        // 11、结尾部分
        // 定义最后数据分隔线
        byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");
        out.write(foot);
        out.flush();
        out.close();

        // 12、读取接口的响应内容
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader reader = null;
        String result = null;
        try {
            //定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
            result = stringBuilder.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        Map resResult = JsonTool.jsonToPojo(result, Map.class);
        // 比如：{type=image, media_id=h32WtqZfXHKotMC3t42RiyLbS8P7efMPf3CrETbfCFILaVdTvvWKDR3ewlevujUs, created_at=1578825624, item=[]}
        log.info("调用上传文件到微信服务器接口响应数据：{}", resResult);

        // 13、获取多媒体文件类型
        String typeName = "media_id";
        if (EqualsUtil.isEquals(type, "thumb")) {
            typeName = "thumb_media_id";
        }

        // 14、获取多媒体文件ID
        String mediaId = resResult.get(typeName).toString();
        // 比如：h32WtqZfXHKotMC3t42RiyLbS8P7efMPf3CrETbfCFILaVdTvvWKDR3ewlevujUs
        log.info("多媒体文件ID：{}", mediaId);
        return mediaId;
    }

    /**
     * 温馨提示：上传临时素材
     * description: 模拟form表单的形式上传文件，以输出流的形式把文件写入到url中，然后用输入流来获取url的响应
     * @author laiwen
     * @date 2020-01-13 14:41:32
     * @param filePath 文件在服务器保存路径，比如：https://dmszzl.mynatapp.cc/ddwashcar/file/image/fwys.jpg
     * @param accessToken 调用接口凭证
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @return 返回接口的响应信息
     */
    public static String uploadFile(String filePath, String accessToken, String type) {
        String result = null;
        HttpURLConnection downloadCon = null;
        InputStream inputStream = null;
        BufferedReader reader = null;
        try {
            URL urlFile = new URL(filePath);
            downloadCon = (HttpURLConnection) urlFile.openConnection();
            inputStream = downloadCon.getInputStream();

            // 第一部分
            // 请求地址，微信接口地址，比如：http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=image
            String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE", type);
            URL urlObj = new URL(url);

            // 连接
            HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

            // 设置关键值
            // 以Post方式提交表单，默认get方式
            con.setRequestMethod("POST");
            con.setDoInput(true);
            con.setDoOutput(true);
            // post方式不能使用缓存
            con.setUseCaches(false);

            // 设置请求头信息
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");

            // 设置边界
            String boundary = "---------------------------" + System.currentTimeMillis();
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);

            // 请求正文信息
            // 第一部分：
            StringBuilder sb = new StringBuilder();
            String regex = ".*/([^.]+)";
            // 必须多两道线
            sb.append("--");
            sb.append(boundary);
            sb.append("\r\n");
            sb.append("Content-Disposition: form-data;name=\"media\";filename=\"" + filePath.replaceAll(regex, "$1") + "\"\r\n");
            sb.append("Content-Type:application/octet-stream\r\n\r\n");

            byte[] head = sb.toString().getBytes("utf-8");

            // 获得输出流
            OutputStream out = new DataOutputStream(con.getOutputStream());

            // 输出表头
            out.write(head);

            // 文件正文部分
            // 把文件已流文件的方式 推入到url中
            int bytes = 0;
            byte[] bufferOut = new byte[1024];
            while ((bytes = inputStream.read(bufferOut)) != -1) {
                out.write(bufferOut, 0, bytes);
            }
            inputStream.close();

            // 结尾部分
            // 定义最后数据分隔线
            byte[] foot = ("\r\n--" + boundary + "--\r\n").getBytes("utf-8");
            out.write(foot);
            out.flush();
            out.close();

            StringBuffer buffer = new StringBuffer();
            // 定义BufferedReader输入流来读取URL的响应
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
            if (result == null) {
                result = buffer.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * description: 临时素材上传到微信服务器
     * @param filePath 地文件存储路径，比如："H:\\images\\fwys.jpg"
     * @param accessToken 调用接口凭证
     * @param type 媒体文件类型，分别有图片（image）、语音（voice）、视频（video）和缩略图（thumb）
     * @return 返回接口响应内容
     * @author laiwen
     * @date 2020-01-13 17:11:35
     */
    public static JSONObject uploadMedia(String filePath, String accessToken, String type) {
        if (accessToken == null || type == null) {
            return null;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            log.info("上传文件不存在，请检查!");
            return null;
        }
        JSONObject jsonObject = null;
        String uploadUrl = "https://api.weixin.qq.com/cgi-bin/media/upload";
        PostMethod post = new PostMethod(uploadUrl);
        post.setRequestHeader("Connection", "Keep-Alive");
        post.setRequestHeader("Cache-Control", "no-cache");
        FilePart media;
        HttpClient httpClient = new HttpClient();

        //信任任何类型的证书
        Protocol myHttps = new Protocol("https", new SSLProtocolSocketFactory(), 443);
        Protocol.registerProtocol("https", myHttps);

        try {
            media = new FilePart("media", file);
            Part[] parts = new Part[]{
                    new StringPart("access_token", accessToken),
                    new StringPart("type", type),
                    media
            };
            MultipartRequestEntity entity = new MultipartRequestEntity(parts, post.getParams());
            post.setRequestEntity(entity);
            int status = httpClient.executeMethod(post);
            if (status == HttpStatus.SC_OK) {
                String text = post.getResponseBodyAsString();
                jsonObject = JSONObject.parseObject(text);
            } else {
                log.info("upload Media failure status is:" + status);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

}
