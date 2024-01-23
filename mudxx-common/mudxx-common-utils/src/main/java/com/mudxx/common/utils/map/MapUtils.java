package com.mudxx.common.utils.map;

import com.mudxx.common.utils.json.JsonStrUtil;
import com.mudxx.common.utils.json.JsonUtil;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author laiwen
 * @description Map工具类
 * @date 2018年5月23日 上午11:12:21
 */
@SuppressWarnings("ALL")
public class MapUtils {

    /**
     * @param mapString map调用toString()方法转换成的字符串。或者实体类复写的equals方法转换成的map字符串格式。
     * @return 字符串数组，这是个对象，如果想获得字符串，需要使用Arrays.toString()方法转换。
     * @description map字符串转数组
     * 比如{id=1, username=张三, birthday=Sun Jul 30 19:05:09 CST 2017, sex=1, address=苏州}
     * 转成[id=1, username=张三, birthday=Sun Jul 30 19:05:09 CST 2017, sex=1, address=苏州]
     */
    public static String[] mapStringToArray(String mapString) {
        String subMapString = mapString.substring(1, mapString.length() - 1);
        return subMapString.split(", ");
    }

    /**
     * 说明：这里我使用LinkedHashMap，保证转换后键值对顺序不变。
     *
     * @param mapString map字符串转Map对象
     * @return 返回LinkedHashMap对象
     * @description map字符串转map对象
     */
    public static Map<String, String> mapStringToMap(String mapString) {
        String[] keyEqValueArray = mapStringToArray(mapString);
        Map<String, String> map = new LinkedHashMap<>(16);
        for (String keyEqValue : keyEqValueArray) {
            String[] keyValue = keyEqValue.split("=");
            String key = keyValue[0];
            String value = keyValue[1];
            map.put(key, value);
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String, String> string = mapStringToMap("{gmt_create=2024-01-15 14:28:26, charset=UTF-8, gmt_payment=2024-01-15 14:28:38, notify_time=2024-01-15 14:28:39, subject=agilewing, sign=UohRLgIFWjx+DiyT8mpY+RqzwvfXc24HFcSoB4bSuuexx5pb6Z+GoRpZoqhmceyXgXpIaPxpudJ3xAeYzLIVGWQn5F4Y7MkQOYu6i8dV9fhMQzmBDyv0+SjbswfumJvD1XHMg+iz7tYZCI/8Lrc16c4PVD0+SaEWLlz9Mi1T054+jA81pQ2Y96r74phr0pQVcNobGueVJDCyFrs5irYSP9dm7schCAUPAJsjni4tlZWJZgiwE7mU4iG03+N1dsQpafjFvPdlg/jsuGCfW4sS7lovzRXA6KWwVJmNrFz7dWVqhAQjDNYyFUB6cqqxLjHoTR2IBD0ID1Eglp+8LK+CJQ==, buyer_id=2088721025368419, invoice_amount=0.05, version=1.0, notify_id=2024011501222142839168410501875020, fund_bill_list=[{\"amount\":\"0.05\",\"fundChannel\":\"ALIPAYACCOUNT\"}], notify_type=trade_status_sync, out_trade_no=P-11051E00072903, total_amount=0.05, trade_status=TRADE_SUCCESS, trade_no=2024011522001468410501821436, auth_app_id=9021000133698136, receipt_amount=0.05, point_amount=0.00, buyer_pay_amount=0.05, app_id=9021000133698136, sign_type=RSA2, seller_id=2088721028138804}");
        System.out.println(JsonStrUtil.toJsonStrMelody(string));
    }
}
