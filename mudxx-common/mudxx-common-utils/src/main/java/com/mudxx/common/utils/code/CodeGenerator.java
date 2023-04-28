package com.mudxx.common.utils.code;

import com.mudxx.common.utils.reflect.ReflectUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;

/**
 * @description 编号规则生成器
 * @author laiwen
 * @date 2017年7月4日 上午10:10:07
 */
@SuppressWarnings("ALL")
public class CodeGenerator {

	/**
	 * 说明：BI + yyyyMM + 3位流水号，比如 BI201809001
	 * @description 横幅广告配置编号规则前缀
	 */
	public static final String BANNER_INFO_PREFIX = "BI";

	/**
	 * 说明：CI + yyyyMMdd + 4位流水号，比如 CI201809170001
	 * @description 商品信息编号规则前缀
	 */
	public static final String COMMODITY_INFO_PREFIX = "CI";
	
	/**
	 * 说明：LI + yyyyMMdd + 4位流水号，比如 LI201809170001
	 * @description 物流信息编号规则前缀
	 */
	public static final String LOGISTICS_INFO_PREFIX = "LI";
	
	/**
	 * 说明：OI + yyyyMMdd + 4位流水号，比如 OI201809170001
	 * @description 订单信息编号规则前缀
	 */
	public static final String ORDER_INFO_PREFIX = "OI";
	
	//包含年月的编号：横幅广告配置
	private static final String[] YEARMONTHARRAY = {BANNER_INFO_PREFIX};

	//包含年月日的编号：商品信息、物流信息、订单信息
	private static final String[] YEARMONTHDAYARRAY = {COMMODITY_INFO_PREFIX, LOGISTICS_INFO_PREFIX, ORDER_INFO_PREFIX};

	//流水号是3位的编号：横幅广告配置
	private static final String[] SERIALNOTHREEARRAY = {BANNER_INFO_PREFIX};

	//流水号是4位的编号：商品信息、物流信息、订单信息
	private static final String[] SERIALNOFOURARRAY = {COMMODITY_INFO_PREFIX, LOGISTICS_INFO_PREFIX, ORDER_INFO_PREFIX};

	//流水号是6位的编号：
	private static final String[] SERIALNOSIXARRAY = {};

	/**
	 * @description 获取下一个流水号
	 * @param prefix 规则前缀
	 * @param maxSerialNo 最大流水号
	 * @return 返回下一个流水号
	 */
	public static String getSerialNo(String prefix, String maxSerialNo) {
		String serialNo = "";
		if (Arrays.asList(SERIALNOTHREEARRAY).contains(prefix)) {
			if (StringUtils.isBlank(maxSerialNo)) {//最大流水号不存在
				serialNo = "001";//默认生成001
			} else {
				serialNo = String.format("%03d", Integer.valueOf(maxSerialNo) + 1);
			}
		} else if (Arrays.asList(SERIALNOFOURARRAY).contains(prefix)) {
			if (StringUtils.isBlank(maxSerialNo)) {//最大流水号不存在
				serialNo = "0001";//默认生成0001
			} else {
				serialNo = String.format("%04d", Integer.valueOf(maxSerialNo) + 1);
			}
		} else if (Arrays.asList(SERIALNOSIXARRAY).contains(prefix)) {
			if (StringUtils.isBlank(maxSerialNo)) {//最大流水号不存在
				serialNo = "000001";//默认生成000001
			} else {
				serialNo = String.format("%06d", Integer.valueOf(maxSerialNo) + 1);
			}
		}
		return serialNo;
	}

	/**
	 * @description 获取规则编号
	 * @param prefix 编号前缀
	 * @param yearMonth 年月字符串，比如："201708"
	 * @param yearMonthDay 年月日字符串，比如："20170826"
	 * @param serialNo 流水号
	 * @return 返回规则编号
	 */
	public static String getCode(String prefix, String yearMonth, String yearMonthDay, String serialNo) {
		String code = "";//规则编号
		if (Arrays.asList(YEARMONTHARRAY).contains(prefix)) {
			code = prefix + yearMonth + serialNo;
		} else if (Arrays.asList(YEARMONTHDAYARRAY).contains(prefix)) {
			code = prefix + yearMonthDay + serialNo;
		}
		return code;
	}

	/**
	 * @description 获取年月
	 * @return 返回格式 yyyyMM
	 */
	public static String getCurrentYearMonth() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		return year + "" + ( month < 10 ? "0" + month : month);
	}

	/**
	 * @description 获取年月日
	 * @return 返回格式 yyyyMMdd
	 */
	public static String getCurrentYearMonthDay() {
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		return year + "" + (month < 10 ? "0" + month : month) + "" + (day < 10 ? "0" + day : day);
	}

	/**
	 * @description 根据编号前缀获取当前日期字符串，可能是年月，也可能是年月日
	 * @param prefix 编号前缀
	 * @return 返回当前日期字符串：例如"201708"，或者"20170826"
	 */
	private static String getDateNum(String prefix, String yearMonth, String yearMonthDay) {
		String dateNum = "";
		if (Arrays.asList(YEARMONTHARRAY).contains(prefix)) {
			dateNum = yearMonth;
		} else if (Arrays.asList(YEARMONTHDAYARRAY).contains(prefix)) {
			dateNum = yearMonthDay;
		}
		return dateNum;
	}

	/**
	 * @description 获取与规则编号相关的数据数组
	 * @param prefix 前缀
	 * @param object 对象
	 * @param methodName 方法名
	 * @param mapper mapper方法名
	 * @return 返回值是一个code、dateNum、serialNo组成的字符串数组
	 */
	public static String[] getCodeInfo(String prefix, Object object, String methodName, String mapper) {
		String yearMonth = getCurrentYearMonth();//当前年月字符串
		String yearMonthDay = getCurrentYearMonthDay();//当前年月日字符串
		Class<?>[] classes = new Class[]{String.class, Object.class};//参数类型数组
		Object[] params = new Object[2];//参数值数组
		params[0] = mapper;//第一个参数，mapper方法名
		String dateNum = getDateNum(prefix, yearMonth, yearMonthDay);
		params[1] = dateNum;//第二个参数，当前日期字符串
		String maxSerialNo = (String) ReflectUtil.invoke(object, methodName, classes, params);//数据库已存在最大流水号
		String serialNo = getSerialNo(prefix, maxSerialNo);//获取当前编号需要的流水号
		String code = getCode(prefix, yearMonth, yearMonthDay, serialNo);//编号、日期数组
		return new String[]{code, dateNum, serialNo};//编号、日期、流水号数组
	}

	/**
	 * @description 测试编号规则生成方式是否正确
	 * @param args main默认参数
	 */
	public static void main(String[] args) {
		String prefix1 = CodeGenerator.BANNER_INFO_PREFIX;
		//第一次为null（从数据库里根据条件查流水号，不建议使用count(*)来查询因为数据有删减，建议使用max()函数）
		String maxSerialNo1 = "001";
		String serialNo1 = CodeGenerator.getSerialNo(prefix1, maxSerialNo1);
		String yearMonth = CodeGenerator.getCurrentYearMonth();
		String code1 = CodeGenerator.getCode(CodeGenerator.BANNER_INFO_PREFIX, yearMonth, null, serialNo1);
		System.out.println(code1);
		
		String prefix2 = CodeGenerator.COMMODITY_INFO_PREFIX;
		//第一次为null（从数据库里根据条件查流水号，不建议使用count(*)来查询因为数据有删减，建议使用max()函数）
		String maxSerialNo2 = "0001";
		String serialNo2 = CodeGenerator.getSerialNo(prefix2, maxSerialNo2);
		String yearMonthDay = CodeGenerator.getCurrentYearMonthDay();
		String code2 = CodeGenerator.getCode(prefix2, null, yearMonthDay, serialNo2);
		System.out.println(code2);
	}
	
	public void test() {
		String serialNo = "00001";
		Integer num = Integer.valueOf(serialNo) + 100;
		System.out.println(num);//101
	}

}
