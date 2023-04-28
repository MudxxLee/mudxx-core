package com.mudxx.common.utils.http;

import org.apache.http.Header;

import java.util.Arrays;

/**
 * @description 自定义HTTP响应对象
 * @author laiwen
 * @date 2017年7月3日 下午3:54:59
 */
@SuppressWarnings("ALL")
public class HttpResponse {
	
	/**
	 * @description 响应体
	 */
	private String body;

	/**
	 * @description 响应头
	 */
	private Header[] headers;
	
	/**
	 * @description 响应状态信息（比如：200请求成功、303重定向、400请求错误、401未授权、403禁止访问、404文件未找到、500服务器错误）
	 */
	private String reasonPhrase;

	/**
	 * @description 响应状态码（比如：200请求成功、303重定向、400请求错误、401未授权、403禁止访问、404文件未找到、500服务器错误）
	 */
	private Integer statusCode;
	
	public HttpResponse() {}

	public HttpResponse(String body, Header[] headers, String reasonPhrase, Integer statusCode) {
		this.body = body;
		this.headers = headers;
		this.reasonPhrase = reasonPhrase;
		this.statusCode = statusCode;
	}
	
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}

	public Header[] getHeaders() {
		return headers;
	}
	public void setHeaders(Header[] headers) {
		this.headers = headers;
	}

	public String getReasonPhrase() {
		return reasonPhrase;
	}
	public void setReasonPhrase(String reasonPhrase) {
		this.reasonPhrase = reasonPhrase;
	}
	
	public Integer getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(Integer statusCode) {
		this.statusCode = statusCode;
	}

	@Override
	public String toString() {
		return "HttpResponse [body=" + body + ", headers=" + Arrays.toString(headers) + ", reasonPhrase=" + reasonPhrase + ", statusCode=" + statusCode + "]";
	}
	
}
