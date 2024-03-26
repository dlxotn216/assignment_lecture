package io.taesu.lectureapi.common.utils

import io.taesu.lectureapi.common.vo.LocalDateRange
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/21.
 *
 * @author Lee Tae Su
 */
fun hash(vararg fields: Any?): Int {
    return fields.fold(0) { acc, any ->
        (acc * 31) + any.hashCode()
    }
}

infix fun String.localDateRange(endDate: String): LocalDateRange {
    return LocalDate.parse(this) localDateRange LocalDate.parse(endDate)
}

infix fun LocalDate.localDateRange(endDate: LocalDate): LocalDateRange {
    return LocalDateRange(this, endDate)
}
