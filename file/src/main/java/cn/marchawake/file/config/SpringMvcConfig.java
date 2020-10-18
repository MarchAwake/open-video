package cn.marchawake.file.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Value("${file.path}")
    private String FILE_PATH;
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // http://127.0.0.1:18000/file/static/BuE5Odg9-love.jpg
        registry.addResourceHandler("/static/**").addResourceLocations("file:" + FILE_PATH);

    }
}
