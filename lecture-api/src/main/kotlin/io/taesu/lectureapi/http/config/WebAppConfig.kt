package io.taesu.lectureapi.http.config

import io.taesu.lectureapi.common.i18n.SupportLang
import io.taesu.lectureapi.http.config.properties.CorsFilterProperty
import io.taesu.lectureapi.http.config.resolvers.RealRequestContextResolver
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
 * WebApp 공통 설정
 *
 * @author Lee Tae Su
 */
open class WebAppConfig: WebMvcConfigurer {
    @Bean
    open fun localeResolver(): LocaleResolver = AcceptHeaderLocaleResolver().apply {
        supportedLocales = SupportLang.entries.map { it.locale }
        setDefaultLocale(SupportLang.DEFAULT_LOCALE)
    }

    @Bean
    open fun corsWebFilter(
        corsFilterProperty: CorsFilterProperty,
    ): CorsFilter {
        val corsConfig = CorsConfiguration().apply {
            allowedOrigins = corsFilterProperty.allowedOrigins
            allowCredentials = true
            maxAge = 8000L
            addAllowedMethod("*")
            addAllowedHeader("*")
            addExposedHeader(HttpHeaders.CONTENT_DISPOSITION)
        }
        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", corsConfig)
        return CorsFilter(source)
    }
}
