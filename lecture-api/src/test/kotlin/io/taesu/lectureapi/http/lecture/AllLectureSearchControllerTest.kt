package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.mockk.slot
import io.taesu.lectureapi.helper.lectureVoSlicedResult
import io.taesu.lectureapi.helper.requestContext
import io.taesu.lectureapi.helper.requestContextWithReserved
import io.taesu.lectureapi.lecture.infra.LectureSearchCriteria
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class AllLectureSearchControllerTest: RestControllerTest() {
    @Test
    fun `전체 강연 목록 조회 API 테스트`() {
        // given
        val slicedResult = lectureVoSlicedResult()
        val slot = slot<LectureSearchCriteria>()
        every { lectureSearchService.search(capture(slot)) } returns slicedResult

        val requestContext = requestContextWithReserved()
        val request = setHeader(
            get("/api/v1/lectures")
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
            assertThat(this.lastKey).isEqualTo(7L)
            assertThat(this.releasedAt).isEqualTo(null)
        }
    }
}
