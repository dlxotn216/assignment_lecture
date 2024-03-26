package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.taesu.lectureapi.helper.LECTURE_KEY
import io.taesu.lectureapi.helper.requestContextWithReserved
import io.taesu.lectureapi.http.lecture.dtos.LectureCreateHttpRequest
import io.taesu.lectureapi.http.lecture.dtos.LectureCreateHttpResponse
import io.taesu.lectureapi.lecture.application.LectureCreateCommand
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LectureCreateControllerTest: RestControllerTest() {
    @Test
    fun `강연 등록 API 테스트`() {
        // given
        val httpRequest = LectureCreateHttpRequest(
            name = "강연 제목",
            location = "강연 설명",
            releasedAt = LocalDateTime.parse("2024-03-24T00:00:00"),
            trainer = "강연자",
            maxTraineeCount = 100,
            content = "contents"
        )

        val command = LectureCreateCommand(
            name = "강연 제목",
            location = "강연 설명",
            releasedAt = LocalDateTime.parse("2024-03-24T00:00:00"),
            trainer = "강연자",
            maxTraineeCount = 100,
            content = "contents"
        )
        val requestContext = requestContextWithReserved()
        every { lectureCreateService.create(command, requestContext.nowDateTime) } returns LECTURE_KEY

        val request = setHeader(post("/api/v1/lectures", LECTURE_KEY).content(jsonBody(httpRequest)))

        // when, then
        mockMvc.perform(request)
            .andExpect(status().isCreated())
            .andExpect(content().json(successResponse(LectureCreateHttpResponse(LECTURE_KEY), requestContext)))
            .andDo(MockMvcResultHandlers.print())
    }
}
