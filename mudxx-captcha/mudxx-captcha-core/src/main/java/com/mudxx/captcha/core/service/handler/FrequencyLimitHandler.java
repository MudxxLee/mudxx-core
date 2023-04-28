package com.mudxx.captcha.core.service.handler;

import cn.hutool.core.util.StrUtil;
import com.mudxx.captcha.core.common.Const;
import com.mudxx.captcha.core.service.cache.CaptchaCacheService;
import com.mudxx.common.exception.code.biz.BizException;
import com.mudxx.common.exception.code.biz.CaptchaErrorCode;

import java.util.Objects;
import java.util.Properties;

/**
 * @author WongBin
 * @date 2021/1/21
 */
public interface FrequencyLimitHandler {

    String LIMIT_KEY = "MD.CAPTCHA.REQ.LIMIT-%s-%s";

    Long WAIT_FOR_A_MINUTE = 60L;

    /**
     * get 接口限流
     *
     * @param clientUid 验证入参
     */
    void validateGet(String clientUid);

    /**
     * check接口限流
     *
     * @param clientUid 验证入参
     */
    void validateCheck(String clientUid);

    /**
     * verify接口限流
     *
     * @param clientUid 验证入参
     */
    void validateVerify(String clientUid);


    /***
     * 验证码接口限流:
     *      客户端ClientUid 组件实例化时设置一次，如：场景码+UUID，客户端可以本地缓存,保证一个组件只有一个值
     *
     * 针对同一个客户端的请求，做如下限制:
     * get
     * 	 1分钟内check失败5次，锁定5分钟
     * 	 1分钟内不能超过120次。
     * check:
     *   1分钟内不超过600次
     * verify:
     *   1分钟内不超过600次
     */
    class DefaultLimitHandler implements FrequencyLimitHandler {
        private Properties config;
        private CaptchaCacheService cacheService;

        public DefaultLimitHandler(Properties config, CaptchaCacheService cacheService) {
            this.config = config;
            this.cacheService = cacheService;
        }

        private String getClientLimitKey(String type, String clientUid) {
            return String.format(LIMIT_KEY, type, clientUid);
        }

        @Override
        public void validateGet(String clientUid) {
            // 无客户端身份标识，不限制
        	if(StrUtil.isBlank(clientUid)){
        		return;
			}
            String getKey = getClientLimitKey("GET", clientUid);
            String lockKey = getClientLimitKey("LOCK", clientUid);
            // 失败次数过多，锁定
            if (Objects.nonNull(cacheService.get(lockKey))) {
                throw new BizException(CaptchaErrorCode.REQ_LOCK_GET_ERROR);
            }
            String getCounts = cacheService.get(getKey);
            if (Objects.isNull(getCounts)) {
                cacheService.set(getKey, "1", WAIT_FOR_A_MINUTE);
                getCounts = "1";
            }
            cacheService.increment(getKey, 1);
            // 1分钟内请求次数过多
            if (Long.parseLong(getCounts) > Long.parseLong(config.getProperty(Const.REQ_GET_MINUTE_LIMIT, "60"))) {
                throw new BizException(CaptchaErrorCode.REQ_LIMIT_GET_ERROR);
            }
            // 失败次数验证
            String failKey = getClientLimitKey("FAIL", clientUid);
            String failCounts = cacheService.get(failKey);
            // 没有验证失败，通过校验
            if (Objects.isNull(failCounts)) {
                return ;
            }
            // 1分钟内失败5次
            if (Long.parseLong(failCounts) > Long.parseLong(config.getProperty(Const.REQ_GET_LOCK_LIMIT, "5"))) {
                // get接口锁定5分钟
                cacheService.set(lockKey, "1", Long.parseLong(config.getProperty(Const.REQ_GET_LOCK_SECONDS, "60")));
                throw new BizException(CaptchaErrorCode.REQ_LOCK_GET_ERROR);
            }
        }

        @Override
        public void validateCheck(String clientUid) {
			// 无客户端身份标识，不限制
			if(StrUtil.isBlank(clientUid)){
				return ;
			}
            String key = getClientLimitKey("CHECK", clientUid);
            String v = cacheService.get(key);
            if (Objects.isNull(v)) {
                cacheService.set(key, "1", WAIT_FOR_A_MINUTE);
                v = "1";
            }
            cacheService.increment(key, 1);
            if (Long.parseLong(v) > Long.parseLong(config.getProperty(Const.REQ_CHECK_MINUTE_LIMIT, "100"))) {
                throw new BizException(CaptchaErrorCode.REQ_LIMIT_CHECK_ERROR);
            }
        }

        @Override
        public void validateVerify(String clientUid) {
            // 无客户端身份标识，不限制
            if(StrUtil.isBlank(clientUid)){
                return ;
            }
            String key = getClientLimitKey("VERIFY", clientUid);
            String v = cacheService.get(key);
            if (Objects.isNull(v)) {
                cacheService.set(key, "1", WAIT_FOR_A_MINUTE);
                v = "1";
            }
            cacheService.increment(key, 1);
            if (Long.parseLong(v) > Long.parseLong(config.getProperty(Const.REQ_VALIDATE_MINUTE_LIMIT, "100"))) {
                throw new BizException(CaptchaErrorCode.REQ_LIMIT_VERIFY_ERROR);
            }
        }
    }

}