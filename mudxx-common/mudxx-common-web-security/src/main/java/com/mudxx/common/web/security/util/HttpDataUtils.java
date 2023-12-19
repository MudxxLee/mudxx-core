package com.mudxx.common.web.security.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mudxx.common.utils.json.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author laiw
 * @date 2023/12/1 16:35
 */
public class HttpDataUtils {

    /**
     * post请求处理：获取 Body 参数，转换为SortedMap
     */
    public static SortedMap<String, String> getBodyParams(final HttpServletRequest request) throws IOException {
        byte[] requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        String body = new String(requestBody, StandardCharsets.UTF_8);
        return JsonUtil.toCustom(body, new TypeReference<SortedMap<String, String>>() {
        });
    }

    /**
     * get请求处理：将URL请求参数转换成SortedMap
     */
    public static SortedMap<String, String> getUrlParams(HttpServletRequest request) {
        String param = "";
        SortedMap<String, String> result = new TreeMap<>();
        if (StringUtils.isBlank(request.getQueryString())) {
            return result;
        }
        try {
            param = URLDecoder.decode(request.getQueryString(), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String[] params = param.split("&");
        for (String s : params) {
            String[] array = s.split("=", 2);
            if (array.length == 1) {
                result.put(array[0], null);
            } else {
                result.put(array[0], array[1]);
            }
        }
        return result;
    }

}
