package com.mudxx.common.thread.pool.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author laiwen
 */
public class ExecutorTaskUtils {

    public static int maxJoin(int total, int pageSize) {
        if (total % pageSize == 0) {
            return total / pageSize;
        }
        return (int) (Math.ceil(total / pageSize) + 1);
    }

    /**
     * 把oList拆分成n个长度为size的list集合 主要用于sql语句in语句组合条件 因为sql语句in不能超过1000个
     *
     * @param oList 源list
     * @param len   子list大小
     */
    public static <T> List<Collection<T>> split(Collection<T> oList, int len) {
        List<Collection<T>> tempList = new ArrayList<>();
        if (ObjectUtils.isEmpty(oList)) {
            return tempList;
        }
        if (len < 1 || oList.size() <= len) {
            tempList.add(oList);
            return tempList;
        }
        List<T> list = new ArrayList<>();
        for (T id : oList) {
            list.add(id);
            if (list.size() == len) {
                tempList.add(list);
                list = new ArrayList<>();
            }
        }
        if (list.size() > 0) {
            tempList.add(list);
        }
        return tempList;
    }


    public static void main(String[] args) {
//        Set<String> list = new HashSet<>();
        List<String> list = new ArrayList<>();
        list.add("1");
        list.add("11");
        list.add("12");
        list.add("13");
        list.add("14");
        list.add("15");
        list.add("16");
        System.out.println("list:" + list);

        List<Collection<String>> lists = split(list, 2);
        System.out.println(lists);
        System.out.println(lists.size());
        for (Collection<String> iList : lists) {
            System.out.println(lists.indexOf(iList));
        }
    }

}

