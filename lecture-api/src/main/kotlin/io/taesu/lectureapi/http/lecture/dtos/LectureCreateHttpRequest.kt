package io.taesu.lectureapi.http.lecture.dtos

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDateTime

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class LectureCreateHttpRequest(
    @field:NotBlank(message = "EXCEPTION.REQUIRED_PARAMETER")
    val name: String,
    @field:NotBlank(message = "EXCEPTION.REQUIRED_PARAMETER")
    val location: String,
    @field:DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    val releasedAt: LocalDateTime,
    @field:NotBlank(message = "EXCEPTION.REQUIRED_PARAMETER")
    val trainer: String,
    @field:Min(1L, message = "MESSAGE.INVALID.MAX_TRAINEE_COUNT.MIN_MAX")
    @field:Max(Int.MAX_VALUE.toLong(), message = "MESSAGE.INVALID.MAX_TRAINEE_COUNT.MIN_MAX")
    val maxTraineeCount: Int,
    @field:NotBlank(message = "EXCEPTION.REQUIRED_PARAMETER")
    val content: String,
)
