package com.mudxx.common.utils.string;

import com.mudxx.common.utils.date.CalendarUtil;
import com.mudxx.common.utils.empty.EmptyUtil;
import com.mudxx.common.utils.transfer.TransferTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description String工具类，对字符串进行操作以达到我们更方便使用的目的
 * @author laiwen
 * @date 2018年5月23日 上午11:13:31
 */
@SuppressWarnings("ALL")
public class StringUtil {

	private static final Logger log = LoggerFactory.getLogger(StringUtil.class);

	/**
	 * Integer类型
	 */
	public final static String INTEGER = "INTEGER";

	/**
	 * Float类型
	 */
	public final static String FLOAT = "FLOAT";

	/**
	 * 只有日期没有时间
	 */
	public final static String DATE = "DATE";

	/**
	 * 既有日期也有时间
	 */
	public final static String DATETIME = "DATETIME";

	/**
	 * 布尔类型
	 */
	public final static String BOOLEAN = "BOOLEAN";

	/**
	 * @description 获取唯一随机数UUID，比如：539b34659ae3431c804d02bae470bd72（32个字符）
	 * @return
	 */
	public static String getUuid() {
		return UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * @description 统计字符串中某个子字符串出现的次数
	 * @param str 字符串
	 * @param subStr 子字符串
	 * @return 返回字符串中某个子字符串出现的次数
	 */
	public static Integer count(String str, String subStr) {
		if (!str.contains(subStr)) {
			return 0;
		} else if (Objects.equals(str, subStr)) {
			return 1;
		} else if (str.endsWith(subStr)) {
			return (str.split(subStr)).length;
		} else {
			return (str.split(subStr)).length - 1;
		}
	}

	/**
	 * 说明：获取某个子字符串在整个字符串中第N次出现时的下标索引（正着数第N次）
	 * @description 子字符串modelStr在字符串str中第count次出现时的下标（正着数第count次）
	 * @param str 字符串str
	 * @param modelStr 子字符串modelStr
	 * @param count 第count次出现（正着数）
	 * @return 下标
	 */
	public static Integer getFromIndex(String str, String modelStr, Integer count) {
		//对子字符串进行匹配
	    Matcher slashMatcher = Pattern.compile(modelStr).matcher(str);
		int index = 0;
	    //matcher.find();尝试查找与该模式匹配的输入序列的下一个子序列
		while (slashMatcher.find()) {
		    index++;
		    //当modelStr字符第count次出现的位置
		    if (index == count) {
		       break;
		    }
		}
	    //matcher.start();返回以前匹配的初始索引。
		return slashMatcher.start();
	}

	/**
	 * 说明：获取某个子字符串在整个字符串中第N次出现时的下标索引（倒着数第N次）
	 * @description 子字符串modelStr在字符串str中第count次出现时的下标（倒着数第count次）
	 * @param str 字符串str
	 * @param modelStr 子字符串modelStr
	 * @param count 第count次出现（倒着数）
	 * @return 下标
	 */
	public static Integer getLastIndex(String str, String modelStr, Integer count) {
		for (int i = count; i >= 1; i--) {
			int lastIndex = str.lastIndexOf(modelStr);
			str = str.substring(0, lastIndex);
		}
		return str.length();
	}

	/**
	 * @description 判断字符串的长度是否在[min, max]范围内
	 * @param content 字符串
	 * @param min 最小长度
	 * @param max 最大长度
	 * @return 如果不在指定范围内返回false，否则返回true
	 */
	public static Boolean validateLength(String content, Integer min, Integer max) {
		Integer length = content.length();
		Boolean flag = true;
		if (length < min || length > max) {
			flag = false;
		}
		return flag;
	}

	/**
	 * @description 判断字符串的长度是否在[0, max]范围内
	 * @param content 字符串
	 * @param max 最大长度
	 * @return 如果不在指定范围内返回false，否则返回true
	 */
	public static Boolean validateLength(String content, Integer max) {
		return validateLength(content, 0, max);
	}

	/**
	 * @description 如果参数为blank，那么返回""，如果不为blank，那么返回参数的字符串类型
	 * @param object
	 * @return
	 */
	public static String checkBlankToString(Object object) {
		return atLeastOneIsBlank(object) ? "" : object.toString();
	}

	/**
	 * @description 如果参数为blank，那么返回blankToString，如果不为blank，那么返回参数的字符串类型
	 * @param object
	 * @param blankToString
	 * @return
	 */
	public static String checkBlankToString(Object object, String blankToString) {
		return atLeastOneIsBlank(object) ? blankToString : object.toString();
	}

	/**
	 * @description 一次性判断多个或单个对象至少有一个为空。
	 * @param objects
	 * @return 只要有一个元素为Blank，则返回true。
	 */
	public static Boolean atLeastOneIsBlank(Object...objects) {
		Boolean result = false;
		//System.out.println(objects.length);//可以确定的是objects的类型是数组，不是集合
		for (Object object : objects) {
			if (null == object || "".equals(object.toString().trim())
							  || "null".equals(object.toString().trim())
							  || "[]".equals(object.toString().trim())
							  || "[null]".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * @description 一次性判断多个或单个对象均不为空。
	 * @param objects
	 * @return 所有元素均不为Blank，则返回true。
	 */
	public static Boolean allIsNotBlank(Object...objects) {
		return !atLeastOneIsBlank(objects);
	}

	/**
	 * @description  一次性判断多个或单个对象至少有一个不为空。
	 * @param objects
	 * @return 只要有一个元素不为Blank，则返回true
	 */
	public static Boolean atLeastOneIsNotBlank(Object...objects) {
		Boolean result = false;
		//System.out.println(objects.length);//可以确定的是objects的类型是数组，不是集合
		for (Object object : objects) {
			if (null != object && !"".equals(object.toString().trim())
							  && !"null".equals(object.toString().trim())
							  && !"[]".equals(object.toString().trim())
							  && !"[null]".equals(object.toString().trim())) {
				result = true;
				break;
			}
		}
		return result;
	}

	/**
	 * @description 一次性判断多个或单个对象均为空。
	 * @param objects
	 * @return 所有元素均为Blank，则返回true。
	 */
	public static Boolean allIsBlank(Object...objects) {
		return !atLeastOneIsNotBlank(objects);
	}

	/**
	 * @description 对字符串str进行md5加密获取信息摘要，即获取md5的信息摘要实例并对字符串str（字节数组）进行计算摘要
	 * @param str
	 * 		String str = (System.currentTimeMillis() + new Random().nextInt(999999999)) + "";
	 * @return
	 */
	public static String stringToMd5(String str) {
		try {
			//获取md5加密
			return new String(MessageDigest.getInstance("md5").digest(str.getBytes()));
			//异常提示表示没有该算法（比如md5）
		} catch (NoSuchAlgorithmException e) {
			//必须要有，否则会认为没有返回值
			return "";
		}
	}

	/**
	 * @description 使用md5的算法进行加密
	 * @param plainText
	 */
	public static String md5(String plainText) {
		byte[] secretBytes ;
		try {
			secretBytes = MessageDigest.getInstance("md5")
						 .digest(plainText.getBytes());
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("没有md5这个算法！");
		}
		// 16进制数字
		String md5code = new BigInteger(1, secretBytes).toString(16);
		// 如果生成数字未满32位，需要前面补0
		for (int i = 0; i < 32 - md5code.length(); i++) {
			md5code = "0" + md5code;
		}
		return md5code;
	}

	/**
	 * @description 字符转换为ASCII码（字符转10进制数）
	 * @param ch
	 * @return
	 */
	public static Integer charToAscii(char ch) {
		return ch & 0xffff;
	}

	/**
	 * @description ASCII码转Unicode编码（10进制数转16进制数）
	 * @param i
	 * @return
	 */
	public static String asciiToUnicode(int i) {
		return "\\u" + Integer.toHexString(i);
	}

	/**
	 * @description 字符串转换为Unicode编码（字符串转16进制数）
	 * @param str
	 * @return
	 */
	public static String strToUnicode(String str) {
		String s = "";
		for (int i=0; i<str.length(); i++) {
			//字符转换为ASCII码
			int num = charToAscii(str.charAt(i));
			//ASCII码转Unicode编码
			s += asciiToUnicode(num);
		}
		return s;
	}

	/**
	 * @description 字符串转换为Unicode编码（字符串转16进制数）
	 * @param str
	 * @return
	 */
	public static String strToUnicodeAnother(String str) {
		String[] strArray = new String[str.length()];
		String s = "";
		// 方法一
		// for (int i=0; i<str.length(); i++) {
		// 方法二
		for (int i=0; i<strArray.length; i++) {
			int num = str.charAt(i) & 0xffff;
			strArray[i] = Integer.toHexString(num);
			s += "\\u" + strArray[i];
		}
		return s;
	}

	/**
	 * @description 16进制数转字符
	 * @return
	 */
	public static char hexToChar(String hex) {
		return (char) Integer.parseInt(hex, 16);
	}

	/**
	 * @description 16进制数转10进制数
	 * @param hex
	 * @return
	 */
	public static Integer hexToDecimal(String hex) {
		return Integer.parseInt(hex, 16);
	}

	/**
	 * @description 10进制数转字符
	 * @returnb
	 */
	public static char decimalToChar(String decimal) {
		return (char)Integer.parseInt(decimal);
	}

	/**
	 * @description Unicode编码转中文字符串
	 * （该方法貌似看似用不着，因为直接打印Unicode编码就可以得到我们想要的中文字符串）
	 * @param unicode
	 * @return
	 */
	public static String unicodeToStr(String unicode) {
		int n = unicode.length() / 6;
		StringBuilder sb = new StringBuilder(n);
		for (int i = 0, j = 2; i < n; i++, j += 6) {
			String code = unicode.substring(j, j + 4);
			char ch = (char) Integer.parseInt(code, 16);
			sb.append(ch);
		}
		return sb.toString();
	}

	/**
	 * @description 字符串转URLCode
	 * @param str
	 * @return
	 */
	public static String strToUrlCode(String str) {
		try {
			//转码
			return URLEncoder.encode(str,"utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("字符串转换为URLCode失败，str："+str,e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * @description URLCode转字符串
	 * @param urlCode
	 * @return
	 */
	public static String urlCodeToStr(String urlCode) {
		try {
			//解码
			return URLDecoder.decode(urlCode,"utf-8");
		} catch (UnsupportedEncodingException e) {
			log.error("URLCode转换为字符串失败，urlCode："+urlCode,e);
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 注意：该方法貌似不起作用
	 * @description HttpServletRequest请求数据转字符串
	 * 服务器端用request.getInputStream() 从流里面读JSON字符串
	 * @param request
	 * @return
	 * @throws IOException
	 */
	public static String requestToString(HttpServletRequest request) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String line ;
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		return sb.toString();
	}

	/**
	 * @description  判断一个字符是否是中文
	 * @param c 待验证字符
	 * @return
	 */
	public static Boolean isChinese(char c) {
		//判断该字符是否处于汉字字节码范围之内
		return c >= 0x4E00  && c <= 0x9FA5;
	}

	/**
	 * @description  判断一个字符串是否含有中文
	 * @param str 待验证字符串
	 * @return
	 */
	public static Boolean isChinese(String str) {
		if (str == null) {
			return false;
		}
		for (char c : str.toCharArray()) {
			if (isChinese(c)) {
				//有一个中文字符就返回
				return true;
			}
		}
		return false;
	}

	/**
	 * @description 替换字符串
	 * @param str 字符串
	 * @param nowStr 要被替换的字符串
	 * @param replaceStr 用来替换的字符串
	 * @return
	 */
	public static String replaceString(String str, String nowStr, String replaceStr) {
		nowStr = atLeastOneIsBlank(nowStr) ? "" : nowStr;
		replaceStr = atLeastOneIsBlank(replaceStr) ? "" : replaceStr;
		if (atLeastOneIsNotBlank(str)) {
			return str.replace(nowStr,replaceStr);
		}
		return "";
	}

	/**
	 * @description Double转字符串
	 * @param d
	 * @return
	 */
	public static String getDoubleToString(Double d) {
		String str = d.toString();
		if (!str.contains("E")) {
			return str;
		} else {
			BigDecimal bigDecimal = new BigDecimal(d.toString());
			return bigDecimal.toPlainString();
		}
	}

	/**
	 * @description 字符串转换成其他数据类型
	 * @param string 字符串
	 * @param type 其他类型
	 * @return
	 */
	public static Object string2OtherType(String string, String type) {
		if (string == null || type == null) {
			return null;
		}
		//因为没有else所以必须给object进行初始化
		Object object = null;
		if (INTEGER.equals(type)) {
			//整型数据
			object = Integer.valueOf(string);
		} else if(FLOAT.equals(type)) {
			//浮点型数据
			object = Float.valueOf(string);
		} else if(DATE.equals(type)) {
			//日期型数据，只有日期没有时间
			object = CalendarUtil.stringToDate(string, "yyyy-MM-dd");
		} else if(DATETIME.equals(type)) {
			//日期型数据，既有日期也有时间
			object = CalendarUtil.stringToDate(string, "yyyy-MM-dd HH:mm:ss");
		} else if(BOOLEAN.equals(type)) {
			//布尔型数据
			object = Boolean.valueOf(string);
		}
		return object;
	}

	/**
	 * @description 将以逗号分隔的字符串转换成字符串数组
	 * @param valStr 以逗号分隔的字符串，形如："12,34,45,66,36"
	 * @return
	 */
	public static String[] string2Array(String valStr) {
		int i = 0;
		//这步的目的是：将参数临时赋值，防止下面的replace操作改变valStr的本身。
		String tempStr = valStr;
		//以逗号分隔的字符串形如："12,34,45,66,36"，其中逗号4个，而元素个数是5个
		String[] strArray = new String[valStr.length() + 1 - tempStr.replace(",", "").length()];
		valStr = valStr + ",";
		//如果字符串valStr含有,字符
		while (valStr.indexOf(",") > 0) {
			//包含开头但不包含结尾
			strArray[i] = valStr.substring(0, valStr.indexOf(","));
			valStr = valStr.substring(valStr.indexOf(",") + 1, valStr.length());
			i++;
		}
		return strArray;
	}

	/**
	 * 说明：主要是用来解决GET请求中文乱码问题（比如?name=张三）。
	 * @description 字符编码转换
	 * @param oldData
	 * @return
	 */
	public static String resolveMessy(String oldData) {
		String newData = "";
		try {
			newData = new String(oldData.getBytes("ISO-8859-1"), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newData;
	}

	/**
	 * 说明：该方法没啥用！
	 * @description 获取字符串的字符编码格式
	 * @param str
	 * @return
	 */
	public static String getEncoding(String str) throws Exception {
		String encoding = "ISO-8859-1";
		if (str.equals(new String(str.getBytes(encoding), encoding))) {
			return encoding;
		}
		encoding = "GB2312";
		if (str.equals(new String(str.getBytes(encoding), encoding))) {
			return encoding;
		}
		encoding = "GBK";
		if (str.equals(new String(str.getBytes(encoding), encoding))) {
			return encoding;
		}
		encoding = "UTF-8";
		if (str.equals(new String(str.getBytes(encoding), encoding))) {
			return encoding;
		}
		return "";
	}

	/**
	 * 强调：对小数不适用
	 * @description 阿拉伯数字转中文数字，仅限整数，示例：13015  ===>  "一万三千零一十五"
	 * @param number 阿拉伯数字
	 * @return 返回中文数字字符串
	 */
	public static String numberToChinese(Integer number) {//转化一个阿拉伯数字为中文字符串
		if (number == 0) {
			return "零";
		}
		//节权位标识
		int unitPos = 0;
		String all = "";
		//中文数字字符串
		String chineseNum = "";
		//下一小结是否需要补零
		Boolean needZero = false;
		String strIns;
		while (number > 0) {
			//取最后面的那一个小节
			int section = number % 10000;
			//判断上一小节千位是否为零，为零就要加上零
			if (needZero) {
				all = TransferTool.chnNumChar[0] + all;
			}
			//处理当前小节的数字，然后用chineseNum记录当前小节数字
			chineseNum = sectionToChinese(section, chineseNum);
			//此处用if else 选择语句来执行加节权位
			if ( section != 0 ) {
				//当小节不为0，就加上节权位
				strIns = TransferTool.chnUnitSection[unitPos];
				chineseNum = chineseNum + strIns;
			} else {
				//否则不用加
				strIns = TransferTool.chnUnitSection[0];
				chineseNum = strIns + chineseNum;
			}
			all = chineseNum + all;
			chineseNum = "";
			needZero = (section < 1000) && (section > 0);
			number = number / 10000;
			unitPos++;
		}
		return all;
	}

	/**
	 * @description 处理当前小节的数字，然后用chineseNum记录当前小节数字
	 * @param section 当前小节的数字
	 * @param chineseNum 中文数字
	 * @return 返回中文数字字符串
	 */
	private static String sectionToChinese(int section, String chineseNum) {
		//小节部分用独立函数操作
		String setionChinese;
		//小节内部的权值计数器
		int unitPos = 0;
		//小节内部的制零判断，每个小节内只能出现一个零
		Boolean zero = true;
		while (section > 0) {
			//取当前最末位的值
			int v = section % 10;
			if (v == 0) {
				if ( !zero ) {
					//需要补零的操作，确保对连续多个零只是输出一个
					zero = true;
					chineseNum = TransferTool.chnNumChar[0] + chineseNum;
				}
			} else {
				//有非零的数字，就把制零开关打开
				zero = false;
				//对应中文数字位
				setionChinese = TransferTool.chnNumChar[v];
				//对应中文权位
				setionChinese = setionChinese + TransferTool.chnUnitChar[unitPos];
				chineseNum = setionChinese + chineseNum;
			}
			unitPos++;
			section = section / 10;
		}
		return chineseNum;
	}

	/**
	 * 强调：对小数不适用
	 * @description 中文数字转阿拉伯数字，仅限整数，示例："一万三千零一十五"  ===>  13015
	 * @param chinese 中文数字字符串
	 * @return 返回阿拉伯数字
	 */
	public static Integer chineseToNumber(String chinese) {
		String str1 = "";
		String str2 = "";
		String str3 = "";
		int k = 0;
		Boolean dealflag = true;
		//先把字符串中的“零”除去
		for (int i = 0; i < chinese.length(); i++) {
			if ('零' == (chinese.charAt(i))) {
				chinese = chinese.substring(0, i) + chinese.substring(i + 1);
			}
		}
		String chineseNum = chinese;
		for (int i = 0; i < chineseNum.length(); i++) {
			if (chineseNum.charAt(i) == '亿') {
				//截取亿前面的数字，逐个对照表格，然后转换
				str1 = chineseNum.substring(0, i);
				k = i + 1;
				//已经处理
				dealflag = false;
			}
			if (chineseNum.charAt(i) == '万') {
				str2 = chineseNum.substring(k, i);
				str3 = chinese.substring(i + 1);
				//已经处理
				dealflag = false;
			}
		}
		//如果没有处理
		if (dealflag) {
			str3 =  chineseNum;
		}
		return sectionChinese(str1) * 100000000 +
				sectionChinese(str2) * 10000 + sectionChinese(str3);
	}

	/**
	 * @description 中文数字字符串转阿拉伯数字
	 * @param str 中文数字字符串
	 * @return 返回阿拉伯数字
	 */
	private static Integer sectionChinese(String str) {
		int value = 0;
		int sectionNum = 0;
		for (int i = 0; i < str.length(); i++) {
			int v = (Integer) TransferTool.intList.get(str.charAt(i));
			//如果数值是权位则相乘
			if ( v == 10 || v == 100 || v == 1000 ) {
				sectionNum = v * sectionNum;
				value = value + sectionNum;
			} else if (i == str.length() - 1) {
				value = value + v;
			} else {
				sectionNum = v;
			}
		}
		return value;
	}

	/**
	 * description: 将手机号中间4位替换成4位*，比如将18776549876中间4位替换成4位*变成187****9876
	 * @author laiwen
	 * @date 2022-01-19 11:50:09
	 * @param phone 手机号
	 * @return 返回加星手机号
	 */
	public static String getStarPhone(String phone) {
		if (EmptyUtil.isEmpty(phone)) {
			throw new RuntimeException("手机号不允许为空！");
		}
		return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
	}

	/**
	 * description: 将18位身份证号中间10位替换成4位*，比如123456789987654321中间10位替换成4位*变成1234****4321
	 * @author laiwen
	 * @date 2022-01-19 11:56:39
	 * @param idCard 身份证号
	 * @return 返回加星身份证号
	 */
	public static String getStarIdCard(String idCard) {
		if (EmptyUtil.isEmpty(idCard)) {
			throw new RuntimeException("身份证号不允许为空！");
		}
		return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1****$2");
	}

	/**
	 * description: 使用正则表达式来判断字符串中是否包含英文字母
	 * @author laiwen
	 * @date 2022-05-19 17:51:26
	 * @param content 待检验的字符串
	 * @return 返回true表示包含英文字母；返回false表示不包含英文字母
	 */
	public static Boolean judgeContainsLetter(String content) {
		String regex = ".*[a-zA-Z]+.*";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		return matcher.matches();
	}

}
