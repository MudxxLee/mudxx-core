package com.mudxx.common.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import okhttp3.*;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author laiw
 * @date 2023/10/19 16:05
 */
public class ApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiClient.class);

    private final OkHttpClient client;

    public ApiClient() {
        this.client = new OkHttpClient().newBuilder().build();
    }

    public ApiClient(long connectTimeout, long readTimeout, long writeTimeout) {
        this.client = new OkHttpClient().newBuilder()
                // 设置超时时间
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                // 设置读取超时时间
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                // 设置写入超时时间
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }

    /*----------------------------------------------------------GET----------------------------------------------------------*/

    public <T> T doGet(String urlString, Map<String, String> headers, Map<String, String> queryParams,
                       ApiReturnType<T> returnType, String logName) throws ApiException {
        return this.doGet(this.withQueryString(urlString, queryParams), headers, returnType, logName);
    }

    public <T> T doGet(String urlString, Map<String, String> headers,
                       ApiReturnType<T> returnType, String logName) throws ApiException {
        try {
            LOGGER.info("-----[{}] api.get.urlString={}, api.get.headers={}",
                    logName, urlString, headers);
            long start = System.currentTimeMillis();
            ApiResponse<T> response = this.doGet(urlString, headers, returnType);
            LOGGER.info("-----[{}] api.response.code={}, api.response.body={}, api.response.time-consuming={}s",
                    logName, response.getCode(), response.getBody(), (System.currentTimeMillis() - start) / 1000);
            return response.getData();
        } catch (ApiException e) {
            LOGGER.error("-----[{}] api.response.error={}", logName, e.toSimpleJsonStr(), e);
            throw e;
        }
    }

    public <T> ApiResponse<T> doGet(String urlString, Map<String, String> headers, Map<String, String> queryParams,
                                    ApiReturnType<T> returnType) throws ApiException {
        return this.doGet(this.withQueryString(urlString, queryParams), headers, returnType);
    }

    public <T> ApiResponse<T> doGet(String urlString, Map<String, String> headers,
                                    ApiReturnType<T> returnType) throws ApiException {
        Request.Builder builder = new Request.Builder().url(urlString);
        if (ObjectUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        Request request = builder.build();
        return this.doExecute(request, returnType);
    }

    /*----------------------------------------------------------POST----------------------------------------------------------*/

    public <T> T doPostJson(String url, Map<String, String> headers, String params,
                            String logName) throws ApiException {
        return this.doPostJson(url, headers, params, null, logName);
    }

    public <T> T doPostJson(String url, Map<String, String> headers, Map<String, String> queryParams,
                            String params, ApiReturnType<T> returnType, String logName) throws ApiException {
        return this.doPostJson(this.withQueryString(url, queryParams), headers, params, returnType, logName);
    }

    public <T> T doPostJson(String url, Map<String, String> headers, String params,
                            ApiReturnType<T> returnType, String logName) throws ApiException {
        try {
            LOGGER.info("-----[{}] api.post.url={}, api.post.headers={}, api.post.params={}",
                    logName, url, headers, params);
            long start = System.currentTimeMillis();
            ApiResponse<T> response = this.doPostJson(
                    url,
                    headers,
                    params,
                    returnType);
            LOGGER.info("-----[{}] api.response.code={}, api.response.body={}, api.response.time-consuming={}s",
                    logName, response.getCode(), response.getBody(), (System.currentTimeMillis() - start) / 1000);
            return response.getData();
        } catch (ApiException e) {
            LOGGER.error("-----[{}] api.response.error={}", logName, e.toSimpleJsonStr(), e);
            throw e;
        }
    }

    public <T> ApiResponse<T> doPostJson(String url, Map<String, String> headers, String params,
                                         ApiReturnType<T> returnType) throws ApiException {
        Request.Builder builder = new Request.Builder().url(url);
        if (ObjectUtils.isNotEmpty(headers)) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        RequestBody body = RequestBody.create(params, MediaType.get("application/json;charset=UTF-8"));
        Request request = builder.post(body).build();
        return this.doExecute(request, returnType);
    }

    /*----------------------------------------------------------execute----------------------------------------------------------*/

    protected <T> ApiResponse<T> doExecute(Request request, ApiReturnType<T> returnType) throws ApiException {
        try (Response response = this.client.newCall(request).execute()) {
            String responseBody = this.handleResponse(response);
            return new ApiResponse<>(
                    response.code(),
                    response.message(),
                    responseBody,
                    this.deserialize(responseBody, returnType)
            );
        } catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(e.getMessage(), e, null);
        }
    }

    protected String handleResponse(Response response) throws ApiException {
        if (!response.isSuccessful()) {
            throw new ApiException(response.code(), response.message(), response.headers().toMultimap());
        }
        String responseBody = null;
        try {
            ResponseBody body = response.body();
            if (body != null) {
                responseBody = body.string();
            }
        } catch (IOException ignore) {
            // ignore
        }
        if (StringUtils.isBlank(responseBody)) {
            throw new ApiException("No response body", response.headers().toMultimap(), responseBody);
        }
        if (!this.validJson(responseBody)) {
            throw new ApiException("Error response body", response.headers().toMultimap(), responseBody);
        }
        return responseBody;
    }

    protected <T> T deserialize(String responseBody, ApiReturnType<T> returnType) {
        if (returnType == null || returnType.getType() == null) {
            return null;
        }
        switch (returnType.getType()) {
            case Class:
                return JSON.parseObject(responseBody, returnType.getTypeClass());
            case TypeReference:
                return JSON.parseObject(responseBody, returnType.getTypeReference());
            default:
                return null;
        }
    }

    public String withQueryString(Map<String, String> queryParams) {
        if (ObjectUtils.isEmpty(queryParams)) {
            return "";
        }
        StringBuilder queryString = new StringBuilder();
        for (Map.Entry<String, String> entry : queryParams.entrySet()) {
            if (StringUtils.isBlank(entry.getKey())) {
                continue;
            }
            queryString.append(entry.getKey()).append("=");
            if (StringUtils.isNotBlank(entry.getValue())) {
                queryString.append(entry.getValue());
            }
            queryString.append("&");
        }
        return queryString.substring(0, queryString.length() - 1);
    }

    public String withQueryString(String url, Map<String, String> queryParams) {
        String queryString = this.withQueryString(queryParams);
        if (StringUtils.isBlank(queryString)) {
            return url;
        }
        if (url.endsWith("?")) {
            return url + queryString;
        }
        if (url.contains("=")) {
            return url + "&" + queryString;
        }
        return url + "?" + queryString;
    }

    protected boolean validJson(String str) {
        try {
            JSON.parse(str);
            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    public static void main(String[] args) {

    }

}

