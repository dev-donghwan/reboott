package com.donghwan.team_reboott.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class SwaggerConfig {

//    @Bean
//    fun api(): OpenAPI = OpenAPI()
//        .info(
//            Info()
//                .title("Team Reboott API")
//                .description("AI 기능 크레딧 및 사용량 제한 API 문서")
//                .version("v1.0")
//        )

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .components(Components())
            .info(
                Info()
                    .title("데모 프로젝트 API Document")
                    .version("v0.0.1")
                    .description("데모 프로젝트의 API 명세서입니다.")
            );
    }
}