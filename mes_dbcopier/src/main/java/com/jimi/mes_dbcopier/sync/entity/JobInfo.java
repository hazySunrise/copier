package com.jimi.mes_dbcopier.sync.entity;

/**
 * 定时同步任务信息类
 */
public class JobInfo {
    //任务名称
    private String name;
    //源数据源sql
    private String srcSql;
    //目标数据表
    private String destTable;
    //目标表数据字段
    private String destTableFields;
    //目标表主键
    private String destTableKey;
    //目标表可更新的字段
    private String destTableUpdate;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSrcSql() {
        return srcSql;
    }

    public void setSrcSql(String srcSql) {
        this.srcSql = srcSql;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public String getDestTableFields() {
        return destTableFields;
    }

    public void setDestTableFields(String destTableFields) {
        this.destTableFields = destTableFields;
    }

    public String getDestTableKey() {
        return destTableKey;
    }

    public void setDestTableKey(String destTableKey) {
        this.destTableKey = destTableKey;
    }

    public String getDestTableUpdate() {
        return destTableUpdate;
    }

    public void setDestTableUpdate(String destTableUpdate) {
        this.destTableUpdate = destTableUpdate;
    }
}
