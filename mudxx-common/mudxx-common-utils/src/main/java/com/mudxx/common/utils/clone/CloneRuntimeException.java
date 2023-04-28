package com.mudxx.common.utils.clone;


import com.mudxx.common.utils.exception.ExceptionUtil;

/**
 * description: 克隆异常
 * @author laiwen
 * @date 2020-04-14 14:42:16
 */
@SuppressWarnings("ALL")
public class CloneRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 6363469942934068528L;

    public CloneRuntimeException(Throwable e) {
        super(ExceptionUtil.getStackTrace(e), e);
    }

}
