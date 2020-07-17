package com.jimi.mes_dbcopier.sync.entity;

public class DBInfo {

    //数据库连接
    private String url;
    //数据库用户名
    private String username;
    //数据库密码
    private String password;
    //数据库类型
    private String dbtype;
    //数据库驱动
    private String driver;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }
}
