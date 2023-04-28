package com.mudxx.common.utils.properties;

import java.io.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @description 操作properties配置文件的工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:12:31
 */
@SuppressWarnings("ALL")
public class PropertiesUtil {

	/**
	 * 说明：根据key获取value值，其实跟map差不多。
	 * 注意：在静态方法里面我们无法使用this关键字，所以this.getClass()我们不能使用。
	 * @description 使用字节流获取properties文件的key对应的value值
	 * @param filePath properties配置文件类路径，比如："db.properties"。
	 * @param key properties配置文件中的key
	 * @throws IOException
	 */
	public static String getValueByInputStream(String filePath, String key) throws IOException {
		Properties properties = new Properties();
		//获取文件流方法一
		//InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/db.properties");
		//获取文件流方法二
		//InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("db.properties");
		InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
		properties.load(inputStream);
		//如果properties文件编码格式是idea默认的GBK那么获取中文有会有乱码
		//貌似改成UTF-8字符编码格式中文乱码就不见了。
		//System.out.println(properties.getProperty("name"));
		//System.out.println(properties.getProperty("jdbc.url"));
		//返回key对应的value值
		return properties.getProperty(key);
	}

	/**
	 * 说明：根据key获取value值，其实跟map差不多。
	 * 注意：在静态方法里面我们无法使用this关键字，所以this.getClass()我们不能使用。
	 * @description 使用字符流获取properties文件的key对应的value值
	 * @param filePath properties配置文件类路径，比如："/db.properties"。
	 * @param key properties配置文件中的key
	 * @throws IOException
	 */
	public static String getValueByReader(String filePath, String key) throws IOException {
		Properties properties = new Properties();
		//获取文件流方法一
		//InputStream inputStream = PropertiesUtil.class.getResourceAsStream("/db.properties");
		InputStream inputStream = PropertiesUtil.class.getResourceAsStream(filePath);
		//获取文件流方法二
		//InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("db.properties");
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		properties.load(bufferedReader);
		//如果properties文件编码格式是idea默认的GBK那么获取中文有会有乱码
		//我们可以进行编码格式转换即可解决中文乱码问题，new InputStreamReader(inputStream, "GBK")。
		//当然了，如果我们改成UTF-8字符编码格式，不进行编码转换也没有中文乱码出现。
		//System.out.println(properties.getProperty("name"));
		//System.out.println(properties.getProperty("jdbc.url"));
		//返回key对应的value值
		return properties.getProperty(key);
	}

	/**
	 * 注意：该方法稍微麻烦了一点，不推荐使用。
	 * 说明：filePath既可以是绝对路径，也可以是相对路径。
	 * @description 根据key获取value
	 * @param filePath properties文件类路径
	 * @param key properties文件中的key
	 * @return 返回key对应的value
	 */
	public static String getValueByFilePath(String filePath, String key) {
		try {
			Properties properties = new Properties();

			//使用File的好处是可以判断文件是否存在file.exists();
			//File file = new File(filePath);
			//InputStream inputStream = new BufferedInputStream(new FileInputStream(file));

			InputStream inputStream = new BufferedInputStream(new FileInputStream(filePath));
			properties.load(inputStream);
			//noinspection UnnecessaryLocalVariable
			String value = properties.getProperty(key);
			return value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 说明：该方法非常好，将properties转换为更易为操作的Map集合。
	 * @description 读取Properties的全部信息
	 * @param filePath properties配置文件类路径，比如："db.properties"。
	 * @return 返回key、value的map集合形式。
	 */
	public static Map<String, String> getAllProperties(String filePath) {
		try {
			Properties properties = new Properties();
			InputStream inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream(filePath);
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			properties.load(bufferedReader);
			//得到配置文件key的枚举
			Enumeration<?> enumeration = properties.propertyNames();
			//创建Map集合，用于封装配置文件的键值对信息
			Map<String, String> map = new HashMap<>(properties.size());
			//循环遍历
			while (enumeration.hasMoreElements()) {
				String key = (String) enumeration.nextElement();
				String value = properties.getProperty(key);
				map.put(key, value);
			}
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @description 往properties文件里面写入键值对。
	 * @param filePath 这里使用绝对路径或者相对路径。
	 * @param key 键
	 * @param value 值
	 */
	public static void writeKeyValueForProperties(String filePath, String key, String value) {
		try {
			Properties properties = new Properties();
			InputStream inputStream = new FileInputStream(filePath);
			properties.load(inputStream);
			properties.setProperty(key, value);
			OutputStream out = new FileOutputStream(filePath);
			properties.store(out, "保存文件");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
