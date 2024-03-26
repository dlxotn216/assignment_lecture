package io.taesu.lectureapi.common.vo

import java.time.LocalDateTime

data class LocalDateTimeRange(
    val startDateTime: LocalDateTime,
    val endDateTime: LocalDateTime,
)
