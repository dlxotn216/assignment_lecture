package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.mockk.slot
import io.taesu.lectureapi.helper.lectureVo
import io.taesu.lectureapi.helper.requestContextWithReserved
import io.taesu.lectureapi.http.lecture.dtos.PopularLecture
import io.taesu.lectureapi.lecture.domain.LectureVo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class PopularLectureRetrieveControllerTest: RestControllerTest() {
    @Test
    fun `인기 강연 목록 조회 API 테스트`() {
        // given
        val requestContext = requestContextWithReserved()
        every { popularLectureService.retrieveTodayPopularLectureKeys(requestContext.nowDate) } returns listOf(
            12L to 6.1,
            1L to 10.2,
            34L to 9.2,
        )
        val slot = slot<Collection<Long>>()
        val lecture34 = lectureVo(34L)
        val lecture12 = lectureVo(12L)
        val lecture1 = lectureVo(1L)
        every { lectureRetrieveService.retrieveAllVo(capture(slot)) } returns listOf(lecture34, lecture12, lecture1)

        val request = setHeader(get("/api/v1/popular-lectures"))

        // when, then
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(
                // 순위가 높은 순서대로 정렬된다
                content().json(
                    successResponse(
                        listOf(
                            lecture1.popularLecture(10.2),
                            lecture34.popularLecture(9.2),
                            lecture12.popularLecture(6.1),
                        ), requestContext
                    )
                )
            )
            .andDo(print())

        slot.captured.run {
            assertThat(this).containsExactly(12L, 1L, 34L)
        }
    }

    private fun LectureVo.popularLecture(score: Double) = PopularLecture(
        lectureKey = lectureKey,
        score = score,
        name = name,
        maxTraineeCount = maxTraineeCount,
        remainTraineeCount = remainTraineeCount,
        releasedAt = releasedAt,
    )
}
