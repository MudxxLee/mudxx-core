package com.mudxx.common.utils.session;

import com.mudxx.common.utils.empty.EmptyUtil;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * description：session相关操作工具类
 * @author laiwen
 * @date 2019-10-11 18:21
 */
@SuppressWarnings("ALL")
public class SessionUtil {

    /**
     * description：根据session的key获取session的value
     * user laiwen
     * time 2019-10-11 18:27
     * @param request HTTP请求对象
     * @param name session的key
     * @return 返回session的value
     */
    public static Object getSessionAttribute(HttpServletRequest request, String name) {
        Assert.notNull(request, "request不能为空！");
        //如果当前request里面没有session，那么session就为null，不会进行创建
        HttpSession session = request.getSession(false);
        Object value = null;
        if (EmptyUtil.isNotEmpty(session)) { //如果session不为空
            value = session.getAttribute(name); //获取name对应的value值
        }
        return value;
    }

    /**
     * description：往session里面设置键值对
     * user laiwen
     * time 2019-10-11 18:27
     * @param request HTTP请求对象
     * @param name session的key
     * @param value session的value
     */
    public static void setSessionAttribute(HttpServletRequest request, String name, Object value) {
        if (EmptyUtil.isNotEmpty(value)) { //如果value不为空
            //往session里面设置键值对（如果request里面session不存在，则创建session）
            request.getSession().setAttribute(name, value);
        } else { //如果value为空
            //如果当前request里面没有session，那么session就为null，不会进行创建
            HttpSession session = request.getSession(false);
            if (EmptyUtil.isNotEmpty(session)) { //如果session不为空
                session.removeAttribute(name); //从session里面移除key为name对应的键值对
            }
        }
    }

}
