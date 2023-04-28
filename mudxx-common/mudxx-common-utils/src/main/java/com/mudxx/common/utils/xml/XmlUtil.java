package com.mudxx.common.utils.xml;

import com.mudxx.common.utils.empty.EmptyUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description XML相关工具类
 * @author laiwen
 * @date 2019-06-23 20:19
 */
@SuppressWarnings("ALL")
public class XmlUtil {

    private static final Logger log = LoggerFactory.getLogger(XmlUtil.class);

    private static Map<String, Object> MAP;

    /**
     * description：解析XML格式的数据并将其设置到Map集合中
     * user laiwen
     * time 2019-06-23 20:20
     * @param xmlData xml格式的数据
     * @return 返回Map集合
     */
    public static Map<String, Object> xmlParse(String xmlData) {
        log.info("待解析的XML格式数据：{}", xmlData);
        try {
            // XML报文转成doc对象
            Document doc = DocumentHelper.parseText(xmlData);
            // 获取根元素，准备递归解析这个XML树
            Element parent = doc.getRootElement();
            // 初始化MAP集合
            MAP = new HashMap<>(16);
            // 递归遍历XML节点并往MAP集合里面插入元素
            traverseNode(parent);
            log.info("XML解析之后生成的Map数据：{}", MAP);
            return MAP;
        } catch (DocumentException e) {
            e.printStackTrace();
            log.error("【解析XML异常：{}】", e);
        }
        return null;
    }

    /**
     * description：递归遍历XML节点
     * user laiwen
     * time 2019-06-23 20:42
     * @param parent 节点对象
     */
    private static void traverseNode(Element parent) {
        if (EmptyUtil.isNotEmpty(parent.elements())) {
            // 如果当前根节点有子节点，找到子节点
            List<Element> list = parent.elements();
            // 遍历每个节点
            for (Element child : list) {
                if (child.elements().size() > 0) {
                    // 当前节点不为空的话，递归遍历子节点；
                    traverseNode(child);
                }
                if (child.elements().size() == 0) {
                    // 如果为叶子节点，那么直接把名字和值放入map
                    MAP.put(child.getName(), child.getTextTrim());
                }
            }
        }
    }

}
