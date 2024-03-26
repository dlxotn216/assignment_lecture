package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.application.PopularLectureService
import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.http.lecture.dtos.PopularLecture
import io.taesu.lectureapi.http.lecture.dtos.PopularLectureRetrieveResponse
import io.taesu.lectureapi.lecture.application.LectureRetrieveService
import io.taesu.lectureapi.lecture.domain.LectureVo
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
class PopularLectureRetrieveController(
    private val popularLectureService: PopularLectureService,
    private val lectureRetrieveService: LectureRetrieveService,
) {
    /**
     * 실시간 인기 강연 목록 조회 API
     */
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        value = ["/api/v1/popular-lectures"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun retrievePopularLectures(
        requestContext: RequestContext,
    ): SuccessResponse<List<PopularLecture>> {
        val lectureKeyScores = popularLectureService.retrieveTodayPopularLectureKeys(requestContext.nowDate)
        val lectures = lectureRetrieveService.retrieveAllVo(lectureKeyScores.map { it.first })
        val popularLectures = PopularLectureRetrieveResponse(lectureKeyScores, lectures).resolvePopularLectures()

        return SuccessResponse(popularLectures, requestContext)
    }
}
