package cn.marchawake.system;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Eureka模块启动主类
 *
 * @author March
 * @date 2020/7/1
 */
@SpringBootApplication
@EnableEurekaClient
@Slf4j
@ComponentScan("cn.marchawake")
@MapperScan("cn.marchawake.server.mapper")
public class SystemApplication {

    public static void main(String[] args) {
        
        SpringApplication app = new SpringApplication(SystemApplication.class);
        ConfigurableEnvironment evn = app.run(args).getEnvironment();

        log.info("启动成功");
        log.info("System 地址：\thttp://127.0.0.1:{}",evn.getProperty("server.port"));


    }
}
