package io.taesu.lectureapi.http.lecture

import io.taesu.lectureapi.common.context.RequestContext
import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.http.lecture.dtos.MyLectureSearchCriteria
import io.taesu.lectureapi.lecture.application.MyLectureSearchService
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import io.taesu.lectureapi.lecture.infra.dtos.MyLectureRow
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
class MyLectureSearchController(private val myLectureSearchService: MyLectureSearchService) {
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(
        value = ["/api/v1/trainees/{traineeId}/lectures"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
    )
    fun search(
        criteria: MyLectureSearchCriteria,
        requestContext: RequestContext,
    ): SuccessResponse<SlicedResult<Long, MyLectureRow>> {
        return SuccessResponse(
            myLectureSearchService.search(criteria.toLectureTraineeSearchCriteria()),
            requestContext
        )
    }

    private fun MyLectureSearchCriteria.toLectureTraineeSearchCriteria() = LectureTraineeSearchCriteria(
        size = this.size,
        traineeId = this.traineeId,
        lastKey = this.lastKey,
    )
}
