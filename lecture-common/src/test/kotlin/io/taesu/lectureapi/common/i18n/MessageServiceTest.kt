package io.taesu.lectureapi.common.i18n

import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest
class MessageServiceTest {
    @Autowired
    private lateinit var messageService: MessageService

    @ParameterizedTest
    @MethodSource("i18nTestInput")
    fun `다국어 테스트`(input: Pair<SupportLang, String>) {
        // when
        val message = messageService.getMessage("MESSAGE.HELLO", input.first)

        // then
        assertThat(message).isEqualTo(input.second)
    }

    companion object {
        @JvmStatic
        fun i18nTestInput() = listOf(
            SupportLang.ENGLISH to "Hello",
            SupportLang.KOREAN to "안녕하세요",
            SupportLang.JAPANESE to "こんにちは",
        )
    }

}
