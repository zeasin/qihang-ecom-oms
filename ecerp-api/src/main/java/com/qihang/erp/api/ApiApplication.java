package com.qihang.erp.api;


import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 启动程序
 * 
 * @author qihang
 */
// @ComponentScan("com.zhijian")
@Log
@NacosPropertySource(dataId = "ecerp-dev.yaml", autoRefreshed = true)
//@EnableTransactionManagement
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class ApiApplication  {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(ApiApplication.class, args);

        log.info("启航电商ERP系统启动成功   \n" +
                "   ____   _____  _    _            _   _   _____  ______  _____   _____  \n" +
                "   / __ \\ |_   _|| |  | |    /\\    | \\ | | / ____||  ____||  __ \\ |  __ \\ \n" +
                "  | |  | |  | |  | |__| |   /  \\   |  \\| || |  __ | |__   | |__) || |__) |\n" +
                "  | |  | |  | |  |  __  |  / /\\ \\  | . ` || | |_ ||  __|  |  _  / |  ___/ \n" +
                "  | |__| | _| |_ | |  | | / ____ \\ | |\\  || |__| || |____ | | \\ \\ | |     \n" +
                "   \\___\\_\\|_____||_|  |_|/_/    \\_\\|_| \\_| \\_____||______||_|  \\_\\|_|     \n");
        // System.out.println("启航电商ERP系统启动成功 \n" +
        // " ____ _____ _ _ _ _ _____ ______ _____ _____ \n" +
        // " / __ \\ |_ _|| | | | /\\ | \\ | | / ____|| ____|| __ \\ | __ \\ \n" +
        // " | | | | | | | |__| | / \\ | \\| || | __ | |__ | |__) || |__) |\n" +
        // " | | | | | | | __ | / /\\ \\ | . ` || | |_ || __| | _ / | ___/ \n" +
        // " | |__| | _| |_ | | | | / ____ \\ | |\\ || |__| || |____ | | \\ \\ | | \n" +
        // " \\___\\_\\|_____||_| |_|/_/ \\_\\|_| \\_| \\_____||______||_| \\_\\|_|
        // \n");

        // System.out.println("(♥◠‿◠)ﾉﾞ 启航电商ERP系统启动成功 ლ(´ڡ`ლ)ﾞ \n" +
        // " .-------. ____ __ \n" +
        // " | _ _ \\ \\ \\ / / \n" +
        // " | ( ' ) | \\ _. / ' \n" +
        // " |(_ o _) / _( )_ .' \n" +
        // " | (_,_).' __ ___(_ o _)' \n" +
        // " | |\\ \\ | || |(_,_)' \n" +
        // " | | \\ `' /| `-' / \n" +
        // " | | \\ / \\ / \n" +
        // " ''-' `'-' `-..-' ");
    }


}
