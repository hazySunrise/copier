package com.jimi.mes_dbcopier.sync.db;

import com.jimi.mes_dbcopier.sync.entity.JobInfo;
import com.jimi.mes_dbcopier.util.CountBox;
import com.jimi.mes_dbcopier.util.StringUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * 备份到目的数据库
 */
public class DestExecutor {

    /**
     * 根据单个表字段判断表数据是否存在于目的表中
     * 执行策略：先删后增
     * Sql语句格式：
     * delete from table where SN in (x,x,x,x) ;
     * insert into table (x,x,x) values (x,x,x),(x,x,x);
     */
    protected static void executeSingleConditionSQL(JobInfo jobInfo, String destTable, ResultSet rs, Connection outConn) throws SQLException {
        String fieldStr = jobInfo.getDestTableFields();
        StringBuffer sql = new StringBuffer();
        StringBuffer deSql = new StringBuffer();
        String destTableKey = jobInfo.getDestTableKey();
        long count = 0;
        long number = 0;
        deSql.append("delete from ").append(destTable).append(" where ").append(destTableKey).append(" in (");
        sql.append("insert into ").append(destTable).append(" (").append(fieldStr).append(") values ");
        while (rs.next()) {
            deSql.append("'").append(rs.getString(destTableKey)).append("',");
            sql.append("(");
            sql = assembleSQL(rs,sql,fieldStr);
            sql.append("),");
            count++;

            //每隔一千条插入一次
            if (count == 1000) {
                // 清空上一次添加的数据
                updateSql(deSql,sql,outConn);
                count = 0;
                deSql = new StringBuffer();
                deSql = deSql.append("delete from ").append(destTable).append(" where SN in (");
                sql = new StringBuffer();
                sql.append("insert into ").append(destTable).append(" (").append(fieldStr).append(") values ");
            }
            number++;
        }
        if (count > 0) {
            updateSql(deSql,sql,outConn);
        }
        CountBox.add(destTable,number);
    }


    private static void updateSql(StringBuffer desql, StringBuffer sql ,Connection outConn) throws SQLException{
        StringBuffer deSql = desql.deleteCharAt(desql.length() - 1).append(")");
        executeSQL(deSql.toString(), outConn);
        sql = sql.deleteCharAt(sql.length() - 1);
        executeSQL(sql.toString(), outConn);
    }

    /**
     * 根据多个表字段判断表数据是否存在于目的表中
     * 执行策略：存在时更新，不存在时新增
     * Sql语句格式：
     *if not exists (select SN from table where x='aa' and y = 'bb') insert into table (x,x) values (y,y)
     * else update( x = a,y=b) where x='aa' and y = 'bb'
     */
    protected static void executeMulConditionSQL(JobInfo jobInfo, String destTable, ResultSet rs, Connection outConn)throws SQLException{
        String fieldStr = jobInfo.getDestTableFields();
        String[] updateFields = StringUtils.split(jobInfo.getDestTableUpdate());
        String[] destTableKeys = StringUtils.split(jobInfo.getDestTableKey());

        StringBuffer sql = new StringBuffer();
        long count = 0;
        long number = 0;
        while (rs.next()) {
            sql.append("if not exists (select * from ").append(destTable).append(" where ");
            for (int i = 0;i < destTableKeys.length; i++) {
                sql.append(destTableKeys[i]).append("='").append(rs.getString(destTableKeys[i])).append(i == (destTableKeys.length - 1) ? "')" : "' and ");
            }
            //不存在插入
            sql.append("insert into ").append(destTable).append("(").append(fieldStr).append(") values(");
            sql= assembleSQL(rs,sql,fieldStr);
            //存在时更新
            sql.append(") else update ").append(destTable).append(" set ");
            for (int i = 0; i< updateFields.length; i++) {
                String value = rs.getString(updateFields[i]);
                if (value != null) {
                    sql.append(updateFields[i]).append("='").append(value).append(i == (updateFields.length - 1) ? "'" : "',");
                }else {
                    sql.append(updateFields[i]).append("='").append(" ").append(i == (updateFields.length - 1) ? "'" : "',");
                }
            }
            sql.append(" where ");
            for (int i = 0;i < destTableKeys.length; i++) {
                sql.append(destTableKeys[i]).append("='").append(rs.getString(destTableKeys[i])).append(i == (destTableKeys.length - 1) ? "';" : "'and ");
            }
            count++;
            //每隔一千条插入一次
            if (count == 1000) {
                executeSQL(sql.toString(), outConn);
                sql = new StringBuffer();
                count = 0;
            }
            number++;
        }
        if (count > 0) {
            executeSQL(sql.toString(), outConn);
        }
        CountBox.add(destTable,number);
    }

    private static StringBuffer assembleSQL( ResultSet rs, StringBuffer sql,String fieldStr)throws SQLException{
        String[] fields = StringUtils.split(fieldStr);
        for (int index = 0; index < fields.length; index++) {
            String value = rs.getString(fields[index]);
            if (value != null) {
                sql.append("'").append(value).append(index == (fields.length - 1) ? "'" : "',");
            } else {
                sql.append("'").append(" ").append(index == (fields.length - 1) ? "'" : "',");
            }
        }
        return sql;
    }

    private static void executeSQL(String sql, Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        conn.commit();
        pst.close();
    }
}
