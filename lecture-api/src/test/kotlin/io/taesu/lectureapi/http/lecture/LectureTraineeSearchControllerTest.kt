package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.mockk.slot
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.helper.LECTURE_TRAINEE_KEY
import io.taesu.lectureapi.helper.TRAINEE_ID
import io.taesu.lectureapi.helper.requestContextWithReserved
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeRow
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LectureTraineeSearchControllerTest: RestControllerTest() {
    @Test
    fun `강연 별 신청 목록 조회 API 테스트`() {
        // given
        val slicedResult = lectureTraineeSlicedResult()
        val slot = slot<LectureTraineeSearchCriteria>()
        every { lectureTraineeSearchService.search(capture(slot)) } returns slicedResult

        val requestContext = requestContextWithReserved()
        val request = setHeader(
            MockMvcRequestBuilders.get("/api/v1/lectures/trainees")
                .param("size", "3")
                .param("lastKey", "7")
        )

        // when, then
        mockMvc.perform(request)
            .andExpect(status().isOk())
            .andExpect(content().json(successResponse(slicedResult, requestContext)))
            .andDo(print())

        slot.captured.run {
            assertThat(this.size).isEqualTo(3)
            assertThat(this.lastKey!!).isEqualTo(7L)
        }
    }


    private fun lectureTraineeSlicedResult(): SlicedResult<Long, LectureTraineeRow> {
        val slicedResult = SlicedResult(
            contents = listOf(lectureTraineeRow(6L), lectureTraineeRow(5L), lectureTraineeRow(4L)),
            isFirst = false,
            isLast = false,
            size = 3,
            hasNext = true,
            isEmpty = false
        )
        return slicedResult
    }

    private fun lectureTraineeRow(lectureKey: Long) = LectureTraineeRow(
        LECTURE_TRAINEE_KEY,
        lectureKey,
        "강연 제목 $lectureKey",
        "train$lectureKey",
        LocalDateTime.parse("2024-03-24T00:00:00")
    )
}
