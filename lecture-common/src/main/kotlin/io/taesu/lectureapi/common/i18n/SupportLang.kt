package io.taesu.lectureapi.common.i18n

import java.util.*

/**
 * Created by itaesu on 2024/03/21.
 *
 * 서비스에서 지원하는 언어
 *
 * @author Lee Tae Su
 */
enum class SupportLang(val locale: Locale) {
    KOREAN(Locale.forLanguageTag("ko")),
    ENGLISH(Locale.forLanguageTag("en")),
    JAPANESE(Locale.forLanguageTag("ja"));


    companion object {
        val DEFAULT = KOREAN
        val DEFAULT_LOCALE = KOREAN.locale

        fun from(locale: Locale?): SupportLang {
            return entries.find { it.locale == locale } ?: DEFAULT
        }

        fun from(headerValue: String?): SupportLang? {
            return when (headerValue?.lowercase()) {
                null -> null
                in setOf("eng", "en") -> ENGLISH
                in setOf("kor", "ko") -> KOREAN
                in setOf("jpn", "ja") -> JAPANESE
                else -> DEFAULT
            }
        }
    }
}
