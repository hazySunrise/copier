package com.jimi.mes_dbcopier.sync.handler;

import com.jfinal.kit.PropKit;

import java.util.Calendar;
import java.util.Date;

public class BackupTimeHandler {

    private static Date firstBackupTime = null;

    /**
     * 根据上一次备份时间，得到下一次备份时间
     * @param lastBackupTime
     * @return
     */
    public static Date getCurrentBackupTime(Date lastBackupTime)  {
        //备份间隔时间
        int intervalTime = PropKit.use("properties.ini").getInt("intervalTime");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(lastBackupTime);
        calendar.add(Calendar.HOUR_OF_DAY, intervalTime); //得到六个钟以后的时间
        return calendar.getTime();

    }



    private static Date firstBackup(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, -30); //得到前半个钟
        return calendar.getTime();
    }

    /**
     * 首次备份，获取半小时前的时间，用于备份半小时前的所有记录
     * @return
     */
    public static Date getFirstBackupTime(Date date) {
        if (firstBackupTime ==null){
            firstBackupTime = firstBackup(date);
        }
        return firstBackupTime;
    }
}
