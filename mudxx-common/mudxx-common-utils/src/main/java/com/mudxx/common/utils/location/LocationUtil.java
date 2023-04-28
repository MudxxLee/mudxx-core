package com.mudxx.common.utils.location;

/**
 * description: 地理位置工具类
 * @author laiwen
 * @date 2020-09-22 15:17:45
 */
@SuppressWarnings("ALL")
public class LocationUtil {

    /**
     * 地球半径（单位km）
     */
    private static final double EARTH_RADIUS = 6378.137;

    /**
     * 提示：PI在数学方法中为π，而此时的π在角度里为180°，Math.PI/180就为1°
     * description: 计算弧度
     * @author laiwen
     * @date 2020-09-22 10:14:21
     * @param d 角度
     * @return 返回根据角度计算出的弧度
     */
    public static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * description: 通过经纬度获取距离（单位m），不同的计算方式存在误差
     * @author laiwen
     * @date 2020-09-22 10:30:58
     * @param lat1 第一个点的纬度
     * @param lng1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lng2 第二个点的经度
     * @return 距离（单位m）
     */
    public static double getDistance(double lat1, double lng1, double lat2, double lng2) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = radLat1 - radLat2;
        double b = rad(lng1) - rad(lng2);
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000d) / 10000d;
        // km转换为m
        s = s * 1000;
        return s;
    }

}
