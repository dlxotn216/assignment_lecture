package io.taesu.lectureapi.lecture.infra

import io.taesu.lectureapi.common.infra.SliceCriteria
import io.taesu.lectureapi.common.vo.LocalDateTimeRange
import io.taesu.lectureapi.lecture.domain.Lecture
import io.taesu.lectureapi.utils.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KProperty1

/**
 * Created by itaesu on 2024/03/22.
 *
 * @author Lee Tae Su
 */
class LectureSearchCriteria(
    size: Int = 10,
    lastKey: Long? = null,
    val releasedAt: LocalDateTimeRange? = null,
): SliceCriteria<Long?>(size, lastKey) {

    enum class SortType(
        private val kProperty: KProperty1<Lecture, Long>,
        private val direction: Sort.Direction,
    ) {
        LECTURE_KEY_DESC(Lecture::lectureKey, Sort.Direction.DESC)
        ;

        fun toSort() = Sort.by(direction, kProperty.name)
    }

    val pageRequest = PageRequest.ofSize(size).withSort(SortType.LECTURE_KEY_DESC.toSort())

}

class LectureSpecification private constructor() {
    companion object {
        fun from(criteria: LectureSearchCriteria): Specification<Lecture>? {
            return listOfNotNull<Specification<Lecture>>(
                criteria.lastKey.specIf { Lecture::lectureKey lt it },
                criteria.releasedAt.specIf { Lecture::releasedAt betweenRangeClosed it },
            ).takeIf { it.isNotEmpty() }
                ?.reduce { acc, specification -> acc.and(specification) }
        }
    }
}
