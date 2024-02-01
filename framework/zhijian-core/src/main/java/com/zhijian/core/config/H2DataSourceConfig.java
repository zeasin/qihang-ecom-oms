//package com.zhijian.core.config;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.jdbc.datasource.init.ScriptUtils;
//import org.springframework.stereotype.Service;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.AutoConfigureAfter;
//import org.springframework.core.io.Resource;
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.io.File;
///**
// * 初始化h2数据库
// */
//@Service
////@AutoConfigureAfter(DataSource.class) //DataSource创建完后才初始化此类
//@Configuration
//public class H2DataSourceConfig {
//    private Logger logger = LoggerFactory.getLogger(this.getClass());
//    //初始化sql
//    private static final String schema="classpath:db/schema-h2.sql";
//    @Autowired
//    private ResourceLoader resourceLoader;
//    @Autowired
//    DataSource dataSource;
//
//
//    // JDK提供的注解,在方法上加该注解会在项目启动的时候执行该方法,也可以理解为在spring容器初始化的时候执行该方法
//    @PostConstruct
//    public  void init() throws Exception {
//        logger.info("初始化h2 SQL");
//        //初始化本地数据库
//        String userHome= System.getProperty("user.home");//获取系统用户目录
//        // 创建一个标识文件,只有在第一次初始化数据库时会创建,如果系统用户目录下有这个文件,就不会重新执行sql脚本
//        File f = new File(userHome+ File.separator+"my.lock");
//        if(!f.exists()){
////            log.info("--------------初始化h2数据----------------------");
//            f.createNewFile();
//            // 加载资源文件
//
////            Resource resource= (Resource) applicationContextRegister.getResource(schema);
//            Resource resource = resourceLoader.getResource(schema);
//            // 手动执行SQL语句
//            ScriptUtils.executeSqlScript(dataSource.getConnection(),resource);
//        }
//    }
//
//}
