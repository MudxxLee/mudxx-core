package com.mudxx.common.utils.serialize;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @description 序列化操作工具类
 * @author laiwen
 * @date 2019年4月12日 下午1:56:35
 */
@SuppressWarnings("ALL")
public class SerializeUtil {

	private static final Logger log = LoggerFactory.getLogger(SerializeUtil.class);

	/**
	 * @description 序列化
	 * @author rambo
	 * @param object 实现了序列化接口的对象
	 * @param fileNamne 传输保存的文件位置，即序列化文件存储路径（绝对路径，比如："d://obj.bin"）
	 */
	public static void writeObject(Serializable object, String fileNamne) {
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileNamne);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
			objectOutputStream.writeObject(object);
			log.info("序列化成功");
			objectOutputStream.close();
			fileOutputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @description 反序列化
	 * @author rambo
	 * @param fileNamne 传输保存的文件位置，即序列化文件存储路径（绝对路径，比如："d://obj.bin"）
	 * @return 返回反序列化之后的对象
	 */
	public static Object readObject(String fileNamne) {
		Object object = null;
		try {
			FileInputStream fileInputStream = new FileInputStream(fileNamne);
			ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
			object = objectInputStream.readObject();
			log.info("反序列化成功");
			objectInputStream.close();
			fileInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object;
	}
	
}
