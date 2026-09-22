package com.example.talisman.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("내운내뽑 API Document")
                .description("Gen-Z 타겟의 위트 있는 사주/부적 서비스 '내운내뽑' 백엔드 API 명세서")
                .version("v1.0.0"))
                ;
    }
}
