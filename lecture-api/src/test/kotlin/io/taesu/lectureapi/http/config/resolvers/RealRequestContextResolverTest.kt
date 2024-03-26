package io.taesu.lectureapi.http.config.resolvers

import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.context.RequestContext.Companion.ZONE_ID_HEADER
import io.taesu.lectureapi.common.i18n.SupportLang
import jakarta.servlet.http.HttpServletRequest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.core.MethodParameter
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.ModelAndViewContainer
import org.springframework.web.servlet.LocaleResolver
import java.time.ZoneId
import java.util.*

/**
 * Created by itaesu on 2024/03/25.
 *
 * @author Lee Tae Su
 */
@ExtendWith(MockKExtension::class)
class RealRequestContextResolverTest {
    @MockK
    private lateinit var localeResolver: LocaleResolver

    @SpyK
    @InjectMockKs
    private lateinit var service: RealRequestContextResolver

    @Test
    fun `RequestContext는 Locale 정보와 ZoneID Header를 기반으로 Resolve 된다`() {
        // given
        val parameter: MethodParameter = mockk()
        val mavContainer: ModelAndViewContainer = mockk()
        val webRequest: NativeWebRequest = mockk()
        val httpServletRequest: HttpServletRequest = mockk()
        val binderFactory: WebDataBinderFactory = mockk()

        every { webRequest.nativeRequest } returns httpServletRequest
        every { localeResolver.resolveLocale(any()) } returns Locale.KOREA
        every { webRequest.getHeader(ZONE_ID_HEADER) } returns "Asia/Seoul"

        // when
        val requestContext = service.resolveArgument(parameter, mavContainer, webRequest, binderFactory)

        // then
        assertThat(requestContext).isNotNull
        assertThat(requestContext).isInstanceOf(RequestContext::class.java)
        requestContext as RequestContext
        assertThat(requestContext.supportLang).isEqualTo(SupportLang.KOREAN)
        assertThat(requestContext.zoneId).isEqualTo(ZoneId.of("Asia/Seoul"))
    }

}
