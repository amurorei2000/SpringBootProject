package com.github.springbootproject.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("스프링부트 Swagger API")
                        .version("1.0.0")
                        .description("슈퍼코딩 연습용 스프링부트 swagger 페이지입니다."));
    }
}
