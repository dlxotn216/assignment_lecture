package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.dtos.SlicedResult.Companion
import io.taesu.lectureapi.lecture.domain.LectureTraineeSearchRepository
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeRow
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSearchCriteria
import io.taesu.lectureapi.lecture.infra.dtos.LectureTraineeSpecification
import org.springframework.data.domain.Slice
import org.springframework.data.domain.SliceImpl
import org.springframework.stereotype.Service

/**
 * Created by itaesu on 2024/03/24.
 *
 * @author Lee Tae Su
 */
@Service
class LectureTraineeSearchService(
    private val lectureTraineeSearchRepository: LectureTraineeSearchRepository,
    private val lectureSearchService: LectureSearchService,
) {
    fun search(criteria: LectureTraineeSearchCriteria): SlicedResult<Long, LectureTraineeRow> {
        return searchAsSlice(criteria)
            .run(Companion::from)
    }

    fun searchAsSlice(criteria: LectureTraineeSearchCriteria): Slice<LectureTraineeRow> {
        val slice = lectureTraineeSearchRepository.findAll(
            LectureTraineeSpecification.from(criteria),
            criteria.pageRequest,
        )
        val lectureByKey =
            lectureSearchService.searchByLectureKeys(slice.content.map { it.lectureKey }).associateBy { it.lectureKey }

        return slice
            .mapNotNull {
                val lectureVo = lectureByKey[it.lectureKey] ?: return@mapNotNull null
                LectureTraineeRow(
                    lectureTraineeKey = it.lectureTraineeKey,
                    lectureKey = lectureVo.lectureKey,
                    name = lectureVo.name,
                    traineeId = it.traineeId,
                    applyAt = it.createdAt!!
                )
            }
            .run { SliceImpl(this, criteria.pageRequest, slice.hasNext()) }
    }
}
