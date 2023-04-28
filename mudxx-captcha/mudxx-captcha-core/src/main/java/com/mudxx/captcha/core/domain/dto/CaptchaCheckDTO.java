package com.mudxx.captcha.core.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author laiw
 * @date 2023/4/10 15:53
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaCheckDTO extends CaptchaBaseDTO {
    private static final long serialVersionUID = -7751102401526682259L;
    /**
     * 验证码类型:(clickWord,blockPuzzle)
     */
    private String captchaType;
    /**
     * UUID(每次请求的验证码唯一标识)
     */
    private String token;
    /**
     * 点坐标(base64加密传输)
     */
    private String pointJson;
}
