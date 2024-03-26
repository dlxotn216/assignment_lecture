package io.taesu.lectureapi.http

import io.taesu.lectureapi.common.exception.BusinessException
import io.taesu.lectureapi.common.exception.ErrorCode
import io.taesu.lectureapi.common.exception.throwResourceNotFound
import io.taesu.lectureapi.common.exception.throwUnexpected
import org.springframework.context.annotation.Profile
import org.springframework.core.MethodParameter
import org.springframework.validation.BeanPropertyBindingResult
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

/**
 * Created by itaesu on 2024/03/26.
 *
 * 실패 응답 테스트 용 Exception Controller
 *
 * @author Lee Tae Su
 */
@Profile("test")
@RestController
class TestExceptionController {
    @GetMapping("/api/v1/errors/{type}")
    @Throws(Exception::class)
    fun getSpecificException(@PathVariable type: String) {
        when(type) {
            "business" -> throw BusinessException(ErrorCode.INVALID_REQUEST, debugMessage =  "for testing")
            "methodArgumentNotValid" -> throw MethodArgumentNotValidException(
                MethodParameter(this::class.java.getDeclaredMethod("getSpecificException", String::class.java), 0),
                BeanPropertyBindingResult(this, "getSpecificException")
                    .apply {
                        addError(FieldError("getSpecificException", "type", "Invalid type"))
                    }
            )
            "illegalArgument" -> throw IllegalArgumentException("Invalid type")
        }
    }
}
