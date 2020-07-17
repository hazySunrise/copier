package com.jimi.mes_dbcopier.sync.task;

import com.jimi.mes_dbcopier.service.BackupLogService;
import com.jimi.mes_dbcopier.sync.db.SQLServer;
import com.jimi.mes_dbcopier.sync.entity.DBInfo;
import com.jimi.mes_dbcopier.sync.entity.JobInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

public class SyncJob implements Job {
    private Logger logger = LogManager.getLogger(SyncJob.class);

    /**
     * 执行同步数据库任务
     *
     */
    @Override
    public void execute(JobExecutionContext context) {
        Date time = new Date();
        //开始任务调度
        Connection inConn = null;
        Connection outConn = null;
        //Job执行时，从JobDataMap中获取数据
        JobDataMap data = context.getJobDetail().getJobDataMap();
        DBInfo srcDb = (DBInfo) data.get("srcDb");
        DBInfo destDb = (DBInfo) data.get("destDb");
        JobInfo jobInfo = (JobInfo) data.get("jobInfo");
        String logTitle = (String) data.get("logTitle");
        try {
            inConn = SQLServer.createConnection(srcDb);
            outConn = SQLServer.createConnection(destDb);
            if (inConn == null) {
                this.logger.info("请检查源数据连接!");
            } else if (outConn == null) {
                this.logger.info("请检查目标数据连接!");
            }else {

                long eStart = System.currentTimeMillis();
                SQLServer.copySrcDbToDestDb(inConn, outConn, jobInfo, time);
                long consumeTime = System.currentTimeMillis() - eStart;

                //记录日志
                BackupLogService.addBackupLog(time,jobInfo.getDestTable(),consumeTime);
            }
        } catch (SQLException e) {
            this.logger.error(logTitle + " SQL执行出错，请检查是否存在语法错误"+ e.getMessage());
        } finally {
            // 关闭源数据库连接
            SQLServer.closeConnection(inConn);
            //关闭目标数据库连接
            SQLServer.closeConnection(outConn);
        }
    }



}
