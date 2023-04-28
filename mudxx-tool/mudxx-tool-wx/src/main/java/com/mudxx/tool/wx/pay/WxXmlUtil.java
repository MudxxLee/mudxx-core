package com.mudxx.tool.wx.pay;

import com.mudxx.common.utils.empty.EmptyUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * description: 微信XML解析工具类
 * @author laiwen
 * @date 2019-12-05 16:58:51
 */
@SuppressWarnings("ALL")
public class WxXmlUtil {

    private static final Logger log = LoggerFactory.getLogger(WxXmlUtil.class);

    /**
     * description: XML数据解析成Map（会进行签名校验）
     * @author laiwen
     * @date 2019-10-23 09:51:25
     * @param xmlStr xml格式的字符串
     * @return 返回TreeMap对象
     */
    public static SortedMap<String, Object> xmlParse(String xmlStr) {
        log.info("XML原始待解析数据：{}", xmlStr);
        xmlStr = xmlStr.replaceFirst("encoding=\".*\"", "encoding=\"UTF-8\"");
        log.info("XML正常待解析数据：{}", xmlStr);
        // TreeMap默认字母序
        SortedMap<String, Object> map = new TreeMap<>();
        if (EmptyUtil.isEmpty(xmlStr)) {
            return map;
        }
        try {
            InputStream in = new ByteArrayInputStream(xmlStr.getBytes("UTF-8"));
            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            for (Object aList : list) {
                Element e = (Element) aList;
                String k = e.getName();
                String v;
                List children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }
                map.put(k, v);
            }
            //关闭流
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("XML解析结果：{}", map);
        return map;
    }

    /**
     * description: 获取子结点的XML
     * @author laiwen
     * @date 2019-10-23 09:52:35
     * @param children 子节点列表
     * @return 返回子结点的xml
     */
    private static String getChildrenText(List children) {
        StringBuilder sb = new StringBuilder();
        if (EmptyUtil.isNotEmpty(children)) {
            for (Object aChildren : children) {
                Element e = (Element) aChildren;
                String name = e.getName();
                String value = e.getTextNormalize();
                sb.append("<").append(name).append(">");
                List list = e.getChildren();
                if (EmptyUtil.isNotEmpty(list)) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</").append(name).append(">");
            }
        }
        return sb.toString();
    }

    /**
     * description: 将请求参数封装的Map对象转换成XML字符串
     * @author laiwen
     * @date 2019-12-05 17:55:07
     * @param paramsMap 参数封装对象
     * @return 返回XML字符串
     */
    public static String paramsMapToXml(SortedMap<String, Object> paramsMap) {
        log.info("待转换成XML的请求参数：{}", paramsMap);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("<xml>");
        Set<Map.Entry<String, Object>> entries = paramsMap.entrySet();
        for (Map.Entry<String, Object> entry : entries) {
            String k = entry.getKey();
            Object v = entry.getValue();
            if ("attach".equalsIgnoreCase(k) || "body".equalsIgnoreCase(k) || "sign".equalsIgnoreCase(k)) {
                stringBuilder.append("<").append(k).append(">").append("<![CDATA[").append(v).append("]]></").append(k).append(">");
            } else {
                stringBuilder.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
            }
        }
        stringBuilder.append("</xml>");
        String xmlStr = stringBuilder.toString();
        log.info("生成的XML数据：{}", xmlStr);
        return xmlStr;
    }

}
