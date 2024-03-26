package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.application.LectureApplyWithLockService
import io.taesu.lectureapi.common.context.RequestContext
import jakarta.validation.Valid
import jakarta.validation.constraints.Size
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@RestController
class LectureApplyController(private val lectureApplyWithLockService: LectureApplyWithLockService) {
    /**
     * 강의 신청 API
     *
     * @param lectureKey 강의 키
     * @param traineeId 사번
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/api/v1/lectures/{lectureKey}/trainees/{traineeId}")
    fun apply(
        @PathVariable lectureKey: Long,
        @Valid @Size(max = 5) @PathVariable traineeId: String,
        requestContext: RequestContext,
    ) {
        lectureApplyWithLockService.apply(lectureKey, traineeId, requestContext)
    }

    /**
     * 강의 신청 취소 API
     *
     * @param lectureKey 강의 키
     * @param traineeId 사번
     */
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/lectures/{lectureKey}/trainees/{traineeId}")
    fun cancel(
        @PathVariable lectureKey: Long,
        @Valid @Size(max = 5) @PathVariable traineeId: String,
        requestContext: RequestContext,
    ) {
        lectureApplyWithLockService.cancel(lectureKey, traineeId, requestContext)
    }
}
