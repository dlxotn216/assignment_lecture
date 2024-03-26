package io.taesu.lectureapi.common.i18n

import com.fasterxml.jackson.databind.annotation.JsonSerialize

/**
 * Created by itaesu on 2024/03/21.
 *
 * 다국어 지원을 위한 메시지를 표현하기 위한 인터페이스
 *
 * @author Lee Tae Su
 */
@JsonSerialize(
    using = TranslatableSerializer::class,
)
interface Translatable {
    val messageId: String?
    val messageArgs: Array<Any>
}
