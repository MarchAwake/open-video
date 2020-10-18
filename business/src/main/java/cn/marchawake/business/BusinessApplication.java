package cn.marchawake.business;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <h1>business模块启动主类</h1>
 *
 * @author March
 * @date 2020/7/1
 */
@SpringBootApplication
@EnableEurekaClient
@Slf4j
@ComponentScan("cn.marchawake")
@MapperScan("cn.marchawake.server.mapper")
public class BusinessApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BusinessApplication.class);
        ConfigurableEnvironment evn = app.run(args).getEnvironment();

        log.info("启动成功");
        log.info("Business 地址：\thttp://127.0.0.1:{}",evn.getProperty("server.port"));
    }
}
