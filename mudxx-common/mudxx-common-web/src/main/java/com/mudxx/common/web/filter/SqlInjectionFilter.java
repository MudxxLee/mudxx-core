package com.mudxx.common.web.filter;

import com.mudxx.common.exception.code.biz.ParameterErrorCode;
import com.mudxx.common.web.response.CommonResult;
import com.mudxx.common.web.util.WebUtils;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

/**
 * @author laiw
 * @date 2023/4/6 15:24
 */
@Component
public class SqlInjectionFilter implements Filter {

    private static final String BAD_STRING = "'|and|exec|execute|insert|select|delete|update|count|drop|*|%|chr|mid|master|truncate|" +
            "char|declare|sitename|net user|xp_cmdshell|;|or|-|+|,|like'|and|exec|execute|insert|create|drop|" +
            "table|from|grant|use|group_concat|column_name|" +
            "information_schema.columns|table_schema|union|where|select|delete|update|order|by|count|*|" +
            "chr|mid|master|truncate|char|declare|or|;|-|--|+|,|like|//|/|%|#";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse res = (HttpServletResponse)servletResponse;
        //获得所有请求参数名
        Enumeration<String> params = req.getParameterNames();
        StringBuilder sql = new StringBuilder();
        while (params.hasMoreElements()) {
            // 得到参数名
            String name = params.nextElement();
            // 得到参数对应值
            String[] value = req.getParameterValues(name);
            for (String s : value) {
                sql.append(s);
            }
        }
        if (sqlValidate(sql.toString())) {
            CommonResult<?> result = CommonResult.failed(ParameterErrorCode.ILLEGAL_CHARACTER);
            WebUtils.writeJsonToClient(res, result);
        } else {
            chain.doFilter(servletRequest,servletResponse);
        }
    }

    /**
     * 关键词校验
     * @param str 字符串
     * @return boolean
     */
    protected static boolean sqlValidate(String str) {
        // 统一转为小写
        str = str.toLowerCase();
        // 过滤掉的sql关键字，可以手动添加
        String[] badStrings = BAD_STRING.split("\\|");
        for (String badString : badStrings) {
            if (str.contains(badString)) {
                return true;
            }
        }
        return false;
    }
}

