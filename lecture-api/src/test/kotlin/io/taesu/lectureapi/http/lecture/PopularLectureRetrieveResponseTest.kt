package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.helper.lectureVo
import io.taesu.lectureapi.http.lecture.dtos.PopularLectureRetrieveResponse
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
class PopularLectureRetrieveResponseTest {
    @Test
    fun `인기 순위가 높은 순서대로 정렬 된다`() {
        // given
        val lectureKeyScores = listOf(
            1L to 23.1,
            2L to 21.2,
            3L to 9.2,
            4L to 12.3,
            5L to 52.1,
        )
        val lectures = listOf(
            lectureVo(1L),
            lectureVo(2L),
            lectureVo(3L),
            lectureVo(4L),
            lectureVo(5L),
        )

        // when
        val popularLectures = PopularLectureRetrieveResponse(lectureKeyScores, lectures).resolvePopularLectures()

        // then
        assertEquals(5, popularLectures.size)
        assertEquals(5L, popularLectures[0].lectureKey)
        assertEquals(1L, popularLectures[1].lectureKey)
        assertEquals(2L, popularLectures[2].lectureKey)
        assertEquals(4L, popularLectures[3].lectureKey)
        assertEquals(3L, popularLectures[4].lectureKey)
    }
}
