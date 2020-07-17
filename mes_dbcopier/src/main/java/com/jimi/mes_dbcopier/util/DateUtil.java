package com.jimi.mes_dbcopier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static Date yyyyMMddHHmm(String date) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.parse(date);
    }

    public static String yyyyMMddHHmm(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

}
