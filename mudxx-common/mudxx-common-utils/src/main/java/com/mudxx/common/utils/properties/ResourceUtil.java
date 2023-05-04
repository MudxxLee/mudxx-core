package com.mudxx.common.utils.properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * @description 操作properties配置文件工具类
 * @author laiwen
 * @date 2018年7月10日 下午1:22:57
 */
@SuppressWarnings("ALL")
public class ResourceUtil {

	private static final Logger log = LoggerFactory.getLogger(ResourceUtil.class);

    private static ResourceBundle BUNDLE;
    
    /**
     * @description 构造函数
     * @param resourcePath 配置文件地址，比如：resource/resource
     */
    public ResourceUtil(String resourcePath) {
    	BUNDLE = ResourceBundle.getBundle(resourcePath);
    }
    
    /**
     * @description 获取配置文件里面指定的键对应的值
     * @return 返回配置文件里面指定的键对应的值
     */
    public String getValue(String key) {
    	String value = null;
    	try {
    		value = new String(BUNDLE.getString(key).getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			log.info("【不支持编码异常，请确保您设置的字符编码没有问题！】");
		} catch (NullPointerException e) {
			log.info("【空指针异常，请确保key不为null】");
		} catch (MissingResourceException e) {
			log.info("【读取配置文件出错，请确保您要查找的{}在该配置文件里面存在！】", key);
		}
        return value;
    }
    
}
