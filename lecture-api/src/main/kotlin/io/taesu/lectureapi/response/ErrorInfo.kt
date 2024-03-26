package io.taesu.lectureapi.response

import com.fasterxml.jackson.annotation.JsonInclude
import io.taesu.lectureapi.common.exception.ErrorCode

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class ErrorInfo(
    val errorCode: ErrorCode,
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val fieldErrors: List<FieldError> = emptyList(),
)
