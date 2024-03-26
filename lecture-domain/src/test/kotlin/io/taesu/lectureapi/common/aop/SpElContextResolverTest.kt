package io.taesu.lectureapi.common.aop

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.expression.spel.support.StandardEvaluationContext

/**
 * Created by itaesu on 2024/03/26.
 *
 * @author Lee Tae Su
 */
class SpElContextResolverTest {
    @Test
    fun `SpEL Key resolve 테스트`() {
        // given
        val context = StandardEvaluationContext().apply {
            setVariable("lectureKey", 123L)
        }

        // when
        val key = SpElContextResolver.resolveKey(context, "'lock:lecture:apply:' + #lectureKey")

        // then
        assertThat(key).isEqualTo("lock:lecture:apply:123")
    }

}
