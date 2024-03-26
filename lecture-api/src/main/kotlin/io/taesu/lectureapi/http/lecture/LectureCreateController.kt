package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.lecture.application.LectureCreateCommand
import io.taesu.lectureapi.lecture.application.LectureCreateService
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.http.lecture.dtos.LectureCreateHttpRequest
import io.taesu.lectureapi.http.lecture.dtos.LectureCreateHttpResponse
import io.taesu.lectureapi.lecture.domain.LectureRepository
import io.taesu.lectureapi.response.SuccessResponse
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@RestController
class LectureCreateController(private val lectureCreateService: LectureCreateService) {
    /**
     * 강의 생성 API
     * @param request 강의 생성 요청
     * @param requestContext 요청 컨텍스트
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        value = ["/api/v1/lectures"],
        consumes = [MediaType.APPLICATION_JSON_VALUE],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun create(
        @Valid @RequestBody request: LectureCreateHttpRequest,
        requestContext: RequestContext,
    ): SuccessResponse<LectureCreateHttpResponse> {
        val lectureKey = lectureCreateService.create(request.toCommand(), requestContext.nowDateTime)
        return SuccessResponse(LectureCreateHttpResponse(lectureKey), requestContext)
    }
}

private fun LectureCreateHttpRequest.toCommand(): LectureCreateCommand {
    return LectureCreateCommand(
        name = this.name,
        location = this.location,
        releasedAt = this.releasedAt,
        trainer = this.trainer,
        maxTraineeCount = this.maxTraineeCount,
        content = this.content,
    )
}
