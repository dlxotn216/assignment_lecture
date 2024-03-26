package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.lecture.application.LectureTraineeSearchService
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeRow
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import io.taesu.lectureapi.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@RestController
class LectureTraineeSearchController(private val lectureTraineeSearchService: LectureTraineeSearchService) {
    /**
     * 수강생별 강의 목록 조회 API
     *
     * @param criteria 수강생별 강의 검색 조건
     * @param requestContext 요청 컨텍스트
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        value = ["/api/v1/lectures/trainees"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun search(
        criteria: LectureTraineeSearchCriteria,
        requestContext: RequestContext,
    ): SuccessResponse<SlicedResult<Long, LectureTraineeRow>> {
        return SuccessResponse(
            lectureTraineeSearchService.search(criteria),
            requestContext
        )
    }
}
