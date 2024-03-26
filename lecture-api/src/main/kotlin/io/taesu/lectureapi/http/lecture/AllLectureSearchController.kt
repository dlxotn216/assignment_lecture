package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.http.lecture.dtos.AllLectureSearchCriteria
import io.taesu.lectureapi.lecture.application.LectureSearchService
import io.taesu.lectureapi.lecture.domain.LectureVo
import io.taesu.lectureapi.lecture.infra.LectureSearchCriteria
import io.taesu.lectureapi.response.SuccessResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@RestController
class AllLectureSearchController(private val lectureSearchService: LectureSearchService) {
    /**
     * 전체 강의 목록 조회 API
     *
     * @param criteria 전체 강의 검색 조건
     * @param requestContext 요청 컨텍스트
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        value = ["/api/v1/lectures"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun search(
        criteria: AllLectureSearchCriteria,
        requestContext: RequestContext,
    ): SuccessResponse<SlicedResult<Long, LectureVo>> {
        return SuccessResponse(lectureSearchService.search(criteria.toLectureSearchCriteria()), requestContext)
    }

    private fun AllLectureSearchCriteria.toLectureSearchCriteria() = LectureSearchCriteria(
        size = this.size,
        lastKey = this.lastKey,
    )
}
