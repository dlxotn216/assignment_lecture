package io.taesu.lectureapi.lecture.infra.dtos

import io.taesu.lectureapi.common.infra.SliceCriteria
import io.taesu.lectureapi.lecture.domain.LectureTrainee
import io.taesu.lectureapi.utils.eq
import io.taesu.lectureapi.utils.lt
import io.taesu.lectureapi.utils.specIf
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.data.jpa.domain.Specification
import kotlin.reflect.KProperty1


class LectureTraineeSearchCriteria(
    size: Int = 10,
    val traineeId: String? = null,
    lastKey: Long? = null,
): SliceCriteria<Long?>(size, lastKey) {
    enum class SortType(
        private val kProperty: KProperty1<LectureTrainee, *>,
        private val direction: Sort.Direction,
    ) {
        LECTURE_TRAINEE_KEY_DESC(LectureTrainee::lectureTraineeKey, Sort.Direction.DESC),
        ;

        fun toSort() = Sort.by(direction, kProperty.name)
    }

    val pageRequest = PageRequest.ofSize(size).withSort(
        SortType.LECTURE_TRAINEE_KEY_DESC.toSort()
    )
}

class LectureTraineeSpecification private constructor() {
    companion object {
        fun from(criteria: LectureTraineeSearchCriteria): Specification<LectureTrainee>? {
            return listOfNotNull<Specification<LectureTrainee>>(
                criteria.traineeId.specIf { LectureTrainee::traineeId eq it },
                criteria.lastKey.specIf { LectureTrainee::lectureTraineeKey lt it },
            ).takeIf { it.isNotEmpty() }
                ?.reduce { acc, specification -> acc.and(specification) }
        }

    }
}
