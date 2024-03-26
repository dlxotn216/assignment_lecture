package io.taesu.lectureapi.common.context

import io.taesu.lectureapi.common.i18n.SupportLang
import io.taesu.lectureapi.common.utils.hash
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Created by itaesu on 2024/03/21.
 *
 * 요청에 대한 컨텍스트 정보
 *
 * @author Lee Tae Su
 */
class RequestContext(
    val supportLang: SupportLang,
    val zoneId: ZoneId,
    val nowDateTime: LocalDateTime = ZonedDateTime.now(zoneId).toLocalDateTime()
) {
    val nowDate: LocalDate get() = nowDateTime.toLocalDate()

    override fun equals(other: Any?): Boolean =
        this === other || (
            other is RequestContext
                && other.supportLang == this.supportLang
            )

    override fun hashCode() = hash(supportLang)

    companion object {
        val DEFAULT_ZONE_ID: ZoneId = ZoneId.of("Asia/Seoul")
        const val ZONE_ID_HEADER = "X-ZONE_ID"
    }
}
