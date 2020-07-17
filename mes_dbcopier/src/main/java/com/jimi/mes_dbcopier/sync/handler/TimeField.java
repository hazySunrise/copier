package com.jimi.mes_dbcopier.sync.handler;

import com.jimi.mes_dbcopier.constant.Table;

/**
 * 数据库记录操作时间
 */
public class TimeField {
    public static final String RECORD_TIME = "RecordTime";

    public static final String TEST_TIME = "TestTime";


    public static String select(String tableName){
        String timeField;
        if (tableName.equals(Table.Gps_Test_Result)) {
            timeField = RECORD_TIME;
        }else {
            timeField = TEST_TIME;
        }
        return timeField;
    }
}
