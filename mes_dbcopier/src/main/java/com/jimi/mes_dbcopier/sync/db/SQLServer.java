package com.jimi.mes_dbcopier.sync.db;

import com.jimi.mes_dbcopier.constant.Table;
import com.jimi.mes_dbcopier.sync.entity.DBInfo;
import com.jimi.mes_dbcopier.sync.entity.JobInfo;

import java.sql.*;
import java.util.Date;

/**
 * SQLServer
 *
 */
public class SQLServer {

    /**
     * 从源数据库拷贝到目的数据库
     */
    public static void copySrcDbToDestDb(Connection conn, Connection outConn, JobInfo jobInfo, Date date) throws SQLException {
        String destTable = jobInfo.getDestTable();
        //从源数据库获取增量结果集
        PreparedStatement pst= SrcCopier.getDataFromSrcDb(conn,jobInfo,date);
        ResultSet rs = pst.executeQuery();

        if (destTable.equals(Table.Gps_AutoTest_AntiDup) ) {
            DestExecutor.executeSingleConditionSQL(jobInfo, destTable, rs, outConn);
        }else{
           DestExecutor.executeMulConditionSQL(jobInfo, destTable, rs, outConn);
        }
        if (rs != null) {
            rs.close();
        }
        if (pst != null) {
            pst.close();
        }
    }

    /**
     * 创建数据库连接
     * @param db
     * @return
     */
    public static Connection createConnection(DBInfo db) {
        try {
            Class.forName(db.getDriver());
            Connection conn = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭数据库连接
     * @param conn
     */
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
