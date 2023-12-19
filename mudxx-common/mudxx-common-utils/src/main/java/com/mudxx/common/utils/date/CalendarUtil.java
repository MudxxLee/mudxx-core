package com.mudxx.common.utils.date;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @description Java Calendar 类时间操作：获取时间，时间加减，以及比较时间大小等等。
 * @author laiwen
 * @date 2018年5月23日 上午11:11:21
 */
@SuppressWarnings("ALL")
public class CalendarUtil {

	private static final Logger log = LoggerFactory.getLogger(CalendarUtil.class);

	public static final String YYYY_MM_DD = "yyyy-MM-dd";

	public static final String HH_MM_SS = "HH:mm:ss";

	public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";

	public static final String TIME_STARTING_POINT = " 00:00:00";

	public static final String TIME_ENDING_POINT = " 23:59:59";

	/**
	 * @description 显示当前年份
	 * @author laiwen
	 * @date 2018-05-23 12:20:58
	 * @return 返回年份
	 */
	public static Integer getYear() {
		return Calendar.getInstance().get(Calendar.YEAR);
	}

	/**
	 * @description 显示当前月份 (从0开始, 实际显示要加1)
	 * @author laiwen
	 * @date 2018-05-23 12:25:58
	 * @return 返回月份
	 */
	public static Integer getMonth() {
		return Calendar.getInstance().get(Calendar.MONTH) + 1;
	}

	/**
	 * @description 获取今天是本月的第几天
	 * @author laiwen
	 * @date 2018-05-23 13:20:58
	 * @return 返回今天是本月的第几天
	 */
	public static Integer getDayOfMonth() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @description 获取今天是本周几（星期几，礼拜几）
	 * @author laiwen
	 * @date 2018-05-23 13:24:58
	 * @return 返回英文周几
	 */
	public static String getDayOfWeek() {
		int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
		String day = "";
		// int，short，char，byte
		switch (dayOfWeek) {
			case 1:
				// 星期天（星期日）
				day = "SUNDAY";
				break;
			case 2:
				// 星期一
				day = "MONDAY";
				break;
			case 3:
				// 星期二
				day = "TUESDAY";
				break;
			case 4:
				// 星期三
				day = "WEDNESDAY";
				break;
			case 5:
				// 星期四
				day = "THURSDAY";
				break;
			case 6:
				// 星期五
				day = "FRIDAY";
				break;
			default:
				// 星期六
				day = "SATURDAY";
		}
		return day;
	}

	/**
	 * @description 获取今天是今年的第几天
	 * @author laiwen
	 * @date 2018-05-23 13:27:58
	 * @return 返回今天是今年的第几天
	 */
	public static Integer getDayOfYear() {
		return Calendar.getInstance().get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * @description 获取当前时间的小时数
	 * @author laiwen
	 * @date 2018-05-23 13:30:58
	 * @return 返回当前时间的小时数
	 */
	public static Integer getHour() {
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @description 获取当前时间n小时以后的小时数
	 * @author laiwen
	 * @date 2018-05-23 13:33:58
	 * @param n 小时增量
	 * @return 获取的是小时，比如现在是18点，那么该方法就返回18+n点
	 */
	public static Integer getHourOfDayAfterN(Integer n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, n);
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @description 获取当前时间n小时以前的小时数
	 * @author laiwen
	 * @date 2018-05-23 14:10:58
	 * @param n 小时减量
	 * @return 获取的是小时，比如现在是18点，那么该方法就返回18-n点
	 */
	public static Integer getHourOfDayBeforeN(Integer n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.HOUR_OF_DAY, unm(n));
		return calendar.get(Calendar.HOUR_OF_DAY);
	}

	/**
	 * @description 获取当前时间的分钟数
	 * @author laiwen
	 * @date 2018-05-23 14:20:58
	 * @return 返回当前时间的分钟数
	 */
	public static Integer getMinute() {
		return Calendar.getInstance().get(Calendar.MINUTE);
	}

	/**
	 * @description 获取当前时间n分钟以后的分钟数
	 * @author laiwen
	 * @date 2018-05-23 15:02:58
	 * @param n 分钟增量
	 * @return 比如现在是38分，那么10分钟以后就是48分
	 */
	public static Integer getMinuteAfterN(Integer n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, n);
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @description 获取当前时间n分钟以前的分钟数
	 * @author laiwen
	 * @date 2018-06-05 10:20:58
	 * @param n 分钟减量
	 * @return 比如现在是38分，那么10分钟以前就是28分
	 */
	public static Integer getMinuteBeforeN(Integer n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, unm(n));
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * @description 获取指定整数的相反数
	 * @author laiwen
	 * @date 2018-07-05 10:20:58
	 * @param num 指定整数
	 * @return 返回指定整数的相反数
	 */
	public static Integer unm(Integer num) {
		return -num;
	}

	/**
	 * @description 将指定日期根据指定规则转换成字符串
	 * @author laiwen
	 * @date 2018-08-05 10:20:58
	 * @param date 指定日期
	 * @param regex 指定规则
	 * @return 返回字符串格式的日期
	 */
	public static String dateToString(Date date, String regex) {
		if (date != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return new SimpleDateFormat(regex).format(calendar.getTime());
		}
		return "";
	}

	/**
	 * @description 将指定日期根据固定年月日时分秒的规则转换成字符串
	 * @author laiwen
	 * @date 2018-08-06 10:20:58
	 * @param date 指定日期
	 * @return 返回字符串格式的日期
	 */
	public static String dateToString(Date date) {
		return dateToString(date, YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * @description 将指定日期根据指定规则转换成字符串
	 * @author laiwen
	 * @date 2018-08-07 10:20:58
	 * @param date 指定日期
	 * @param regex 指定规则
	 * @return 返回字符串格式的日期
	 */
	public static String dateToStringAnother(Date date, String regex) {
		return new SimpleDateFormat(regex).format(date);
	}

	/**
	 * @description 根据指定规则获取字符串格式的当前日期
	 * @author laiwen
	 * @date 2018-08-08 10:20:58
	 * @param regex 指定规则
	 * @return 返回字符串格式的当前日期
	 */
	public static String getNowDate(String regex) {
		Calendar calendar = Calendar.getInstance();
		// 默认就是执行此方法，可以省略不写
		calendar.setTime(new Date());
		return new SimpleDateFormat(regex).format(calendar.getTime());
	}

	/**
	 * @description 根据固定规则年月日时分秒获取字符串格式的当前日期
	 * @author laiwen
	 * @date 2018-08-09 10:20:58
	 * @return 返回字符串格式的当前日期
	 */
	public static String getNowDate() {
		return getNowDate(YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * @description 根据指定规则获取字符串格式的当前日期
	 * @author laiwen
	 * @date 2018-08-10 10:20:58
	 * @param regex 指定规则
	 * @return 返回字符串格式的当前日期
	 */
	public static String getNowDateAnother(String regex) {
		return new SimpleDateFormat(regex).format(new Date());
	}

	/**
	 * @description 获取系统当前时间，其实就是new Date()
	 * @author laiwen
	 * @date 2018-08-11 10:20:58
	 * @return 返回系统当前时间
	 */
	public static Date getSystemCurrentDate() {
		return Calendar.getInstance().getTime();
	}

	/**
	 * @description 获取增加指定秒数后的时间
	 * @author laiwen
	 * @date 2018-08-12 10:20:58
	 * @param second 增加指定秒数
	 * @return 返回增加指定秒数后的时间
	 */
	public static String getDateForAddSecond(Integer second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) + second);
		return new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS).format(calendar.getTime());
	}

	/**
	 * @description 根据指定规则将日期字符串转换为日期格式
	 * @author laiwen
	 * @date 2018-08-13 10:20:58
	 * @param dateStr 日期字符串
	 * @param regex 指定规则（要和dateStr的格式一致）
	 * @return 返回日期格式
	 */
	public static Date stringToDate(String dateStr, String regex) {
		try {
			return new SimpleDateFormat(regex).parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
			log.info("日期格式转换异常：" + dateStr + "和" + regex + "的格式不匹配");
			System.out.println("日期格式转换异常：" + dateStr + "和" + regex + "的格式不匹配");
			log.error("【字符串转日期失败，异常信息为】：" + e.getMessage());
			System.out.println("【字符串转日期失败，异常信息为】：" + e.getMessage());
		}
		return null;
	}

	/**
	 * @description 根据固定规则年月日时分秒将日期字符串转换为日期格式
	 * @author laiwen
	 * @date 2018-08-14 10:20:58
	 * @param dateStr 日期字符串
	 * @return 返回日期格式
	 */
	public static Date stringToDate(String dateStr) {
		return stringToDate(dateStr, YYYY_MM_DD_HH_MM_SS);
	}

	/**
	 * @description 在只考虑yyyy-MM-dd和yyyy-MM-dd HH:mm:ss这两种情况下使用该方法，当然了如果还有其他情况可以继续写判断！
	 * @author laiwen
	 * @date 2018-08-15 10:20:58
	 * @param dateStr 日期字符串
	 * @return 返回日期格式
	 */
	public static Date commonToDate(String dateStr) {
		if (dateStr.length() == 10) {
			return stringToDate(dateStr, YYYY_MM_DD);
		} else if(dateStr.length() == 19) {
			return stringToDate(dateStr);
		}
		return null;
	}

	/**
	 * @description 计算需要的时间
	 * @author laiwen
	 * @date 2018-08-16 10:20:58
	 * @param date 原时间
	 * @param year 可以为null，为正数代表加，为负数代表减
	 * @param month 可以为null，为正数代表加，为负数代表减
	 * @param day 可以为null，为正数代表加，为负数代表减
	 * @param hour 可以为null，为正数代表加，为负数代表减
	 * @param minute 可以为null，为正数代表加，为负数代表减
	 * @param second 可以为null，为正数代表加，为负数代表减
	 * @return 返回加减后的时间
	 */
	public static Date getWantedDate(Date date, Integer year, Integer month, Integer day, Integer hour, Integer minute, Integer second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		if (null != year) {
			calendar.add(Calendar.YEAR, year);
		}
		if (null != month) {
			calendar.add(Calendar.MONTH, month);
		}
		if (null != day) {
			calendar.add(Calendar.DAY_OF_MONTH, day);
		}
		if (null != hour) {
			calendar.add(Calendar.HOUR_OF_DAY, hour);
		}
		if (null != minute) {
			calendar.add(Calendar.MINUTE, minute);
		}
		if (null != second) {
			calendar.add(Calendar.SECOND, second);
		}
		return calendar.getTime();
	}

	/**
	 * @description Date对象转Calendar对象
	 * @author laiwen
	 * @date 2018-08-17 10:20:58
	 * @param date Date对象
	 * @return 返回Calendar对象
	 */
	public static Calendar dateToCalendar(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar;
	}

	/**
	 * @description 比较大小
	 * 后者大，显示 -1
	 * 前者大，显示 1
	 * 时间相同，显示 0
	 * @author laiwen
	 * @date 2018-08-18 10:20:58
	 * @param date1 被比较时间
	 * @param date2 比较时间
	 * @return 如果被比较时间晚于比较时间返回1，否则返回-1
	 */
	public static Integer compare(Date date1, Date date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date2);
		return calendar1.compareTo(calendar2);
	}

	/**
	 * @description 比较大小
	 * 后者大，显示 -1
	 * 前者大，显示 1
	 * 时间相同，显示 0
	 * @author laiwen
	 * @date 2018-08-19 10:20:58
	 * @param date1 被比较时间
	 * @param regex1 规则
	 * @param date2 比较时间
	 * @param regex2 规则
	 * @return 如果被比较时间晚于比较时间返回1，否则返回-1
	 */
	public static Integer compare(String date1, String regex1, String date2, String regex2) {
		Date date11 = stringToDate(date1,regex1);
		Date date22 = stringToDate(date2,regex2);
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTime(date11);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTime(date22);
		return calendar1.compareTo(calendar2);
	}

	/**
	 * @description 获取系统当前时间（单位：毫秒）
	 * @author laiwen
	 * @date 2018-08-20 10:20:58
	 * @return 返回当前时间毫秒数
	 */
	public static Long getStartTime() {
		return System.currentTimeMillis();
	}

	/**
	 * @description 计算系统间隔时间（单位：秒）
	 * @author laiwen
	 * @date 2018-08-21 10:20:58
	 * @param startTime 系统开始时间
	 * @return 返回系统间隔时间
	 */
	public static Long interval(Long startTime) {
		long endTime = System.currentTimeMillis();
		return (endTime - startTime) / 1000;
	}

	/**
	 * @description 计算2个日期之间相隔的天数
	 * @author laiwen
	 * @date 2018-08-22 10:20:58
	 * @param d1 开始日期
	 * @param d2 结束日期
	 * @return 返回2个日期之间相隔的天数
	 */
	public static Integer getDaysBetween(Calendar d1, Calendar d2) {
		//如果d1在d2后面，那么把两者的值进行互换，确保d2在d1的后面
		if (d1.after(d2)) {
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
		int y2 = d2.get(Calendar.YEAR);
		if (d1.get(Calendar.YEAR) != y2) {
			d1 = (Calendar) d1.clone();
			do {
				days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
				d1.add(Calendar.YEAR, 1);
			} while (d1.get(Calendar.YEAR) != y2);
		}
		return days;
	}

	/**
	 * @description 计算2个日期之间相隔的工作日天数
	 * @author laiwen
	 * @date 2018-08-23 10:20:58
	 * @param d1 开始日期
	 * @param d2 结束日期
	 * @return 返回2个日期之间相隔的工作日天数
	 */
	public static Integer getWorkingDay(Calendar d1, Calendar d2) {
		int result ;
		//如果d1在d2后面，那么把两者的值进行互换，确保d2在d1的后面
		if (d1.after(d2)) {
			Calendar swap = d1;
			d1 = d2;
			d2 = swap;
		}
		//开始日期的日期偏移量
		int chargeStartDate = 0;

		//结束日期的日期偏移量
		int chargeEndDate = 0;

		// 日期不在同一个日期内
		int stmp;
		int etmp;
		stmp = 7 - d1.get(Calendar.DAY_OF_WEEK);
		etmp = 7 - d2.get(Calendar.DAY_OF_WEEK);
		//开始日期为星期六和星期日时偏移量为0
		if (stmp != 0 && stmp != 6) {
			chargeStartDate = stmp - 1;
		}
		//结束日期为星期六和星期日时偏移量为0
		if (etmp != 0 && etmp != 6) {
			chargeEndDate = etmp - 1;
		}
		result = (getDaysBetween(getNextMonday(d1), getNextMonday(d2)) / 7) * 5 + chargeStartDate - chargeEndDate;
		return result;
	}

	/**
	 * @description 根据日期获知当天星期几
	 * @author laiwen
	 * @date 2018-08-24 10:20:58
	 * @param date
	 * @return 返回中文星期几
	 */
	public static String getChineseWeek(Calendar date) {
		//1-星期日 2-星期一 3-星期二 4-星期三 5-星期四 6-星期五 7-星期六
		final String[] dayNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int dayOfWeek = date.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * @description 根据日期获知当天星期几
	 * @author laiwen
	 * @date 2018-08-25 10:20:58
	 * @param date
	 * @return 返回中文星期几
	 */
	public static String getChineseWeek(Date date) {
		//1-星期日 2-星期一 3-星期二 4-星期三 5-星期四 6-星期五 7-星期六
		final String[] dayNames = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
		return dayNames[dayOfWeek - 1];
	}

	/**
	 * description: 根据指定日期得知当天是周几
	 * @author laiwen
	 * @date 2022-05-07 16:02:33
	 * @param date 指定日期
	 * @return 返回周几数字，比如指定日期是周一那么就返回1，指定日期是周日就返回7
	 */
	public static Integer getWeekDay(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			return 7;
		} else {
			return dayOfWeek - 1;
		}
	}

	/**
	 * @description 获得指定日期的下一个星期一的日期
	 * @author laiwen
	 * @date 2018-08-04 10:20:58
	 * @param date 指定日期
	 * @return 返回指定日期的下一个星期一的日期
	 */
	public static Calendar getNextMonday(Calendar date) {
		Calendar result ;
		result = date;
		do {
			result = (Calendar) result.clone();
			result.add(Calendar.DATE, 1);
		} while (result.get(Calendar.DAY_OF_WEEK) != 2);
		return result;
	}

	/**
	 * @description 计算2个日期之间的休息日天数（只考虑周六周日不考虑节假日）
	 * @author laiwen
	 * @date 2018-08-08 10:20:58
	 * @param d1 小日期
	 * @param d2 大日期
	 * @return 返回2个日期之间的休息日天数
	 */
	public static Integer getOffDays(Calendar d1, Calendar d2) {
		return getDaysBetween(d1, d2) - getWorkingDay(d1, d2);
	}

	/**
	 * @description 将日期时间中时间部分的:改成中文时分，最后加上秒
	 * @author laiwen
	 * @date 2018-09-05 10:20:58
	 * @param dateTime 日期时间字符串
	 * @param h 时(点)
	 * @param m 分
	 * @param s 秒
	 * @return 返回结果比如：2017-07-05 11时28分06秒
	 */
	public static String convertDateTime2Zh(String dateTime, String h, String m, String s) {
		StringBuilder stringBuilder = new StringBuilder();

		//方法一：str.replaceFirst(a, b)只将字符串str中第一次出现的字符串a替换为字符串b
		//dateTime = dateTime.replaceFirst(":", h);
		//dateTime = dateTime.replaceFirst(":", m);

		//方法二：StringUtils.replaceOnce(str, a, b)只将字符串str中第一次出现的字符串a替换为字符串b
		dateTime = StringUtils.replaceOnce(dateTime, ":", h);
		dateTime = StringUtils.replaceOnce(dateTime, ":", m);

		//2017-07-05 11时28分06秒
		return stringBuilder.append(dateTime).append(s).toString();
	}

	/**
	 * @description 计算两个日期相差多少天小时分钟秒
	 * @author laiwen
	 * @date 2020-11-10 16:20:58
	 * @param endDate 结束日期
	 * @param startDate 开始日期
	 * @return 返回相差时间的拼接字符串，比如2,3,4,5表示相差2天3小时4分钟5秒
	 */
	public static Long[] getDatePoor(Date endDate, Date startDate) {
		// 1秒
		Long oneSecond = 1000L;
		// 1分钟
		Long oneMinute = 1000 * 60L;
		// 1小时
		Long oneHour = 1000 * 60 * 60L;
		// 1天
		Long oneDay = 1000 * 24 * 60 * 60L;
		// 获得两个时间的毫秒时间差异
		Long interval = endDate.getTime() - startDate.getTime();
		// 计算差多少天
		Long day = interval / oneDay;
		// 计算差多少小时
		Long hour = interval % oneDay / oneHour;
		// 计算差多少分钟
		Long minute = interval % oneDay % oneHour / oneMinute;
		// 计算差多少秒
		Long second = interval % oneDay % oneHour % oneMinute / oneSecond;
		return new Long[]{day, hour, minute, second};
	}

	/**
	 * description: 获取指定日期所在月份的第一天日期
	 * @author laiwen
	 * @date 2020-11-10 16:20:58
	 * @param date 指定日期
	 * @return 返回指定日期所在月份的第一天日期
	 */
	public static Date getMonthFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		// Date转Calendar
		calendar.setTime(date);
		// Calendar.DATE和Calendar.DAY_OF_MONTH效果相同，使用哪个都行
		// getActualMinimum方法获取日期最小值，这里是获取指定月份的日期最小值
		calendar.set(Calendar.DATE, calendar.getActualMinimum(Calendar.DATE));
		// Calendar转Date
		return calendar.getTime();
	}

	/**
	 * description: 获取指定日期所在月份的最后一天日期
	 * @author laiwen
	 * @date 2020-11-10 16:20:58
	 * @param date 指定日期
	 * @return 返回指定日期所在月份的最后一天日期
	 */
	public static Date getMonthLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		// Date转Calendar
		calendar.setTime(date);
		// Calendar.DATE和Calendar.DAY_OF_MONTH效果相同，使用哪个都行
		// getActualMaximum方法获取日期最大值，这里是获取指定月份的日期最大值
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		// Calendar转Date
		return calendar.getTime();
	}

	/**
	 * description: 获取指定日期所在年份的第一天日期
	 * @author laiwen
	 * @date 2022-04-28 10:27:11
	 * @param date 指定日期
	 * @return 返回指定日期所在年份的第一天日期
	 */
	public static Date getYearFirstDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		// Date转Calendar
		calendar.setTime(date);
		// getActualMinimum方法获取日期最小值，这里是获取指定年份的日期最小值
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMinimum(Calendar.DAY_OF_YEAR));
		// Calendar转Date
		return calendar.getTime();
	}

	/**
	 * description: 获取指定日期所在年份的最后一天日期
	 * @author laiwen
	 * @date 2022-04-28 10:27:17
	 * @param date 指定日期
	 * @return 返回指定日期所在年份的最后一天日期
	 */
	public static Date getYearLastDay(Date date) {
		Calendar calendar = Calendar.getInstance();
		// Date转Calendar
		calendar.setTime(date);
		// getActualMaximum方法获取日期最大值，这里是获取指定年份的日期最大值
		calendar.set(Calendar.DAY_OF_YEAR, calendar.getActualMaximum(Calendar.DAY_OF_YEAR));
		// Calendar转Date
		return calendar.getTime();
	}

	/**
	 * description: 时间戳转时间
	 * @author laiwen
	 * @date 2022-12-03 21:14:48
	 * @param timeStamp 时间戳
	 * @return 返回时间对象
	 */
	public static Date timeStampToDate(Long timeStamp) {
		Date date = new Date(timeStamp);
		String dateStr = dateToString(date);
		log.info("时间戳【" + timeStamp + "】转时间，结果：{}", dateStr);
		return date;
	}

	/**
	 * description: 时间转时间戳
	 * @author laiwen
	 * @date 2022-12-03 21:17:08
	 * @param date 时间对象
	 * @return 返回时间戳
	 */
	public static Long dateToTimeStamp(Date date) {
		Long timeStamp = date.getTime();
		String dateStr = dateToString(date);
		log.info("时间【" + dateStr + "】转时间戳，结果：{}", timeStamp);
		return timeStamp;
	}

	/**
	 * description: 时间对象LocalTime转字符串
	 * @author laiwen
	 * @date 2022-12-03 21:39:42
	 * @param localTime LocalTime时间对象
	 * @param regex 规则
	 * @return 返回时间字符串
	 */
	public static String localTimeToString(LocalTime localTime, String regex) {
		return localTime.format(DateTimeFormatter.ofPattern(regex));
	}

	/**
	 * description: 时间对象LocalTime转字符串（默认规则HH:mm:ss）
	 * @author laiwen
	 * @date 2022-12-03 21:39:42
	 * @param localTime LocalTime时间对象
	 * @return 返回时间字符串
	 */
	public static String localTimeToString(LocalTime localTime) {
		return localTimeToString(localTime, "HH:mm:ss");
	}

	/**
	 * description: 字符串转时间对象LocalTime
	 * @author laiwen
	 * @date 2022-12-03 21:39:48
	 * @param localTimeStr 时间字符串
	 * @param regex 规则
	 * @return 返回LocalTime时间对象
	 */
	public static LocalTime stringToLocalTime(String localTimeStr, String regex) {
		return LocalTime.parse(localTimeStr, DateTimeFormatter.ofPattern(regex));
	}

	/**
	 * description: 字符串转时间对象LocalTime（默认规则HH:mm:ss）
	 * @author laiwen
	 * @date 2022-12-03 21:39:48
	 * @param localTimeStr 时间字符串
	 * @return 返回LocalTime时间对象
	 */
	public static LocalTime stringToLocalTime(String localTimeStr) {
		return stringToLocalTime(localTimeStr, "HH:mm:ss");
	}

	public static Date addMinute(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(12, i);
		return calendar.getTime();
	}

	public static int getMinute(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(date.getTime());
		return calendar.get(Calendar.MINUTE);
	}

	public static boolean isFiveMinute(Date date) {
		try {
			return getMinute(date) % 5 == 0;
		} catch (Exception ignored) {
		}
		return false;
	}

	private static JSONObject fillInOtherTimePoints(Date date, int addMinute, JSONObject item) {
		Date datetime = addMinute(date, addMinute);
		JSONObject object = JSON.parseObject(JSON.toJSONString(item, SerializerFeature.WriteMapNullValue), JSONObject.class);
		object.put("datetime", datetime.getTime()/1000);
		return object;
	}

	public static void main(String[] args) {
		Date date = CalendarUtil.stringToDate("2023-12-11 00:15:00");
		JSONObject item = new JSONObject();
		item.put("domain" , "domain");
		item.put("datetime", date.getTime()/1000);
		item.put("statType", 1233);
		List<JSONObject> list = new ArrayList<>();
		list.add(item);
		if(isFiveMinute(date)) {
			list.add(fillInOtherTimePoints(date, 1, item));
			list.add(fillInOtherTimePoints(date, 2, item));
			list.add(fillInOtherTimePoints(date, 3, item));
			list.add(fillInOtherTimePoints(date, 4, item));
		}
		System.out.println(JSON.toJSONString(list));

	}
}
