package com.mudxx.common.utils.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @description 处理异常信息的工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:11:31
 */
@SuppressWarnings("ALL")
public class ExceptionUtil {

	/**
	 * @description 获取异常的堆栈信息
	 * @param throwable
	 * @return
	 */
	public static String getStackTrace(Throwable throwable) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		try {
			throwable.printStackTrace(printWriter);
			return stringWriter.toString();
		} finally {//finally里的代码无论如何都会执行，即使try、catch里面有return语句！
			printWriter.close();
		}
	}

	/**
	 * @description 输出异常信息日志
	 * @param e Throwable包含Exception和Error（比如out of memory）
	 * @return 返回值的内容与e.printStackTrace()输出的内容相同。
	 */
	public static String logExceptionStack(Throwable e) {
		//使用字符串作为物理节点的字符输入输出流的用法
		StringWriter errorsWriter = new StringWriter();
		//new PrintWriter(errorsWriter)只有一个参数，第二个参数默认缺省值是false表示覆盖原内容。
		e.printStackTrace(new PrintWriter(errorsWriter));
		return errorsWriter.toString();
	}

	/**
	 * @description 获取异常堆栈信息
	 * @param e Exception
	 * @return 返回值的内容与e.printStackTrace()输出的内容相同。
	 */
	public static String getStackTrace(Exception e) {
		//使用字符串作为物理节点的字符输入输出流的用法
		StringWriter errorsWriter = new StringWriter();
		//第二个参数true表示不覆盖之前文件里面已经有的内容，而是追加内容。默认缺省值false表示覆盖。
		e.printStackTrace(new PrintWriter(errorsWriter, true));
		return errorsWriter.toString();
	}

}
