package com.mudxx.common.web.exception;

import com.mudxx.common.exceptiion.code.CommonErrorCode;
import com.mudxx.common.exceptiion.code.biz.BizErrorCode;
import com.mudxx.common.exceptiion.code.biz.BizException;
import com.mudxx.common.web.response.CommonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.security.SignatureException;

/**
 * @author laiw
 * @date 2023/3/30 17:23
 */
@Order(0)
@RestControllerAdvice
public class ExtGlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExtGlobalExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected CommonResult<?> handleException(Exception e) {
        outPutError(Exception.class, CommonErrorCode.SYSTEM_ERROR, e);
        return CommonResult.failed(CommonErrorCode.SYSTEM_ERROR, e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public CommonResult<?> handleRuntimeException(RuntimeException e) {
        outPutError(RuntimeException.class, BizErrorCode.BIZ_ERROR, e);
        return CommonResult.failed(BizErrorCode.BIZ_ERROR, e.getMessage());
    }

    @ExceptionHandler(BizException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    protected CommonResult<?> handleBizException(BizException e) {
        outPutError(BizException.class, BizErrorCode.BIZ_ERROR, e);
        return CommonResult.failed(e.getCode(), e.getMessage());
    }

    /**
     * NoHandlerFoundException 404 异常处理
     */
    @ExceptionHandler(value = NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public CommonResult<?> handleNoHandlerFoundException(NoHandlerFoundException e) {
        outPutErrorWarn(NoHandlerFoundException.class, CommonErrorCode.NOT_FOUND, e);
        return CommonResult.failed(CommonErrorCode.NOT_FOUND);
    }

    /**
     * HttpRequestMethodNotSupportedException 405 异常处理
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public CommonResult<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        outPutErrorWarn(HttpRequestMethodNotSupportedException.class, CommonErrorCode.NOT_FOUND, e);
        return CommonResult.failed(CommonErrorCode.NOT_FOUND);
    }

    /**
     * HttpMediaTypeNotSupportedException 415 异常处理
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseBody
    public CommonResult<?> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class, CommonErrorCode.NOT_FOUND, e);
        return CommonResult.failed(CommonErrorCode.NOT_FOUND, e.getMessage());
    }

    @ExceptionHandler(value = {SignatureException.class})
    @ResponseBody
    public CommonResult<?> handlerSignatureException(SignatureException e) {
        outPutErrorWarn(HttpMediaTypeNotSupportedException.class, CommonErrorCode.UNAUTHORIZED, e);
        return CommonResult.failed(CommonErrorCode.UNAUTHORIZED, e.getMessage());
    }

    private void outPutError(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        LOGGER.error("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(), throwable);
    }

    private void outPutErrorWarn(Class<?> errorType, Enum<?> secondaryErrorType, Throwable throwable) {
        LOGGER.warn("[{}] {}: {}", errorType.getSimpleName(), secondaryErrorType, throwable.getMessage(), throwable);
    }

}
