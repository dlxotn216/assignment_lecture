package io.taesu.lectureapi.common.utils

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class ExceptionUtilsTest {
    @Test
    fun `최상단의 원인, Root cause를 반환한다`() {
        // given
        val rootCause = RuntimeException("root cause")
        val cause = RuntimeException("cause", rootCause)
        val cause2 = RuntimeException("cause2", cause)
        val cause3 = RuntimeException("cause3", cause2)

        // when
        val result = ExceptionUtils.resolveRootCause(cause3)

        // then
        assertThat(result).isEqualTo(rootCause)
    }

    @Test
    fun `Throwable이 null일 경우 빈 문자열을 반환한다`() {
        // given, when
        val result = ExceptionUtils.resolveDebugMessage(null)

        // then
        assertThat(result).isEqualTo("")
    }

    @Test
    fun `RootCause가 ApplicationException이라면 디버깅 메시지를 반환한다`() {
        // given
        val rootCause = BusinessException(
            ErrorCode.UNEXPECTED,
            debugMessage = "debug message"
        )
        val cause = java.lang.RuntimeException("cause", rootCause)
        val cause2 = java.lang.RuntimeException("cause2", cause)
        val cause3 = java.lang.RuntimeException("cause3", cause2)

        // when
        val result = ExceptionUtils.resolveDebugMessage(cause3)

        // then
        assertThat(result).isEqualTo("debug message")
    }

}
