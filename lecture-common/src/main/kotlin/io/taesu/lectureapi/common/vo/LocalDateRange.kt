package io.taesu.lectureapi.common.vo

import java.time.LocalDate
import java.time.temporal.ChronoUnit

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
data class LocalDateRange(
    val startDate: LocalDate,
    val endDate: LocalDate,
) {
    fun toLocalDateTimeRange(): LocalDateTimeRange {
        return LocalDateTimeRange(
            startDateTime = startDate.atStartOfDay(),
            endDateTime = endDate.plusDays(1).atStartOfDay()
        )
    }

    fun getDates(): List<LocalDate> {
        return (0L..ChronoUnit.DAYS.between(startDate, endDate)).map {
            startDate.plusDays(it)
        }
    }
}
