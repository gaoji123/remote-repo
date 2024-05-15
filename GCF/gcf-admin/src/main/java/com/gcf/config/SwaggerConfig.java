package com.gcf.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.gcf.controller"))
                .build();
    }

    private ApiInfo apiInfo() {
        Contact contact = new Contact("软件金融203班_赵奕", "https://www.bilibili.com/", "zhaoyiwodi123@gmail.com");
        return new ApiInfoBuilder()
                .title("垃圾分类")
                .description("垃圾分类相关接口")
                .contact(contact)   // 联系方式
                .version("1.1.0")  // 版本
                .build();
    }
}