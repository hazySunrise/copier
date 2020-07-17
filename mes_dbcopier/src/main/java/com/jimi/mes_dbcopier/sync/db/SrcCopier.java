package com.jimi.mes_dbcopier.sync.db;

import com.jimi.mes_dbcopier.service.BackupLogService;
import com.jimi.mes_dbcopier.sync.entity.JobInfo;
import com.jimi.mes_dbcopier.sync.handler.BackupTimeHandler;
import com.jimi.mes_dbcopier.sync.handler.TimeField;
import com.jimi.mes_dbcopier.util.DateUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;


/**
 * 从源数据库获取增量结果集
 *
 */
public class SrcCopier {


    protected static PreparedStatement getDataFromSrcDb(Connection conn, JobInfo jobInfo, Date date)throws SQLException {
        String srcSql = jobInfo.getSrcSql();
        String destTable = jobInfo.getDestTable();
        String time = TimeField.select(destTable) ;
        Date lastBackupTime = BackupLogService.getLastBackupTime(destTable);
        PreparedStatement pst ;

        //根据最后一次备份时间，拼接查询源数据库语句
        StringBuffer sql = new StringBuffer(srcSql);
        if (lastBackupTime == null) {
            sql.append(" where "+time+"<= ?");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1,DateUtil.yyyyMMddHHmm(BackupTimeHandler.getFirstBackupTime(date)));
        }else {
            sql.append(" where "+time +"> ? and "+time+ "<= ? ");
            pst = conn.prepareStatement(sql.toString());
            pst.setString(1, DateUtil.yyyyMMddHHmm(lastBackupTime));
            pst.setString(2, DateUtil.yyyyMMddHHmm(BackupTimeHandler.getCurrentBackupTime(lastBackupTime)));
        }
        return pst;
    }





}
