package com.mudxx.common.utils.http;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @description HttpClient工具类，客户端用于调用服务端的服务
 * @author laiwen
 * @date 2017年7月3日 下午3:25:50
 */
@SuppressWarnings("ALL")
public class HttpClientTool {

	/**
	 * @description 发送http get请求 
	 * @param url 请求url
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpGet(String url, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		String content = null;
		// since 4.3 不再使用 DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpGet httpGet = new HttpGet(url);
		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpGet.setHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = closeableHttpClient.execute(httpGet);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { // 关闭连接、释放资源
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 发送http post请求，参数以form表单键值对的形式提交。 
	 * @param url 请求url
	 * @param params 类似form表单键值对
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpPostForm(String url, Map<String, String> params, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		// HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url);

		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 组织请求参数
		List<NameValuePair> paramList = new ArrayList<>();
		if (params != null && params.size() > 0) {
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				paramList.add(new BasicNameValuePair(key, params.get(key)));
			}
		}
		try {
			httpost.setEntity(new UrlEncodedFormEntity(paramList, encode));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = closeableHttpClient.execute(httpost);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { // 关闭连接、释放资源
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 发送http post请求，参数以原生字符串（json）进行提交 
	 * @param url 请求url
	 * @param stringJson 原生json字符串
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpPostRaw(String url, String stringJson, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		// HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url);

		// 设置header
		httpost.setHeader("Content-type", "application/json");
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(stringJson, encode);
		httpost.setEntity(stringEntity);
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			// 响应信息
			httpResponse = closeableHttpClient.execute(httpost);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { // 关闭连接、释放资源
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 发送http put请求，参数以原生字符串（json）进行提交 
	 * @param url 请求url
	 * @param stringJson 原生json字符串
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpPutRaw(String url, String stringJson, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		// HttpClients.createDefault()等价于 HttpClientBuilder.create().build();
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPut httpput = new HttpPut(url);

		// 设置header
		httpput.setHeader("Content-type", "application/json");
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpput.setHeader(entry.getKey(), entry.getValue());
			}
		}
		// 组织请求参数
		StringEntity stringEntity = new StringEntity(stringJson, encode);
		httpput.setEntity(stringEntity);
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			// 响应信息
			httpResponse = closeableHttpClient.execute(httpput);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			closeableHttpClient.close(); // 关闭连接、释放资源
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 发送http delete请求 
	 * @param url 请求url
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpDelete(String url, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		String content = null;
		// since 4.3 不再使用 DefaultHttpClient
		CloseableHttpClient closeableHttpClient = HttpClientBuilder.create().build();
		HttpDelete httpdelete = new HttpDelete(url);
		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpdelete.setHeader(entry.getKey(), entry.getValue());
			}
		}
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = closeableHttpClient.execute(httpdelete);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { // 关闭连接、释放资源
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * @description 发送http post请求，支持文件上传
	 * @param url 请求url
	 * @param params 类似form表单键值对
	 * @param files 上传文件
	 * @param headers 请求头信息（如果没有请求头，实参为null）
	 * @param encode 字符编码（一般实参为null，默认utf-8）
	 * @return 返回自定义的HTTP响应对象
	 */
	public static HttpResponse httpPostFormMultipart(String url, 
			Map<String, String> params, List<File> files, Map<String, String> headers, String encode) {
		HttpResponse response = new HttpResponse();
		if (encode == null) {
			encode = "utf-8";
		}
		CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
		HttpPost httpost = new HttpPost(url);

		// 设置header
		if (headers != null && headers.size() > 0) {
			for (Map.Entry<String, String> entry : headers.entrySet()) {
				httpost.setHeader(entry.getKey(), entry.getValue());
			}
		}
		MultipartEntityBuilder mEntityBuilder = MultipartEntityBuilder.create();
		mEntityBuilder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		mEntityBuilder.setCharset(Charset.forName(encode));

		// 普通参数
		ContentType contentType = ContentType.create("text/plain", Charset.forName(encode));// 解决中文乱码
		if (params != null && params.size() > 0) {
			Set<String> keySet = params.keySet();
			for (String key : keySet) {
				mEntityBuilder.addTextBody(key, params.get(key), contentType);
			}
		}
		// 二进制参数
		if (files != null && files.size() > 0) {
			for (File file : files) {
				mEntityBuilder.addBinaryBody("file", file);
			}
		}
		httpost.setEntity(mEntityBuilder.build());
		String content = null;
		CloseableHttpResponse httpResponse = null;
		try {
			httpResponse = closeableHttpClient.execute(httpost);
			HttpEntity entity = httpResponse.getEntity();
			content = EntityUtils.toString(entity, encode);
			response.setBody(content);
			response.setHeaders(httpResponse.getAllHeaders());
			response.setReasonPhrase(httpResponse.getStatusLine().getReasonPhrase());
			response.setStatusCode(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				httpResponse.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try { // 关闭连接、释放资源
			closeableHttpClient.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

}
