package io.taesu.lectureapi.http.config.resolvers

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.context.RequestContext.Companion.DEFAULT_ZONE_ID
import io.taesu.lectureapi.common.context.RequestContext.Companion.ZONE_ID_HEADER
import io.taesu.lectureapi.common.i18n.SupportLang
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.LocaleResolver
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by itaesu on 2024/03/21.
 *
 * Http Request의 정보를 기반 RequestContextResolver 인터페이스
 * Production 용 RealRequestContextResolver와 Test 용 MockRequestContextResolver 구현체 제공
 *
 * @author Lee Tae Su
 */
interface RequestContextResolver: HandlerMethodArgumentResolver

class RealRequestContextResolver(
    private val localeResolver: LocaleResolver,
): RequestContextResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return RequestContext::class.java.isAssignableFrom(parameter.parameterType)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val httpServletRequest = webRequest.nativeRequest as? HttpServletRequest
        val supportLang = resolveSupportLang(httpServletRequest)
        val zoneId = resolveZoneId(webRequest)
        return RequestContext(supportLang, zoneId)
    }

    private fun resolveSupportLang(httpServletRequest: HttpServletRequest?): SupportLang {
        val supportLang = httpServletRequest?.let {
            SupportLang.from(localeResolver.resolveLocale(it))
        }
        return supportLang ?: SupportLang.DEFAULT
    }
}

class MockRequestContextResolver: RequestContextResolver {
    override fun supportsParameter(parameter: MethodParameter): Boolean {
        return RequestContext::class.java.isAssignableFrom(parameter.parameterType)
    }

    override fun resolveArgument(
        parameter: MethodParameter,
        mavContainer: ModelAndViewContainer?,
        webRequest: NativeWebRequest,
        binderFactory: WebDataBinderFactory?,
    ): Any {
        val supportLang = webRequest.getHeader("Accept-Language")?.let { SupportLang.from(it) } ?: SupportLang.DEFAULT
        val zoneId = resolveZoneId(webRequest)
        val nowDateTime = webRequest.getHeader("X-NOW_DATETIME")?.let { LocalDateTime.parse(it) } ?: LocalDateTime.now()

        return RequestContext(supportLang, zoneId, nowDateTime)
    }
}


private fun resolveZoneId(webRequest: NativeWebRequest): ZoneId =
    webRequest.getHeader(ZONE_ID_HEADER)?.let { ZoneId.of(it) } ?: DEFAULT_ZONE_ID
