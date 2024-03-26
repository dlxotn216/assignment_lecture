package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.common.utils.localDateRange
import io.taesu.lectureapi.common.vo.LocalDateTimeRange
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.http.lecture.dtos.AvailableLectureSearchCriteria
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
 * Created by itaesu on 2024/03/23.
 *
 * @author Lee Tae Su
 */
@RestController
class AvailableLectureSearchController(private val lectureSearchService: LectureSearchService) {
    /**
     * 신청 가능한 강의 목록 조회 API
     *
     * @param criteria 신청 가능한 강의 검색 조건
     * @param requestContext 요청 컨텍스트
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        value = ["/api/v1/available-lectures"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun search(
        criteria: AvailableLectureSearchCriteria,
        requestContext: RequestContext,
    ): SuccessResponse<SlicedResult<Long, LectureVo>> {
        val releasedAtRange = resolveReleasedAtRange(requestContext)
        return SuccessResponse(
            lectureSearchService.search(criteria.toAvailableLectureSearchCriteria(releasedAtRange)),
            requestContext
        )
    }

    private fun resolveReleasedAtRange(requestContext: RequestContext): LocalDateTimeRange {
        val nowDate = requestContext.nowDate
        // 노출 가능한 강의는 강연 시작일로 부터 일주일 전 ~ 강연 시작 하루 후
        return (nowDate.minusDays(1L) localDateRange nowDate.plusDays(7)).toLocalDateTimeRange()
    }

    private fun AvailableLectureSearchCriteria.toAvailableLectureSearchCriteria(
        releasedAtRange: LocalDateTimeRange,
    ) = LectureSearchCriteria(
        size = this.size,
        lastKey = this.lastKey,
        releasedAt = releasedAtRange,
    )
}
