package io.taesu.lectureapi.lecture.application

import io.taesu.lectureapi.dtos.SlicedResult
import io.taesu.lectureapi.dtos.SlicedResult.Companion
import io.taesu.lectureapi.lecture.domain.LectureVo
import io.taesu.lectureapi.lecture.infra.LectureSearchCriteria
import io.taesu.lectureapi.lecture.infra.LectureSearchRepository
import io.taesu.lectureapi.lecture.infra.LectureSpecification
import org.springframework.stereotype.Service

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
@Service
class LectureSearchService(private val lectureSearchRepository: LectureSearchRepository) {
    /**
     * @return 강의 검색 결과 Slice
     */
    fun search(criteria: LectureSearchCriteria): SlicedResult<Long, LectureVo> {
        return lectureSearchRepository.findAll(
            LectureSpecification.from(criteria),
            criteria.pageRequest,
        ).map(LectureVo.Companion::from)
            .run(Companion::from)
    }

    /**
     * @param lectureKeys 강의 식별자 목록
     * @return 강의 검색 결과 리스트
     */
    fun searchByLectureKeys(lectureKeys: Collection<Long>): List<LectureVo> {
        return lectureSearchRepository.findAllByIdIn(lectureKeys, LectureVo::class.java)
    }
}
