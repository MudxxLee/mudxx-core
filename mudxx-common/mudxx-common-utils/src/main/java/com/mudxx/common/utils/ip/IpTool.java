package com.mudxx.common.utils.ip;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * description: IP工具类
 * @author laiwen
 * @date 2019-07-19 10:39:55
 */
@SuppressWarnings("ALL")
public class IpTool {

    /**
     * description: 检查IP是否合法
     * @author laiwen
     * @date 2019-07-19 10:42:12
     * @param ip IP地址
     * @return 如果IP地址合法返回true，否则返回false
     */
    public static boolean ipValid(String ip) {
        String regex0 = "(2[0-4]\\d)" + "|(25[0-5])";
        String regex1 = "1\\d{2}";
        String regex2 = "[1-9]\\d";
        String regex3 = "\\d";
        String regex = "(" + regex0 + ")|(" + regex1 + ")|(" + regex2 + ")|(" + regex3 + ")";
        regex = "(" + regex + ").(" + regex + ").(" + regex + ").(" + regex + ")";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(ip);
        return m.matches();
    }

    /**
     * description: 获取本地ip地址，适合windows与linux
     * @author laiwen
     * @date 2019-07-19 10:42:26
     * @return 返回本地ip地址
     */
    public static String getLocalIp() {
        String localIp = "127.0.0.1";
        try {
            Enumeration netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                InetAddress ip = ni.getInetAddresses().nextElement();
                if (!ip.isLoopbackAddress() && !ip.getHostAddress().contains(":")) {
                    localIp = ip.getHostAddress();
                    break;
                }
            }
        } catch (Exception e) {
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            }
        }
        return localIp;
    }

    /**
     * description: 获取客户端的ip地址
     * @author laiwen
     * @date 2019-07-19 10:43:02
     * @param request 请求对象
     * @return 返回客户端的ip地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("http_client_ip");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        // 如果是多级代理，那么取第一个ip为客户ip
        if (ip != null && ip.contains(",")) {
            ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
        }
        return ip;
    }

    /**
     * description: 把ip转化为整数
     * @author laiwen
     * @date 2019-07-19 10:43:39
     * @param ip IP地址
     * @return 返回ip转化后的整数
     */
    public static Integer translateIp2Int(String ip) {
        String[] intArr = ip.split("\\.");
        Integer[] ipInt = new Integer[intArr.length];
        for (Integer i = 0; i < intArr.length; i++) {
            ipInt[i] = Integer.valueOf(intArr[i]);
        }
        return ipInt[0] * 256 * 256 * 256 + +ipInt[1] * 256 * 256 + ipInt[2] * 256 + ipInt[3];
    }

}
