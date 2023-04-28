package com.mudxx.captcha.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mudxx.captcha.core.common.CaptchaStorageTypeEnum;
import com.mudxx.captcha.core.common.Const;
import com.mudxx.captcha.core.domain.vo.CaptchaVO;
import com.mudxx.captcha.core.factory.CaptchaServiceFactory;
import com.mudxx.captcha.core.service.CaptchaService;
import com.mudxx.captcha.core.service.cache.CaptchaCacheService;
import com.mudxx.captcha.core.service.handler.FrequencyLimitHandler;
import com.mudxx.captcha.core.utils.CacheUtils;
import com.mudxx.captcha.core.utils.FontUtils;
import com.mudxx.captcha.core.utils.ImageUtils;
import com.mudxx.common.utils.crypt.AESUtil;
import com.mudxx.common.utils.crypt.md5.Md5Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Properties;

/**
 * @author laiwen
 */
public abstract class AbstractCaptchaService implements CaptchaService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    protected static final String IMAGE_TYPE_PNG = "png";

    protected static int HAN_ZI_SIZE = 25;

    protected static int HAN_ZI_SIZE_HALF = HAN_ZI_SIZE / 2;
    /**
     * check校验坐标
     */
    protected static String REDIS_CAPTCHA_KEY = "RUNNING:CAPTCHA:%s";
    /**
     * 后台二次校验坐标
     */
    protected static String REDIS_SECOND_CAPTCHA_KEY = "RUNNING:CAPTCHA:second-%s";

    protected static Long EXPIRES_SECONDS = 2 * 60L;

    protected static Long EXPIRES_THREE = 3 * 60L;

    /**配置项*/
    protected String cacheType;

    protected String waterMark;

    protected String waterMarkFontStr;

    protected Font waterMarkFont;

    protected String slipOffset;

    protected Boolean captchaAesStatus;

    protected int captchaInterferenceOptions;


    private FrequencyLimitHandler limitHandler;

    @Override
    public void init(final Properties config) {
        logger.info("--->>>初始化验证码底图({})<<<---" + captchaType());
        boolean aBoolean = Boolean.parseBoolean(config.getProperty(Const.CAPTCHA_INIT_ORIGINAL));
        if ( ! aBoolean) {
            // 自定义resources目录下初始化底图
            ImageUtils.cacheImage(config.getProperty(Const.ORIGINAL_PATH_JIGSAW), config.getProperty(Const.ORIGINAL_PATH_PIC_CLICK));
        }
        this.cacheType = config.getProperty(Const.CAPTCHA_CACHETYPE, CaptchaStorageTypeEnum.local.name());
        this.waterMark = config.getProperty(Const.CAPTCHA_WATER_MARK, "我的水印");
        this.waterMarkFontStr = config.getProperty(Const.CAPTCHA_WATER_FONT, "WenQuanZhengHei.ttf");
        this.waterMarkFont = FontUtils.loadFont(waterMarkFontStr, HAN_ZI_SIZE);
        this.slipOffset = config.getProperty(Const.CAPTCHA_SLIP_OFFSET, "5");
        this.captchaInterferenceOptions = Integer.parseInt(config.getProperty(Const.CAPTCHA_INTERFERENCE_OPTIONS, "0"));
        this.captchaAesStatus = Boolean.parseBoolean(config.getProperty(Const.CAPTCHA_AES_STATUS, "true"));
        if (this.cacheType.equals(CaptchaStorageTypeEnum.local.name())) {
            logger.info("初始化local缓存...");
            CacheUtils.init(Integer.parseInt(config.getProperty(Const.CAPTCHA_CACAHE_MAX_NUMBER, "1000")),
                    Long.parseLong(config.getProperty(Const.CAPTCHA_TIMING_CLEAR_SECOND, "180")));
        }
        if ("1".equals(config.getProperty(Const.REQ_FREQUENCY_LIMIT_ENABLE, "0"))) {
            if (this.limitHandler == null) {
                logger.info("接口分钟内限流开关...开启...");
                this.limitHandler = new FrequencyLimitHandler.DefaultLimitHandler(config, getCacheService(cacheType));
            }
        }
        if ("1".equals(config.getProperty(Const.HISTORY_DATA_CLEAR_ENABLE, "0"))) {
            logger.info("历史资源清除开关...开启...(将会在JVM销毁前执行)" + captchaType());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> destroy(config)));
        }
    }

    protected CaptchaCacheService getCacheService(String cacheType) {
        return CaptchaServiceFactory.getCache(cacheType);
    }

    @Override
    public void destroy(Properties config) {

    }

    protected void preGetLimit(String browserInfo) {
        if(limitHandler == null) {
            return;
        }
        String clientUid = getValidateClientId(browserInfo);
        limitHandler.validateGet(clientUid);
    }

    protected void preCheckLimit(String browserInfo) {
        if(limitHandler == null) {
            return;
        }
        String clientUid = getValidateClientId(browserInfo);
        limitHandler.validateVerify(clientUid);
    }

    protected String getValidateClientId(String browserInfo) {
        // 以服务端获取的客户端标识 做识别标志
        if (StrUtil.isNotEmpty(browserInfo)) {
            return Md5Util.md5(browserInfo);
        }
        return null;
    }

    protected void afterValidateFail(String browserInfo) {
        if(limitHandler == null) {
            return;
        }
        String clientUid = getValidateClientId(browserInfo);
        // 无客户端身份标识，不限制
        if(StrUtil.isBlank(clientUid)){
            return ;
        }
        // 验证失败 分钟内计数
        String fails = String.format(FrequencyLimitHandler.LIMIT_KEY, "FAIL", clientUid);
        CaptchaCacheService cs = getCacheService(cacheType);
        if (!cs.exists(fails)) {
            cs.set(fails, "1", 60);
        }
        cs.increment(fails, 1);
    }

    public static boolean base64StrToImage(String imgStr, String path) {
        if (imgStr == null) {
            return false;
        }

        Base64.Decoder decoder = Base64.getDecoder();
        try {
            // 解密
            byte[] b = decoder.decode(imgStr);
            // 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            //文件夹不存在则自动创建
            File tempFile = new File(path);
            if (!tempFile.getParentFile().exists()) {
                tempFile.getParentFile().mkdirs();
            }
            OutputStream out = new FileOutputStream(tempFile);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 解密前端坐标aes加密
     *
     * @param point
     * @return
     * @throws Exception
     */
    public static String decrypt(String point, String key) throws Exception {
        return AESUtil.aesDecrypt(point, key);
    }

    protected static int getEnOrChLength(String s) {
        int enCount = 0;
        int chCount = 0;
        for (int i = 0; i < s.length(); i++) {
            int length = String.valueOf(s.charAt(i)).getBytes(StandardCharsets.UTF_8).length;
            if (length > 1) {
                chCount++;
            } else {
                enCount++;
            }
        }
        int chOffset = (HAN_ZI_SIZE / 2) * chCount + 5;
        int enOffset = enCount * 8;
        return chOffset + enOffset;
    }


}
