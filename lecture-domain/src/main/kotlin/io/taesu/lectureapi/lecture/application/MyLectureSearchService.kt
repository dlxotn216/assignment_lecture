package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.dtos.SlicedResult.Companion
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import io.taesu.lectureapi.lecture.infra.dtos.MyLectureRow
import org.springframework.stereotype.Service

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Service
class MyLectureSearchService(private val lectureTraineeSearchService: LectureTraineeSearchService) {
    /**
     * @return 나의 강의 검색 결과 Slice
     */
    fun search(criteria: LectureTraineeSearchCriteria): SlicedResult<Long, MyLectureRow> {
        val slice = lectureTraineeSearchService.searchAsSlice(criteria)
        return slice.map {
            MyLectureRow(
                lectureTraineeKey = it.lectureTraineeKey,
                lectureKey = it.lectureKey,
                name = it.name,
                applyAt = it.applyAt
            )
        }.run(Companion::from)
    }
}
