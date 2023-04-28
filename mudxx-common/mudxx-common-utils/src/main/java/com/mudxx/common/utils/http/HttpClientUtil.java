package com.mudxx.common.utils.http;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description HttpClient工具类，客户端用于调用服务端的服务
 * @author laiwen
 * @date 2017年5月23日 上午11:11:41
 */
@SuppressWarnings("ALL")
public class HttpClientUtil {

	/**
	 * 温馨提示：默认采用UTF-8编码
	 * @description 请求调用get类型服务接口含请求头含参
	 * @param url 请求服务接口地址
	 * @param header 请求头信息
	 * @param param 请求参数（url参数）
	 * @return 返回服务接口响应的数据
	 */
	public static String doGet(String url, Map<String, String> header, Map<String, String> param) {
		//1、创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//服务接口响应的数据
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			//2、创建uri并且传递参数
			URIBuilder builder = new URIBuilder(url);
			if (param != null) {
				for (String key : param.keySet()) {
					builder.addParameter(key, param.get(key));
				}
			}
			
			URI uri = builder.build();

			//3、创建基于http协议的get请求
			HttpGet httpGet = new HttpGet(uri);
			
			//4、设置请求头信息
			if (header != null) {
				for (String key : header.keySet()) {
					httpGet.setHeader(key, header.get(key));
				}
			}

			//5、执行请求
			response = httpClient.execute(httpGet);

			//6、获取响应数据
			resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(httpClient, response);
		}
		return resultString;
	}

	/**
	 * @description 请求调用get类型服务接口不含请求头含参
	 * @param url 请求服务接口地址
	 * @param param 请求参数（url参数）
	 * @return 返回服务接口响应的数据
	 */
	public static String doGet(String url, Map<String, String> param) {
		return doGet(url, null, param);
	}
	
	/**
	 * @description 请求调用get类型服务接口含请求头不含参
	 * @param header 请求头信息
	 * @param url 请求服务接口地址
	 * @return 返回服务接口响应的数据
	 */
	public static String doGet(Map<String, String> header, String url) {
		return doGet(url, header, null);
	}
	
	/**
	 * @description 请求调用get类型服务接口不含请求头不含参
	 * @param url 请求服务接口地址
	 * @return 返回服务接口响应的数据
	 */
	public static String doGet(String url) {
		return doGet(url, null, null);
	}

	/**
	 * 温馨提示：请求数据类型为Content-Type: application/x-www-form-urlencoded; charset=UTF-8，默认采用UTF-8编码
	 * @description 请求调用post类型服务接口含请求头含参
	 * @param url 请求服务接口地址
	 * @param header 请求头信息
	 * @param param 请求参数（请求体内容）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPost(String url, Map<String, String> header, Map<String, String> param) {
		//1、创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//服务接口响应的数据
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			//2、创建基于http协议的post请求
			HttpPost httpPost = new HttpPost(url);
			
			//3、设置请求头信息
			if (header != null) {
				for (String key : header.keySet()) {
					httpPost.setHeader(key, header.get(key));
				}
			}
			
			//4、创建参数列表，模拟表单提交数据
			if (param != null) {
				List<NameValuePair> paramList = new ArrayList<>();
				for (String key : param.keySet()) {
					paramList.add(new BasicNameValuePair(key, param.get(key)));
				}
				StringEntity entity = new UrlEncodedFormEntity(paramList, Consts.UTF_8);
				httpPost.setEntity(entity);
			}
			
			//5、执行请求
			response = httpClient.execute(httpPost);

			//6、获取响应数据
			resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(httpClient, response);
		}
		return resultString;
	}
	
	/**
	 * @description 请求调用post类型服务接口不含请求头含参
	 * @param url 请求服务接口地址
	 * @param param 请求参数（请求体内容）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPost(String url, Map<String, String> param) {
		return doPost(url, null, param);
	}
	
	/**
	 * @description 请求调用post类型服务接口含请求头不含参
	 * @param header 请求头信息
	 * @param url 请求服务接口地址
	 * @return 返回服务接口响应的数据
	 */
	public static String doPost(Map<String, String> header, String url) {
		return doPost(url, header, null);
	}

	/**
	 * @description 请求调用post类型服务接口不含请求头不含参
	 * @param url 请求服务接口地址
	 * @return 返回服务接口响应的数据
	 */
	public static String doPost(String url) {
		return doPost(url, null, null);
	}
	
	/**
	 * 温馨提示：请求数据类型为Content-Type: application/json; charset=UTF-8，默认采用UTF-8编码
	 * @description 请求参数是json字符串形式的post请求调用服务接口（含请求头）
	 * @param url 请求服务接口地址
	 * @param header 请求头信息
	 * @param json 请求参数是json字符串（请求体内容）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPostJson(String url, Map<String, String> header, String json) {
		//1、创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//服务接口响应的数据
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			//2、创建基于http协议的post请求
			HttpPost httpPost = new HttpPost(url);
			
			//3、设置请求头信息
			if (header != null) {
				for (String key : header.keySet()) {
					httpPost.setHeader(key, header.get(key));
				}
			}
			
			//4、创建请求内容
			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
			httpPost.setEntity(entity);
			
			//5、执行请求
			response = httpClient.execute(httpPost);

			//6、获取响应数据
			resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(httpClient, response);
		}
		return resultString;
	}
	
	/**
	 * @description 请求参数是json字符串形式的post请求调用服务接口（不含请求头）
	 * @param url 请求服务接口地址
	 * @param json 请求参数是json字符串（请求体内容）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPostJson(String url, String json) {
		return doPostJson(url, null, json);
	}

	/**
	 * 温馨提示：请求数据类型为Content-Type: multipart/form-data，默认采用UTF-8编码
	 * @description 发送HTTP的POST请求，支持文件上传（含请求头）
	 * @param url 请求url地址
	 * @param params 类似form表单键值对（普通参数）（如果没有普通参数，实参传null）
	 * @param filesMap 待上传的文件Map集合（二进制参数）（如果没有二进制参数，实参传null）
	 * @param headers 请求头信息（如果没有请求头，实参传null）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPostFormMultipart(String url, Map<String, String> params, Map<String, List<File>> filesMap, Map<String, String> headers) {
		//1、创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//服务接口响应的数据
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			HttpPost httPost = new HttpPost(url);

			//2、设置header
			if (headers != null && headers.size() > 0) {
				for (Map.Entry<String, String> entry : headers.entrySet()) {
					httPost.setHeader(entry.getKey(), entry.getValue());
				}
			}

			//3、创建请求体
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			builder.setCharset(Consts.UTF_8);

			//3.1、添加普通参数
			//解决中文乱码
			ContentType contentType = ContentType.create("text/plain", Consts.UTF_8);
			if (params != null && params.size() > 0) {
				Set<String> keySet = params.keySet();
				for (String key : keySet) {
					builder.addTextBody(key, params.get(key), contentType);
				}
			}

			//3.2、添加二进制参数
			if (filesMap != null && filesMap.size() > 0) {
				for (Map.Entry<String, List<File>> entry: filesMap.entrySet()) {
					String key = entry.getKey();
					List<File> files = entry.getValue();
					for (File file : files) {
						builder.addBinaryBody(key, file);
					}
				}
			}

			//4、设置请求体
			httPost.setEntity(builder.build());

			//5、执行请求
			response = httpClient.execute(httPost);

			//6、获取响应数据
			resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(httpClient, response);
		}
		return resultString;
	}

	/**
	 * @description 发送HTTP的POST请求，支持文件上传（不含请求头）
	 * @param url 请求url地址
	 * @param params 类似form表单键值对（普通参数）（如果没有普通参数，实参传null）
	 * @param filesMap 待上传的文件Map集合（二进制参数）（如果没有二进制参数，实参传null）
	 * @return 返回服务接口响应的数据
	 */
	public static String doPostFormMultipart(String url, Map<String, String> params, Map<String, List<File>> filesMap) {
		return doPostFormMultipart(url, params, filesMap, null);
	}

	/**
	 * @description 请求参数是xml格式的post请求调用服务接口
	 * @param url 请求地址
	 * @param xmlData xml格式请求参数
	 * @return 返回服务接口响应的数据
	 */
	public static String doPostXml(String url, String xmlData) {
		//1、创建HttpClient对象
		CloseableHttpClient httpClient = HttpClients.createDefault();

		//服务接口响应的数据
		String resultString = "";
		CloseableHttpResponse response = null;
		try {
			//2、创建HTTP POST请求对象
			HttpPost httpPost = new HttpPost(url);

			//3、设置请求头（指明请求内容类型以及字符编码）
			httpPost.setHeader("Content-Type", "text/xml; charset=UTF-8");

			//4、创建请求体（指明字符编码）
			StringEntity entity = new StringEntity(xmlData, Consts.UTF_8);

			//5、设置请求体
			httpPost.setEntity(entity);

			//6、执行请求
			response = httpClient.execute(httpPost);

			//7、获取响应数据
			resultString = EntityUtils.toString(response.getEntity(), Consts.UTF_8);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeStream(httpClient, response);
		}
		return resultString;
	}

	/**
	 * 温馨提醒：URL解码操作最好放在具体业务里面进行
	 * 强调：返回值与请求数据格式一模一样保持不变，比如name=张三&age=10，{"name":"张三","age":10}
	 * 说明：这是服务端调用的方法（支持POST请求不支持GET请求）
	 * @description 获取客户端请求的数据内容
	 * @param request 请求对象
	 * @return 返回客户端请求的数据
	 */
	public static String getRequestContent(HttpServletRequest request) {
		//默认空字符串
		StringBuilder stringBuilder = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream(), Consts.UTF_8));
			String line ;
			while ((line = in.readLine()) != null) {
				stringBuilder.append(line);
			}
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stringBuilder.toString();
	}
	
	/**
	 * 强调：该方法不支持GET请求，支持POST请求、PUT请求
	 * 说明：如果是POST请求（Content-Type=application/x-www-form-urlencoded），Servlet先调用了getParameter系列方法，
	 * 那么再调用该方法无效，PUT请求没有该问题。
	 * @description 获取客户端请求的请求体数据
	 * @param request 请求对象
	 * @return 返回客户端请求的数据
	 */
	public static String getRequestBodyData(HttpServletRequest request) {
		String bodyData = "";
		try {
			bodyData = IOUtils.toString(request.getInputStream(), Consts.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bodyData;
	}

	/**
	 * @description 关闭流对象释放内存
	 * @param httpClient Http客户端对象
	 * @param response 服务接口响应对象
	 */
	private static void closeStream(CloseableHttpClient httpClient, CloseableHttpResponse response) {
		try {
			if (response != null) {
				response.close();
			}
			httpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
