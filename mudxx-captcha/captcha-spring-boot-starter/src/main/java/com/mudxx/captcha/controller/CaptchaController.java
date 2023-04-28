package com.mudxx.captcha.controller;

import cn.hutool.core.util.StrUtil;
import com.mudxx.captcha.core.domain.dto.CaptchaCheckDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaGetDTO;
import com.mudxx.captcha.core.domain.dto.CaptchaVerifyDTO;
import com.mudxx.captcha.core.service.CaptchaService;
import com.mudxx.common.exception.code.biz.ParameterErrorCode;
import com.mudxx.common.exception.utils.VUtils;
import com.mudxx.common.web.response.CommonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author laiwen
 */
@RestController
@RequestMapping("/captcha")
public class CaptchaController {

    @Autowired
    private CaptchaService captchaService;

    @PostMapping("/get")
    public CommonResult<?> get(@RequestBody CaptchaGetDTO getDTO, HttpServletRequest request) {
        VUtils.isEmpty(request.getRemoteHost()).throwMessage(ParameterErrorCode.NULL_ERROR, "request.remote-host");
        getDTO.setBrowserInfo(getRemoteId(request));
        return CommonResult.success(captchaService.get(getDTO));
    }

    @PostMapping("/check")
    public CommonResult<?> check(@RequestBody CaptchaCheckDTO checkDTO, HttpServletRequest request) {
        checkDTO.setBrowserInfo(getRemoteId(request));
        return CommonResult.success(captchaService.check(checkDTO));
    }

    @PostMapping("/verify")
    public CommonResult<?> verify(@RequestBody CaptchaVerifyDTO verifyDTO, HttpServletRequest request) {
        verifyDTO.setBrowserInfo(getRemoteId(request));
        captchaService.verification(verifyDTO);
        return CommonResult.success();
    }

    public static String getRemoteId(HttpServletRequest request) {
        String xfwd = request.getHeader("X-Forwarded-For");
        String ip = getRemoteIpFromXfwd(xfwd);
        String ua = request.getHeader("user-agent");
        if (StrUtil.isNotBlank(ip)) {
            return ip + ua;
        }
        return request.getRemoteAddr() + ua;
    }

    private static String getRemoteIpFromXfwd(String xfwd) {
        if (StrUtil.isNotBlank(xfwd)) {
            String[] ipList = xfwd.split(",");
            return StrUtil.trim(ipList[0]);
        }
        return null;
    }

}
