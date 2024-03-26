package io.taesu.lectureapi.common.i18n

import io.taesu.lectureapi.common.utils.hash

/**
 * Created by itaesu on 2024/03/21.
 *
 * 단순 텍스트 메시지
 *
 * @author Lee Tae Su
 */
class SimpleText(
    override val messageId: String,
): Translatable {
    override val messageArgs: Array<Any> = emptyArray()

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is SimpleText
                && other.messageId == this.messageId
            )

    override fun hashCode() = hash(messageId)

    override fun toString() = "SimpleText($messageId)"
}
