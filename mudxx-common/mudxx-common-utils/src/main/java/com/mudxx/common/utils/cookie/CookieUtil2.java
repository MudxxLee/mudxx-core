package com.mudxx.common.utils.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * description: cookie相关工具类2（适合tomcat8.5版本及以上的情况，包含tomcat8.5版本）
 * @author laiwen
 * @date 2019-07-19 16:23:14
 */
@SuppressWarnings("ALL")
public class CookieUtil2 {

    /**
     * description: 得到Cookie的值（因为没有进行编码所以不需要进行解码）
     * @author laiwen
     * @date 2019-07-19 16:25:47
     * @param request 请求对象
     * @param cookieName cookie名称
     * @return 返回cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName) {
        return getCookieValue(request, cookieName, false);
    }

    /**
     * description: 得到Cookie的值
     * @author laiwen
     * @date 2019-07-19 16:26:58
     * @param request 请求对象
     * @param cookieName cookie名称
     * @param isDecoder 是否需要进行解码，true表示需要解码（默认UTF-8进行解码），false表示不需要解码
     * @return 返回cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, boolean isDecoder) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie aCookieList : cookieList) {
                if (aCookieList.getName().equals(cookieName)) {
                    if (isDecoder) {
                        retValue = URLDecoder.decode(aCookieList.getValue(), "UTF-8");
                    } else {
                        retValue = aCookieList.getValue();
                    }
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * description: 得到Cookie的值（因为有进行编码所以需要进行解码，解码方式根据实际编码方式选择）
     * @author laiwen
     * @date 2019-07-19 16:31:10
     * @param request 请求对象
     * @param cookieName cookie名称
     * @param encodeString 编码方式
     * @return 返回cookie的值
     */
    public static String getCookieValue(HttpServletRequest request, String cookieName, String encodeString) {
        Cookie[] cookieList = request.getCookies();
        if (cookieList == null || cookieName == null) {
            return null;
        }
        String retValue = null;
        try {
            for (Cookie aCookieList : cookieList) {
                if (aCookieList.getName().equals(cookieName)) {
                    retValue = URLDecoder.decode(aCookieList.getValue(), encodeString);
                    break;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return retValue;
    }

    /**
     * description: 设置Cookie的值（不设置生效时间默认浏览器关闭即失效）（不进行编码）
     * @author laiwen
     * @date 2019-07-19 16:33:32
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue) {
        setCookie(request, response, cookieName, cookieValue, -1);
    }

    /**
     * description: 设置Cookie的值（设置生效时间）（不进行编码）
     * @author laiwen
     * @date 2019-07-19 16:37:16
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param cookieMaxAge 生效时间（单位秒）
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, int cookieMaxAge) {
        setCookie(request, response, cookieName, cookieValue, cookieMaxAge, false);
    }

    /**
     * description: 设置Cookie的值（不设置生效时间默认浏览器关闭即失效）（进行编码，默认UTF-8编码）
     * @author laiwen
     * @date 2019-07-19 16:40:27
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param isEncode 是否进行编码：true表示进行编码（默认UTF-8进行编码），false则表示不进行编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, boolean isEncode) {
        setCookie(request, response, cookieName, cookieValue, -1, isEncode);
    }

    /**
     * description: 设置Cookie的值（设置生效时间）（进行编码，默认UTF-8编码）
     * @author laiwen
     * @date 2019-07-19 16:44:35
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param cookieMaxAge 生效时间（单位秒）
     * @param isEncode 是否进行编码：true表示进行编码（默认UTF-8进行编码），false则表示不进行编码
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, int cookieMaxAge, boolean isEncode) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, isEncode);
    }

    /**
     * description: 设置Cookie的值（设置生效时间）（根据指定编码方式进行编码）
     * @author laiwen
     * @date 2019-07-19 16:52:33
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param cookieMaxAge 生效时间（单位秒）
     * @param encodeString 指定编码方式
     */
    public static void setCookie(HttpServletRequest request, HttpServletResponse response,
                                 String cookieName, String cookieValue, int cookieMaxAge, String encodeString) {
        doSetCookie(request, response, cookieName, cookieValue, cookieMaxAge, encodeString);
    }

    /**
     * description: 删除cookie（将cookie的值设置为空）（当浏览器关闭时自动删除）
     * @author laiwen
     * @date 2019-07-19 18:16:25
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     */
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        //setMaxAge方法设置cookie经过多长秒后被删除。如果参数是0，就说明立即删除。如果是负数就表明当浏览器关闭时自动删除。
        doSetCookie(request, response, cookieName, "", -1, false);
    }

    /**
     * description: 设置cookie的值（设置生效时间）（进行编码，默认UTF-8编码）
     * @author laiwen
     * @date 2019-07-19 17:02:51
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param cookieMaxAge 生效时间（单位秒）
     * @param isEncode 是否进行编码：true表示进行编码（默认UTF-8进行编码），false则表示不进行编码
     */
    private static void doSetCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String cookieName, String cookieValue,
                                    int cookieMaxAge, boolean isEncode) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else if (isEncode) {
                //根据utf-8进行编码
                cookieValue = URLEncoder.encode(cookieValue, "utf-8");
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            //设置生效时间（如果不设置，默认关闭浏览器cookie失效）
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            //设置域名
            if (null != request) {
                String domainName = getDomainName(request);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            //在同一应用服务器内共享
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * description: 设置cookie的值（设置生效时间）（根据指定编码方式进行编码）
     * @author laiwen
     * @date 2019-07-19 16:59:59
     * @param request 请求对象
     * @param response 响应对象
     * @param cookieName cookie名称
     * @param cookieValue cookie值
     * @param cookieMaxAge 生效时间（单位秒）
     * @param encodeString 指定编码方式
     */
    private static void doSetCookie(HttpServletRequest request,
                                    HttpServletResponse response,
                                    String cookieName, String cookieValue,
                                    int cookieMaxAge, String encodeString) {
        try {
            if (cookieValue == null) {
                cookieValue = "";
            } else {
                //根据指定编码方式进行编码
                cookieValue = URLEncoder.encode(cookieValue, encodeString);
            }
            Cookie cookie = new Cookie(cookieName, cookieValue);
            //设置生效时间（如果不设置，默认关闭浏览器cookie失效）
            if (cookieMaxAge > 0) {
                cookie.setMaxAge(cookieMaxAge);
            }
            //设置域名
            if (null != request) {
                String domainName = getDomainName(request);
                if (!"localhost".equals(domainName)) {
                    cookie.setDomain(domainName);
                }
            }
            //在同一应用服务器内共享
            cookie.setPath("/");
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * description: 得到cookie的域名
     * @author laiwen
     * @date 2019-07-19 16:59:17
     * @param request 请求对象
     * @return 返回cookie的域名
     */
    private static String getDomainName(HttpServletRequest request) {
        String domainName;
        String serverName = request.getRequestURL().toString();
        if ("".equals(serverName)) {
            domainName = "";
        } else {
            serverName = serverName.toLowerCase();
            serverName = serverName.substring(7);
            final int end = serverName.indexOf("/");
            serverName = serverName.substring(0, end);
            final String[] domains = serverName.split("\\.");
            int len = domains.length;
            if (len > 3) {
                domainName = domains[len - 3] + "." + domains[len - 2] + "." + domains[len - 1];
            } else if (len > 1) {
                domainName = domains[len - 2] + "." + domains[len - 1];
            } else {
                domainName = serverName;
            }
        }
        if (domainName.indexOf(":") > 0) {
            String[] ary = domainName.split(":");
            domainName = ary[0];
        }
        return domainName;
    }

}
