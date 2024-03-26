package io.taesu.lectureapi.http.config

import io.taesu.lectureapi.common.i18n.SupportLang
import io.taesu.lectureapi.http.config.properties.CorsFilterProperty
import io.taesu.lectureapi.http.config.resolvers.MockRequestContextResolver
import io.taesu.lectureapi.http.config.resolvers.RealRequestContextResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpHeaders
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.LocaleResolver
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver

/**
 * Created by itaesu on 2024/03/21.
 *
 * Spring profile에 따른 WebMvcConfigurer 설정
 *
 * @author Lee Tae Su
 */
@Profile("!test")
@Configuration
class ProductionWebAppConfig: WebAppConfig() {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(resolvers)
        resolvers.add(RealRequestContextResolver(localeResolver()))
    }
}

@Profile("test")
@Configuration
class TestWebAppConfig: WebAppConfig() {
    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        super.addArgumentResolvers(resolvers)
        resolvers.add(MockRequestContextResolver())
    }
}
