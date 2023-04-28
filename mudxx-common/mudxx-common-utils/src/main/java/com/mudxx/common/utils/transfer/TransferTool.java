package com.mudxx.common.utils.transfer;

import java.util.HashMap;

/**
 * @description 阿拉伯数字和中文数字转换工具类
 * @author laiwen
 * @date 2018年5月23日 上午11:14:31
 */
@SuppressWarnings("all")
public class TransferTool {

	//数字位
	public static String[] chnNumChar = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};
	public static char[] chnNumChinese = {'零', '一', '二', '三', '四', '五', '六', '七', '八', '九'};

	//节权位
	public static String[] chnUnitSection = {"", "万", "亿", "万亿"};

	//权位
	public static String[] chnUnitChar = {"", "十", "百", "千"};

	public static HashMap intList = new HashMap();

	static {
		for (int i = 0; i < chnNumChar.length; i++) {
			intList.put(chnNumChinese[i], i);
		}
		intList.put('十', 10);
		intList.put('百', 100);
		intList.put('千', 1000);
	}

}
