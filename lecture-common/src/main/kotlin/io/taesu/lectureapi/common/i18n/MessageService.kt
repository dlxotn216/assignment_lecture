package io.taesu.lectureapi.common.i18n

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import org.slf4j.LoggerFactory
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
@Component
class MessageService(private val messageSource: MessageSource) {
    fun getMessage(
        e: BusinessException,
        supportLang: SupportLang,
    ) = getMessage(e.messageId, supportLang, e.messageArgs)

    fun getMessage(
        errorCode: ErrorCode,
        supportLang: SupportLang,
        args: Array<Any>? = null,
    ) = getMessage(errorCode.messageId, supportLang, args)


    fun getMessage(
        transformable: Translatable,
        supportLang: SupportLang,
    ): String? {
        if (transformable is SimpleText) {
            return transformable.messageId
        }
        val messageId = transformable.messageId ?: return null
        return getMessage(messageId, supportLang, transformable.messageArgs)
    }

    fun getMessage(
        messageId: String,
        supportLang: SupportLang,
        args: Array<Any>? = null,
    ): String {
        val newArgs = args?.map { arg ->
            when (arg) {
                is Translatable -> arg.messageId?.let { getMessage(it, supportLang, arg.messageArgs) }
                is Number -> arg.toString()
                else -> arg
            }
        }?.toTypedArray() ?: args
        return try {
            messageSource.getMessage(messageId, newArgs, supportLang.locale)
        } catch (e: Exception) {
            log.warn("""
                i18n getMessage 실패: [$messageId]
                args: [${newArgs?.joinToString()}]
                supportLang: [$supportLang]
            """.trimIndent(), e)
            ""
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(this::class.java)
    }
}

