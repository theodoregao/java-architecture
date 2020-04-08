package com.sg.shopping.config;

import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class Swagger2 {

    // original uri:                http://localhost:8088/swagger-ui.html
    // swagger-bootstrap-ui uri:    http://localhost:8088/doc.html

    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(getApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.sg.shopping.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder()
                .title("Shopping API")
                .contact(new Contact("Theodore",
                        "https://fakeurl.com",
                        "theodore.gao.1986@gmail.com"))
                .description("APIs for Shopping application.")
                .version("0.0.1")
                .termsOfServiceUrl("https://fakeurl.com")
                .build();
    }

}
