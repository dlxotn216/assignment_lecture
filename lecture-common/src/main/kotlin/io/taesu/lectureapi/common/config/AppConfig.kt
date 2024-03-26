package io.taesu.lectureapi.common.config

import io.taesu.lectureapi.common.config.jackson.EmptyStringAsNullModule
import org.springframework.context.MessageSource
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.support.ReloadableResourceBundleMessageSource

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Configuration
class AppConfig {
    @Bean
    fun emptyStringAsNullModule() = EmptyStringAsNullModule()

    @Bean
    fun messageSource(): MessageSource {
        return ReloadableResourceBundleMessageSource().apply {
            // this.setUseCodeAsDefaultMessage(true)
            this.setAlwaysUseMessageFormat(true)
            this.setBasename("classpath:messages/message")
            this.setDefaultEncoding("UTF-8")
        }
    }
}
