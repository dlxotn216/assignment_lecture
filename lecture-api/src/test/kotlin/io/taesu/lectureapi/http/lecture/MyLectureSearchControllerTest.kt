package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.mockk.slot
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.helper.TRAINEE_ID
import io.taesu.lectureapi.helper.requestContextWithReserved
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import io.taesu.lectureapi.lecture.infra.dtos.MyLectureRow
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class MyLectureSearchControllerTest: RestControllerTest() {
    @Test
    fun `나의 강연 신청 내역 목록 조회 API 테스트`() {
        // given
        val slicedResult = myLectureSlicedResult()
        val slot = slot<LectureTraineeSearchCriteria>()
        every { myLectureSearchService.search(capture(slot)) } returns slicedResult

        val requestContext = requestContextWithReserved()
        val request = setHeader(
            MockMvcRequestBuilders.get("/api/v1/trainees/{traineeId}/lectures", TRAINEE_ID)
                .param("size", "3")
                .param("lastKey", "7")
        )

        // when, then
        mockMvc.perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().json(successResponse(slicedResult, requestContext)))
            .andDo(MockMvcResultHandlers.print())

        slot.captured.run {
            assertThat(this.traineeId).isEqualTo(TRAINEE_ID)
            assertThat(this.size).isEqualTo(3)
            assertThat(this.lastKey!!).isEqualTo(7L)
        }
    }

    private fun myLectureSlicedResult(): SlicedResult<Long, MyLectureRow> {
        val slicedResult = SlicedResult(
            contents = listOf(myLectureRow(6L), myLectureRow(5L), myLectureRow(4L)),
            isFirst = false,
            isLast = false,
            size = 3,
            hasNext = true,
            isEmpty = false
        )
        return slicedResult
    }

    private fun myLectureRow(lectureKey: Long) = MyLectureRow(
        lectureKey,
        lectureKey,
        "강연 제목 $lectureKey",
        LocalDateTime.parse("2024-03-24T00:00:00")
    )
}
