package com.jimi.mes_dbcopier.util;

public class StringUtils {

    /**
     * 去除String数组每个元素中的空格
     * @param src
     * @return
     */
    public static String[] trimArrayItem(String[] src){
        if(src == null || src.length == 0) return src;
        String[] dest = new String[src.length];
        for(int i = 0; i < src.length; i++){
            dest[i] = src[i].trim();
        }
        return dest;
    }

    public static String[] split(String str){
        return trimArrayItem(str.split(","));
    }
}
