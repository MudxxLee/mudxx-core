package com.mudxx.component.mq.idempotent.strategy.impl;

import com.mudxx.component.mq.idempotent.common.IdempotentBizResult;
import com.mudxx.component.mq.idempotent.common.IdempotentElement;
import com.mudxx.component.mq.idempotent.common.IdempotentResult;
import com.mudxx.component.mq.idempotent.strategy.IdempotentStrategy;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Function;

/**
 * 消息幂等策略-无策略
 * @author laiw
 * @date 2023/2/17 16:09
 */
@Slf4j
public class NormalIdempotentStrategy implements IdempotentStrategy {

    @Override
    public IdempotentResult invoke(IdempotentElement element, Function<Object, IdempotentBizResult> callbackMethod, Object callbackMethodParam) {
        return IdempotentResult.createSuccess(defaultBizApply(callbackMethod, callbackMethodParam));
    }
}
