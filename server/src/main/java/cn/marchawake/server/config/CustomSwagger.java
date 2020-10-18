package cn.marchawake.server.config;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;



@Configuration
@EnableOpenApi
public class CustomSwagger {

    private final static String PATH = "cn.marchawake.business.controller.admin" + "," + "cn.marchawake.system.controller.admin";

    /**
     * 配置 swagger2 核心配置 docket
     */
    @Bean
    public Docket createRestApi() {

        return new Docket(DocumentationType.OAS_30)      // 指定 api 类型为 swagger2
                    .apiInfo(apiInfo()) // 用于定义 api 文档汇总信息
                    .select().apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))  // 指定 controller 路径
                    .paths(PathSelectors.any()) // 所有 controller
                    .build();
    }

    private ApiInfo apiInfo() {

       return new ApiInfoBuilder()
                .title("开源视频")  // 文档页标题
                .contact(new Contact("MarchAwake", "http://marchawake.cn", "marchawake@163.com"))   // 联系人信息
                .description("开源视频接口文档")    // 详细信息
                .version("1.0.0")   // 文档版本号
                .termsOfServiceUrl("http://marchawake.cn")  // 网站地址
                .build();
    }




}
