package io.taesu.lectureapi.common.i18n

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024/03/21.
 *
 * 다국어 지원을 위한 메시지를 직렬화하기 위한 Serializer
 *
 * @author Lee Tae Su
 */
@Component
class TranslatableSerializer(private val messageService: MessageService): JsonSerializer<Translatable?>() {
    override fun serialize(i18n: Translatable?, generator: JsonGenerator, provider: SerializerProvider) {
        val label = i18n?.let {
            messageService.getMessage(i18n, SupportLang.from(LocaleContextHolder.getLocale()))
        }
        generator.writeString(label)
    }
}
