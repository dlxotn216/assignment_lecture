package io.taesu.lectureapi.http

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.http.lecture.RestControllerTest
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.bind.MethodArgumentNotValidException

/**
 * Created by itaesu on 2024/03/26.
 *
 * @author Lee Tae Su
 */
class ExceptionHandlerAdviceTest: RestControllerTest() {
    @Test
    fun `BusinessException으로 인한 실패 응답 테스트`() {
        mockMvc.perform(
            get("/api/v1/errors/{type}", "business")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
            .andExpect { assertInstanceOf(BusinessException::class.java, it.resolvedException) }
            .andExpect(jsonPath("$.errorInfo.errorCode").value("INVALID_REQUEST"))
            .andExpect(jsonPath("$.status").value("FAIL"))
    }

    @Test
    fun `MethodArgumentNotValidException 으로 인한 실패 응답 테스트`() {
        mockMvc.perform(
            get("/api/v1/errors/{type}", "methodArgumentNotValid")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
            .andExpect { assertInstanceOf(MethodArgumentNotValidException::class.java, it.resolvedException) }
            .andExpect(jsonPath("$.errorInfo.errorCode").value("INVALID_REQUEST"))
            .andExpect(jsonPath("$.status").value("FAIL"))
    }

    @Test
    fun `IllegalArgumentException 으로 인한 실패 응답 테스트`() {
        mockMvc.perform(
            get("/api/v1/errors/{type}", "illegalArgument")
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isBadRequest())
            .andExpect { assertInstanceOf(IllegalArgumentException::class.java, it.resolvedException) }
            .andExpect(jsonPath("$.errorInfo.errorCode").value("UNEXPECTED"))
            .andExpect(jsonPath("$.status").value("FAIL"))
    }
}
