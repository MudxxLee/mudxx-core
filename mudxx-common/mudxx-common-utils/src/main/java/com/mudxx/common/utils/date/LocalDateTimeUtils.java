package com.mudxx.common.utils.date;

import org.apache.commons.lang3.ObjectUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;

/**
 * @author laiw
 * @date 2023/12/6 14:30
 */
public class LocalDateTimeUtils {

    public static final ZoneId Z_UTC = ZoneId.of("UTC");
    public static final ZoneId Z_BEIJING = ZoneId.of("Asia/Shanghai");

    public static LocalDateTime of(Instant instant, ZoneId zoneId) {
        return null == instant ? null : LocalDateTime.ofInstant(instant,
                ObjectUtils.defaultIfNull(zoneId, ZoneId.systemDefault()));
    }

    public static LocalDateTime ofUtc(long epochMillis) {
        return of(Instant.ofEpochMilli(epochMillis), Z_UTC);
    }

    public static LocalDateTime ofBeijing(long epochMillis) {
        return of(Instant.ofEpochMilli(epochMillis), Z_BEIJING);
    }

    public static long getEpochSecond(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant().getEpochSecond();
    }

    public static long toEpochMillis(LocalDateTime localDateTime, ZoneId zoneId) {
        return localDateTime.atZone(zoneId).toInstant().toEpochMilli();
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     * yyyy-MM-dd'T'HH:mm:ss'Z'
     * yyyy-MM-dd'T'HH:mm:ss
     */
    public static LocalDateTime parseUtc(String utcStr, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(utcStr, formatter);
    }

    /**
     * yyyy-MM-dd'T'HH:mm:ss.SSS'Z'
     * yyyy-MM-dd'T'HH:mm:ss.SSS
     * yyyy-MM-dd'T'HH:mm:ss'Z'
     * yyyy-MM-dd'T'HH:mm:ss
     */
    public static String formatUtc(LocalDateTime localDateTime, String pattern) {
        TimeZone utc = TimeZone.getTimeZone("UTC");
        DateFormat dft = new SimpleDateFormat(pattern);
        dft.setTimeZone(utc);
        return dft.format(Date.from(localDateTime.atZone(Z_UTC).toInstant()));
    }

    public static String formatUtcSssZ(LocalDateTime localDateTime) {
        return formatUtc(localDateTime, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public static String formatBeijing(LocalDateTime localDateTime, String pattern) {
        TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");
        DateFormat dft = new SimpleDateFormat(pattern);
        dft.setTimeZone(timeZone);
        return dft.format(Date.from(localDateTime.atZone(Z_BEIJING).toInstant()));
    }

    public static String formatBeijingNormal(LocalDateTime localDateTime) {
        return formatBeijing(localDateTime, "yyyy-MM-dd HH:mm:ss");
    }

    public static void main(String[] args) {
        LocalDateTime localDateTime = parseUtc("2024-01-16T09:50:00Z", "yyyy-MM-dd'T'HH:mm:ss'Z'");
        System.out.println(getEpochSecond(localDateTime, Z_UTC));
        System.out.println(formatUtcSssZ(localDateTime));

        LocalDateTime ofBeijing = ofBeijing(toEpochMillis(localDateTime, Z_UTC));
        System.out.println(getEpochSecond(ofBeijing, Z_BEIJING));
        System.out.println(formatBeijingNormal(ofBeijing));

    }

}

