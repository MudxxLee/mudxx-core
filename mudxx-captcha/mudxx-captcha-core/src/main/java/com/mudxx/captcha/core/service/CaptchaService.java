package com.mudxx.captcha.core.service;

import com.mudxx.captcha.core.domain.dto.CaptchaCheckDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaGetDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaVerifyDTO;
import com.mudxx.captcha.core.domain.vo.CaptchaVO;

import java.util.Properties;

/**
 * 验证码服务接口
 * @author laiwen
 * @date 2020-05-12
 */
public interface CaptchaService {

    /***
     * 验证码类型
     * 通过java SPI机制，接入方可自定义实现类，实现新的验证类型
     * @return
     */
    String captchaType();

    /**
     * 配置初始化
     * @param config
     */
    void init(Properties config);

    /**
     * 历史资源清除(过期的图片文件，生成的临时图片...)
     * @param config 配置项 控制资源清理的粒度
     */
    void destroy(Properties config);

    /**
     * 获取验证码
     * @param getDTO
     * @return
     */
    CaptchaVO get(CaptchaGetDTO getDTO);

    /**
     * 核对验证码(前端)
     * @param checkDTO
     * @return
     */
    CaptchaVO check(CaptchaCheckDTO checkDTO);

    /**
     * 二次校验验证码(后端)
     * @param verifyDTO
     * @return
     */
    void verification(CaptchaVerifyDTO verifyDTO);
}
