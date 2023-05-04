package com.mudxx.tool.aliyun.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.aliyun.oss.model.ObjectMetadata;
import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.exception.ExceptionUtil;
import com.mudxx.common.utils.id.NumberGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.List;
import java.util.*;

/**
 * @description 阿里云OSS客户端工具类（完善版）
 * @author laiwen
 * @date 2019-07-01 21:43
 */
@SuppressWarnings("ALL")
public class OssUtil {

    private static final Logger log = LoggerFactory.getLogger(OssUtil.class);

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
     * @description 上传单个文件到阿里云OSS（MultipartFile文件）
     * @param multipartFile 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）
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
     * @description 上传多个文件到阿里云OSS（MultipartFile文件）
     * @param multipartFiles 上传文件对象集合
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）（多个以,分隔）
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
     * @description 上传单个文件到阿里云OSS（File文件，测试用）
     * @param file 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @return 返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）
     */
    public static String uploadFile(File file, String endpoint, String accessKeyId,
                                    String accessKeySecret, String bucketName, String projectName, String modelName) {
        String url = "";
        try {
            if (EmptyUtil.isNotEmpty(file)) {//确保上传文件不为空
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
     * @return 返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）
     * @throws Exception 异常信息
     */
    private static String uploadFile(MultipartFile multipartFile, File file, String endpoint, String accessKeyId,
                                     String accessKeySecret, String bucketName, String projectName, String modelName) throws Exception {

        //定义上传文件访问url地址变量
        String result = null;

        // 1、创建OSSClient实例
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {
            // 2、判断Bucket是否存在，如果已存在就不再创建，否则创建
            if (!ossClient.doesBucketExist(bucketName)) {
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

            // 8、获取上传文件访问url地址
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);

            // 9、返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）
            if (EmptyUtil.isNotEmpty(url)) {
                result = url.toString().split("[?]")[0];
            }
        } catch (Exception e) {
            log.error("上传文件至阿里云OSS发生异常：{}", ExceptionUtil.logExceptionStack(e));
        }  finally {
            // 10、关闭OSSClient实例
            ossClient.shutdown();
        }
        return result;
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

    //******************************************************************************************************************//

    /**
     * @description 先对单个文件进行缩放然后再上传到阿里云OSS（MultipartFile文件）
     * @param multipartFile 上传文件对象
     * @param endpoint 服务终端   比如：http://oss-cn-hangzhou.aliyuncs.com
     * @param accessKeyId 许可证ID 比如：LTAIgGMrJS06Femx
     * @param accessKeySecret 许可证秘钥   比如：GK4c1PLxGTeNZz6KAp4MhYp6OjQkCi
     * @param bucketName 存储空间名   比如：xxssxbbucket
     * @param projectName 项目名  比如：education
     * @param modelName 模块名  比如：videoInfo
     * @param widthDist 指定图片宽度（缩放宽度）
     * @param heightDist 指定图片高度（缩放高度）
     * @return 返回上传文件访问url地址（只保留问号?之前的部分，去除参数部分）
     */
    public static String resizeUploadFile(MultipartFile multipartFile, String endpoint, String accessKeyId,
                                          String accessKeySecret, String bucketName, String projectName, String modelName,
                                          int widthDist, int heightDist) {
        try {
            // 确保上传文件不为空
            if (EmptyUtil.isNotEmpty(multipartFile)) {
                // 1、获取文件的原始名
                String fileName = multipartFile.getOriginalFilename();

                // 2、生成新的文件名
                fileName = NumberGenerator.createId() + fileName.substring(fileName.lastIndexOf("."));

                // 3、对上传的单个文件进行缩放
                InputStream inputStream = multipartFile.getInputStream();
                BufferedImage bufImg = ImageIO.read(inputStream);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BufferedImage newBufferedImage = new BufferedImage(widthDist, heightDist, BufferedImage.TYPE_INT_RGB);
                newBufferedImage.createGraphics().drawImage(bufImg, 0, 0, Color.WHITE, null);
                ImageIO.write(newBufferedImage, "jpg", bos);
                inputStream = new ByteArrayInputStream(bos.toByteArray());
                Integer available = inputStream.available();
                Long size = available.longValue();

                // 4、将缩放后的文件上传到阿里云OSS
                String url = uploadObject2Oss(endpoint, accessKeyId, accessKeySecret, bucketName,
                        projectName, modelName, inputStream, fileName, size);
                log.info("【上传文件地址：{}】", url);
                return url;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("先对单个文件进行缩放然后再上传到阿里云OSS异常信息：{}", ExceptionUtil.logExceptionStack(e));
        }
        return null;
    }

    /**
     * @description 上传文件至OSS
     * @param endpoint 服务终端
     * @param accessKeyId 许可证ID
     * @param accessKeySecret 许可证秘钥
     * @param bucketName 存储空间名
     * @param projectName 项目名
     * @param modelName 模块名
     * @param is 输入流
     * @param fileName 文件名
     * @param fileSize 文件大小
     * @return 返回上传文件访问URL地址（只保留问号?之前的部分，去除参数部分）
     */
    public static String uploadObject2Oss(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
                                          String projectName, String modelName, InputStream is, String fileName, Long fileSize) {
        // 定义上传文件访问url地址变量
        String result = null;
        // 图片流为空，直接返回空
        if (is == null) {
            return null;
        }
        // 1、创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 2、创建上传Object的ObjectMetadata    
            ObjectMetadata metadata = new ObjectMetadata();
            // 上传的文件的长度  
            metadata.setContentLength(is.available());
            // 指定该Object被下载时的网页的缓存行为  
            metadata.setCacheControl("no-cache");
            // 指定该Object下设置Header
            metadata.setHeader("Pragma", "no-cache");
            // 指定该Object被下载时的内容编码格式  
            metadata.setContentEncoding("utf-8");
            // 文件的MIME，定义文件的类型及网页编码，决定浏览器将以什么形式、什么编码读取文件。如果用户没有指定则根据Key或文件名的扩展名生成，
            // 如果没有扩展名则填默认值application
            metadata.setContentType(getContentType(fileName));
            // 指定该Object被下载时的名称（指示MINME用户代理如何显示附加的文件，打开或下载，及文件名称）  
            metadata.setContentDisposition("filename/filesize=" + fileName + "/" + fileSize + "Byte.");

            // 3、判断Bucket是否存在，如果已存在就不再创建，否则创建
            if (!ossClient.doesBucketExist(bucketName)) {
                ossClient.createBucket(bucketName);
            }

            // 4、生成文件存储目录（比如：education/userPrize/）
            String dir = projectName + "/" + modelName + "/";

            // 5、文件最终上传地址（比如：education/userPrize/29124_5.jpg）（相对于存储空间的地址）
            String filePath = dir + fileName;

            // 6、上传文件(上传文件流的形式)
            ossClient.putObject(bucketName, filePath, is, metadata);

            // 7、获取上传文件访问URL地址
            Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
            URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);

            // 8、返回上传文件访问URL地址（只保留问号?之前的部分，去除参数部分）
            if (EmptyUtil.isNotEmpty(url)) {
                result = url.toString().split("[?]")[0];
            }
        } catch (Exception e) {
            log.error("uploadObject2OSS异常信息：{}", ExceptionUtil.logExceptionStack(e));
        } finally {
            // 9、关闭OSSClient实例
            ossClient.shutdown();
        }
        return result;
    }

    /**
     * @description 判断OSS是否已存在该文件
     * @param endpoint 服务终端
     * @param accessKeyId 许可证ID
     * @param accessKeySecret 许可证秘钥
     * @param bucketName 存储空间名
     * @param projectName 项目名
     * @param modelName 模块名
     * @param fileName 文件名
     * @return 如果文件存在则返回上传文件访问URL地址（只保留问号?之前的部分，去除参数部分），否则返回null
     */
    public static String ossIsExistUrl(String endpoint, String accessKeyId, String accessKeySecret, String bucketName,
                                       String projectName, String modelName, String fileName) {
        //定义上传文件访问url地址变量
        String result = null;
        // 1、创建OSSClient实例。
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {
            // 2、生成文件存储目录（比如：education/education/）
            String dir = projectName + "/" + modelName + "/";
            // 3、文件最终上传地址（比如：education/education/userPrize/29124_5.jpg）（相对于存储空间的地址）
            String filePath = dir + fileName;
            Boolean isExist = ossClient.doesObjectExist(bucketName, filePath);
            if (isExist) {
                // 4、获取上传文件访问URL地址
                Date expiration = new Date(System.currentTimeMillis() + 3600L * 1000 * 24 * 365 * 10);
                URL url = ossClient.generatePresignedUrl(bucketName, filePath, expiration);
                if (EmptyUtil.isNotEmpty(url)) {
                    // 5、返回上传文件访问URL地址（只保留问号?之前的部分，去除参数部分）
                    result = url.toString().split("[?]")[0];
                }
            }
        } catch (Exception e) {
            log.error("OSSIsExistURL异常信息：{}", ExceptionUtil.logExceptionStack(e));
        } finally {
            // 6、关闭OSSClient实例
            ossClient.shutdown();
        }
        return result;
    }

    /**
     * @description 通过文件名判断并获取OSS服务文件上传时文件的contentType
     * @param fileName 文件名
     * @return 返回文件的contentType
     */
    private static String getContentType(String fileName) {
        // 文件的后缀名
        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        if (".bmp".equalsIgnoreCase(fileExtension)) {
            return "image/bmp";
        }
        if (".gif".equalsIgnoreCase(fileExtension)) {
            return "image/gif";
        }
        if (".jpeg".equalsIgnoreCase(fileExtension)
                || ".jpg".equalsIgnoreCase(fileExtension)
                || ".png".equalsIgnoreCase(fileExtension)) {
            return "image/jpeg";
        }
        if (".html".equalsIgnoreCase(fileExtension)) {
            return "text/html";
        }
        if (".txt".equalsIgnoreCase(fileExtension)) {
            return "text/plain";
        }
        if (".vsd".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.visio";
        }
        if (".ppt".equalsIgnoreCase(fileExtension)
                || "pptx".equalsIgnoreCase(fileExtension)) {
            return "application/vnd.ms-powerpoint";
        }
        if (".doc".equalsIgnoreCase(fileExtension)
                || "docx".equalsIgnoreCase(fileExtension)) {
            return "application/msword";
        }
        if (".xml".equalsIgnoreCase(fileExtension)) {
            return "text/xml";
        }
        // 默认返回类型
        return "image/jpeg";
    }

}
