package cn.marchawake.eureka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Eureka模块启动主类
 *
 * @author March
 * @date 2020/7/1
 */
@SpringBootApplication
@EnableEurekaServer
@Slf4j
public class EurekaApplication {

    public static void main(String[] args) {
        
        SpringApplication app = new SpringApplication(EurekaApplication.class);
        ConfigurableEnvironment evn = app.run(args).getEnvironment();

        log.info("启动成功");
        log.info("Eureka 地址：\thttp://127.0.0.1:{}",evn.getProperty("server.port"));
    }
}
