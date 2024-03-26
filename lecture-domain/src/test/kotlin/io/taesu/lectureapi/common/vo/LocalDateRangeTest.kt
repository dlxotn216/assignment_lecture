package io.taesu.lectureapi.common.vo

import io.taesu.lectureapi.common.utils.localDateRange
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LocalDateRangeTest {
    @Test
    fun `startDate의 시작시간 ~ 다음날 시작시간을 반환한다`() {
        // given
        val range = LocalDate.parse("2024-03-20") localDateRange LocalDate.parse("2024-03-23")

        // when
        val localDateTimeRange = range.toLocalDateTimeRange()

        // then
        assertThat(localDateTimeRange).isEqualTo(
            LocalDateTimeRange(
                startDateTime = LocalDateTime.parse("2024-03-20T00:00:00"),
                endDateTime = LocalDateTime.parse("2024-03-24T00:00:00")
            )
        )
    }


    @Test
    fun `DateRange 사이 날짜 목록을 반환한다`() {
        // given
        val range = LocalDate.parse("2024-03-20") localDateRange LocalDate.parse("2024-03-23")

        // when
        val dates = range.getDates()

        // then
        assertThat(dates).isEqualTo(
            listOf(
                LocalDate.parse("2024-03-20"),
                LocalDate.parse("2024-03-21"),
                LocalDate.parse("2024-03-22"),
                LocalDate.parse("2024-03-23"),
            )
        )
    }
}
