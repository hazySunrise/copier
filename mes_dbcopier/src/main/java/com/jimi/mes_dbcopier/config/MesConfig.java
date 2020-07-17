package com.jimi.mes_dbcopier.config;

import com.jfinal.config.*;
import com.jfinal.json.MixedJsonFactory;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.template.Engine;
import com.jimi.mes_dbcopier.interceptor.ErrorLogInterceptor;
import com.jimi.mes_dbcopier.model.MappingKit;
import com.jimi.mes_dbcopier.sync.task.SyncBuilder;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;

public class MesConfig extends JFinalConfig {
    private static DruidPlugin dbPlugin;
    private Level level;

    @Override
    public void configConstant(Constants me) {
        // 设置当前是否为开发模式
        me.setDevMode(true);
        // 开启依赖注入
        me.setInjectDependency(true);
        me.setJsonFactory(new MixedJsonFactory());
    }

    @Override
    public void configRoute(Routes me) {

    }

    @Override
    public void configEngine(Engine me) {

    }

    @Override
    public void configPlugin(Plugins me) {
        PropKit.use("properties.ini");
        Configurator.setRootLevel(Level.OFF);
        dbPlugin = null;
        this.level = null;
        if (isProductionEnvironment()) {
            this.level = Level.INFO;
            dbPlugin = new DruidPlugin(PropKit.get("p_url"), PropKit.get("p_user"), PropKit.get("p_password"));
            System.out.println("System is in production environment");
        } else if (isTestEnvironment()) {
            this.level = Level.INFO;
            dbPlugin = new DruidPlugin(PropKit.get("t_url"), PropKit.get("t_user"), PropKit.get("t_password"));
            System.out.println("System is in testing environment");
        } else {
            this.level = Level.DEBUG;
            dbPlugin = new DruidPlugin(PropKit.get("d_url"), PropKit.get("d_user"), PropKit.get("d_password"));
            System.out.println("System is in development environment");
        }
        ActiveRecordPlugin arp = new ActiveRecordPlugin(dbPlugin);
        arp.setShowSql(false);
        arp.setDialect(new MysqlDialect());
        dbPlugin.setDriverClass("com.mysql.jdbc.Driver");
        MappingKit.mapping(arp);
        me.add(dbPlugin);
        me.add(arp);
    }


    @Override
    public void configInterceptor(Interceptors me) {
        //具有先后顺序
        me.addGlobalActionInterceptor(new ErrorLogInterceptor());

    }

    @Override
    public void configHandler(Handlers me) {

    }

    public static Connection getLog4j2JDBCAppenderConnection() throws SQLException {
        return dbPlugin.getDataSource().getConnection();
    }


    public static boolean isProductionEnvironment() {
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            if (new File(roots[i].toString() + "PRODUCTION_ENVIRONMENT_FLAG").exists()) {
                return true;
            }
        }
        return false;
    }


    public static boolean isTestEnvironment() {
        File[] roots = File.listRoots();
        for (int i = 0; i < roots.length; i++) {
            if (new File(roots[i].toString() + "TEST_ENVIRONMENT_FLAG").exists()) {
                return true;
            }
        }
        return false;
    }

    public void afterJFinalStart() {
        //设置日志等级
        Configurator.setRootLevel(level);
        SyncBuilder.builder().init().start();
    }
}
