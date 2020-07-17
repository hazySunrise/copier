package com.jimi.mes_dbcopier.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 备份表和备份数量映射
 */
public class CountBox {

    private static final Map<String,Long> countMap = new HashMap<>();

    public static synchronized Long getCount(String table) {
        return countMap.get(table);
    }

    public static synchronized void add(String table, Long count) {
        countMap.put(table, count);
    }

    public static synchronized void remove(String table) {
        countMap.remove(table);
    }
}
