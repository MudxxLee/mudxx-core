package com.mudxx.captcha.core.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author laiw
 * @date 2023/4/10 15:53
 */
@Data
public class CaptchaBaseDTO implements Serializable {
    private static final long serialVersionUID = -7751102401526682259L;
    /***
     * 客户端ip+userAgent
     */
    private String browserInfo;
    /***
     * 客户端的请求时间，预留字段
     */
    private Long ts;
}
