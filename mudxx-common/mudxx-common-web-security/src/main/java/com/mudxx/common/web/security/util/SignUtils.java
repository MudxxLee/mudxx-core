package com.mudxx.common.web.security.util;

import com.mudxx.common.utils.json.JsonUtil;
import com.mudxx.common.web.security.model.SecurityHeader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.SortedMap;

/**
 * 签名验证工具类
 *
 * @author laiw
 * @date 2023/12/1 16:52
 */
@Slf4j
public class SignUtils {

    /**
     * 验证签名
     * 验证算法：把timestamp + JsonUtil.object2Json(SortedMap)合成字符串，然后MD5
     */
    @SneakyThrows
    public static boolean verifySign(SortedMap<String, String> map, SecurityHeader requestHeader) {
        String params = requestHeader.getNonce() + requestHeader.getTimestamp() + JsonUtil.toString(map);
        log.info("服务器构建: map={}, params={}", map, params);
        return verifySign(params, requestHeader);
    }

    /**
     * 验证签名
     */
    public static boolean verifySign(String params, SecurityHeader requestHeader) {
        if (StringUtils.isEmpty(params)) {
            return false;
        }
        log.info("客户端签名={}", requestHeader.getSign());
        String paramsSign = DigestUtils.md5DigestAsHex(params.getBytes()).toUpperCase();
        log.info("服务器加密后的签名结果={}", paramsSign);
        return requestHeader.getSign().equals(paramsSign);
    }
}
