package com.mudxx.common.utils.http.okhttp;


import com.mudxx.common.utils.json.JsonUtil;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class OkHttpUtils {

    public static OkHttpResponse execute(OkHttpClient client, Request request) throws IOException {
        try (Response response = client.newCall(request).execute()) {
            OkHttpResponse.Builder res = new OkHttpResponse.Builder()
                    .responseBool(response.isSuccessful())
                    .responseCode(response.code());
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                res.responseBody(responseBody.string());
            }
            return res.build();
        }
    }

    //---------------------------------------get---------------------------------------

    public static OkHttpResponse get(OkHttpClient client, String urlString) throws IOException {
        Request request = new Request.Builder()
                .url(urlString)
                .build();
        return execute(client, request);
    }

    public static String doGet(OkHttpClient client, String urlString) throws IOException {
        return get(client, urlString).getResponseBody();
    }

    public static <T> String doGet(OkHttpClient client, String url, Map<String, T> queryMap) throws IOException {
        return get(client, urlWithForm(url, queryMap)).getResponseBody();
    }

    public static String doGet(OkHttpClient client, String url, String queryString) throws IOException {
        return get(client, urlWithForm(url, queryString)).getResponseBody();
    }

    //---------------------------------------post---------------------------------------

    public static OkHttpResponse post(OkHttpClient client, String url, RequestBody body) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return execute(client, request);
    }

    public static OkHttpResponse postJson(OkHttpClient client, String url, String params) throws IOException {
        RequestBody body = RequestBody.create(params, MediaType.get("application/json;charset=UTF-8"));
        return post(client, url, body);
    }

    public static <T> OkHttpResponse postJson(OkHttpClient client, String url, Map<String, T> paramMap) throws IOException {
        return postJson(client, url, JsonUtil.toString(paramMap));
    }

    public static OkHttpResponse postXwForm(OkHttpClient client, String url, Map<String, String> paramMap) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramMap.keySet()) {
            // 追加表单信息
            builder.add(key, paramMap.get(key));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(body)
                .build();
        return execute(client, request);
    }

    public static String doPostJson(OkHttpClient client, String url, String params) throws IOException {
        return postJson(client, url, params).getResponseBody();
    }

    public static <T> String doPostJson(OkHttpClient client, String url, Map<String, T> paramMap) throws IOException {
        return postJson(client, url, paramMap).getResponseBody();
    }

    public static String doPostFormData(OkHttpClient client, String url, Map<String, String> paramsMap) throws IOException {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(Objects.requireNonNull(MediaType.parse("multipart/form-data")));
        for (String key : paramsMap.keySet()) {
            // 追加表单信息
            builder.addFormDataPart(key, paramsMap.get(key));
        }
        MultipartBody body = builder.build();
        return post(client, url, body).getResponseBody();
    }

    /**
     * doPostXWwwForm
     *
     * @param queryString A=1&B=2
     * @return String
     */
    public static String doPostXwForm(OkHttpClient client, String url, String queryString) throws IOException {
        RequestBody body = RequestBody.create(queryString, MediaType.parse("application/x-www-form-urlencoded; charset=utf-8"));
        return post(client, url, body).getResponseBody();
    }

    public static <T> String doPostXwForm(OkHttpClient client, String url, Map<String, T> queryMap) throws IOException {
        return doPostXwForm(client, url, mapToQueryParam(queryMap));
    }

    /**
     * 补全URL
     */
    public static <T> String urlWithForm(String url, Map<String, T> queryMap) {
        return urlWithForm(url, mapToQueryParam(queryMap));
    }

    /**
     * 补全URL
     */
    public static String urlWithForm(String url, String queryString) {
        if (url.contains("?")) {
            return url + queryString;
        } else {
            return url + "?" + queryString;
        }
    }

    /**
     * Map转化成查询参数
     */
    public static <T> String mapToQueryParam(Map<String, T> queryMap) {
        if (queryMap == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, T> e : queryMap.entrySet()) {
            sb.append(e.getKey()).append("=");
            if (e.getValue() != null) {
                sb.append(e.getValue());
            }
            sb.append("&");
        }
        String str = sb.toString();
        if (str.endsWith("&")) {
            return str.substring(0, str.length() - 1);
        }
        return str;
    }


    public static void main(String[] args) throws IOException {
        OkHttpClient client = OkHttpClientFactory.createDefaultClient();
        // 请求地址
        String url = "http://localhost:9236/api/post/okhttp3/post/xwwwform_urlencoded";

        // 传值方式一: 键值
        Map<String, String> paramsMap = new HashMap<String, String>();
        paramsMap.put("fileName", "testName");
        paramsMap.put("fileSize", "999");
        OkHttpResponse response = postXwForm(client, url, paramsMap);
        System.out.println(response.getResponseBody());

        // 传值方式二: 把键值对参数拼接起来
        String params = "fileName=TTTT&fileSize=666";
        String res = doPostXwForm(client, url, params);
        System.out.println(res);

        // 请求地址
        url = "http://localhost:9236/api/post/okhttp3/post/json";
        String json = "{\"fileName\":\"testName\", \"fileSize\":\"999\"}";
        response = postJson(client, url, json);
        System.out.println(response.getResponseBody());

        // 请求地址
        url = "http://localhost:9236/api/post/okhttp3/post/json";
        json = "{\"fileName\":\"testName\", \"fileSize\":\"999\"}";
        res = doPostJson(client, url, json);
        System.out.println(res);

        // 请求地址
        url = "http://localhost:9236/api/get/okhttp3/get/params?param1=EEE&param2=PPP";
        res = doGet(client, url);
        System.out.println(res);

        // 请求地址
        url = "http://localhost:9236/api/post/okhttp3/post/formData";
        // 请求参数
        paramsMap = new HashMap<String, String>();
        paramsMap.put("fileName", "GGGGG");
        paramsMap.put("fileSize", "77777");
        res = doPostFormData(client, url, paramsMap);
        System.out.println(res);

    }
}
