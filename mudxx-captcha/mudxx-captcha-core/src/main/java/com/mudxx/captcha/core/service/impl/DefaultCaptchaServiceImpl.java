package com.mudxx.captcha.core.service.impl;

import com.mudxx.captcha.core.common.CaptchaTypeEnum;
import com.mudxx.captcha.core.domain.dto.CaptchaCheckDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaGetDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaVerifyDTO;
import com.mudxx.captcha.core.domain.vo.CaptchaVO;
import com.mudxx.captcha.core.factory.CaptchaServiceFactory;
import com.mudxx.captcha.core.service.CaptchaService;
import com.mudxx.common.exception.code.biz.BizErrorCode;
import com.mudxx.common.exception.code.biz.BizException;
import com.mudxx.common.exception.code.biz.CaptchaErrorCode;
import com.mudxx.common.exception.code.biz.ParameterErrorCode;
import com.mudxx.common.exception.utils.VUtils;

import java.util.Properties;

/**
 * @author laiwen
 */
public class DefaultCaptchaServiceImpl extends AbstractCaptchaService {

    @Override
    public String captchaType() {
        return CaptchaTypeEnum.DEFAULT.getCode();
    }

    @Override
    public void init(Properties config) {
        for (String s : CaptchaServiceFactory.instances.keySet()) {
            if (captchaType().equals(s)) {
                continue;
            }
            getService(s).init(config);
        }
    }

    @Override
    public void destroy(Properties config) {
        for (String s : CaptchaServiceFactory.instances.keySet()) {
            if (captchaType().equals(s)) {
                continue;
            }
            getService(s).destroy(config);
        }
    }

    private CaptchaService getService(String captchaType) {
        return CaptchaServiceFactory.instances.get(captchaType);
    }

    @Override
    public CaptchaVO get(CaptchaGetDTO getDTO) {
        VUtils.isBlank(getDTO.getCaptchaType())
                .throwMessage(ParameterErrorCode.NULL_ERROR, "captchaType");
        VUtils.isTrue(captchaType().equals(getDTO.getCaptchaType()))
                .throwMessage(ParameterErrorCode.INVALID_VALUE, "captchaType");
        return getService(getDTO.getCaptchaType()).get(getDTO);
    }

    @Override
    public CaptchaVO check(CaptchaCheckDTO checkDTO) {
        VUtils.isBlank(checkDTO.getCaptchaType())
                .throwMessage(ParameterErrorCode.NULL_ERROR, "captchaType");
        VUtils.isBlank(checkDTO.getToken())
                .throwMessage(ParameterErrorCode.NULL_ERROR, "token");
        VUtils.isTrue(captchaType().equals(checkDTO.getCaptchaType()))
                .throwMessage(ParameterErrorCode.INVALID_VALUE, "captchaType");
        return getService(checkDTO.getCaptchaType()).check(checkDTO);
    }

    @Override
    public void verification(CaptchaVerifyDTO verifyDTO) {
        VUtils.isBlank(verifyDTO.getCaptchaVerification()).throwMessage(BizErrorCode.NOT_EXIST, "captchaVerification");
        try {
            String codeKey = String.format(REDIS_SECOND_CAPTCHA_KEY, verifyDTO.getCaptchaVerification());
            if (!CaptchaServiceFactory.getCache(cacheType).exists(codeKey)) {
                throw new BizException(CaptchaErrorCode.CAPTCHA_INVALID);
            }
            //二次校验取值后，即刻失效
            CaptchaServiceFactory.getCache(cacheType).delete(codeKey);
        } catch (Exception e) {
            logger.error("验证码坐标解析失败", e);
            throw new BizException(CaptchaErrorCode.CAPTCHA_FAILED, e.getMessage());
        }
    }

}
