package com.mudxx.common.utils.xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能：通过属性文件处理XML报文
 *
 * @author laiwen
 * @version 1.0
 * @date 2017-6-5
 */
public class ParseXmlByPropertyFile {
    /**
     * 将多结点多层级的Map转为多包体的list集合
     *
     * @param respStr
     * @param listNode
     * @return
     */
    public static List<Map<String, Object>> map2ListbyHandler(Map respStr, String listNode) {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map map = respStr;
        String[] strArray = listNode.split(">");
        String key = null;
        Object value = null;
        for (int i = 0; i < strArray.length; i++) {
            if (map.get(strArray[i]) instanceof List) {
                if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
                    Map<String, Object> mapList = map;
                    if (((List<Map<String, Object>>) mapList.get(strArray[i])).size() > 0) {
                        for (int j = 0; j < ((List<Map<String, Object>>) mapList.get(strArray[i])).size(); j++) {
                            Map mapj = new HashMap();
                            map = ((List<Map<String, Object>>) mapList.get(strArray[i])).get(j);
                            mapj.put(strArray[i + 1], map.get(strArray[i + 1]) + "");//listNode
                            resultList.add(mapj);
                        }
                    }
                }
                break;
            } else {
                if (i < strArray.length - 1 && null != map.get(strArray[i]) && "" != map.get(strArray[i])) {
                    map = (Map) map.get(strArray[i]);
                } else {
                    Map<String, Object> resultMap = new HashMap<String, Object>();
                    resultMap.put(strArray[i], map.get(strArray[i]) + "");//listNode
                    resultList.add(resultMap);
                }
            }
        }
        System.out.println("resultList >>> " + resultList);
        return resultList;
    }

    /**
     * 根据节点的list集合将xml报文组装成多包体list
     *
     * @param nodeList
     * @param respXml
     * @return
     */
    public static List<Map<String, Object>> str2ListbyHandler(List<String> nodeList, String respXml) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        Map<String, Object> commonMap = new HashMap<String, Object>();
        int flag = 0;
        try {
            for (String node : nodeList) {
                SAXReader saxReader = new SAXReader();
                Document document = saxReader.read(new ByteArrayInputStream(respXml.getBytes()));
                Element incomingForm = document.getRootElement();
                Map map = ParseXmlUtils.xmlElement2Map(incomingForm);
                List<Map<String, Object>> thirdList = map2ListbyHandler(map, node);
                List<Map<String, Object>> listGroup = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> maxList = new ArrayList<Map<String, Object>>();
                List<Map<String, Object>> minList = new ArrayList<Map<String, Object>>();
                //----------------------------------
                if (thirdList.size() > 0 && thirdList.size() < 2) {//公共参数
                    commonMap.putAll(thirdList.get(0));
                } else {//某节点下为list集合;比较list集合的大小
                    if (flag == 0) {
                        resultList = thirdList;
                    } else {
                        if (resultList.size() < thirdList.size()) {
                            maxList = thirdList;
                            minList = resultList;
                        } else {
                            maxList = resultList;
                            minList = thirdList;
                        }
                        for (int i = 0; i < maxList.size(); i++) {
                            for (int j = 0; j < minList.size(); j++) {
                                Map<String, Object> mapGroup = new HashMap<String, Object>();
                                mapGroup.putAll(thirdList.get(i));
                                mapGroup.putAll(resultList.get(j));
                                listGroup.add(mapGroup);
                                System.out.println("listGroup >> " + listGroup);
                            }
                        }
                        resultList = listGroup;
                    }
                    flag++;
                }
                //-----------------------
            }
            if (resultList.size() == 0) {
                resultList.add(commonMap);
            } else {
                for (int i = 0; i < resultList.size(); i++) {
                    resultList.get(i).putAll(commonMap);
                }
            }
        } catch (Exception e) {
        }
        System.out.println("resultList >> " + resultList);
        return resultList;
    }

    public static void main(String[] args) {
        StringBuffer sb = new StringBuffer();
        sb.append("<response>");
        sb.append(" <resultCode>0</resultCode>");
        sb.append(" <resultMsg>成功</resultMsg>");
        sb.append(" <orderOffer>");
        sb.append("     <offer>");
        sb.append("         <offerId>105000808918</offerId>");
        sb.append("         <offerSpecId>980010992</offerSpecId>");
        sb.append("         <offerSpecName>980010992)商务助手（OCS）30元</offerSpecName>");
        sb.append("         <startDt>2012-11-27 16:48:12</startDt>");
        sb.append("         <endDt>3000-1-1 0:00:00</endDt>");
        sb.append("         <params>");
        sb.append("             <param>");
        sb.append("                 <offerParamId>101000048281</offerParamId>");
        sb.append("                 <itemSpecId>5030</itemSpecId>");
        sb.append("                 <itemSpecName>计费区分</itemSpecName>");
        sb.append("                 <value>CDMA预付费</value>");
        sb.append("             </param>");
        sb.append("             <param>");
        sb.append("                 <offerParamId>101000048282</offerParamId>");
        sb.append("                 <itemSpecId>5031</itemSpecId>");
        sb.append("                 <itemSpecName>经分区分</itemSpecName>");
        sb.append("                 <value>天翼商话</value>");
        sb.append("             </param>");
        sb.append("         </params>");
        sb.append("     </offer>");
        sb.append(" </orderOffer>");
        sb.append(" <saleOffer>");
        sb.append("     <categoryNode id = \"100472\">");
        sb.append("         <offer>");
        sb.append("             <id>980001995</id>");
        sb.append("             <name>(980001995)商务助手（OCS）201209版-30元</name>");
        sb.append("             <summary>待定...</summary>");
        sb.append("         </offer>");
        sb.append("         <offer>");
        sb.append("             <id>980001996</id>");
        sb.append("             <name>(980001996)商务助手（OCS）201209版-50元</name>");
        sb.append("             <summary>待定...</summary>");
        sb.append("         </offer>");
        sb.append("         <offer>");
        sb.append("             <id>980001997</id>");
        sb.append("             <name>(980001997)商务助手（OCS）201209版-80元</name>");
        sb.append("             <summary>待定...</summary>");
        sb.append("         </offer>");
        sb.append("         <offer>");
        sb.append("             <id>980010993</id>");
        sb.append("             <name>(980010993)商务助手（OCS）50元</name>");
        sb.append("             <summary>待定...</summary>");
        sb.append("         </offer>");
        sb.append("     </categoryNode>");
        sb.append(" </saleOffer>");
        sb.append("</response>");
        String respXml = sb.toString();
        try {
            //xml转map
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(new ByteArrayInputStream(respXml.getBytes()));
            Element incomingForm = document.getRootElement();
            Map map = ParseXmlUtils.xmlElement2Map(incomingForm);
            System.out.println("map >>> " + map);
            /*
             * {resultMsg=成功, orderOffer={offer={offerSpecName=980010992)商务助手（OCS）30元, endDt=3000-1-1 0:00:00, startDt=2012-11-27 16:48:12, offerSpecId=980010992, params={param=[{offerParamId=101000048281, itemSpecName=计费区分, itemSpecId=5030, value=CDMA预付费}, {offerParamId=101000048282, itemSpecName=经分区分, itemSpecId=5031, value=天翼商话}]}, offerId=105000808918}}, resultCode=0, saleOffer={categoryNode={offer=[{summary=待定..., id=980001995, name=(980001995)商务助手（OCS）201209版-30元}, {summary=待定..., id=980001996, name=(980001996)商务助手（OCS）201209版-50元}, {summary=待定..., id=980001997, name=(980001997)商务助手（OCS）201209版-80元}, {summary=待定..., id=980010993, name=(980010993)商务助手（OCS）50元}]}}}
             */
            System.out.println("***************从map中将单个节点的值查找出来*********************");
            //map转list
            map2ListbyHandler(map, "saleOffer>categoryNode>offer>id");
            /*
             * resultList >>> [{id=980001995}, {id=980001996}, {id=980001997}, {id=980010993}]
             */
            System.out.println("***************从map中将节点集合中的所有节点值都查找出来*******************************");
            List<String> nodeList = new ArrayList<String>();
            nodeList.add("resultCode");
            nodeList.add("resultMsg");
            nodeList.add("orderOffer>offer>offerSpecName");
            nodeList.add("orderOffer>offer>endDt");
            nodeList.add("orderOffer>offer>startDt");
            nodeList.add("orderOffer>offer>offerSpecId");
            nodeList.add("orderOffer>offer>offerId");
            nodeList.add("orderOffer>offer>params>param>offerParamId");
            nodeList.add("orderOffer>offer>params>param>itemSpecName");
            nodeList.add("orderOffer>offer>params>param>itemSpecId");
            nodeList.add("orderOffer>offer>params>param>value");
            nodeList.add("saleOffer>categoryNode>offer>id");
            nodeList.add("saleOffer>categoryNode>offer>summary");
            nodeList.add("saleOffer>categoryNode>offer>name");
            str2ListbyHandler(nodeList, respXml);
            /**
             * [{offerParamId=101000048281, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001995, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048282, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001995, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048281, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001996, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048282, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001996, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048281, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001997, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048282, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980001997, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *       name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048281, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980010993, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918},
             * {offerParamId=101000048282, summary=待定..., endDt=3000-1-1 0:00:00, itemSpecId=5031, id=980010993, resultMsg=成功,
             *      itemSpecName=经分区分, offerSpecName=980010992)商务助手（OCS）30元, offerSpecId=980010992, startDt=2012-11-27 16:48:12,
             *      name=(980010993)商务助手（OCS）50元, resultCode=0, value=天翼商话, offerId=105000808918}]
             */
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}