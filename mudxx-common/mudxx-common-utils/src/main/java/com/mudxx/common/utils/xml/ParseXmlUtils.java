package com.mudxx.common.utils.xml;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.*;

/**
 * 功能：XML解析工具类
 *
 * @author laiwen
 * @version 1.0
 * @date 2017-6-5
 */
public class ParseXmlUtils {

    /**
     * string转为Map（String→Document→Map）
     *
     * @param xmlData
     * @return
     */
    public static Map<String, Object> xmlData2Map(String xmlData) {
        try {
            if (StringUtils.isBlank(xmlData)) {
                return null;
            }
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new ByteArrayInputStream(xmlData.getBytes()));
            if (document == null) {
                return null;
            }
            return xmlDocument2Map(document);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将Document对象转为Map（String→Document→Map）
     *
     * @param doc
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Map<String, Object> xmlDocument2Map(Document doc) {
        Map<String, Object> map = new HashMap<>();
        Element root = doc.getRootElement();
        for (Iterator iterator = root.elementIterator(); iterator.hasNext(); ) {
            Element e = (Element) iterator.next();
            List list = e.elements();
            if (list.size() > 0) {
                map.put(e.getName(), xmlElement2Map(e));
            } else {
                map.put(e.getName(), e.getText());
            }
        }
        return map;
    }

    /**
     * 将Element对象转为Map（String→Document→Element→Map）
     *
     * @param e
     * @return
     */
    public static Map<String, Object> xmlElement2Map(Element e) {
        Map<String, Object> map = new HashMap<>();
        List<Element> list = e.elements();
        if (list.size() > 0) {
            for (Element iter : list) {
                List<Object> mapList = new ArrayList<>();
                if (iter.elements().size() > 0) {
                    Map<String, Object> m = xmlElement2Map(iter);
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!"java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = new ArrayList<>();
                            mapList.add(obj);
                            mapList.add(m);
                        }
                        if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = (List) obj;
                            mapList.add(m);
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        map.put(iter.getName(), m);
                    }
                } else {
                    if (map.get(iter.getName()) != null) {
                        Object obj = map.get(iter.getName());
                        if (!"java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = new ArrayList<>();
                            mapList.add(obj);
                            mapList.add(iter.getText());
                        }
                        if ("java.util.ArrayList".equals(obj.getClass().getName())) {
                            mapList = (List) obj;
                            mapList.add(iter.getText());
                        }
                        map.put(iter.getName(), mapList);
                    } else {
                        //公共map resultCode=0
                        map.put(iter.getName(), iter.getText());
                    }
                }
            }
        } else {
            map.put(e.getName(), e.getText());
        }
        return map;
    }

    public static void main(String[] args) {
        String str1 = "<HEADER>" +
                "       <POOL_ID>2</POOL_ID>" +
                "       <DB_ID>EUR</DB_ID>" +
                "       <CHANNEL_ID>11</CHANNEL_ID>" +
                "       <USERNAME>tom</USERNAME>" +
                "       <PASSWORD>sss</PASSWORD>" +
                "   </HEADER>";
        String str2 = "<ROOT>" +
                "  <HEADER>" +
                "      <POOL_ID>2</POOL_ID>" +
                "      <CHANNEL_ID>11</CHANNEL_ID>" +
                "      <USERNAME>tom</USERNAME>" +
                "      <PASSWORD>sss</PASSWORD>" +
                "  </HEADER>" +
                "  <BODY>" +
                "      <BUSLIST>" +
                "          <PHONE_NO>7107300212</PHONE_NO>" +
                "          <TRACE_ID>97D2C7D26224A2DAE9A1CB501E60F395</TRACE_ID>" +
                "          <TENANT_ID>EUR</TENANT_ID>" +
                "          <LANG>zh_CN</LANG>" +
                "      </BUSLIST>" +
                "      <BUSLIST>" +
                "          <PHONE_NO>2222300212</PHONE_NO>" +
                "          <TRACE_ID>444424A2DAE9A1CB501E60F395</TRACE_ID>" +
                "          <TENANT_ID>USA</TENANT_ID>" +
                "          <LANG>zh_CN</LANG>" +
                "      </BUSLIST>" +
                "  </BODY>" +
                "</ROOT>";

        Map<String, Object> xmlData2Map = xmlData2Map(str1);
        System.out.println("map>>> " + xmlData2Map);
        /*
        {DB_ID=EUR, CHANNEL_ID=11, USERNAME=tom, PASSWORD=sss, POOL_ID=2}
        {BODY={BUSLIST=[{TRACE_ID=97D2C7D26224A2DAE9A1CB501E60F395, PHONE_NO=7107300212, LANG=zh_CN, TENANT_ID=EUR}, {TRACE_ID=444424A2DAE9A1CB501E60F395, PHONE_NO=2222300212, LANG=zh_CN, TENANT_ID=USA}]}, HEADER={CHANNEL_ID=11, USERNAME=tom, PASSWORD=sss, POOL_ID=2}}
        */
    }
}
