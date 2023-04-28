package com.mudxx.captcha.core.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author laiw
 * @date 2023/4/28 15:23
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CaptchaVerifyDTO extends CaptchaBaseDTO {
    private static final long serialVersionUID = 7398376149980828527L;
    /**
     * 后台二次校验参数
     */
    private String captchaVerification;

}
