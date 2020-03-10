package ru.tinkoff.fintech.configuration

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.DefaultResponseErrorHandler
import org.springframework.web.client.RestTemplate
import java.time.Duration

@Configuration
class Configuration {
    @Bean
    fun restTemplate(restTemplateBuilder: RestTemplateBuilder) : RestTemplate {
        return restTemplateBuilder
            .setConnectTimeout(Duration.ofSeconds(20))
            .setReadTimeout(Duration.ofSeconds(20))
            .errorHandler(DefaultResponseErrorHandler())
            .build()
    }
}