package com.mudxx.common.utils.date;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author laiw
 * @date 2023/12/6 14:30
 */
public class DateUtc {

    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        return null == instant ? null : LocalDateTime.ofInstant(instant,
                ObjectUtils.defaultIfNull(zoneId, ZoneId.systemDefault()));
    }

    public static LocalDateTime ofUtc(Instant instant) {
        return of(instant, ZoneId.of("UTC"));
    }

    public static LocalDateTime ofUtc(long epochMilli) {
        return ofUtc(Instant.ofEpochMilli(epochMilli));
    }

    public static LocalDateTime ofUtcSeconds(long epochSeconds) {
        return ofUtc(epochSeconds * 1000L);
    }

    public static long getEpochSecond(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("UTC")).toInstant().getEpochSecond();
    }

    public static long toEpochMilli(LocalDateTime localDateTime) {
        return localDateTime.atZone(ZoneId.of("UTC")).toInstant().toEpochMilli();
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     * yyyy-MM-dd'T'HH:mm:ss
     */
    public static String format(LocalDateTime localDateTime, String pattern) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        DateFormat dft = new SimpleDateFormat(pattern);
        dft.setTimeZone(utc);
        return dft.format(Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant()));
    }

    public static String formatSssZ(LocalDateTime localDateTime) {
        return format(localDateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public static void main(String[] args) {
//        System.out.println(ZoneId.systemDefault());
//        DateTime dateTime = DateUtil.parseUTC("2023-12-17T11:00:00Z");
//        Instant instant = Instant.ofEpochMilli(dateTime.getTime());
//        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
//        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zoneId);
//        System.out.println(localDateTime);

        ZoneId beijingZone = ZoneId.of("Asia/Shanghai");
        ZonedDateTime beijingTime = ZonedDateTime.now(beijingZone);
        LocalDateTime beijingLocalTime = beijingTime.toLocalDateTime();
        System.out.println("当前北京时间: " + beijingLocalTime.format(DateTimeFormatter.ofPattern(DatePattern.NORM_DATETIME_PATTERN)));

    }

}
