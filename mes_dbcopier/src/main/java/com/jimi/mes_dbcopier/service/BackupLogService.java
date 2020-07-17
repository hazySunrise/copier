package com.jimi.mes_dbcopier.service;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jimi.mes_dbcopier.model.BackupLog;
import com.jimi.mes_dbcopier.sync.handler.BackupTimeHandler;
import com.jimi.mes_dbcopier.util.CountBox;

import java.util.Date;

public class BackupLogService {
    public  static final String SELECT_LAST_BACKUP_LOG=" SELECT DATE_FORMAT(backup_time,'%Y-%m-%d %H:%i') as backupTime FROM backup_log b where b.table = ? ORDER BY id DESC LIMIT 1";

    /**
     * 获取最后一次备份时间
     * 时间格式：yyyy-MM-dd HH:mm
     * @return
     */
    public static Date getLastBackupTime(String tableName){
        Record backupLog = Db.findFirst(SELECT_LAST_BACKUP_LOG,tableName);
        if (backupLog != null){
            return backupLog.getDate("backupTime");
        }
        return null;
    }

    /**
     * 增加备份日志
     */
    public static void addBackupLog(Date time, String table, long consumeTime){
        BackupLog backupLog = new BackupLog();
        backupLog.setTime(time);//备份操作执行时间
        backupLog.setTable(table);
        backupLog.setConsumeTime((int) consumeTime);
        backupLog = setBackupTime(backupLog);
        backupLog.setTable(table);
        backupLog.setNumber(CountBox.getCount(table));
        backupLog.save();
    }


    static BackupLog setBackupTime(BackupLog log){
        //记录首次备份时间或者再次备份时间
        Date lastBackupTime = BackupLogService.getLastBackupTime(log.getTable());
        Date time ;
        if (lastBackupTime == null){
            time = BackupTimeHandler.getFirstBackupTime(log.getTime());
        }else{
           time = BackupTimeHandler.getCurrentBackupTime(lastBackupTime);
        }
        log.setBackupTime(time);
        return log;
    }
}
