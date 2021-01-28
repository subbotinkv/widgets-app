package ru.subbotinkv.widgets.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

    @Bean
    public Docket api() {

        final Contact contact = new Contact("Subbotin Konstantin",
                "https://github.com/subbotinkv",
                "kosteo-osa@mail.ru");

        final ApiInfo apiInfo = new ApiInfoBuilder()
                .title("Widget Api")
                .version("1.0")
                .description("Widget management API")
                .contact(contact)
                .build();

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
                .paths(PathSelectors.any())
                .build()
                .useDefaultResponseMessages(false);
    }
}
