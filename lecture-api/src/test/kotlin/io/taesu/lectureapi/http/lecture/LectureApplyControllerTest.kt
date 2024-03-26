package io.taesu.lectureapi.http.lecture

import io.mockk.every
import io.taesu.lectureapi.common.aop.lock.LockContext
import io.taesu.lectureapi.helper.LECTURE_KEY
import io.taesu.lectureapi.helper.TRAINEE_ID
import io.taesu.lectureapi.helper.requestContextWithReserved
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
class LectureApplyControllerTest: RestControllerTest() {
    @Test
    fun `강연 신청 API 테스트`() {
        // given
        val requestContext = requestContextWithReserved()
        every { lectureApplyWithLockService.apply(LECTURE_KEY, TRAINEE_ID, requestContext) } answers {}

        val request = setHeader(
            post("/api/v1/lectures/{lectureKey}/trainees/{traineeId}", LECTURE_KEY, TRAINEE_ID)
        )

        // when, then
        mockMvc.perform(request)
            .andExpect(status().isNoContent())
            .andDo(print())
    }

    @Test
    fun `강연 신청 취소 API 테스트`() {
        // given
        val requestContext = requestContextWithReserved()
        every { lectureApplyWithLockService.cancel(LECTURE_KEY, TRAINEE_ID, requestContext) } answers {}

        val request = setHeader(
            delete("/api/v1/lectures/{lectureKey}/trainees/{traineeId}", LECTURE_KEY, TRAINEE_ID)
        )

        // when, then
        mockMvc.perform(request)
            .andExpect(status().isNoContent())
            .andDo(print())
    }
}
